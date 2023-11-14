package app.json;

import app.serializerfactory.Serializer;
import app.serializerfactory.SerializerFactory;

public class JsonSerializerFactory extends SerializerFactory {
    @Override
    public Serializer createSerializer() {
        return new JsonSerializer();
    }
}
