package io.github.fallwizard.rabbitmq.mgmt;

import io.github.fallwizard.test.RequireProperties;
import io.github.fallwizard.test.TestProperties;
import org.junit.Rule;
import org.junit.Test;
import io.github.fallwizard.rabbitmq.mgmt.model.AmqpUri;
import io.github.fallwizard.rabbitmq.mgmt.model.RegEx;
import io.github.fallwizard.rabbitmq.mgmt.model.federation.ExchangeFederationOptions;
import io.github.fallwizard.rabbitmq.mgmt.model.federation.FederationPolicy;
import io.github.fallwizard.rabbitmq.test.RabbitAssert;

import static io.github.fallwizard.rabbitmq.test.FederationLinkMatchers.*;

/**
 * @author Richard Clayton (Berico Technologies)
 */
public class FederationIT extends ITBase {

    public static final String HOSTNAME2 = "it.rabbit2.hostname";
    public static final String PORT2 = "it.rabbit2.port";
    public static final String USERNAME2 = "it.rabbit2.username";
    public static final String PASSWORD2 = "it.rabbit2.password";

    @Rule
    public TestProperties properties = new TestProperties();

    @Test
    @RequireProperties({
            USERNAME, PASSWORD, HOSTNAME, PORT,
            USERNAME2, PASSWORD2, HOSTNAME2, PORT2
    })
    public void federation_successfully_setup_between_two_nodes(){

        RabbitMgmtService mgmt = getManagementService();

        String upstreamHostname = System.getProperty(HOSTNAME2);
        int upstreamPort = Integer.parseInt(System.getProperty(PORT2));
        String upstreamUsername = System.getProperty(USERNAME2);
        String upstreamPassword = System.getProperty(PASSWORD2);

        String expectedConnectionName = "hare-federation";
        String expectedExchange1 = "amq.direct";
        String expectedExchange2 = "amq.topic";

        String remoteConnectionUri =
             AmqpUri.builder()
                .host(upstreamHostname)
                .port(upstreamPort)
                .credentials(upstreamUsername, upstreamPassword)
                .build()
                .getUri();

        mgmt.federation()
                .establishConnectionWith(
                        expectedConnectionName,
                        ExchangeFederationOptions.builder().uri(remoteConnectionUri).build());

        mgmt.federation()
                .createPolicy("hare-fed-policy",
                                FederationPolicy.builder()
                                .useAllUpstreams()
                                .pattern(RegEx.oneOf(expectedExchange1, expectedExchange2))
                                .build());

        RabbitAssert rabbitAssert = new RabbitAssert(mgmt);

        String expectedUri = "amqp://" + upstreamHostname + ":" + upstreamPort;

        rabbitAssert
                .hasFederationLink(
                        federatedExchange(expectedExchange1),
                        connectionName(expectedConnectionName),
                        upstreamUri(expectedUri))
                .hasFederationLink(
                        federatedExchange(expectedExchange2),
                        connectionName(expectedConnectionName),
                        upstreamUri(expectedUri));
    }

}
