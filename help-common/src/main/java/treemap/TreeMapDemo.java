package treemap;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by fitz.li on 2017/3/6.
 */
public class TreeMapDemo {
    public static void main(String[] args) {
        TreeMap<Integer, String> treeMap = new TreeMap<Integer, String>();
        treeMap.put(1,"one");
        treeMap.put(2,"two");
        //treeMap.put(3,"three");
        treeMap.put(4,"four");
        treeMap.put(5,"five");
        treeMap.put(6,"six");

        SortedMap<Integer, String> sortedMap = treeMap.tailMap(3);
        Integer firstKey = sortedMap.firstKey();
        System.out.println(firstKey);
        System.out.println("========================");
        Multimap<String,String> labels = TreeMultimap.create();

        labels.put("B","3");
        labels.put("B","2");
        labels.put("A","3");
        labels.put("A","2");
        labels.put("A","1");
        System.out.println(labels.values().iterator().next());

        Multimap<String, Label> multimap = TreeMultimap.create();
        multimap.put("A",new Label("5001","5001","5001","A","1"));
        multimap.put("A",new Label("5002","5002","5002","A","2"));
        multimap.put("A",new Label("5003","5003","5003","A","1"));
        System.out.println(multimap.asMap());


        TreeSet<Label> treeSet = new TreeSet<>();
        treeSet.add(new Label("5001","5001","5001","A","3"));
        treeSet.add(new Label("5002","5002","5002","A","1"));
        treeSet.add(new Label("5003","5003","5003","A","2"));

        System.out.println(treeSet);
}


    static class Label implements Comparable<Label>{
        private String code;
        private String name;
        private String desc;
        private String region;
        private String priority;

        public Label(String code, String name, String desc, String region, String priority) {
            this.code = code;
            this.name = name;
            this.desc = desc;
            this.region = region;
            this.priority = priority;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Label label = (Label) o;

            if (code != null ? !code.equals(label.code) : label.code != null) return false;
            if (name != null ? !name.equals(label.name) : label.name != null) return false;
            if (desc != null ? !desc.equals(label.desc) : label.desc != null) return false;
            if (region != null ? !region.equals(label.region) : label.region != null) return false;
            return priority != null ? priority.equals(label.priority) : label.priority == null;

        }

        @Override
        public int hashCode() {
            int result = code != null ? code.hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            result = 31 * result + (desc != null ? desc.hashCode() : 0);
            result = 31 * result + (region != null ? region.hashCode() : 0);
            result = 31 * result + (priority != null ? priority.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Label{" +
                    "code='" + code + '\'' +
                    ", name='" + name + '\'' +
                    ", desc='" + desc + '\'' +
                    ", region='" + region + '\'' +
                    ", priority='" + priority + '\'' +
                    '}';
        }

        @Override
        public int compareTo(Label o) {
            return Integer.valueOf(this.priority) - Integer.valueOf(o.priority);
        }
    }
}
