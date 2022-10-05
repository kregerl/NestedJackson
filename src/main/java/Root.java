import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = Root.Deserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Root {

    @NestedJsonProperty("interface_data.data")
    public String data;

    @JsonProperty("data")
    public String hello;

    static class Deserializer extends NestedJsonDeserializer<Root> {
        public Deserializer() {
            super(Root.class);
        }
    }
}

