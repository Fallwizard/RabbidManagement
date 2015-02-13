package io.github.fallwizard.rabbitmq.mgmt;

import com.google.common.base.Optional;
import io.github.fallwizard.rabbitmq.mgmt.model.Policy;

import java.util.Collection;

/**
 * @author Richard Clayton (Berico Technologies)
 */
public class PolicyOperations extends BaseFluent {

    public PolicyOperations(HttpContext httpContext, RabbitMgmtService mgmtService) {
        super(httpContext, mgmtService);
    }

    public Collection<Policy> all(){

        return HTTP.GET("/policies", POLICY_COLLECTION).get();
    }

    public Optional<Collection<Policy>> allOnDefault(){

        return allOnVHost("/");
    }

    public Optional<Collection<Policy>> allOnVHost(String vhost){

        return HTTP.GET(String.format("/policies/%s", encodeSlashes(vhost)), POLICY_COLLECTION);
    }

    public Optional<Policy> get(String vhost, String name){

        return HTTP.GET(String.format("/policies/%s/%s", encodeSlashes(vhost), name), POLICY);
    }

    public PolicyOperations delete(String vhost, String name){

        HTTP.DELETE(String.format("/policies/%s/%s", encodeSlashes(vhost), name));

        return this;
    }

    public PolicyOperations create(String name, Policy policy){

        return create("/", name, policy);
    }

    public PolicyOperations create(String vhost, String name, Policy policy){

        HTTP.PUT(String.format("/policies/%s/%s", encodeSlashes(vhost), name), policy);

        return this;
    }
}
