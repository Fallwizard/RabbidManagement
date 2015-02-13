package io.github.fallwizard.rabbitmq.loader;

import com.google.common.collect.Lists;
import io.github.fallwizard.rabbitmq.mgmt.model.*;

import java.util.Collection;

/**
 * @author Richard Clayton (Berico Technologies)
 */
public class Manifest {

    ConnectionInfo connectionInfo;

    Collection<VirtualHost> vhosts = Lists.newArrayList();

    Collection<Exchange> exchanges = Lists.newArrayList();

    Collection<Queue> queues = Lists.newArrayList();

    Collection<User> users = Lists.newArrayList();

    Collection<Permission> permissions = Lists.newArrayList();

    Collection<Binding> bindings = Lists.newArrayList();

    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    public void setConnectionInfo(ConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
    }

    public Collection<VirtualHost> getVhosts() {
        return vhosts;
    }

    public void setVhosts(Collection<VirtualHost> vhosts) {
        this.vhosts = vhosts;
    }

    public Collection<Exchange> getExchanges() {
        return exchanges;
    }

    public void setExchanges(Collection<Exchange> exchanges) {
        this.exchanges = exchanges;
    }

    public Collection<Queue> getQueues() {
        return queues;
    }

    public void setQueues(Collection<Queue> queues) {
        this.queues = queues;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public Collection<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Collection<Permission> permissions) {
        this.permissions = permissions;
    }

    public Collection<Binding> getBindings() {
        return bindings;
    }

    public void setBindings(Collection<Binding> bindings) {
        this.bindings = bindings;
    }
}
