package stream;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Created by fitz.li on 2018/10/28.
 */
public class StreamTest {
    public static void main(String[] args) {
        //reduceTest();
        parallelTest();
    }

    public static  void parallelTest(){
        Random random = new Random();
        int[] ints = IntStream.generate(() -> random.nextInt(10000)).limit(10000).toArray();
        ints[9999] = -10;
        /*long start = System.currentTimeMillis();
        Arrays.stream(ints).sorted().toArray();
        System.out.println(System.currentTimeMillis()-start);
        start = System.currentTimeMillis();
        Arrays.stream(ints).parallel().sorted().toArray();
        System.out.println(System.currentTimeMillis() - start);*/
        Arrays.parallelSort(ints);
    }

    public static void reduceTest(){
        Random random = new Random();
        IntStream.generate(() -> random.nextInt(10000)).limit(10)
                .filter(x -> {
                    System.out.println("filter:" + x);
                    return x > 1000;
                }).reduce(0,(a, b) -> {
                    System.out.println("reduce a:" + a +",b:"+b);
                    return a + b;
        });
    }


}
