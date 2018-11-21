package spi;

/**
 * Created by fitz.li on 2017/1/25.
 */
public class Client {

    public static void main(String[] args) {
        Spi spi = SpiFactory.getSpi("SPIB");
        System.out.println(spi.sayHello());
    }

}
