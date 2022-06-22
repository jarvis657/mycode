package origin.spring.infra.bean.processors;

import java.lang.reflect.Method;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * @Author:jarvmuqiliu
 * @Date: 2022/6/16
 * @Desc:
 * IntrospectorCleanupListener
 **/
public class BeanTestMethodInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if (method.getName().equalsIgnoreCase("getName")) {
            System.out.println("调用 getName 方法 ");
        } else if (method.getName().equalsIgnoreCase("setName")) {
            objects = new Object[]{"被替换掉啦"};
        }

        Object object = methodProxy.invokeSuper(o, objects);
        return object;
    }
}
