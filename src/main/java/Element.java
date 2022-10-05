import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = Element.Deseralizer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Element {

    @NestedJsonProperty("nested.element")
    public String element;

    static class Deseralizer extends NestedJsonDeserializer<Element> {
        public Deseralizer() {
            super(Element.class);
        }
    }
}
