package guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Created by fitz.li on 2018/2/9.
 */
public class CacheTest {
    public static void main(String[] args) {
        Cache<CacheKey, Integer> cache = CacheBuilder.newBuilder().build();
        CacheKey cacheKey1 = new CacheKey("f1", "f2");
        cache.put(cacheKey1, 1);
        System.out.println(cache.getIfPresent(cacheKey1));
        System.out.println("------");
        CacheKey cacheKey2 = new CacheKey("f1", "f2");
        System.out.println(cache.getIfPresent(cacheKey2));

        System.out.println("123".contains("123"));
    }
}
