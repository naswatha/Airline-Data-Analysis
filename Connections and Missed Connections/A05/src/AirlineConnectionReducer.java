/**
 * @classname: AirlineConnectionReducer
 * 
 * @author Hu,Ruinan Aswathanarayana,Naveen	
 * @description Reducer outputs key value pair, Text as key and Text as Value
 * (Key,Value) => 
 * (<YEAR,CARRIER>,<CONNECTIONS,MISSEDCONNECTIONS>)
 *
 */

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.util.*;
import java.lang.Math;

public class AirlineConnectionReducer
	extends Reducer<Text, CustomWritable, Text, Text> {
	public boolean DEV_MODE = false;
@Override
	public void reduce(Text Key, Iterable<CustomWritable> Values, Context context)
	throws IOException, InterruptedException {
		ArrayList<CustomWritableComparable> flightTable = new ArrayList<CustomWritableComparable>();
		Calendar cal = Calendar.getInstance(); 
		//adding the list of values to comparable to sort
		for (CustomWritable Value : Values){
			flightTable.add(new CustomWritableComparable(Value.getCityName(), Value.getFlightType(), Value.getFlightTime(), Value.getActualTime(), Value.getFT(), Value.getAT(), Value.getDate()));
		}
		Collections.sort(flightTable);
		
		/*for(CustomWritableComparable flight: flightTable){
			cal.setTimeInMillis((long)flight.actualTime*60*1000);
			context.write(Key, new Text(Integer.toString(flight.flightType)+','+
							Long.toString(flight.actualTime)+','+
							Long.toString(flight.fT)+','+
							flight.date.toString()+","+
							Integer.toString(cal.get(Calendar.YEAR))+","+
							Integer.toString(cal.get(Calendar.MONTH)+1)+","+
							Integer.toString(cal.get(Calendar.DAY_OF_MONTH))+","+
							Integer.toString(cal.get(Calendar.HOUR_OF_DAY))+","+
							Integer.toString(cal.get(Calendar.MINUTE))+","+
							Integer.toString(cal.get(Calendar.SECOND))));
		}*/

		//find the connections 
		int flightI=0;
		int connection=0; 
		int missedConnection=0;
		for(CustomWritableComparable flight: flightTable){
			//check for departure flights
			int flightII=flightI+1;
			if(flightTable.get(flightI).flightType==0){
				flightI++;
				continue;			
			}
			//Check arrival flights for the conditions <= 360 and >= 30 minutes based on scheduled time.
			while(flightII >= 0 && flightII < flightTable.size() && (flightTable.get(flightII).flightTime - flightTable.get(flightI).flightTime) <= (long)(6*60)){
				
				if((flightTable.get(flightII).flightType == 0) && (flightTable.get(flightII).flightTime - flightTable.get(flightI).flightTime) >= (long)(30)){
					connection++;
					if (flightTable.get(flightII).actualTime - flightTable.get(flightI).actualTime < (long)30)
						missedConnection++;	
					//context.write(Key, new Text(Long.toString(flightTable.get(flightI).flightTime) + ","  +Long.toString(flightTable.get(flightII).flightTime)));
				}
				flightII+=1;		
			}
			flightI++;	
		}
		
		
		String Year = Key.toString().split(",", 3)[0];
		String Carrier = Key.toString().split(",", 3)[1];
		context.write(new Text(Year+","+Carrier), new Text(Integer.toString(connection)+","+Integer.toString(missedConnection)));
		
		
		
	}
}


