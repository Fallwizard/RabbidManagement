package io.github.fallwizard.rabbitmq.mgmt;

import com.google.common.base.Optional;
import io.github.fallwizard.rabbitmq.mgmt.model.Parameter;

import java.util.Collection;

/**
 * @author Richard Clayton (Berico Technologies)
 */
public class ParameterOperations extends BaseFluent {

    public ParameterOperations(HttpContext httpContext, RabbitMgmtService mgmtService) {
        super(httpContext, mgmtService);
    }

    public Collection<Parameter> all(){

        return HTTP.GET("/parameters", PARAMETER_COLLECTION).get();
    }

    public Optional<Collection<Parameter>> all(String component){

        return HTTP.GET(String.format("/parameters/%s", component), PARAMETER_COLLECTION);
    }

    public Optional<Collection<Parameter>> allOnDefault(String component){

        return allOnVHost(component, "/");
    }

    public Optional<Collection<Parameter>> allOnVHost(String component, String vhost){

        return HTTP.GET(String.format("/parameters/%s/%s", component, encodeSlashes(vhost)), PARAMETER_COLLECTION);
    }

    public Optional<Parameter> get(String component, String vhost, String parameterName){

        return HTTP.GET(
                String.format("/parameters/%s/%s/%s", component, encodeSlashes(vhost), parameterName), PARAMETER);
    }

    public ParameterOperations delete(String component, String vhost, String parameterName){

        HTTP.DELETE(String.format("/parameters/%s/%s/%s", component, encodeSlashes(vhost), parameterName));

        return this;
    }

    public ParameterOperations create(Parameter parameter){

        HTTP.PUT(String.format("/parameters/%s/%s/%s",
                parameter.getComponent(),
                encodeSlashes(parameter.getVhost()),
                parameter.getName()),
                parameter);

        return this;
    }
}
