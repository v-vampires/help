package thread;

import com.google.common.collect.Lists;

import java.util.Queue;
import java.util.Random;

/**
 * Created by fitz.li on 2018/11/2.
 */
public class ProducerConsumer {
    static Random random = new Random();
    private static Queue<Integer> queue = Lists.newLinkedList();

    public static void main(String[] args) {
        Product p = new Product();
        new Thread(new Producer(p)).start();
        new Thread(new Consumer(p)).start();
    }

    static class Product{
        public void produce(){
            synchronized (queue){
                while (queue.size() == 10){
                    try {
                        System.out.println("queue is full!");
                        queue.wait();
                        System.out.println("queue produce has been notify!");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int i = random.nextInt(1000);
                queue.offer(i);
                System.out.println("queue produce: " + i);
                queue.notifyAll();
            }
        }

        public void consume(){
            synchronized (queue){
                while (queue.size() == 0){
                    try {
                        System.out.println("queue is empty!");
                        queue.wait();
                        System.out.println("queue consume has been notify!");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Integer poll = queue.poll();
                System.out.println("queue consume: " + poll);
                queue.notifyAll();
            }
        }
    }

    static class Producer implements Runnable{
        private Product product;
        public Producer(Product product) {
            this.product = product;
        }

        @Override
        public void run() {
            while(true){
                product.produce();
            }
        }
    }

    static class Consumer implements Runnable{
        private Product product;

        public Consumer(Product product) {
            this.product = product;
        }

        @Override
        public void run() {
            while(true){
                product.consume();
            }
        }
    }
}
