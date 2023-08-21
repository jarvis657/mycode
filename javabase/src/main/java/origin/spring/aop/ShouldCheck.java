package origin.spring.aop;

import javax.validation.Validator;
import java.lang.annotation.*;

/**
 * @Author:lmq
 * @Date: 2020/1/8
 * @Desc:
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
//@Constraint(validatedBy = )
public @interface ShouldCheck {
    String[] params() default {};
    Class validatedBy() default Validator.class;
}
