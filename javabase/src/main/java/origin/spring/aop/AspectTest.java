package origin.spring.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 远程服务注解，主要用于拦截日志、错误码等方面处理
 */
//@Documented
//@Retention(RetentionPolicy.RUNTIME)
//@Target(value = {ElementType.METHOD, ElementType.TYPE})
//@interface RpcService {
//}

/**
 * 声明切面的几种方式
 *
 * @author muqi.lmq
 * @date 2018/6/9.
 */
public class AspectTest {

//    @Aspect
//    @Component
    public static class ExecutionAspect {

        @Before("execution(* club.myapp.aspectJ.*.before*(..))")
        public void doBefore(JoinPoint joinPoint) throws Throwable {
            System.err.println("这是一个前置通知，在方法调用之前被执行！！！");
        }

        @After("execution(* club.myapp.aspectJ.*.after*(..))")
        public void doAfter(JoinPoint joinPoint) throws Throwable {
            System.err.println("这是一个后置通知，在方法调用之后被执行！！！");
        }

        @Around("execution(* club.myapp.aspectJ.*.around*(..))")
        public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
            System.err.println("这是一个环绕通知，在方法调用前后都会执行！！！");
            System.err.println("执行前");
            final Object proceed = joinPoint.proceed();
            System.err.println("执行后");
            return proceed;
        }

        @Pointcut("execution(public * com.controller..*.*(..))")
        public void pointCut() {
        }

        @Around("pointCut()")
        public Object Access(ProceedingJoinPoint joinPoint) throws Throwable {
            return null;
        }

//        @Aspect
//        @Component
        public static class AnnotationAspect {

            @Around("@annotation(club.myapp.annotation.RpcService)")
            public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
                System.err.println("这是一个环绕通知，在方法调用前后都会执行！！！");
                System.err.println("执行前");
                final Object proceed = joinPoint.proceed();
                System.err.println("执行后");
                return proceed;
            }

        }
    }

}

