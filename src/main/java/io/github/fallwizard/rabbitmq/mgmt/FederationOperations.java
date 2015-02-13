package io.github.fallwizard.rabbitmq.mgmt;

import io.github.fallwizard.rabbitmq.mgmt.model.federation.*;
import io.github.fallwizard.rabbitmq.mgmt.model.ValueWrapper;

import java.util.Collection;

/**
 * Assumes that you have installed the plugins:
 *
 * rabbitmq-plugins enable rabbitmq_federation
 * rabbitmq-plugins enable rabbitmq_federation_management
 *
 * @author Richard Clayton (Berico Technologies)
 */
public class FederationOperations extends BaseFluent {

    public FederationOperations(HttpContext httpContext, RabbitMgmtService mgmtService) {
        super(httpContext, mgmtService);
    }

    public FederationOperations establishConnectionWith(String connectionName, ExchangeFederationOptions options){

        return establish("/", connectionName, options);
    }

    public FederationOperations establishConnectionWith(String vhost, String connectionName, ExchangeFederationOptions options){

        return establish(vhost, connectionName, options);
    }

    public FederationOperations establishConnectionWith(String connectionName, QueueFederationOptions options){

        return establish("/", connectionName, options);
    }

    public FederationOperations establishConnectionWith(String vhost, String connectionName, QueueFederationOptions options){

        return establish(vhost, connectionName, options);
    }

    private FederationOperations establish(String vhost, String connectionName, FederationOptions options){

        // For some reason, they wrap the options object in a "value" parameter.
        ValueWrapper value = new ValueWrapper(options);

        HTTP.PUT(String.format("/parameters/federation-upstream/%s/%s", encodeSlashes(vhost), connectionName), value);

        return this;
    }

    public FederationOperations createPolicy(String name, FederationPolicy policy){

        return createPolicy("/", name, policy);
    }

    public FederationOperations createPolicy(String vhost, String name, FederationPolicy policy){

        HTTP.PUT(String.format("/policies/%s/%s", encodeSlashes(vhost), name), policy);

        return this;
    }

    public Collection<FederationLink> links(){

        return HTTP.GET("/federation-links", FEDERATION_LINK_COLLECTION).get();
    }
}
