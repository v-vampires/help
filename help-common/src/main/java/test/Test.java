package test;

/**
 * Created by fitz.li on 2017/2/16.
 */
public class Test {

    public static void main(String[] args) {
        int[] arrayDate = getArrayDate(1211104);
        System.out.println(arrayDate[0]);
        System.out.println(arrayDate[0]+"," + arrayDate[1]);
        System.out.println("abc".getBytes().length);
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

}
