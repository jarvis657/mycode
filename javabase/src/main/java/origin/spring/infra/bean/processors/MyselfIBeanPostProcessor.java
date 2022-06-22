package origin.spring.infra.bean.processors;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;

/**
 * @Author:jarvmuqiliu
 * @Date: 2022/6/16
 * @Desc:
 **/
public class MyselfIBeanPostProcessor implements InstantiationAwareBeanPostProcessor {

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        System.out.print("beanName:" + beanName + "执行..postProcessAfterInstantiation");
        return true;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (beanClass == BeanTest.class) {
            System.out.println("beanName:" + beanName + "执行..postProcessBeforeInstantiation 方法");

            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(beanClass);
            enhancer.setCallback(new BeanTestMethodInterceptor());
            BeanTest beanTest = (BeanTest) enhancer.create();
            return beanTest;
        }
        return null;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName)
            throws BeansException {
        System.out.println("beanName:  postProcessProperties    执行..postProcessProperties");
        return pvs;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("执行 postProcessAfterInitialization...");
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("执行..postProcessBeforeInitialization ...");
        return bean;
    }
}

