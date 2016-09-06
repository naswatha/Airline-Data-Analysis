import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.commons.lang.text.StrTokenizer;


/**
 * Mapper class.
 *
 * @author Naveen, Ruinan
 */

public class LeastExpensiveMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {
    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        // get the parameter from the driver
        Configuration conf = context.getConfiguration();
        String LeastCarrier = conf.get("carrier");

        String line = value.toString();
        StrTokenizer t = new StrTokenizer(line, ',','"');
        t.setIgnoreEmptyTokens(false);
        String[] columns = t.getTokenArray();

        String carrier;
        String flightDate;
        int weekNumber;
        DecimalFormat digits = new DecimalFormat("00");


        //
        if ((columns[8].equals(LeastCarrier)) && columns.length == 110 && Double.parseDouble(columns[109]) < 100000 && isSanityPass(columns)) {
            //extract carrier and price and put to map

            flightDate = columns[5];

            try {
                weekNumber = getWeekNumber(flightDate);
            } catch (ParseException e) {
                return;
            }

            carrier = columns[0]+digits.format(weekNumber);
            Double price = Double.parseDouble(columns[109]);
            context.write(new Text(carrier), new DoubleWritable(price));
            carrier = null;
            flightDate = null;
        }
    }

    /**
     * given the date as input provides the week number
     * in that year
     *
     * @author Naveen, Ruinan
     */


    private static int getWeekNumber (String flightDate) throws ParseException {

        String format = "yyyy-MM-dd";

        SimpleDateFormat df = new SimpleDateFormat(format);
        Date date = df.parse(flightDate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.WEEK_OF_YEAR);

        return week;
    }

    /**
     * sanity test method class.
     *
     * @author Naveen, Ruinan
     */
    private static boolean isSanityPass(String[] line) {

        try {

            // assigning all the values to integer variables
            int crsArrTime = Integer.parseInt(line[40]);
            int crsArrTimeHoursPart = crsArrTime / 100 ;
            int crsArrTimeMinutesPart = crsArrTime % 100;
            int finalCrsArrTime = (crsArrTimeHoursPart * 60) + crsArrTimeMinutesPart;
            int crsDepTime = Integer.parseInt(line[40]);
            int crsDepTimeHoursPart = crsDepTime / 100 ;
            int crsDepTimeMinutesPart = crsDepTime % 100;
            int finalcrsDepTime = (crsDepTimeHoursPart * 60) + crsDepTimeMinutesPart;
            int crsElapsedTime = Integer.parseInt(line[50]);
            int timeZone = (finalCrsArrTime - finalcrsDepTime - crsElapsedTime);
            int originAirportId = Integer.parseInt(line[11]);
            int originAirportSequenceId = Integer.parseInt(line[12]);
            String origin = line[14];
            String originCityName = line[15];
            int originStateFips = Integer.parseInt(line[17]);
            String originStateName = line[18];
            int originWac = Integer.parseInt(line[19]);
            int destinationAirportId = Integer.parseInt(line[20]);
            int destinationAirportSequenceId = Integer.parseInt(line[21]);
            String destination = line[23];
            String destinationCityName = line[24];
            int destinationStateFips = Integer.parseInt(line[26]);
            String destinationStateName = line[27];
            int destinationWac = Integer.parseInt(line[28]);
            boolean cancelled = Boolean.parseBoolean(line[47]);
            int arrTime = Integer.parseInt(line[41]);
            int arrTimeHoursPart = arrTime / 100 ;
            int arrTimeMinutesPart = arrTime % 100;
            int finalArrTime = (arrTimeHoursPart * 60) + arrTimeMinutesPart;
            int depTime = Integer.parseInt(line[30]);
            int depTimeHoursPart = depTime / 100 ;
            int depTimeMinutesPart = depTime % 100;
            int finalDepTime = (depTimeHoursPart * 60) + depTimeMinutesPart;
            int actualElapsedTime = Integer.parseInt(line[51]);
            int arrDelay = Integer.parseInt(line[42]);
            double arrDelayMins;

            // if arrival delay is less than zero
            if (arrDelay < 0) {
                arrDelayMins = 0;
            }
            else {
                arrDelayMins = arrDelay;
            }

            //if crsarrtime is zero or departure time is zero
            boolean arrDel15 = Boolean.parseBoolean(line[44]);
            if (crsArrTime == 0 || crsDepTime == 0) {
                return false;
            }

            //check the timzone
            if (timeZone % 60 != 0) {
                return false;
            }

            //various sanity test for flights not cancelled.
            if (originAirportId <= 0 || originAirportSequenceId <= 0 || originAirportSequenceId <= 0 || originStateFips <= 0 || originWac <= 0) {
                return false;
            }
            if (destinationAirportId <= 0 || destinationAirportSequenceId <= 0 || destinationAirportSequenceId <= 0 || destinationStateFips <= 0 || destinationWac <= 0) {
                return false;
            }
            if (origin.isEmpty() || destination.isEmpty() || originCityName.isEmpty() || originStateName.isEmpty() ||
                    destinationCityName.isEmpty() || destinationStateName.isEmpty()) {
                return false;
            }
            if (!cancelled) {
                if ((finalArrTime - finalDepTime - actualElapsedTime - timeZone) != 0) {
                    return false;
                }
                if (arrDelay > 0) {
                    if (arrDelay != arrDelayMins) {
                        return false;
                    }
                }
                if (arrDelay < 0) {
                    if (arrDelayMins != 0) {
                        return false;
                    }
                }
                if (arrDelayMins >= 15) {
                    if (!arrDel15) {
                        return false;
                    }
                }
                return true;
            }
        } catch (NumberFormatException e) {
            return true;
        }
        return true;
    }

}