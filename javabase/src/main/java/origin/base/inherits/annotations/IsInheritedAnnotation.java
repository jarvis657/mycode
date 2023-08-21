package origin.base.inherits.annotations;

import java.lang.annotation.*;

/**
 * @Author:lmq
 * @Date: 2020/8/3
 * @Desc:
 **/

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface IsInheritedAnnotation {
}
