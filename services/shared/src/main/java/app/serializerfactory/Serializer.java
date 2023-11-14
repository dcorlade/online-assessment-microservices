package app.serializerfactory;

public interface Serializer {
    String serialize(Object object);

    Object deserialize(String string, Class<?> classT);
}
