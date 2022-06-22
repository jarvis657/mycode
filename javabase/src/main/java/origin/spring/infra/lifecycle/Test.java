package origin.spring.infra.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class Test implements ApplicationContextAware, SmartInitializingSingleton, DisposableBean {

    @Override
    public void afterSingletonsInstantiated() {
        System.out.println("afterSingletonsInstantiated============================================");
        System.out.println("afterSingletonsInstantiated============================================");
        System.out.println("afterSingletonsInstantiated============================================");
        System.out.println("afterSingletonsInstantiated============================================");
        System.out.println("afterSingletonsInstantiated============================================");
        System.out.println("afterSingletonsInstantiated============================================");
        System.out.println("afterSingletonsInstantiated============================================");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("destroy============================================");
        System.out.println("destroy============================================");
        System.out.println("destroy============================================");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("setApplicationContext============================================");
    }
}
