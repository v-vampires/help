package stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fitz.li on 2018/10/28.
 */
public class Dish {
    private final String name;
    private final boolean vegetarian;
    private final int calories;
    private final Type type;

    public String getName() {
        return name;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public int getCalories() {
        return calories;
    }

    public Type getType() {
        return type;
    }

    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }
    public enum Type { MEAT, FISH, OTHER }

    public static void main(String[] args) {
        List<Dish> menu = Arrays.asList(
        new Dish("pork", false, 800, Dish.Type.MEAT),
        new Dish("prawns", false, 300, Dish.Type.FISH),
        new Dish("beef", false, 700, Dish.Type.MEAT),
        new Dish("chicken", false, 400, Dish.Type.MEAT),
        new Dish("french fries", true, 530, Dish.Type.OTHER),
        new Dish("rice", true, 350, Dish.Type.OTHER),
        new Dish("season fruit", true, 120, Dish.Type.OTHER),
        new Dish("pizza", true, 550, Dish.Type.OTHER),
        new Dish("salmon", false, 450, Dish.Type.FISH) );

        List<String> names = menu.stream().filter(d -> {
            System.out.println("filter:" + d.getName());
            return d.getCalories() > 300;
        }).sorted(Comparator.comparing(Dish::getCalories)).map(d -> {
            System.out.println("map:" + d.getName());
            return d.getName();
        }).skip(2).limit(3).collect(Collectors.toList());
        System.out.println(names);

        System.out.println("---------");
        List<String> words = Arrays.asList("hello","world","all","text");
        List<String> list = words.stream()
                .map(w -> {
                    System.out.println("map:"+ w);
                    return w.split("");
                })
                .flatMap(ws ->{
                    System.out.println("flatMap:"+ ws.length);
                    return Arrays.stream(ws);
                })
                .distinct()
                .limit(3)
                .collect(Collectors.toList());
        System.out.println(list);

        List<Integer> a1 = Arrays.asList(1,2,3,4);
        List<Integer> b1 = Arrays.asList(3,4);
        List<int[]> list1 = a1.stream().flatMap(i -> b1.stream().map(j -> new int[]{i, j})).collect(Collectors.toList());
        int[] arr = new int[]{1,2,3};
        Integer max = a1.stream().min((a, b) -> a<b? a:b).get();
        System.out.println(max);
    }

}
