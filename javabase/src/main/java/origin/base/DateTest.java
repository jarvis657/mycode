package origin.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author:jarvis
 * @Date: 2023/8/21
 * @Desc:
 **/
public class DateTest {

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = "2023-12-30 23:59:59";
        Date date = sdf.parse(dateStr);
        System.out.println("lower date: " + sdf.format(date));

        SimpleDateFormat sdf2 = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        Date date2 = sdf2.parse(dateStr);
        System.out.println("upper date: " + sdf2.format(date2));
    }

}
