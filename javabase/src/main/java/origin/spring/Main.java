package origin.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import origin.spring.ioc.test.beans.BaseProvider;
import origin.spring.ioc.test.beans.Person;
import origin.spring.ioc.test.beans.User;

import java.util.Properties;

@SpringBootApplication(scanBasePackages = {
        "origin.spring"
})
public class Main extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Main.class);
    }

    public static void main(String[] args) throws Exception {
        Properties properties = System.getProperties();
//        System.out.println(properties);
//        String property = System.getProperty("user.home");
//        System.out.println(property);
        int ii;
        System.out.println("=====================");
        System.out.println("=====================");
        int i = 0xFFFFFFFF;
        int x = i & 0xF;
//        System.out.println(Integer.toBinaryString(x));
//        System.out.println("Hello World!");
//        System.setProperty("spring.devtools.restart.enabled", "false");
//        System.out.println("================end========================");
//        BaseProvider<Person> personBaseProvider = new BaseProvider<>();
//        BaseProvider<User> userBaseProvider = new BaseProvider<User>();
//        System.out.println(personBaseProvider.getClass());
//        System.out.println(userBaseProvider.getClass());
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
//        TestService service = context.getBean(TestService.class);
//        System.out.println("service=" + service);
//        service.doSomething();


        SpringApplication.run(Main.class, args);
//        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder();
//        springApplicationBuilder.build().run(args);
    }
}
