package origin.spring.infra.bean.processors;

/**
 * @Author:jarvmuqiliu
 * @Date: 2022/6/17
 * @Desc:
 **/

import java.lang.reflect.Method;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * MyAnnotation注解
 *
 * @author Leon
 * @date 2020-12-07 11:06
 */
@interface MyAnnotation {

}

public class MyAnnotationLookup implements MethodIntrospector.MetadataLookup<MyAnnotation> {

    /**
     * 查询该方法上的第一个的MyAnnotation注解并返回
     */
    @Override
    public MyAnnotation inspect(Method method) {
        return AnnotatedElementUtils.findMergedAnnotation(method, MyAnnotation.class);
    }
}
