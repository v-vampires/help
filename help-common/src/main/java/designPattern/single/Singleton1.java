package designPattern.single;

/**
 * Created by fitz.li on 2018/11/22.
 */
public class Singleton1 {
    private Singleton1(){}
    static Singleton1 instance = null;
    static {
        System.out.println("2222");
        instance = new Singleton1();
    }
    public static Singleton1 getInstance(){
        return instance;
    }
}
