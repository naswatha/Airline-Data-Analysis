/**
 * @classname: CustomWritableComparable
 * 
 * @author Hu,Ruinan Aswathanarayana,Naveen	
 * @description This is used to implement the sort for the CustomWritable objects.
 *
 */

import org.apache.hadoop.io.Text;

public class CustomWritableComparable implements Comparable{
	public Text cityName;
	public int flightType;
	public long flightTime;
	public long actualTime;
	public int fT;
	public int aT;
	public Text date;
	public  CustomWritableComparable(){

	}
	public CustomWritableComparable(Text cityName, int flightType, long flightTime, long actualTime, int fT, int aT, Text date) {
		this.cityName = cityName;
		this.flightType = flightType;
		this.flightTime = flightTime;
		this.actualTime = actualTime;
		this.fT = fT;
		this.aT = aT;
		this.date = date;
	}
	@Override
	public int compareTo(Object comparesTTS) {
		long flightTime=((CustomWritableComparable)comparesTTS).flightTime;
		//For Ascending order /1000 or too large for int 31536000 secs in a year
		return (int)(this.flightTime-flightTime);
	}
	@Override
	public String toString() {
	return "Test";
	}
}
