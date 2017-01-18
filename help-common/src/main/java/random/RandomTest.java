package random;

import java.util.Random;

/**
 * Created by fitz.li on 2017/1/18.
 * 相同种子的random相同次数的随机值是一样的
 */
public class RandomTest {
    public static void main(String[] args) {
        Random r1 = new Random(11);
        for (int i = 0; i < 3; i++) {
            System.out.println(r1.nextInt(100));
        }
        System.out.println("---------------------------");
        Random r2 = new Random();
        for (int i = 0; i < 3; i++) {
            System.out.println(r2.nextInt(100));
        }
    }
}
