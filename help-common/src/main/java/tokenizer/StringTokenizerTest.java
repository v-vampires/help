package tokenizer;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by fitz.li on 2017/7/31.
 */
public class StringTokenizerTest {

    public static void main(String[] args) {
        /*StringTokenizer stCon = new StringTokenizer("北京,上海", ",");
        while (stCon.hasMoreElements()){
            System.out.println(stCon.nextElement());
        }*/
        Set<String> s = Sets.newTreeSet();
        s.add("bf");
        s.add("ap");
        s.add("dp");
        s.add("0p");
        System.out.println("tree:" + s);

        Set<String> h = Sets.newHashSet();
        h.add("bf");
        h.add("dp");
        h.add("ap");
        h.add("0p");

        System.out.println("hash:" + h);
    }
}
