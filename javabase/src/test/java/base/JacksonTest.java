package base;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

/**
 * @Author:lmq
 * @Date: 2020/7/28
 * @Desc:
 **/
public class JacksonTest {


    @Test
    public void test1() throws IOException {
        JsonFactory factory = new JsonFactory();
        // 本处只需演示，向控制台写（当然你可以向文件等任意地方写都是可以的）
        try (JsonGenerator jsonGenerator = factory.createGenerator(System.out, JsonEncoding.UTF8)) {
            jsonGenerator.writeStartObject();

            jsonGenerator.writeFieldName("zhName");
            jsonGenerator.writeString("name");
            // 写对象（记得先写key 否则无效）
            jsonGenerator.writeFieldName("person");
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("enName");
            jsonGenerator.writeString("YourBatman");
            jsonGenerator.writeFieldName("age");
            jsonGenerator.writeNumber(18);
            jsonGenerator.writeEndObject();

            jsonGenerator.writeFieldName("pers");
            jsonGenerator.writeStartArray();
            // 2、写对象
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("enName", "YourBatman");
            jsonGenerator.writeEndObject();
            // 3、写数字
            jsonGenerator.writeNumber(18);
            jsonGenerator.writeEndArray();

            jsonGenerator.writeEndObject();
        }
    }

    @Test
    public void test_decodejson() throws JsonProcessingException {
        String j = "{\"zhName\":\"name\",\"person\":{\"enName\":\"YourBatman\",\"age\":18},\"pers\":[{\"enName\":\"YourBatman\"},18]}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(j);
        System.out.println(jsonNode);
    }

    @Test
    public void test_check_instance() {

    }
}
