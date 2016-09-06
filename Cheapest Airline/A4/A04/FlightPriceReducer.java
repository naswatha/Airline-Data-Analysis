import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class FlightPriceReducer
	extends Reducer<Text, Text, Text, Text> {
	public boolean DEV_MODE = false;
@Override
	public void reduce(Text Key, Iterable<Text> Values, Context context)
	throws IOException, InterruptedException {
		double SumY = 0;
		int SumX = 0;
		double SumXY=0;
		double SumXX=0;
		double AveX = 0;
		double AveY = 0;
		double AveXY = 0;
		double AveXX = 0;
		int Counter = 0;
		boolean Active = Boolean.FALSE;
		for (Text Value : Values) {
			double y = 0;
			int x = 0;
			x = Integer.parseInt(Value.toString().split(",", 3)[0]);
			y = Double.parseDouble(Value.toString().split(",", 3)[1]);
			if (DEV_MODE)
				System.out.println(x+","+y);
			SumX += x;
			SumY += y;
			SumXY += x*y;
			SumXX += x*x;
			Counter++;
		}
		AveX = SumX / Counter;
		AveY = SumY / Counter;
		AveXY = SumXY / Counter;
		AveXX = SumXX / Counter;
		double Beta,Alpha;
		Beta = (AveXY - AveX*AveY)/(AveXX - AveX*AveX);
		Alpha = AveY - Beta*AveX;
		context.write(Key, new Text(Double.toString(Beta) + " " + Double.toString(Alpha)));
	}
}
