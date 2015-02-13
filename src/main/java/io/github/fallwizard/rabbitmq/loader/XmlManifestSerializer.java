package io.github.fallwizard.rabbitmq.loader;

import com.thoughtworks.xstream.XStream;
import io.github.fallwizard.rabbitmq.mgmt.model.*;

/**
 * @author Richard Clayton (Berico Technologies)
 */
public class XmlManifestSerializer implements ManifestSerializer {

    static XStream xstream = new XStream();

    static {

        xstream.alias("manifest", Manifest.class);
        xstream.alias("exchange", Exchange.class);
        xstream.alias("queue", Queue.class);
        xstream.alias("binding", Binding.class);
        xstream.alias("user", User.class);
        xstream.alias("connection", ConnectionInfo.class);
        xstream.alias("vhost", VirtualHost.class);
        xstream.alias("permission", Permission.class);

        xstream.omitField(Queue.class, "memory");
        xstream.omitField(Queue.class, "idle_since");
        xstream.omitField(Queue.class, "policy");
        xstream.omitField(Queue.class, "exclusive_consumer_tag");
        xstream.omitField(Queue.class, "messages_ready");
        xstream.omitField(Queue.class, "messages_unacknowledged");
        xstream.omitField(Queue.class, "messages");
        xstream.omitField(Queue.class, "consumers");
        xstream.omitField(Queue.class, "active_consumers");
        xstream.omitField(Queue.class, "backing_queue_status");
        xstream.omitField(Queue.class, "node");
        xstream.omitField(Queue.class, "messages_details");
        xstream.omitField(Queue.class, "messages_ready_details");
        xstream.omitField(Queue.class, "messages_unacknowledged_details");
    }

    @Override
    public Manifest deserializer(String serializedManifest) {

        return (Manifest) xstream.fromXML(serializedManifest);
    }

    @Override
    public String serialize(Manifest manifest) {

        return xstream.toXML(manifest);
    }
}
