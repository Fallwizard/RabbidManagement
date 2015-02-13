package io.github.fallwizard.rabbitmq.httpclient.deserializers;

import com.google.gson.*;
import io.github.fallwizard.rabbitmq.mgmt.model.Parameter;
import io.github.fallwizard.rabbitmq.mgmt.model.Parameters;

import java.lang.reflect.Type;

/**
 * @author Richard Clayton (Berico Technologies)
 */
public class ParameterDeserializer implements JsonDeserializer<Parameter> {

    static Gson gson = new Gson();

    @Override
    public Parameter deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

        JsonObject obj = jsonElement.getAsJsonObject();

        JsonElement value = obj.get("value");

        if (value.isJsonObject()){

            return gson.fromJson(jsonElement, Parameters.MapParameter.class);
        }

        return gson.fromJson(jsonElement, Parameters.StringParameter.class);
    }
}
