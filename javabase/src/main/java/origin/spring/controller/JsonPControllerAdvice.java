package origin.spring.controller;

//import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;
import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * @author muqi.lmq
 * @date 18/11/2016.
 */
//@ControllerAdvice(basePackageClasses = {JsonPControllerAdvice.class})
@Deprecated
/**
 * AbstractJsonpResponseBodyAdvice was deprecated starting from Spring 5.0.7 and 4.3.18 and in version 5.1 it is completely removed. There is no direct replacement in Spring 5.1 and instead of using of insecure JSON-P you should migrate on CORS (Cross-Origin Resource Sharing) which allows you to specify what kind of cross domain requests are authorized
 * @see https://docs.spring.io/spring/docs/5.0.x/spring-framework-reference/web.html#mvc-cors
 */
public class JsonPControllerAdvice {//extends AbstractJsonpResponseBodyAdvice {
//    public JsonPControllerAdvice() {
//        super("callback");
//    }
}
