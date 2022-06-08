package origin.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @Author:jarvmuqiliu
 * @Date: 2022/5/15
 * @Desc:
 **/
public class JsonTest {

    public static void main(String[] args) throws IOException {
        View v = new View();
        v.setShapes(new ArrayList<>(Arrays.asList(Rectangle.of(3, 6), Circle.of(5))));

        System.out.println("-- serializing --");
        ObjectMapper om = new ObjectMapper();
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE, JsonTypeInfo.As.PROPERTY);
        String s = om.writeValueAsString(v);
        System.out.println(s);

        System.out.println("-- deserializing --");
        View view = om.readValue(s, View.class);
        System.out.println(view);
    }
}
