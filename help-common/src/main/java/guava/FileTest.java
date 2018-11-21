package guava;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.google.common.base.Charsets.UTF_8;

/**
 * Created by fitz.li on 2018/7/5.
 */
public class FileTest {
    public static void main(String[] args) throws IOException {
        List<String> strings = Files.readLines(new File("D:/fuzzy.sql"), UTF_8);
        for (String string : strings) {
            System.out.println(string + "###");
        }
    }
}
