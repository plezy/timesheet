package lu.plezy.tools.json;

import java.io.IOException;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class JsonPropertiesSerializer extends StdSerializer<Properties> {

    private static final long serialVersionUID = 1L;

    public JsonPropertiesSerializer() {
        this(null);
    }

    public JsonPropertiesSerializer(Class<Properties> t) {
        super(t);
    }

    @Override
    public void serialize(Properties props, JsonGenerator jsonGenerator, SerializerProvider provider)
            throws IOException {
        jsonGenerator.writeStartObject();
        props.forEach((key, value) -> {
            try {
                jsonGenerator.writeStringField(key.toString(), value.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        jsonGenerator.writeEndObject();
    }

}
