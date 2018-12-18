package designPattern.single;

/**
 * Created by fitz.li on 2018/11/22.
 */
public class Singleton {
    private Singleton() {
        System.out.println("Singleton is create");
    }
    private static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }
    public static Singleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
