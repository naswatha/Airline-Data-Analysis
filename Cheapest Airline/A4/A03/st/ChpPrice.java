/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @auth|| Ruinan
 */

/*
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputF||mat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputF||mat;
*/
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.zip.GZIPInputStream;
import com.opencsv.CSVReader;
import com.opencsv.CSVParser;
import java.util.*;
import java.util.concurrent.*;


class WorkerThread implements Runnable {
     
    private int id;
	private String path;
	private Map<String, Map<Integer, List<Double>>> Price1;
     
    public WorkerThread(int id, String path, Map<String, Map<Integer, List<Double>>> tPrice){
        this.id=id;
		this.path=path;
		this.Price1 = tPrice;
    }
 
    @Override
    public void run(){
        System.out.println(Thread.currentThread().getName()+" id = "+id);

			GZIPInputStream gzip;
		try{
				gzip = new GZIPInputStream(new FileInputStream(path));
				BufferedReader br = new BufferedReader(new InputStreamReader(gzip));
				CSVReader reader = new CSVReader(new InputStreamReader(gzip));
				String nline;
				nline = br.readLine();
				int i=0;
				int j=0;
				FlightPriceParser FParser = new FlightPriceParser();

				while ((nline = br.readLine()) != null){
					i++;
					//System.out.println(i+","+j);
					if(FParser.map(nline)){
						try{					
							Price1.get(FParser.Carrier).get(FParser.Month).add(FParser.Price);
						}catch(NullPointerException e){
							Map<Integer, List<Double>> t_value = Price1.get(FParser.Carrier);
							if (t_value == null){
								Map<Integer, List<Double>> tttt_value = new HashMap<Integer, List<Double>>();
								List<Double> ttttt_value = new ArrayList();
								ttttt_value.add(FParser.Price);
								tttt_value.put(FParser.Month, ttttt_value);
								Price1.put(FParser.Carrier, tttt_value);
							}
							else{
								List<Double> ttt_value = new ArrayList();
								ttt_value.add(FParser.Price);
								Price1.get(FParser.Carrier).put(FParser.Month, ttt_value);
							}
						}
					}
				j++;
				}
			}catch(Exception e){
				return;
			}
		//for(Map.Entry<String, Map<Integer, List<Double>>> entry : Price1.entrySet()){
		//	System.out.println(entry.getKey()+","+entry.getValue());					
		//}
    }
}


public class ChpPrice {
	public static List<Map<String, Map<Integer, List<Double>>>> Price = new ArrayList<Map<String, Map<Integer, List<Double>>>>();
	public static void main(String[] args) throws Exception {
		if (args.length !=4){
			System.err.println("Usage: ChpPrice <file> <number of threads> <1 avg 2 med> <number of files>");
			System.exit(1);
		}

		//File file = new File("df");
		//FileOutputStream fis = new FileOutputStream(file);
		//PrintStream out = new PrintStream(fis);
		//System.setOut(out);

		File InputDir = new File(args[0]);
		File[] InputFiles = InputDir.listFiles();

		int NumberOfThread = Integer.parseInt(args[1]);
		int NumberOfFile = Integer.parseInt(args[3]);

		ExecutorService Executor = Executors.newFixedThreadPool(NumberOfThread);
		for(int TaskI=0; TaskI<NumberOfFile; TaskI++){ 
			Map<String, Map<Integer, List<Double>>> Price1 = new HashMap<String, Map<Integer, List<Double>>>();
			Price.add(Price1);
			WorkerThread tTask = new WorkerThread(TaskI, args[0], Price1);
			Executor.execute(tTask);
		}
		Executor.shutdown();
		boolean finished = Executor.awaitTermination(10, TimeUnit.MINUTES);
		System.out.println(finished);
		int PriceI=0;
		Set<String> Carrier = new HashSet<String>();
		while (PriceI<Price.size()){
			for(Map.Entry<String, Map<Integer, List<Double>>> entry : Price.get(PriceI).entrySet()){
				Carrier.add(entry.getKey());		
			}	
			PriceI++;		
		}
		for(String name : Carrier){
			//System.out.println(name);
			for (int Month = 1; Month <= 12; Month++){
				List<Double> Temp = new ArrayList<Double>();
				PriceI = 0;
				while (PriceI<Price.size()){
					try{					
						Temp.addAll(Price.get(PriceI).get(name).get(Month));
					}catch(Exception e){
					}
					PriceI++;
				}
				if (args[2] == "1")
					System.out.println(Month+" "+name+" "+GetFlightPriceMean(Temp));
				else
					System.out.println(Month+" "+name+" "+GetFlightPriceMedian(Temp));
			}
		}
	}
	static double GetFlightPriceMean(List<Double> p){
		int i = 0;
		double temp = 0;
		if (p.size() == 0)
			return 0;
		while (i<p.size()){
			temp += p.get(i);
			i++;
		}
		return temp/p.size();
	}
	static double GetFlightPriceMedian(List<Double> p){
		Collections.sort(p);
		if (p.size()/2 != 0)
			return p.get(p.size()/2);
		else if (p.size() == 0)
			return 0;
		else
			return (p.get(p.size()/2)+p.get(p.size()/2-1))/2;
	}
}

