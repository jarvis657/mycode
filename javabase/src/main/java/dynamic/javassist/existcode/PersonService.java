package dynamic.javassist.existcode;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @Author:lmq
 * @Date: 2022/4/25
 * @Desc:
 **/
@Controller
public class PersonService {

    @Autowired
    @JsonFormat
    private String name;

    public void getPerson() {
        System.out.println("get Person");
    }

    public void personFly() {
        System.out.println("oh my god,I can fly");
    }
}
