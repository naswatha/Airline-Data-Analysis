/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @auth|| Ruinan
 */


import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.conf;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Calendar;

public class MedianMaper
	extends Mapper<LongWritable, Text, IntWritable, IntWritable> {
@Override
	public void map(LongWritable Key, Text Value, Context context)
		throws IOException, InterruptedException {
		int YearWeek;
		double Price ;
		Configuration conf = context.getConfiguration();
		String Cheapest_Carrier = conf.get("Cheapest_Carrier");
		Calendar Cal = Calendar.getInstance();
		FlightPriceParser FParser = new FlightPriceParser();
		if (!FParser.map(Value.toString())){
			return;
		}
		if(FParser.Carrier != Cheapest_Carrier)
			return;
		Cal.set(FParser.Year, FParser.Month-1, FParser.DayOfMonth);
		YearWeek = FParser.Year * 100 + Cal.get(Calendar.WEEK_OF_YEAR);
		context.write(new IntWritable(YearWeek), new IntWritable((int)Math.round(FParser.Price*100)));
	}
}
