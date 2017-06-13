package size;

/**
 * Created by fitz.li on 2017/6/6.
 * 查看对象大小
 */
public class Test {
    public static void main(String[] args) {
        long i = Runtime.getRuntime().freeMemory();
        int t = 0;
        char a ='a';
        long l =23410389;
        String s = new String("123");
        Person p = new Person();
        p.setName("122");
        long j = Runtime.getRuntime().freeMemory();
        System.out.println(i-j); // 0
    }

    static class Person {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
