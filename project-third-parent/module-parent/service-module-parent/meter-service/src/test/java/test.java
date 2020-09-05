import com.dotop.smartwater.dependence.core.utils.DateUtils;

import java.util.Date;

public class test {

    public static void main(String args[]) {
        int i = DateUtils.hoursBetween(DateUtils.parseDatetime("2019-10-18 12:19:30"), new Date());
        System.out.println(i);
    }
}
