import com.fasterxml.jackson.annotation.JsonProperty;

public class Person {
    @JsonProperty("first_name")
    public String firstName;
    @JsonProperty("last_name")
    public String lastName;

    @Override
    public String toString() {
        return String.format("(%s, %s)", firstName, lastName);
    }
}
