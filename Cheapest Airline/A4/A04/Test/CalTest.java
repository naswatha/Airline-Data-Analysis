import java.util.Calendar;
 
/**
 * Get the current week
 *
 * @author www.javadb.com
 * @Tester Ruinan Hu
 */
public class CalTest {
 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
 
        Calendar cal = Calendar.getInstance();
	0 -> Jan, 1 -> Feb
	cal.set(1989, 0, 2);
        System.out.println("Current week of month: " + cal.get(Calendar.WEEK_OF_MONTH));
        System.out.println("Current week of year:  " + cal.get(Calendar.WEEK_OF_YEAR));
	System.out.println("Current day of week:  " + cal.get(Calendar.DAY_OF_WEEK));	 
    }
}
