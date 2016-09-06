import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class Median {
	public static String Carrier;
	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			System.err.println("Usage: Median <input path> <output path> <carrier>");
			System.exit(-1);
		}
		Carrier = args[2];
		Configuration conf = new Configuration();
		conf.set("Cheapest_Carrier", Carrier);
		Job job = new Job(conf);
		job.setJarByClass(FlightPrice.class);
		job.setJobName("Flight Price");
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.setMapperClass(FlightPriceMaper.class);
		job.setReducerClass(FlightPriceReducer.class);
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(IntWritable.class);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
