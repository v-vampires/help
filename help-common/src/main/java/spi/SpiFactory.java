package spi;

import java.util.ServiceLoader;

/**
 * Created by fitz.li on 2017/1/25.
 * META-INF/services/spi.Spi
 */
public class SpiFactory {
    private static ServiceLoader<Spi> loader = ServiceLoader.load(Spi.class);

    public static Spi getSpi(String name) {
        for (Spi spi : loader) {
            if (spi.isSupport(name)) {
                return spi;
            }

        }
        return null;
    }
}
