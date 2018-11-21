package guava;

/**
 * Created by fitz.li on 2018/2/9.
 */
public class CacheKey {
    private String f1;
    private String f2;

    public CacheKey(String f1, String f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    @Override
    public String toString() {
        return "CacheKey{" +
                "f1='" + f1 + '\'' +
                ", f2='" + f2 + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CacheKey cacheKey = (CacheKey) o;

        if (f1 != null ? !f1.equals(cacheKey.f1) : cacheKey.f1 != null) return false;
        return f2 != null ? f2.equals(cacheKey.f2) : cacheKey.f2 == null;

    }

    @Override
    public int hashCode() {
        int result = f1 != null ? f1.hashCode() : 0;
        result = 31 * result + (f2 != null ? f2.hashCode() : 0);
        return result;
    }
}
