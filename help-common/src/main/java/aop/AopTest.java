package aop;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by fitz.li on 2017/3/6.
 */
public class AopTest {
    @Test
    public void intecptorTest() throws InterruptedException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/spring.xml");
        SomeService someService = (SomeService)ctx.getBean("someService");
        someService.doSomething(50);
    }
}
