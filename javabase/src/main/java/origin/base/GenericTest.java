package origin.base;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import static java.lang.annotation.ElementType.METHOD;

/**
 * @Author:lmq
 * @Date: 2020/11/26
 * @Desc:
 **/
public class GenericTest {
    @Target(METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface BogusMyBatisSqlAnnotation {
    }

    public class MyModel {
    }

    public interface BaseMapper<M> {
        public void insert(M model);
    }

    public interface MyModelMapper extends BaseMapper<MyModel> {
        @BogusMyBatisSqlAnnotation
        @Override
        public void insert(MyModel model);
    }

    public static void main(String[] args) {
        printMethodList(MyModelMapper.class);
    }

    public static void printMethodList(Class<?> clazz) {
        System.out.println();
        System.out.println("clazzSimpleName:" + clazz.getSimpleName());
        Method[] interfaceMethods = clazz.getMethods();
        for (Method method : interfaceMethods) {
            System.out.println("method:  " + method);
            System.out.println("method    isSynthetic = " + method.isSynthetic() + ", isBridge = " + method.isBridge());
            if (method.getAnnotations().length > 0) {
                for (Annotation annotation : method.getAnnotations()) {
                    System.out.println("method    Annotation = " + annotation);
                }
            } else {
                System.out.println("method    NO ANNOTATIONS!");
            }
        }
    }
}
