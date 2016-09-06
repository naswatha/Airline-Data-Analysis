/**
 * @classname: FlightPriceProcess
 * 
 * @author Ruinan Hu
 *
 */


import java.util.*;
import java.io.*;

public class FlightPriceProcess {
	public static void main(String[] args) throws Exception {
		HashMap<String, HashMap<String, Double[]>> pArray = new HashMap<String, HashMap<String, Double[]>>();
		int N;
		N = Integer.parseInt(args[1]);
		FileInputStream DData = new FileInputStream(args[0]);
		BufferedReader BufData = new BufferedReader(new InputStreamReader(DData));
		String nLine;
		while ((nLine = BufData.readLine()) != null){
			String Carrier = nLine.split(",|\\s+", 4)[0];
			String Year = nLine.split(",|\\s+", 4)[1];
			double Intercept = Double.parseDouble(nLine.split(",|\\s+", 4)[3]);
			double Slope = Double.parseDouble(nLine.split(",|\\s+", 4)[2]);
			HashMap<String, Double[]> InnerMap = new HashMap<String, Double[]>();
			try{
				pArray.get(Carrier).put(Year,new Double[] {Slope, Intercept});
			}catch(Exception e){
				InnerMap.put(Year, new Double[] {Slope, Intercept});
				pArray.put(Carrier, InnerMap);
			}
		}
		//System.out.println(pArray);
		//System.out.println(pArray.get("AA"));
		//System.out.println(pArray.get("AA").get("2015")[0]);
		Map<String, String> CheapestCarrier = new TreeMap<String, String>();
		Map<String, Double> CheapestPrice = new TreeMap<String, Double>();
		//for(Map.Entry<String, HashMap<String, Double[]>> Entry : pArray.entrySet()){
		//	for (Map.Entry<String, Double[]> Entry1 : Entry.getValue().entrySet()){
		//		System.out.println(Entry.getKey()+Entry1.getKey()+Entry1.getValue()[0]+Entry1.getValue()[1]);		
		//	}
		//}
		for(Map.Entry<String, HashMap<String, Double[]>> Entry : pArray.entrySet()){
			for (Map.Entry<String, Double[]> Entry1 : Entry.getValue().entrySet()){
				double t;
				String Entry1Year = Entry1.getKey();				
				try{
					t = CheapestPrice.get(Entry1Year);
				}catch(Exception e){
					CheapestPrice.put(Entry1Year, N*Entry1.getValue()[0] + Entry1.getValue()[1]);
					CheapestCarrier.put(Entry1Year, Entry.getKey());
					continue;
				}
				//if you have multi same Price carrier?
				double tt = N*Entry1.getValue()[0] + Entry1.getValue()[1];
				if (t > tt){
					CheapestPrice.put(Entry1Year, tt);
					CheapestCarrier.put(Entry1Year, Entry.getKey());					
				}
			}
		}
		//for(Map.Entry<String, String> Year : CheapestCarrier.entrySet()){
		//	System.out.println(Year.getKey()+Year.getValue()+CheapestPrice.get(Year.getKey()));		
		//}
		Map<String, Integer> Result = new HashMap<String, Integer>();
		for(Map.Entry<String, HashMap<String, Double[]>> Entry : pArray.entrySet()){
			Result.put(Entry.getKey(), 0);
			for(Map.Entry<String, String> Year : CheapestCarrier.entrySet()){
				if (Year.getValue() == Entry.getKey()){
					Result.put(Entry.getKey(), Result.get(Entry.getKey())+1);			
				}	
			}
		}
		String FinalCarrier="";
		int FinalCarrierWinTimes=0;	
		for(Map.Entry<String, Integer> Carrier : Result.entrySet()){
			if (Carrier.getValue()>FinalCarrierWinTimes){
				FinalCarrierWinTimes = Carrier.getValue();
				FinalCarrier = Carrier.getKey();			
			}
		}
		System.out.println(FinalCarrier);						
	}
}
