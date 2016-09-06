import org.apache.avro.generic.GenericData;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

/**
 * MedianReducer class.
 *
 * @author Naveen, Ruinan
 */
public class LeastExpensiveReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {

    ArrayList<Double> priceList = new ArrayList<Double>();


    @Override
    public void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {

        //median calculation.
        double median = 0;

        for(DoubleWritable value: values){
            priceList.add(value.get());
        }

        //sort the arraylist
        Collections.sort(priceList);
        int size = priceList.size();

        if(size%2 == 0){
            int middle = size/2;
            median = priceList.get(middle);
        }
        else{
            int half = (size+1)/2;
            median = priceList.get(half - 1);
        }

        context.write(key, new DoubleWritable(median));

    }

}