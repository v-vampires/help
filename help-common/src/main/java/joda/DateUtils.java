package joda;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by fitz.li on 2017/2/24.
 */
public class DateUtils {

    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
    /**
     * 将日期转为指定格式的字符串
     * @param date
     * @param formatter
     * @return
     */
    public static String formatDate(Date date, DateTimeFormatter formatter){
        if(null == date) return "";
        DateTime dateTime = new DateTime(date.getTime());
        return formatter.print(dateTime);
    }


    public static void main(String[] args) {
        String dateTime = formatDate(new Date(), dateTimeFormatter);
        System.out.println(dateTime);
        System.out.println(dateTime.substring(0,10));
        System.out.println(dateTime.substring(11));
    }
}
