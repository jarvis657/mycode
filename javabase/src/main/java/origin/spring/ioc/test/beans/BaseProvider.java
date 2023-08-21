package origin.spring.ioc.test.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * @Author:lmq
 * @Date: 2020/5/29
 * @Desc:
 **/
@Component
public class BaseProvider<T> {
//    @Autowired
//    private T t;
//
//    public T getT() {
//        return t;
//    }
//
//    public void setT(T t) {
//        this.t = t;
//    }
//
    public void doSomething() {
        Class<?> clazz = getClass();
        System.out.println(clazz.getName() + " doSomething");
        Type superclass = clazz.getGenericSuperclass();
        System.out.println("superclass=" + superclass);
    }
//    public void test(T t){
//
//    }
}
