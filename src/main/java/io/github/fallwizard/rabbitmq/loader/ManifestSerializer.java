package io.github.fallwizard.rabbitmq.loader;

/**
 * @author Richard Clayton (Berico Technologies)
 */
public interface ManifestSerializer {

    Manifest deserializer(String serializedManifest);

    String serialize(Manifest manifest);

}
