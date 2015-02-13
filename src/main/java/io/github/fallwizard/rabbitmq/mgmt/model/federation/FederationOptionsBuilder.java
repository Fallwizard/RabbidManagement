package io.github.fallwizard.rabbitmq.mgmt.model.federation;

/**
 * @author Richard Clayton (Berico Technologies)
 */
public class FederationOptionsBuilder
        <DERIVED_BUILDER extends FederationOptionsBuilder, DERIVED_FEDOPTS_TYPE extends FederationOptions> {

    DERIVED_FEDOPTS_TYPE options;

    public FederationOptionsBuilder(DERIVED_FEDOPTS_TYPE options) {

        this.options = options;
    }

    public DERIVED_BUILDER uri(String uri){

        options.setUri(uri);

        return self();
    }

    public DERIVED_BUILDER host(String host, int port){

        options.setUri(String.format("amqp://%s:%s", host, port));

        return self();
    }

    public DERIVED_BUILDER ackOnConfirm(){

        options.setAckMode("on-confirm");

        return self();
    }

    public DERIVED_BUILDER ackOnPublish(){

        options.setAckMode("on-publish");

        return self();
    }

    public DERIVED_BUILDER noAck(){

        options.setAckMode("no-ack");

        return self();
    }

    public DERIVED_BUILDER validateUpstreamUsers(){

        options.setTrustUserId(false);

        return self();
    }

    public DERIVED_BUILDER dontValidateUpstreamUsers(){

        options.setTrustUserId(true);

        return self();
    }

    public DERIVED_BUILDER self(){

        return (DERIVED_BUILDER) this;
    }

    public DERIVED_FEDOPTS_TYPE build(){

        return options;
    }
}
