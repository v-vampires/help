package aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;

/**
 * Created by fitz.li on 2018/12/17.
 * 编码实现aop，可以更好的理解aop原理
 * Aop内部使用的是责任链（拦截器）模式
 */
public class ManualAop {

    interface ITask{
        void execute();
    }

    static class MockTask implements ITask {
        public void execute(){
            System.out.println("execute!");
        }
    }

    static class PerformanceMethodInterceptor1 implements MethodInterceptor{

        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            System.out.println("PerformanceMethodInterceptor1 start!");
            Object o = methodInvocation.proceed();
            System.out.println("PerformanceMethodInterceptor1 end!");
            return o;
        }
    }

    static class PerformanceMethodInterceptor2 implements MethodInterceptor{

        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            System.out.println("PerformanceMethodInterceptor2 start!");
            Object o = methodInvocation.proceed();
            System.out.println("PerformanceMethodInterceptor2 end!");
            return o;
        }
    }

    public static void main(String[] args) {
        MockTask task = new MockTask();
        ProxyFactory weaver = new ProxyFactory(task);
        NameMatchMethodPointcutAdvisor advisor1 = new NameMatchMethodPointcutAdvisor();
        advisor1.setMappedName("execute");
        advisor1.setAdvice(new PerformanceMethodInterceptor1());
        weaver.addAdvisor(advisor1);

        NameMatchMethodPointcutAdvisor advisor2 = new NameMatchMethodPointcutAdvisor();
        advisor2.setMappedName("execute");
        advisor2.setAdvice(new PerformanceMethodInterceptor2());
        weaver.addAdvisor(advisor2);
        /* MockTask proxyObject = (MockTask) weaver.getProxy();*/
        ITask proxyObject = (ITask) weaver.getProxy();
        proxyObject.execute();
    }
}
