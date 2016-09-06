/**
 * @classname: AirlineConnection
 * 
 * @author White,Tom 
 * @maintainer Hu,Ruinan Aswathanarayana,Naveen	
 * @description Driver class for mapper and reducer
 *
 */

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
//Job Driver for mapper and reducer 
public class AirlineConnection {
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Wrong Syntax: ArlineConnection <input path> <input path>");
			System.exit(-1);
		}
		Job job = new Job();
		job.setJarByClass(AirlineConnection.class);
		job.setJobName("Airline Connection");
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setMapperClass(AirlineConnectionMapper.class);
		job.setReducerClass(AirlineConnectionReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(CustomWritable.class);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
