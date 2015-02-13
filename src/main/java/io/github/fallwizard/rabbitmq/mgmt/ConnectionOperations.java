package io.github.fallwizard.rabbitmq.mgmt;


import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import io.github.fallwizard.rabbitmq.mgmt.model.Connection;

import java.util.Collection;
import java.util.Map;

public class ConnectionOperations extends BaseFluent {

    public ConnectionOperations(HttpContext httpContext, RabbitMgmtService mgmtService) {
        super(httpContext, mgmtService);
    }

    public Collection<Connection> all(){

        return HTTP.GET("/connections", CONNECTION_COLLECTION).get();
    }

    public Optional<Connection> get(String name){

        return HTTP.GET(String.format("/connections/%s", name), CONNECTION);
    }

    public ConnectionOperations forceDisconnect(String name){

        HTTP.DELETE(String.format("/connections/%s", name));

        return this;
    }

    public ConnectionOperations forceDisconnect(String name, String reason){

        Map<String, Object> headers = Maps.newHashMap();

        headers.put("X-Reason", reason);

        HTTP.DELETE(String.format("/connections/%s", name), headers);

        return this;
    }
}
