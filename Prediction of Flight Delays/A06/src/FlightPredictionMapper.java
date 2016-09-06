/**
 * @classname: FlightPredictionMapper
 * 
 * @author Hu,Ruinan Aswathanarayana,Naveen	
 * @description Mapper outputs key value pair, Text as key and CustomWritable as Value
 * (Key,Value) => 
 * (<MONTH,ORIGIN>,<CARRIER,ORIGIN,DESTINATION,YEAR,MONTH,DAYOFMONTH,CRSARRTIME,CRSDEPTIME,CRSELAPSEDTIME,ARRDEL15>)
 * Uses 50 most popular cities and 10 most popular carrier.
 */


import java.io.IOException;
import java.util.HashMap;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;



public class FlightPredictionMapper
extends Mapper<LongWritable, Text, Text, Text> {
	/**
	 * @name: map
	 * @description: map the airline dataset to the mentioned key and value pair.
	 * @param: Arbitrary key, data in the form of Text and context object to write mapped data to file system.
	 * @return: mapped data.
	 */
	@Override
	public void map(LongWritable Key, Text Value, Context context)
			throws IOException, InterruptedException {

		FlightPriceParser FParser = new FlightPriceParser();
		if (!FParser.map(Value.toString())){
			return;

		}
		// load the popular cities and carriers.
		HashMap<String,Integer> carrierHash = new HashMap<String, Integer>();
		HashMap<String,Integer> cityHash = new HashMap<String, Integer>();
		carrierHash = loadCarrierHash();
		cityHash = loadCityHash();
		// only if the data contains the popular cities and carriers
		// send for reducers for further processing.
		if(validData(FParser,carrierHash,cityHash)){
			Text keyOut = new Text(Integer.toString(FParser.Month) + "," + FParser.Origin);
			Text valueOut = new Text(bulidValueOut(FParser));
			context.write(keyOut,valueOut);
		}
		else{
			return;
		}		
	}

	/**
	 * @name: bulidValueOut
	 * @description: Building the value with only the required columnswe are rounding the value of CRSArrTime, 
	 * 				 CRSDepTime and CRSElapsedTime to reduce the factor levels.
	 * @param: FParser object contains the row data from the dataset.
	 * @return: string containing only required information to construct the data for model with factors.
	 */
	private String bulidValueOut(FlightPriceParser FParser) {

		String value = FParser.Carrier+","+FParser.Origin+","+FParser.Destination+","+FParser.Year+","+FParser.Month+","+FParser.DayOfMonth+","
				+((FParser.CRSArrTime + 50) / 100 ) * 100+","
				+((FParser.CRSDepTime + 50) / 100 ) * 100+","
				+((FParser.CRSElapsedTime + 50) / 100 ) * 100+","
				+FParser.ArrDel15;
		return value;
	}

	/**
	 * @name: validData
	 * @description: check if the data contains the popular city as its origin or destination and belongs to popular carrier.
	 * @param: FParser object contains the row data from the dataset
	 * 		   Hashmap - containing the popular cities.
	 * 		   Hashmap - containing the popular carrier.
	 * @return: true if the data contains popular city or carrier.
	 */
	private boolean validData(FlightPriceParser FParser,HashMap<String,Integer> carrierHash, HashMap<String,Integer> cityHash) {

		if(!carrierHash.containsKey(FParser.Carrier)){
			return false;
		}
		if(!cityHash.containsKey(FParser.Origin)){
			return false;
		}
		if(!cityHash.containsKey(FParser.Destination)){
			return false;
		}		
		return true;
	}

	/**
	 * @name: loadCarrierHash
	 * @description: load the popular carriers
	 * @param: 
	 * @return: hashmap with popular carriers
	 */
	private HashMap<String, Integer> loadCarrierHash() {

		//"AA","AS","CO","DL","HP","NW","TW","UA","US","WN"
		HashMap<String,Integer> carrierHash = new HashMap<String, Integer>();
		carrierHash.put("AA", 1); carrierHash.put("AS", 1); carrierHash.put("CO", 1);
		carrierHash.put("DL", 1); carrierHash.put("HP", 1); carrierHash.put("NW", 1);
		carrierHash.put("TW", 1); carrierHash.put("UA", 1); carrierHash.put("US", 1);
		carrierHash.put("WN", 1);
		return carrierHash;
	}

	/**
	 * @name: loadCityHash
	 * @description: load the popular cities
	 * @param: 
	 * @return: hashmap with popular cities
	 */
	private HashMap<String, Integer> loadCityHash() {

		//		{"ORD","ATL","DFW","LAX","STL","PHX","DTW","MSP","SFO","DEN","CLT","IAH",
		//		"PIT","LAS","EWR","PHL","SEA","LGA","BOS","SLC","DCA","CVG","MCO","SAN",
		//		"BWI","CLE","OAK","MIA","HOU","MCI","PDX","MEM","TPA","MSY","DAL","MDW",
		//		"BNA","SJC","JFK","CMH","ONT","SAT","ABQ","SMF","AUS","IAD","SNA","FLL",
		//		"IND","BUR"};

		HashMap<String,Integer> cityHash = new HashMap<String, Integer>();
		cityHash.put("ORD", 1); cityHash.put("ATL", 1); cityHash.put("DFW", 1);
		cityHash.put("LAX", 1); cityHash.put("STL", 1); cityHash.put("PHX", 1);
		cityHash.put("DTW", 1); cityHash.put("MSP", 1); cityHash.put("SFO", 1);
		cityHash.put("DEN", 1); cityHash.put("CLT", 1); cityHash.put("IAH", 1);
		cityHash.put("PIT", 1); cityHash.put("LAS", 1); cityHash.put("EWR", 1);
		cityHash.put("PHL", 1); cityHash.put("SEA", 1); cityHash.put("LGA", 1);
		cityHash.put("BOS", 1); cityHash.put("SLC", 1); cityHash.put("DCA", 1);
		cityHash.put("CVG", 1); cityHash.put("MCO", 1); cityHash.put("SAN", 1);
		cityHash.put("BWI", 1); cityHash.put("CLE", 1); cityHash.put("OAK", 1);
		cityHash.put("MIA", 1); cityHash.put("HOU", 1); cityHash.put("MCI", 1);
		cityHash.put("PDX", 1); cityHash.put("MEM", 1); cityHash.put("TPA", 1);
		cityHash.put("MSY", 1); cityHash.put("DAL", 1); cityHash.put("MDW", 1);
		cityHash.put("BNA", 1); cityHash.put("SJC", 1); cityHash.put("JFK", 1);
		cityHash.put("CMH", 1); cityHash.put("ONT", 1); cityHash.put("SAT", 1);
		cityHash.put("ABQ", 1); cityHash.put("SMF", 1); cityHash.put("AUS", 1);
		cityHash.put("IAD", 1); cityHash.put("SNA", 1); cityHash.put("FLL", 1);
		cityHash.put("IND", 1); cityHash.put("BUR", 1);
		return cityHash;
	}
}
