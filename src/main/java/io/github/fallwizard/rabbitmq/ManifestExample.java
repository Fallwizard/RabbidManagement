package io.github.fallwizard.rabbitmq;

import io.github.fallwizard.rabbitmq.loader.ConnectionInfo;
import io.github.fallwizard.rabbitmq.loader.Manifest;
import io.github.fallwizard.rabbitmq.loader.ManifestPersister;
import io.github.fallwizard.rabbitmq.mgmt.model.*;

import java.io.File;

/**
 * @author Richard Clayton (Berico Technologies)
 */
public class ManifestExample {

    public static void main(String[] args) throws Exception {

        Manifest manifest = new Manifest();

        manifest.setConnectionInfo(new ConnectionInfo("localhost", 15672, "guest", "guest"));

        manifest.getUsers().add(new User("admin", "administrator", "password"));
        manifest.getUsers().add(new User("app1", "service", "password"));
        manifest.getUsers().add(new User("app2", "service", "password"));
        manifest.getUsers().add(new User("johndoe", "user", "password"));
        manifest.getUsers().add(new User("janedoe", "user", "password"));

        manifest.getExchanges().add(new Exchange("entry", "fanout"));
        manifest.getExchanges().add(new Exchange("apps", "topic"));
        manifest.getExchanges().add(new Exchange("users", "headers"));

        manifest.getQueues().add(new Queue("app1"));
        manifest.getQueues().add(new Queue("app2"));
        manifest.getQueues().add(new Queue("johndoe"));
        manifest.getQueues().add(new Queue("janedoe"));

        manifest.getBindings().add(
                Binding.builder().fromExchange("entry").toExchange("apps").build());
        manifest.getBindings().add(
                Binding.builder().fromExchange("entry").toExchange("users").build());

        manifest.getBindings().add(
                Binding.builder().fromExchange("apps").toQueue("app1").routingKey("security.events.#").build());
        manifest.getBindings().add(
                Binding.builder().fromExchange("apps").toQueue("app1").routingKey("search.events.#").build());
        manifest.getBindings().add(
                Binding.builder().fromExchange("apps").toQueue("app2").routingKey("db.events.#").build());
        manifest.getBindings().add(
                Binding.builder().fromExchange("apps").toQueue("app2").routingKey("mgmt.events.#").build());
        manifest.getBindings().add(
                Binding.builder().fromExchange("users").toQueue("johndoe").arg("x-match", "all").arg("TO", "johndoe").build());
        manifest.getBindings().add(
                Binding.builder().fromExchange("users").toQueue("janedoe").arg("x-match", "all").arg("TO", "janedoe").build());

        // Services and users can only write to "entry" and read from their own queue.
        manifest.getPermissions().add(
                Permission.builder().user("app1").configureNone().writeOnly("entry").readOnly("app1").build());
        manifest.getPermissions().add(
                Permission.builder().user("app2").configureNone().writeOnly("entry").readOnly("app2").build());
        manifest.getPermissions().add(
                Permission.builder().user("johndoe").configureNone().writeOnly("entry").readOnly("johndoe").build());
        manifest.getPermissions().add(
                Permission.builder().user("janedoe").configureNone().writeOnly("entry").readOnly("janedoe").build());

        ManifestPersister.saveToFile(manifest, new File("manifests/manifest.yaml"));
        ManifestPersister.saveToFile(manifest, new File("manifests/manifest.json"));
        ManifestPersister.saveToFile(manifest, new File("manifests/manifest.xml"));

    }
}
