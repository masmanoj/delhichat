package org.casual.civic.core.infra;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

@Primary
@Component
public class FromJsonHelper {
	
	public boolean parameterExists(final String parameterName, final JsonElement element) {
        if (element == null) { return false; }
        return element.getAsJsonObject().has(parameterName);
    }
	
	public String extractStringNamed(final String parameterName, final JsonElement element) {
		String stringValue = null;
        if (element.isJsonObject()) {
            final JsonObject object = element.getAsJsonObject();
            if (object.has(parameterName) && object.get(parameterName).isJsonPrimitive()) {
                final JsonPrimitive primitive = object.get(parameterName).getAsJsonPrimitive();
                final String valueAsString = primitive.getAsString();
                if (StringUtils.isNotBlank(valueAsString)) {
                    stringValue = valueAsString;
                }
            }
        }
        return stringValue;
    }

}
