package io.github.fallwizard.rabbitmq.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Richard Clayton (Berico Technologies)
 */
public class JsonManifestSerializer implements ManifestSerializer {

    static Gson gson;

    static {

        gson = new GsonBuilder().setExclusionStrategies(new GsonFieldExclusionStrategy()).create();
    }

    @Override
    public Manifest deserializer(String serializedManifest) {

        return gson.fromJson(serializedManifest, Manifest.class);
    }

    @Override
    public String serialize(Manifest manifest) {

        return gson.toJson(manifest);
    }
}
