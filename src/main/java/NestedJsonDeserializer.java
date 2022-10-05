import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class NestedJsonDeserializer<T> extends StdDeserializer<T> {

    public NestedJsonDeserializer(Class<T> vc) {
        super(vc);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        // Get codec and grab root node
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode root = codec.readTree(jsonParser);

        // Get config and java type for given deserializer to construct the default jackson deserializer.
        DeserializationConfig config = ctx.getConfig();
        JavaType type = TypeFactory.defaultInstance().constructType(this._valueClass);
        JsonDeserializer<Object> defaultDes = BeanDeserializerFactory.instance.buildBeanDeserializer(ctx, type, config.introspect(type));

        /* Interface used to indicate deserializers that want to do post-processing after construction but before being
        returned to caller (and possibly cached) and used. This is typically used to resolve references to other
        contained types; for example, bean deserializers use this callback to locate deserializers for contained field
        types. */
        // Resolve deserializer before it can be used.
        if (defaultDes instanceof ResolvableDeserializer) {
            ((ResolvableDeserializer) defaultDes).resolve(ctx);
        }

        JsonParser treeParser = codec.treeAsTokens(root);
        config.initialize(treeParser);

        if (treeParser.getCurrentToken() == null) {
            treeParser.nextToken();
        }

        // Deserialize the object into result.
        T result = (T) defaultDes.deserialize(treeParser, ctx);

        // First pass above populated Jackson @JsonProperty annotations, now this populated fields annotated with
        // @NestedJsonProperty.
        Map<Field, String> nestedFields = new HashMap<>();
        for (Field f : this._valueClass.getDeclaredFields()) {
            if (f.isAnnotationPresent(NestedJsonProperty.class)) {
                NestedJsonProperty annotation = f.getAnnotation(NestedJsonProperty.class);
                nestedFields.put(f, annotation.value());
            }
        }

        // Iterate map of fields and their nested json path.
        // TODO: Support "XPath" style nested elements?
        for (Map.Entry<Field, String> entry : nestedFields.entrySet()) {
            Field field = entry.getKey();
            String[] jPath = entry.getValue().split("\\.");
            JsonNode node = root;
            for (String path : jPath) {
                if (node.has(path)) {
                    node = node.get(path);
                } else {
                    System.out.println("Cannot map node to path: " + path);
                    break;
                    // TODO: Throw error telling user which part of the nested path could not be found.
                }
            }

            // Object map each field to their json counter-part.
            try {
                Class<?> c = field.getType();
                Object obj = mapper.convertValue(node, c);
                field.set(result, obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
