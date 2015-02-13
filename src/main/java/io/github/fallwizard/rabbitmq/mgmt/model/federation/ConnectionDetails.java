package io.github.fallwizard.rabbitmq.mgmt.model.federation;

/**
 * @author Richard Clayton (Berico Technologies)
 */
public class ConnectionDetails {

    protected String name;
    protected String peer_port;
    protected String peer_host;

    public String getName() {
        return name;
    }

    public String getPeerPort() {
        return peer_port;
    }

    public String getPeerHost() {
        return peer_host;
    }

}
