package test;

/**
 * Created by fitz.li on 2018/10/3.
 */
public class LinkedListTest {

    public static void main(String[] args) {
        LinkedList linkedList = new LinkedList();
        linkedList.add("a");
        linkedList.add("b");
        linkedList.add("c");
        linkedList.add("a");
        linkedList.add("a");
        int size = 5;

    }

    public static boolean isHuiwen(LinkedList linkedList) {
        Node first = linkedList.getFirst();
        int size = linkedList.getSize();
        Node head = null;
        Node foot = null;
        for (int a = 0; a < size / 2; a++) {
            head = head == null ? first : head.next;
            foot = head;
            for (int i = a; i < size - 1 - a; i++) {
                foot = foot.next;
            }
            if (!head.getData().equals(foot.getData())) {
                return false;
            }
        }
        return true;
    }


    static class LinkedList{
        private Node first;
        private Node last;
        public void add(String s){
            Node newNode = new Node();
            newNode.setData(s);
            if(first == null){
                first = newNode;
            }
            if(last == null){
                last = newNode;
            }else{
                last.next = newNode;
                last = newNode;
            }
        }

        public Node getFirst() {
            return first;
        }

        public int getSize(){
            return 0;
        }

    }

    static class Node{
        private String data;
        private Node next;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }
}
