package test;

import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.*;
import org.joda.time.LocalDate;
import org.joda.time.Seconds;

import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fitz.li on 2017/2/16.
 */
public class Test {

    private static final String line = "------------------------";

    public static void main(String[] args) {
        int[] arrayDate = getArrayDate(1211104);
        System.out.println(arrayDate[0]);
        System.out.println(arrayDate[0]+"," + arrayDate[1]);
        System.out.println("abc".getBytes().length);
        int holdSize = (int) Math.ceil(5 * 30/100.0);
        System.out.println(holdSize);
        List<O1> list = Lists.newArrayList();
        list.add(new O1("1","f1"));
        list.add(new O1("2","f2"));
        Collection<String> keepIds = Collections2.transform(list, new Function<O1, String>() {
            @Override
            public String apply(O1 input) {
                return input.getId();
            }
        });
        List<String> ids = new ArrayList<>(keepIds);
        ids.addAll(Sets.newHashSet("123"));
        System.out.println(ids);
        float x = (float) 1/2;
        System.out.println(x);
        long current = System.currentTimeMillis();
        LocalDate start = new LocalDate(current);
        LocalDate end = new LocalDate("2017-07-05");
        System.out.println((int)current/1000);
        int seconds = Seconds.secondsBetween(start, end).getSeconds() - (int)current/1000;
        System.out.println(seconds);

        MessageFormat format = new MessageFormat("ab,cc,dd,ff");
        System.out.println(format.format(new Object[]{"11","22","33"}));

        Pattern p = Pattern.compile("FSH\\|.*_RF");
        Matcher m = p.matcher("FSS|N_RF");
        System.out.println("pattern matcher:" + m.matches());

        System.out.println(line);

        List<String> cabins = getCabins("/_C/D");
        System.out.println(cabins);

        System.out.println(line);

        Multimap<String, String> map = TreeMultimap.create();
        map.put("A","2");
        map.put("A","1");
        map.put("A","3");
        map.put("B","5");
        map.put("B","6");
        map.put("B","4");
        System.out.println(map.asMap());

        System.out.println(line);
        System.out.println("com.qunar.flight.inter.twell.web.controller.HelpController".replaceAll("\\.","/"));
        List<String> all = all();
        System.out.println(all.hashCode());
        all.add("333");
        System.out.println(all.hashCode());
        System.out.println(line);
        for(int code =1 ; code<=3; code++){
            for (int pos = 1; pos <=2; pos++){
                System.out.println("code:"+ code + ",pos:" + pos + ",result:" + ((code >> (pos - 1) & 1) != 0));
            }
        }
        System.out.println(line);
        Map<String, String> map1 = Maps.newHashMap();
        String s = "11," + map1.get("111");
        System.out.println(s);
        String[] split = s.split(",");
        System.out.println(split.length);


        map1.put("AA", null);
        map1.put("BB", "");
        System.out.println(map1);


        System.out.println(line);
        long now = System.currentTimeMillis();
        int shift = 3;
        System.out.println(now);
        now = now/1000L*10000L+shift*1000+now%1000L;
        System.out.println(now);
        //                  10.90.57.15:l:-6ba56eac:1667fe3f068:-7fb2
        System.out.println("10.90.57.15:l:-6ba56eac:1667fe3f068:-7fb3".hashCode());
        System.out.println("10.90.57.15:l:-6ba56eac:1667fe3f068:-7fb4".hashCode());
        System.out.println("10.90.57.15:l:-6ba56eac:1667fe3f068:-7fb5".hashCode());

        StringBuilder t = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            t.append("'").append(i).append("'").append(",");
        }
        t.deleteCharAt(t.length()-1);
        System.out.println(t.toString());
    }

    public static List<String> all() {
        List<String> all = Lists.newArrayList();
        all.addAll(Lists.newArrayList("1","2"));

        all.addAll(Lists.newArrayList("3","4"));
        return all;
    }

    public static List<String> getCabins(String cabin){
        List<String> cabins = Lists.newArrayList();
        if(Strings.isNullOrEmpty(cabin)){
            return cabins;
        }
        cabin = cabin.replace("_", "/");
        cabin = cabin.replace("~", "/");
        cabins = Splitter.on("/").splitToList(cabin);
        return cabins;
    }

    /**
     * 往返的日期是两程的日期拼接起来的，此处把其解析出来
     * @param date
     * @return
     */
    public static int[] getArrayDate(int date){
        int date1 = date % 10000;
        int date2 = 0;
        if(date / 10000 > 0){
            date2 = (date / 10000) % 10000;
        }

        if(date2 == 0){
            return new int[]{date1};
        }else{
            return new int[]{date2, date1};
        }
    }

    static class O1{
        private String id;
        private String ff;

        public O1(String id, String ff) {
            this.id = id;
            this.ff = ff;
        }

        public String getId() {
            return id;
        }

        public String getFf() {
            return ff;
        }
    }

}
