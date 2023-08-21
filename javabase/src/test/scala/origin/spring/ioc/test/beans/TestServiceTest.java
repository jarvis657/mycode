package origin.spring.ioc.test.beans;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import origin.spring.Main;

/**
 * @Author:lmq
 * @Date: 2020/5/29
 * @Desc:
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class, properties = {"origin.spring.config.location=classpath:/data/,classpath:/", "origin.spring.config.name=application,datas,redis-config"})
class TestServiceTest {

    @Autowired
    private TestService testService;
    @Test
    void doSomething() {
        testService.doSomething();
    }
}
