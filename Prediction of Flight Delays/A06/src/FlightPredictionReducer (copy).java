/**
 * @classname: FlightPriceReducer
 * 
 * @author Ruinan Hu
 *
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
	extends Reducer<Text, IntWritable, Text, Text> {
	public boolean DEV_MODE = false;
@Override
	public void reduce(Text Key, Iterable<IntWritable> Values, Context context)
	throws IOException, InterruptedException {
                String carrier[]={"AA", "AS", "F9", "WN", "DD", "CV", "WE"};
                int year[] = {1, 2, 3, 4, 5, 2, 7};
                int depTime[] = {12 ,13 ,16, 2, 2, 6, 4};
                boolean delay[] = {true, false, false, true, true, false, false};
                int test[] = {};
                REXP x;
                REXP rx;
                REXPLogical rDelay = new REXPLogical(delay);
                double a[]={};
                try {
                        RConnection c = new RConnection("127.0.0.1", 1035);
                        c.assign("carrier", carrier);
                        c.assign("year", year);
                        c.assign("depTime", depTime);
                        c.assign("delay", rDelay);
                        x = c.eval("df = data.frame(carrier, year, depTime, delay)");
                        test = c.eval("df$depTime").asIntegers();
                        x = c.eval("library(randomForest)");
                        x = c.eval("t1 <- randomForest(delay ~ ., df, ntree=3)");
                        x = c.eval("save.image('~/" + Key.toString() + "')");
                } catch (Exception e) {
                        System.out.println("EX:"+e);
                        e.printStackTrace();
                }	
		context.write(Key, new Text(Integer.toString(test[2])));
	}
}
