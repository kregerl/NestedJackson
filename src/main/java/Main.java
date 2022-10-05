import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        /**
         * {
         * 	"interface_data": {
         * 		"data": "Testing",
         * 		"number": 12.5,
         * 		"object": {
         * 			"first_name": "first",
         * 			"last_name": "last"
         *                },
         * 		"object2": {
         * 			"nested": {
         * 				"element": "Element"
         *            }
         *        }* 	},
         * 	"data": "Hello world!"
         * }
         */
        String json = "{\n" +
                "\t\"interface_data\": {\n" +
                "\t\t\"data\": \"Testing\",\n" +
                "\t\t\"number\": 12.5,\n" +
                "\t\t\"object\": {\n" +
                "\t\t\t\"first_name\": \"first\",\n" +
                "\t\t\t\"last_name\": \"last\"\n" +
                "\t\t},\n" +
                "\t\t\"object2\": {\n" +
                "\t\t\t\"nested\": {\n" +
                "\t\t\t\t\"element\": \"Element\"\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"data\": \"Hello world!\"\n" +
                "}";

        ObjectMapper mapper = new ObjectMapper();

        Root b = mapper.readValue(json, Root.class);
        DataNested t = mapper.readValue(json, DataNested.class);
        System.out.println("Base: " + b.data);
        System.out.println("Base data: " + b.hello);
        System.out.println("Test: " + t.number);
        System.out.println("Person: "  + t.person);
        System.out.println("Element: "  + t.element.element);

    }
}
