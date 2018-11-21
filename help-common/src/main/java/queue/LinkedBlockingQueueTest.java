package queue;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by fitz.li on 2018/11/16.
 */
public class LinkedBlockingQueueTest {

    static Random r = new Random();

    static class Provider implements Runnable{
        private LinkedBlockingQueue<Integer> queue;

        public Provider(LinkedBlockingQueue queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                for (int o = 0; o < 5; o++) {
                    int i = r.nextInt(100);
                    queue.put(i);
                    System.out.println("put:" + i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    static class Consumer implements Runnable{
        private LinkedBlockingQueue<Integer> queue;

        public Consumer(LinkedBlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while(true){
                    Integer integer = queue.take();
                    System.out.println("take:" + integer);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>(10);
        for(int i = 0;i < 10;i++){
            new Thread(new Provider(queue)).start();
        }
        new Thread(new Consumer(queue)).start();
    }
}
