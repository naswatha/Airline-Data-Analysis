import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.util.*;

public class MedianReducer
	extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {
	public boolean DEV_MODE = false;
@Override
	public void reduce(IntWritable Key, Iterable<IntWritable> Values, Context context)
	throws IOException, InterruptedException {
		List<Integer> Temp = new ArrayList<Integer>();
		int Size =0;
		for(IntWritable Value : Values){
			Temp.add(Value.get());
			Size++;	
		}
		QuickMedian QM = new QuickMedian();
		int Median = QM.QMedian(Temp, 0, Size-1);
		context.write(Key, new IntWritable(Median));
	}
}


class QuickMedian {
    static int QMedian(List<Integer> Data, int Left, int Right){
        int Pivot;
        int N;
        N = Right - Left + 1;
        Random rand = new Random();
        while(true)
        {
            Pivot = rand.nextInt(Right - Left + 1) + Left;
            //System.out.println("=====");           
            //System.out.println(Pivot);           
            Pivot = QPartition(Data, Left, Right, Pivot);
            //for(int i=Left; i<=Right; i++)
            //    System.out.println(Data.get(i));
            //System.out.println(Left+","+Right+","+Pivot);
            if (Pivot == N/2)
                return (N%2==0 ? (Data.get(N/2) + Data.get(N/2+1)) / 2 : Data.get(N/2));
            else if(Pivot < N/2)
                Left = Pivot+1;
            else
                Right = Pivot-1;
        }
    }
    static int QPartition(List<Integer> Data, int Left, int Right, int Pivot){
        int t;
        t = Data.get(Right);
        Data.set(Right, Data.get(Pivot));
        Data.set(Pivot, t);
        Pivot = Left;
        for (int i = Left; i < Right; i++){          
            if (Data.get(i) < Data.get(Right)){
                t = Data.get(Pivot);
                Data.set(Pivot, Data.get(i));
                Data.set(i, t);
                Pivot++;
            }           
        }
        t = Data.get(Pivot);
        Data.set(Pivot, Data.get(Right));
        Data.set(Right, t);
        return Pivot;               
    }
}
