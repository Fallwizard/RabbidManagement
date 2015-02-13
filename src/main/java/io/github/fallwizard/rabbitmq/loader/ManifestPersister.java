package io.github.fallwizard.rabbitmq.loader;

import com.google.common.collect.Maps;
import com.google.common.io.Files;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author Richard Clayton (Berico Technologies)
 */
public class ManifestPersister {

    static final Map<String, ManifestSerializer> serializers = Maps.newHashMap();

    static {
        serializers.put("yml", new YamlManifestSerializer());
        serializers.put("yaml", new YamlManifestSerializer());
        serializers.put("json", new JsonManifestSerializer());
        serializers.put("xml", new XmlManifestSerializer());
    }

    static ManifestSerializer getDefaultSerializer(){

        return serializers.values().iterator().next();
    }

    public static boolean hasSerializerFor(String extension){

        return serializers.containsKey(extension.toLowerCase());
    }

    public static void saveToFile(Manifest manifest, File file) throws Exception {

        String extension = Files.getFileExtension(file.getName()).toLowerCase();

        if (!serializers.containsKey(extension))
            throw new Exception(String.format("No serializer with extension '%s' found.", extension));

        String manifestString = serializers.get(extension).serialize(manifest);

        Files.write(manifestString, file, Charset.defaultCharset());
    }

    public static Manifest loadFromFile(File file) throws Exception {

        String manifestFile = Files.toString(file, Charset.defaultCharset());

        String extension = Files.getFileExtension(file.getName()).toLowerCase();

        if (!serializers.containsKey(extension))
            throw new Exception(String.format("No serializer with extension '%s' found.", extension));

        return serializers.get(extension).deserializer(manifestFile);
    }

}
