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
    public Object doAround1(ProceedingJoinPoint point) throws Throwable {
        System.out.println("doAround1 start!");
        Object o = point.proceed();
        System.out.println("doAround1 end!");
        return o;
    }

    @Around("anyMethod()")
    public Object doAround2(ProceedingJoinPoint point) throws Throwable {
        System.out.println("doAround2 start!");
        Object o = point.proceed();
        System.out.println("doAround2 end!");
        return o;
    }

}
