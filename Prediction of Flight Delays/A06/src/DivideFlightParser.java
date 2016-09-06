/**
 * @classname: DivideFlightParser
 * 
 * @author Hu,Ruinan Aswathanarayana,Naveen	
 * @description Parse the flight details and sanitize.
 */
import com.opencsv.CSVParser;


public class DivideFlightParser{
	public boolean DEV_MODE = false;
	public int CRSArrTime=0;
	public int CRSDepTime=0;
	public int CRSElapsedTime=0;
	public int Year=0;
	public int Month=0;
	public int DayOfMonth = 0;
	public String FlightNum;
	public String Carrier;
	public String Origin;
	public String Destination;
	public CSVParser Parser;

	/**
	 * @name: DivideFlightParser
	 * @description: default constructor
	 */
	DivideFlightParser(){

	}
	/**
	 * @name: map
	 * @description: check for any faulty data and sanitize the airline data.
	 * @param: row data from the dataset.
	 * @return: return true if data is correct else false.
	 */
	public boolean map(String[] readline){


		Carrier = readline[9];
		if (Carrier.isEmpty()){
			if (DEV_MODE)
				System.out.println("Carrier Strings Empty");
			return true;
		}
		try{
			CRSArrTime = Integer.parseInt(readline[41]); 
		}catch(NumberFormatException e)
		{
			if (DEV_MODE)			
				System.out.println("CRSArrTime:"+readline[41]);
			return false;
		}

		try{
			CRSDepTime = Integer.parseInt(readline[30]);
		}catch(NumberFormatException e) 
		{
			if (DEV_MODE)			
				System.out.println("CRSDepTime:"+readline[30]);
			return false;
		}
		try{
			CRSElapsedTime = Integer.parseInt(readline[51]);
		}catch(NumberFormatException e) 
		{
			if (DEV_MODE)			
				System.out.println("CRSElapsedTime:"+readline[51]);
			return false;
		}
		//Send out error but not do the sanity check
		try{
			Year = Integer.parseInt(readline[1]);
		}catch(NumberFormatException e)
		{
			if (DEV_MODE)
				System.out.println("Year:"+readline[1]);
			return false;
		}
		//Send out error but not do the sanity check
		try{
			Month = Integer.parseInt(readline[3]);
		}catch(NumberFormatException e)
		{
			if (DEV_MODE)
				System.out.println("Month:"+readline[3]);
			return false;
		}

		try{
			DayOfMonth = Integer.parseInt(readline[4]);
		}catch(NumberFormatException e)
		{
			if (DEV_MODE)
				System.out.println("DayOfMonth:"+readline[4]);
			return false;
		}

		FlightNum = readline[11];
		Origin = readline[15];
		Destination = readline[24];
		if (readline[24].length() > 3) {
			if (DEV_MODE)
				System.out.println("Empty Strings");
			return false;
		}

		return true;
	}

}
