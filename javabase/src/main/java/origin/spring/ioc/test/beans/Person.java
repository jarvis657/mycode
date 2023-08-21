package origin.spring.ioc.test.beans;

import org.springframework.stereotype.Component;

/**
 * @Author:lmq
 * @Date: 2020/5/29
 * @Desc:
 **/
@Component
public class Person {
    private Integer age;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
