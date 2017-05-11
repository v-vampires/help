package aop;

/**
 * Created by fitz.li on 2017/3/6.
 */
public class SomeService {

    public void doSomething(int time) throws InterruptedException {
        Thread.sleep(time);
        System.out.println("do something");
    }
}
