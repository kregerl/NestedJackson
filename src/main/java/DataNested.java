import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = DataNested.Deserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataNested {
    @NestedJsonProperty("interface_data.number")
    public float number;

    @NestedJsonProperty("interface_data.object")
    public Person person;

    @NestedJsonProperty("interface_data.object2")
    public Element element;

    static class Deserializer extends NestedJsonDeserializer<DataNested> {
        public Deserializer() {
            super(DataNested.class);
        }
    }
}
