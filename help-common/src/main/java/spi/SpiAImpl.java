package spi;

/**
 * Created by fitz.li on 2017/1/25.
 */
public class SpiAImpl implements Spi {
    @Override
    public boolean isSupport(String name) {
        return"SPIA".equalsIgnoreCase(name.trim());
    }

    @Override
    public String sayHello() {
        return "Hello, is A";
    }
}
