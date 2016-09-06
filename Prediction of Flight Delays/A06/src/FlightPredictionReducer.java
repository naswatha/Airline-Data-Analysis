/**
 * @classname: FlightPredictionReducer
 * 
 * @author Hu,Ruinan Aswathanarayana,Naveen	
 * @description Reducer outputs key value pair, Text as key and Text as Value
 * (Key,Value) => 
 * (<MONTH,ORIGIN>,<CARRIER,ORIGIN,DESTINATION,YEAR,MONTH,DAYOFMONTH,CRSARRTIME,CRSDEPTIME,CRSELAPSEDTIME,ARRDEL15>)
 * using the REngine Rserve to call R instance. Directly feeding the data to the R instances without writing to 
 * the filesystem.
 */

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.util.*;
import java.io.*;
import java.lang.Math;
import java.util.Enumeration;
import org.rosuda.REngine.Rserve.*;
import org.rosuda.REngine.*;

public class FlightPredictionReducer
extends Reducer<Text, Text, Text, Text> {
	public boolean DEV_MODE = false;
	/**
	 * @name: reduce
	 * @description: reduce the key value pair from the mapped data and build models using R serve engine.
	 * @param: 
	 * Key, from the map data 
	 * Values, from the map data containing air line details(factors) required to build model.
	 * context, to write the data to file system.
	 * @return: write the models to the file system.
	 */
	@Override
	public void reduce(Text Key, Iterable<Text> Values, Context context)
			throws IOException, InterruptedException {

		// copy the data to the arraylist for further processing.
		int size = 0;
		ArrayList<Text> temp = new ArrayList<Text>();
		Iterable<Text> Values1 = Values;
		for (Text Value : Values) {
			size++;
			Text tempValue = new Text(Value);
			temp.add(tempValue);
		}		
		String carrier[] = new String[size];
		String origin[] = new String[size];
		String destination[] = new String[size];
		int year[] = new int[size];
		int month[] = new int[size];
		int dayOfMonth[] = new int[size];
		int crsArrTime[] = new int[size];
		int crsDepTime[] = new int[size];
		int crsElapsedTime[] = new int[size];
		boolean delay[] = new boolean[size];

		// feed the string data to the array of factors from the mapped dataset.
		int i=0;
		for (Text Value : temp) {
			carrier[i]=(Value.toString().split(",", 20)[0]);
			origin[i]=(Value.toString().split(",", 20)[1]);
			destination[i]=(Value.toString().split(",", 20)[2]);
			year[i]=(Integer.parseInt(Value.toString().split(",", 20)[3]));
			month[i]=(Integer.parseInt(Value.toString().split(",", 20)[4]));
			dayOfMonth[i]=(Integer.parseInt(Value.toString().split(",", 20)[5]));
			crsArrTime[i]=(Integer.parseInt(Value.toString().split(",", 20)[6]));
			crsDepTime[i]=(Integer.parseInt(Value.toString().split(",", 20)[7]));
			crsElapsedTime[i]=(Integer.parseInt(Value.toString().split(",", 20)[8]));
			delay[i] = (Boolean.parseBoolean(Value.toString().split(",", 20)[9]));
			i++;
		}

		// build the dataframe and submit to the Rserve for further building the model.
		REXP x;
		REXPLogical rDelay = new REXPLogical(delay);
		String modelName = Key.toString().split(",", 20)[1] + Key.toString().split(",", 20)[0];
		String trainName = modelName + "_t";
		try {	
			RConnection c = new RConnection("127.0.0.1", 1035);
			c.assign("carrier", carrier);
			c.assign("origin", origin);
			c.assign("destination", destination);
			c.assign("year", year);
			c.assign("month", month);
			c.assign("dayOfMonth", dayOfMonth);
			c.assign("crsArrTime", crsArrTime);
			c.assign("crsDepTime", crsDepTime);
			c.assign("crsElapsedTime", crsElapsedTime);
			c.assign("delay", rDelay);
			x = c.eval(trainName + " = data.frame(carrier, origin, destination, year, month, dayOfMonth, crsArrTime, crsDepTime, crsElapsedTime, delay)");
			x = c.eval("library(randomForest)");
			x = c.eval(modelName + " <- randomForest(delay ~ ., " + trainName + ", ntree=3)");
			x = c.eval("save(" + modelName + ", " + trainName +" ,file='~/r_t/" + modelName + "')");
		} catch (Exception e) {
			System.out.println("EX:"+e);
			e.printStackTrace();
		}	
		context.write(Key, new Text(Integer.toString(size)));
	}
}
