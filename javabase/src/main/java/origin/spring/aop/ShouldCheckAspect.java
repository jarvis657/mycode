package origin.spring.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author:lmq
 * @Date: 2020/1/19
 * @Desc:
 **/
@Aspect
public class ShouldCheckAspect {
    @Autowired
    private ApplicationContext applicationContext;
    DefaultParameterNameDiscoverer u = new DefaultParameterNameDiscoverer();

    @Pointcut("@annotation(origin.spring.aop.ShouldCheck)")
    public void shouldCheckAnnotationPointcut() {
    }

    @Before("shouldCheckAnnotationPointcut()")
    public void invokeResourceWithCheck(JoinPoint pjp) throws Throwable {
        Method method = resolveMethod(pjp);
        List<String> paramterNames = getParamterNames(method);
        ShouldCheck annotation = method.getAnnotation(ShouldCheck.class);
        if (annotation == null) {
            // Should not go through here.
            throw new IllegalStateException("ShouldCheck miss for ShouldCheck annotation");
        }
        String[] params = annotation.params();
        Class c = annotation.validatedBy();
        Object bean = applicationContext.getBean(c);
        List<Integer> argx = new ArrayList<>();
        for (int i = 0; i < params.length; i++) {
            String param = params[i];
            if (paramterNames.stream().filter(d -> d.equals(param)).findAny().isPresent()) {
                argx.add(i);
            }
        }
        Object[] args = pjp.getArgs();
        List<ConstraintViolation<Object>> list = argx.stream().map(arg -> ((Validator)bean).validate(args[arg])).flatMap(s -> s.stream()).collect(Collectors.toList());
        if (list.size() > 0) {
            throw new IllegalArgumentException(list.get(0).getMessage());
        }
    }


    public List<String> getParamterNames(Method method) {
        String[] params = u.getParameterNames(method);
        return Arrays.asList(params);
    }

    protected Method resolveMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        Method method = getDeclaredMethodFor(targetClass, signature.getName(), signature.getMethod().getParameterTypes());
        if (method == null) {
            throw new IllegalStateException("Cannot resolve target method: " + signature.getMethod().getName());
        }
        return method;
    }

    private Method getDeclaredMethodFor(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return getDeclaredMethodFor(superClass, name, parameterTypes);
            }
        }
        return null;
    }
}
