package origin.spring.aop;

/**
 * @author muqi.lmq
 * @date 2018/6/10.
 */
public aspect as {
    pointcut logPointcut():execution(void SayHelloService.say());
    after():logPointcut(){
        System.out.println("记录日志 ...");
    }
}