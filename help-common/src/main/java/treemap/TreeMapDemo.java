package treemap;

import java.util.SortedMap;
import java.util.TreeMap;

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
    }
}
