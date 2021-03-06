/**
 *
 * @auth|| Ruinan,Naveen,Karthik,Sujith
 */

import com.opencsv.CSVParser;


public class AirlineParser1{
	public boolean DEV_MODE = false;
	public int CRSArrTime;
	public int CRSDepTime;
	public int CRSElapsedTime;
	public int timeZone;
	public int AirportIDO;
	public int AirportIDD;
	public int AirportSeqIDO;
	public int AirportSeqIDD;
	public int CityMarketIDO;
	public int CityMarketIDD;
	public int StateFipsO;
	public int StateFipsD;
	public int WacO;
	public int WacD;
	public int Year;
	public int Month;
	public int DayOfMonth;
	public int Distance;
	public int DistanceGroup;
	public String Carrier;
	public String Origin;
	public String Destination;
	public int flightNum;
	public CSVParser Parser;

	AirlineParser1(){

	}
	public boolean map(String nline){

		CSVParser Parser = new CSVParser();
		String[] readline;		
		try{
			readline = Parser.parseLine(nline);
		}catch(Exception e){
			if (DEV_MODE)			
				System.out.println("Can't parse the nline");
			return false;			
		} 
		
		if(readline.length != 111){
			return false;
		}

		try{
			CRSArrTime = Integer.parseInt(readline[40]); 
		}catch(NumberFormatException e)
		{
			if (DEV_MODE)			
				System.out.println("CRSArrTime:"+readline[40]);
			return false;
		}
		
		try{
			CRSDepTime = Integer.parseInt(readline[29]);
		}catch(NumberFormatException e) 
		{
			if (DEV_MODE)			
				System.out.println("CRSDepTime:"+readline[29]);
			return false;
		}

		try{
			CRSElapsedTime = Integer.parseInt(readline[50]);
		}catch(NumberFormatException e) 
		{
			if (DEV_MODE)			
				System.out.println("CRSElapsedTime:"+readline[50]);
			return false;
		}

		try{
			AirportIDO = Integer.parseInt(readline[11]);
		}catch(NumberFormatException e) 
		{
			if (DEV_MODE)			
				System.out.println("AirportIDO:"+readline[11]);
			return false;
		}

		try{
			AirportIDD = Integer.parseInt(readline[20]);
		}catch(NumberFormatException e) 
		{
			if (DEV_MODE)			
				System.out.println("AirportIDD:"+readline[20]);
			return false;
		}

		try{
			AirportSeqIDO = Integer.parseInt(readline[12]);
		}catch(NumberFormatException e) 
		{
			if (DEV_MODE)			
				System.out.println("AirportSeqIDO:"+readline[12]);
			return false;
		}

		try{
			AirportSeqIDD = Integer.parseInt(readline[21]);
		}catch(NumberFormatException e) 
		{
			if (DEV_MODE)			
				System.out.println("AirportSeqIDD:"+readline[21]);
			return false;
		}

		try{
			CityMarketIDO = Integer.parseInt(readline[13]);
		}catch(NumberFormatException e)
		{
			if (DEV_MODE)
				System.out.println("AirportSeqIDD:"+readline[13]);
			return false;
		}
		
		try{
			CityMarketIDD = Integer.parseInt(readline[22]);
		}catch(NumberFormatException e)
		{
			if (DEV_MODE)
				System.out.println("CityMarketIDD:"+readline[22]);
			return false;
		}

		try{
			StateFipsO = Integer.parseInt(readline[17]);
		}catch(NumberFormatException e)
		{
			if (DEV_MODE)
				System.out.println("StateFipsO:"+readline[17]);
			return false;
		}

		try{
			StateFipsD = Integer.parseInt(readline[26]);
		}catch(NumberFormatException e)
		{
			if (DEV_MODE)
				System.out.println("StateFipsD:"+readline[26]);
			return false;
		}

		try{
			WacO = Integer.parseInt(readline[19]);
		}catch(NumberFormatException e)
		{
			if (DEV_MODE)
				System.out.println("WacO:"+readline[19]);
			return false;
		}

		try{
			WacD = Integer.parseInt(readline[28]);
		}catch(NumberFormatException e)
		{
			if (DEV_MODE)
				System.out.println("WacD:"+readline[28]);
			return false;
		}

		try{
			Distance = (int) Float.parseFloat(readline[54]);
		}catch(NumberFormatException e)
		{
			if (DEV_MODE)
				System.out.println("Distance:"+readline[54]);
			return false;
		}
		
		try{
			DistanceGroup = (int) Float.parseFloat(readline[55]);
		}catch(NumberFormatException e)
		{
			if (DEV_MODE)
				System.out.println("DistanceGroup:"+readline[55]);
			return false;
		}
		
		//Send out error but not do the sanity check
		try{
                        Year = Integer.parseInt(readline[0]);
                }catch(NumberFormatException e)
                {
			if (DEV_MODE)
                        	System.out.println("Year:"+readline[0]);
                        return false;
                }
		//Send out error but not do the sanity check
		try{
                        Month = Integer.parseInt(readline[2]);
                }catch(NumberFormatException e)
                {
			if (DEV_MODE)
                        	System.out.println("Month:"+readline[2]);
                        return false;
                }

		try{
			DayOfMonth = Integer.parseInt(readline[3]);
		}catch(NumberFormatException e)
                {
			if (DEV_MODE)
                        	System.out.println("DayOfMonth:"+readline[3]);
                        return false;
                }

                try{
                        flightNum = Integer.parseInt(readline[10]);
                }catch(NumberFormatException e)
                {
                        if (DEV_MODE)
                                System.out.println("flightNum:"+readline[10]);
                        return false;
                }

		//CRSArrTime[40] and CRSDepTime[29] should not be zero
		if (CRSArrTime == 0 || CRSDepTime == 0) {
			if (DEV_MODE)
				System.out.println("CRS");
			return false;
		}

		int CRSArrMin = 0;
		int CRSDepMin = 0;
		int CRSArrMinO = 0;
		CRSArrMin = CRSArrTime / 100 * 60 + CRSArrTime%100;
		CRSDepMin = CRSDepTime / 100 * 60 + CRSDepTime%100;
		CRSArrMinO = CRSDepMin + CRSElapsedTime;
		timeZone = CRSArrMin - CRSArrMinO;

		//timeZone % 60 should be 0
		if (timeZone%60 != 0) { 
			if (DEV_MODE)
				System.out.println("timeZone");
			return false;
		}

		//AirportID[11][20],  AirportSeqID[12][21], CityMarketID[13][22], StateFips[17][26], Wac[19][28] should be larger than 0

		if ( AirportIDO <= 0 ||
			AirportIDD <= 0 ||
			AirportSeqIDO <= 0 ||
			AirportSeqIDD <= 0 ||
			CityMarketIDO <= 0 ||
			CityMarketIDD <= 0 ||
			StateFipsO <= 0 ||
			StateFipsD <= 0 ||
			WacO <= 0 ||
			WacO <= 0 ) {
			if (DEV_MODE) 
				System.out.println("a lot of names");
			return false;
		}


		//Origin[14], Destination[23],  CityName[15][24], State[16][25], StateName[18][27] should not be empty
		Origin = readline[14];
		Destination = readline[23];
		if (readline[14].isEmpty() || 
			readline[23].isEmpty() ||
			readline[15].isEmpty() ||
			readline[24].isEmpty() ||
			readline[16].isEmpty() ||
			readline[25].isEmpty() ||
			readline[18].isEmpty() ||
			readline[27].isEmpty()) {
			if (DEV_MODE)
				System.out.println("Empty Strings");
			return false;
		}

		Carrier = readline[8];
		if (Carrier.isEmpty()){
			if (DEV_MODE)
				System.out.println("Carrier Strings Empty");
			return false;
		}
		
		return true;
	}

}
