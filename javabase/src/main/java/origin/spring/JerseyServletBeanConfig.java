package origin.spring;

//import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Author:lmq
 * @Date: 2020/8/25
 * @Desc:
 **/
//@Component
public class JerseyServletBeanConfig {

//    @Bean
//    public ServletRegistrationBean jerseyServlet() {
//
//        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new ServletContainer(), "/rest/v1/*");
//        registrationBean.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyResourceConfig.class.getName());
//
//        return registrationBean;
//    }
}
