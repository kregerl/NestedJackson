# Nested Jackson
A small nested json deserializer for Jackson
### Example:
Use a custom deserializer for every object you wish to get nested objects from.
```java

@JsonDeserialize(using = DataNested.Deserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataNested {
    @NestedJsonProperty("interface_data.number")
    public float number;

    @NestedJsonProperty("interface_data.object")
    public Person person;

    static class Deserializer extends NestedJsonDeserializer<DataNested> {
        public Deserializer() {
            super(DataNested.class);
        }
    }
}
```

Compatible with normal JsonProperty annotations.  
Every object using a subclass of NestedJsonDeserializer must ignore unknown properties otherwise it will not work
correctly.    
See json in Main.java for more info.  