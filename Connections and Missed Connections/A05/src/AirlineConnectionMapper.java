/**
 * @classname: AirlineConnectionMapper
 * 
 * @author Hu,Ruinan Aswathanarayana,Naveen	
 * @description Mapper outputs key value pair, Text as key and CustomWritable as Value
 * (Key,Value) => 
 * (<YEAR,CARRIER,INTERMEDIATE-CITY>,<DESTINATION/ORIGIN_CITY,FLAG,SCHEDULED-DEPARTURE/ARRIVAL-TIME,ACTUAL-DEPARTURE/ARRIVAL-TIME>)
 *
 */


import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.util.Calendar;

/**
 * Input: csv records
 * Ouput: Key Value pairs. 
 *	Key: Year Carrier and Intermmediate City
 *	Value: 	Type of the flight 1 to intermediate city 0 from intermediate city
 *		actual arr time/actual dep time
 *		CRS arr time/CRS dep time	
 */
public class AirlineConnectionMapper
	extends Mapper<LongWritable, Text, Text, CustomWritable> {
@Override
	public void map(LongWritable Key, Text Value, Context context)
		throws IOException, InterruptedException {

		AirlineParser FParser = new AirlineParser();
		if (!FParser.map(Value.toString())){
			return;
		}
		//to convert the time data into offset in milliseconds using Calendar.
		Calendar fightCal = Calendar.getInstance();
		Calendar actualTime = Calendar.getInstance();

                int crsOffset=0;
                int actualOffset=0;
                if (FParser.CRSArrTime < FParser.CRSDepTime){
                        crsOffset = 24*60;
                }
                if (FParser.ArrTime < FParser.DepTime){
                        actualOffset= 24*60;
                }
	
		//0 From Intermediate to Dst
		fightCal.set(FParser.Year, FParser.Month-1, FParser.DayOfMonth, FParser.CRSDepTime/100, FParser.CRSDepTime%100);	
		actualTime.set(FParser.Year, FParser.Month-1, FParser.DayOfMonth, FParser.DepTime/100, FParser.DepTime%100);
		Text KeyOut1 = new Text(FParser.Year + "," + FParser.Carrier+","+FParser.Origin);
		CustomWritable ValueOut = new CustomWritable(FParser.Destination, 0, fightCal.getTimeInMillis()/(1000*60),actualTime.getTimeInMillis()/(1000*60),FParser.DayOfMonth, FParser.DepTime, Integer.toString(FParser.DayOfMonth));	
		
		//1 From Origin to Intermediate
		fightCal.set(FParser.Year, FParser.Month-1, FParser.DayOfMonth, FParser.CRSArrTime/100, FParser.CRSArrTime%100);
		actualTime.set(FParser.Year, FParser.Month-1, FParser.DayOfMonth, FParser.ArrTime/100, FParser.ArrTime%100);
		Text KeyOut2 = new Text(FParser.Year + "," + FParser.Carrier + "," + FParser.Destination);
		CustomWritable ValueOu1 = new CustomWritable(FParser.Origin, 1, fightCal.getTimeInMillis()/(1000*60)+crsOffset, actualTime.getTimeInMillis()/(1000*60)+actualOffset,FParser.DayOfMonth, FParser.ArrTime, Integer.toString(FParser.DayOfMonth));	
		context.write(KeyOut2, ValueOu1);
		context.write(KeyOut1, ValueOut);
	}
}
