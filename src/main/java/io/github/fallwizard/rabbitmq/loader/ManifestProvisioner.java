package io.github.fallwizard.rabbitmq.loader;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.fallwizard.rabbitmq.mgmt.RabbitMgmtService;
import io.github.fallwizard.rabbitmq.mgmt.model.*;

import java.util.Collection;

/**
 * @author Richard Clayton (Berico Technologies)
 */
public class ManifestProvisioner {

    private static final Logger logger = LoggerFactory.getLogger(ManifestProvisioner.class);

    public static void provision(Manifest manifest){

        RabbitMgmtService mgmt = manifest.getConnectionInfo().buildConnection();

        checkAliveness(mgmt);

        provisionVHosts(mgmt, manifest);

        provisionUsers(mgmt, manifest);

        provisionPermissions(mgmt, manifest);

        provisionExchanges(mgmt, manifest);

        provisionQueues(mgmt, manifest);

        provisionBindings(mgmt, manifest);
    }

    public static void rollback(Manifest manifest){

        RabbitMgmtService mgmt = manifest.getConnectionInfo().buildConnection();

        checkAliveness(mgmt);

        rollbackBindings(mgmt, manifest);

        rollbackQueues(mgmt, manifest);

        rollbackExchanges(mgmt, manifest);

        rollbackPermissions(mgmt, manifest);

        rollbackUsers(mgmt, manifest);

        rollbackVHosts(mgmt, manifest);
    }

    private static void checkAliveness(RabbitMgmtService mgmt){

        Status status = mgmt.vhosts().status();

        Preconditions.checkNotNull(status);

        Preconditions.checkState(status.getStatus().equals("ok"),
                String.format("Unknown RabbitMQ Management Console Status: %s (i.e. not 'OK')", status.getStatus()));
    }

    private static void provisionVHosts(RabbitMgmtService mgmt, Manifest manifest){

        for (VirtualHost vhost : manifest.getVhosts()){

            mgmt.vhosts().create(vhost);

            logger.info("Created VHost: {}", vhost.getName());
        }
    }

    private static void rollbackVHosts(RabbitMgmtService mgmt, Manifest manifest){

        for (VirtualHost vhost : manifest.getVhosts()){

            if (!vhost.getName().equals("/")){

                mgmt.vhosts().delete(vhost.getName());

                logger.info("Removed VHost: {}", vhost.getName());
            }
            else {

                logger.error("Cannot remove the default virtual host ('/').");
            }
        }
    }

    private static void provisionUsers(RabbitMgmtService mgmt, Manifest manifest){

        for (User user : manifest.getUsers()){

            mgmt.users().create(user);

            logger.info("Created User: {}", user.getName());
        }
    }

    private static void rollbackUsers(RabbitMgmtService mgmt, Manifest manifest){

        for (User user : manifest.getUsers()){

            mgmt.users().delete(user.getName());

            logger.info("Removed User: {}", user.getName());
        }
    }

    private static void provisionPermissions(RabbitMgmtService mgmt, Manifest manifest){

        for (Permission permission : manifest.getPermissions()){

            mgmt.permissions().set(permission);

            logger.info("Set Permission for {} on VHost: {}", permission.getUser(), permission.getVhost());
        }
    }

    private static void rollbackPermissions(RabbitMgmtService mgmt, Manifest manifest){

        for (Permission permission : manifest.getPermissions()){

            mgmt.permissions().remove(permission.getVhost(), permission.getUser());

            logger.info("Removed Permission for {} on VHost: {}", permission.getUser(), permission.getVhost());
        }
    }

    private static void provisionExchanges(RabbitMgmtService mgmt, Manifest manifest){

        for (Exchange exchange : manifest.getExchanges()){

            mgmt.exchanges().create(exchange);

            logger.info("Created Exchange: {} (vhost={})", exchange.getName(), exchange.getVhost());
        }
    }

    private static void rollbackExchanges(RabbitMgmtService mgmt, Manifest manifest){

        for (Exchange exchange : manifest.getExchanges()){

            mgmt.exchanges().delete(exchange.getVhost(), exchange.getName());

            logger.info("Removed Exchange: {} (vhost={})", exchange.getName(), exchange.getVhost());
        }
    }

    private static void provisionQueues(RabbitMgmtService mgmt, Manifest manifest){

        for (Queue queue : manifest.getQueues()){

            mgmt.queues().create(queue);

            logger.info("Created Queue: {} (vhost={})", queue.getName(), queue.getVhost());
        }
    }

    private static void rollbackQueues(RabbitMgmtService mgmt, Manifest manifest){

        for (Queue queue : manifest.getQueues()){

            mgmt.queues().delete(queue.getVhost(), queue.getName());

            logger.info("Removed Queue: {} (vhost={})", queue.getName(), queue.getVhost());
        }
    }

    private static void provisionBindings(RabbitMgmtService mgmt, Manifest manifest){

        for (Binding binding : manifest.getBindings()){

            mgmt.bindings().create(binding);

            logger.info("Created Binding: {} -> {} @ {}",
                    binding.getSource(), binding.getDestination(), binding.getRoutingKey());
        }
    }

    private static void rollbackBindings(RabbitMgmtService mgmt, Manifest manifest){

        // Explanation for:  What the heck is going on here?
        // Bindings do not have "id's" and since it's possible to have as many bindings as you want,
        // and they can even have the same routing key, there's notMatch way to effectively distinguish them.
        // So, RabbitMQ considers the "ID" to be the "property_key", which can be one of the follow:
        // 1.  The routing key URL-encoded, if there are notMatch "arguments" present on the binding.
        // 2.  A hash of the routing key and the "arguments".
        // Since it's not obvious how this hash is generated (and I'm too lazy to look it up),
        // a more practical solution is to query RabbitMQ first and retrieve all bindings between
        // a source and destination.  We then iterate through those bindings, performing an equality
        // test to see if the queried binding matches the binding we want to rollback.  If it is a match
        // we used the "property_key" of the query result to remove the binding.

        for (Binding binding : manifest.getBindings()){

            boolean isEtoE = binding.getDestinationType().equals("e");

            Optional<Collection<Binding>> results =
                    mgmt.bindings().get(binding.getVhost(), binding.getSource(), binding.getDestination(), isEtoE);

            if (results.isPresent()){

                for(Binding potentialMatch : results.get()){

                    if (binding.matches(potentialMatch)) {

                        mgmt.bindings().delete(
                            binding.getVhost(),
                            binding.getSource(),
                            binding.getDestination(),
                            isEtoE,
                            binding.getPropertiesKey());


                        logger.info("Removed Binding: {} -> {} @ {}",
                                binding.getSource(), binding.getDestination(), binding.getRoutingKey());
                    }
                }
            }
            else {

                logger.warn("Could not find bindings between source ({}) and destination ({}) (vhost={}, destType={})",
                            binding.getSource(),
                            binding.getDestination(),
                            binding.getVhost(),
                            binding.getDestinationType());
            }

        }
    }
}
