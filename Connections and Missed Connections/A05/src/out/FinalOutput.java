/**
 * @classname: FinalOutput
 * 
 * @author Hu,Ruinan Aswathanarayana,Naveen	
 * @description To aggregate the output from the reducer.
 *
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class FinalOutput {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		HashMap<String, ArrayList<Long>> finalHash = new HashMap<String, ArrayList<Long>>();
		
		// add the connection and missed connection for each carrier and year.
		try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
		    String line;
		    while ((line = br.readLine()) != null) {
	    	
		    	String[] values = line.split(",");
		    	String[] values1 = values[1].split("\t");
		    	String carrierYear = values1[0]+","+values[0];
		    	if(finalHash.containsKey(carrierYear)){
		    		
		    		ArrayList<Long> update = finalHash.get(carrierYear);
		    		long connection = update.get(0);
		    		long missedConnection = update.get(1);
		    		connection += Long.parseLong(values1[1]);
		    		missedConnection += Long.parseLong(values[2]);
		    		update.set(0, connection);
		    		update.set(1, missedConnection);
		    		finalHash.put(carrierYear, update);
		    	}
		    	else{
		    		
		    		ArrayList<Long> insert = new ArrayList<Long>();
			    	insert.add(Long.parseLong(values1[1]));
			    	insert.add(Long.parseLong(values[2]));
			    	finalHash.put(carrierYear, insert);
		    	}   	
		    }
		}
		catch(Exception e){
			System.out.println("Error reading file");
		}
		
		Map<String, ArrayList<Long>> sortedMap = new TreeMap<String, ArrayList<Long>>(finalHash);
		//printHash(sortedMap);
		printMap(sortedMap,"aggregate");
	}
	
	// write the output to a file
	public static void printMap(Map<String, ArrayList<Long>> finalHash, String filename) throws FileNotFoundException, UnsupportedEncodingException{
				
				PrintWriter writer = new PrintWriter(filename, "UTF-8");
				for (String name: finalHash.keySet()){		
			         String key =name.toString();
			         ArrayList<Long> printLong = finalHash.get(name);
			         writer.println(key + "," + printLong.get(0)+","+printLong.get(1));  
					}
				writer.close();
		  }
	// output the value on console
	 public static void printHash(Map<String, ArrayList<Long>> finalHash){
			
			for (String name: finalHash.keySet()){
	         String key =name.toString();
	         ArrayList<Long> printLong = finalHash.get(name);
	         System.out.println(key + "," + printLong.get(0)+","+printLong.get(1));  
			}
		}
}
