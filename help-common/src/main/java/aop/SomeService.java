package aop;

/**
 * Created by fitz.li on 2017/3/6.
 */
public class SomeService {

    public void doSomething1(int time) throws InterruptedException {
        Thread.sleep(time);
        System.out.println("do something1");
    }

    public void doSomething2(int time) throws InterruptedException {
        Thread.sleep(time);
        System.out.println("do something2");
        doSomething1(time);
    }
}
