package origin.spring.ioc.test.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author:lmq
 * @Date: 2020/5/29
 * @Desc:
 **/
@Component
public class TestService {
    @Autowired
    private BaseProvider<User> userProvdier;
    @Autowired
    private BaseProvider<Person> personBaseProvider;

    public void doSomething() {
        userProvdier.doSomething();
        personBaseProvider.doSomething();
    }
}
