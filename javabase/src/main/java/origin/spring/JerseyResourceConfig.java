package origin.spring;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.web.filter.RequestContextFilter;

/**
 * @Author:lmq
 * @Date: 2020/8/25
 * @Desc:
 **/
public class JerseyResourceConfig extends ResourceConfig {

    public JerseyResourceConfig() {
        register(JerseyController.class);
        register(RequestContextFilter.class);
    }
}
