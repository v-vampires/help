package aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by fitz.li on 2017/3/3.
 * spring aop是基于jdk的动态代理或者是CGLIB, jdk动态代理必须要实现接口，而CGLIB则必须有无参构造器
 */
@Aspect
public class MethodTimeInterceptor {
    /**
     * 定义一个切入点
     */
    @Pointcut("execution(* aop.SomeService.*(..))")
    private void anyMethod(){}

    @Around("anyMethod()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();
        Object o = point.proceed();
        System.out.println(point.getTarget().getClass()+"."+ point.getSignature().getName() + " cost time:" + (System.currentTimeMillis() - start));
        return o;
    }

}
