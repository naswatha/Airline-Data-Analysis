

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.*;


/**
 * Driver class.
 *
 * @author Naveen, Ruinan
 */
public class LeastExpensive extends Configured implements Tool{

    /**
     * main method.
     *
     * initiate toolrunner to run
     * calculate the time taken to perform
     * MR job.
     */
    public static void main(String[] args) throws Exception {

        int res = ToolRunner.run(new Configuration(), new LeastExpensive(), args);
        System.exit(res);
    }

    /**
     * run method implementing Tool interface method.
     *
     *
     */
    @Override
    public int run(String args[]) throws Exception{

        try{

            Configuration conf = new Configuration();

            // file containing the least expensive carrier
            String filePath = readFile(args[2]);
            conf.set("carrier",filePath);

            //System.out.println(args[2]);

            Job job = Job.getInstance(conf);
            job.setJarByClass(LeastExpensive.class);
            job.setJobName("Least Expensive Median Price");

            // file input and out put paths
            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));

            // Set the mapper class
            job.setMapperClass(LeastExpensiveMapper.class);

            // Set the mapper class
            job.setReducerClass(LeastExpensiveReducer.class);

            // map output<Text, DoubleWritable>
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(DoubleWritable.class);

            // output <Text, DoubleWritable>
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(DoubleWritable.class);


            return(job.waitForCompletion(true) ? 0 : 1);

        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error during mapreduce job.");
            return 2;
        }

    }

    /**
     * Method to read the content for the file and
     * returning the contents to run method.
     *
     * references: stackoverflow
     */
    private static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

}