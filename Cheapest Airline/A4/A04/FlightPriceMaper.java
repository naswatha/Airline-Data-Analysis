/**
 * @classname: FlightPriceMaper
 * 
 * @author Ruinan Hu
 *
 */


import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class FlightPriceMaper
	extends Mapper<LongWritable, Text, Text, Text> {
@Override
	public void map(LongWritable Key, Text Value, Context context)
		throws IOException, InterruptedException {

		int CarrierYear;
		int CRSElapsedTime_Price;
		FlightPriceParser FParser = new FlightPriceParser();
		if (!FParser.map(Value.toString())){
			return;
		}
		Text KeyOut = new Text(FParser.Carrier+" "+Integer.toString(FParser.Year));
		Text ValueOut = new Text(Integer.toString(FParser.CRSElapsedTime)+","+Double.toString(FParser.Price));	
		context.write(KeyOut, ValueOut);
	}
}
