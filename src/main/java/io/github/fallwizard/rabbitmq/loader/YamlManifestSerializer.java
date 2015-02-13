package io.github.fallwizard.rabbitmq.loader;

import org.yaml.snakeyaml.Yaml;

/**
 * @author Richard Clayton (Berico Technologies)
 */
public class YamlManifestSerializer implements ManifestSerializer {

    static Yaml yaml = new Yaml();

    @Override
    public Manifest deserializer(String serializedManifest) {

        return (Manifest) yaml.load(serializedManifest);
    }

    @Override
    public String serialize(Manifest manifest) {

        return yaml.dump(manifest);
    }
}
