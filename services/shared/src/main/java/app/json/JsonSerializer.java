package app.json;

import app.serializerfactory.Serializer;
import java.lang.reflect.Method;

public class JsonSerializer implements Serializer {

    /**
     * Uses reflections at runtime to get the appropriate method in ObjectToJson,
     * then calls the method.
     *
     * @param object The object to serialize to JSON.
     * @return The JSON String.
     */
    @Override
    public String serialize(Object object) {
        String className = object.getClass().getSimpleName();
        try {
            Method objectToJsonMethod = ObjectToJson.class.getDeclaredMethod(
                Character.toLowerCase(className.charAt(0)) + className.substring(1),
                Object.class);

            return (String) objectToJsonMethod.invoke(null, object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Uses reflections at runtime to get the appropriate method in ObjectToJson,
     * then calls the method.
     *
     * @param string The JSON representation of the object, as String.
     * @param classT The class of the object to deserialize/parse.
     * @return The object parsed from JSON.
     */
    @Override
    public Object deserialize(String string, Class<?> classT) {
        String className = classT.getSimpleName();

        try {
            Method jsonToObjectMethod = JsonToObject.class.getDeclaredMethod(
                Character.toLowerCase(className.charAt(0)) + className.substring(1),
                String.class);
            return jsonToObjectMethod.invoke(null, string);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Classtype not valid.";
    }
}
