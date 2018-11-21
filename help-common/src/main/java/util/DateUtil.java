package util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by fitz.li on 2018/8/2.
 */
public class DateUtil {

    private static final long ONE_HOUR = 60 * 60 * 1000L;

    public static String formateYYYYMMDDHH(long instant){
        DateTime datetime = new DateTime(instant);
        return formatDate(datetime, Constants.YYYYMMDDHH);
    }

    /**
     * 将日期转为指定格式的字符串
     *
     * @param datetime
     * @param dateFormatter
     * @return
     */
    public static String formatDate(DateTime datetime, DateTimeFormatter dateFormatter){
        return dateFormatter.print(datetime);
    }

    public static void main(String[] args) {
        long currTime = System.currentTimeMillis();
        long interVal= 720 * 60 * 60 * 1000L;
        long delTime = currTime - 24 *24 * 60 * 60 * 1000;
        String delVerStr = DateUtil.formateYYYYMMDDHH(delTime);
        int delVersion = Integer.parseInt(delVerStr);
        System.out.println(delVersion);

        long s = 1540368042245L;
        long s1 = 720*60*60*1000L;
        System.out.println(s-s1);
    }
}
