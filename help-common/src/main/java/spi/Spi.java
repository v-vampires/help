package spi;

/**
 * Created by fitz.li on 2017/1/25.
 */
public interface Spi {
    boolean isSupport(String name);
    String sayHello();
}
