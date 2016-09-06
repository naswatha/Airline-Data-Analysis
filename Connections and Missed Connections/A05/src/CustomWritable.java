/**
 * @classname: CustomWritable
 * 
 * @author Hu,Ruinan Aswathanarayana,Naveen	
 * @description This is used to write custom type from mapper.
 *
 */
import org.apache.hadoop.io.*;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CustomWritable implements WritableComparable<CustomWritable>{
	private Text cityName;
	private int flightType;
	private long flightTime;
	private long actualTime;
	private int fT;
	private int aT;
	private Text date;
	public CustomWritable() {
		set(new Text(), 0, 0, 0, 0, 0, new Text());
	}
	public CustomWritable(Text cityName, int flightType, long flightTime, long actualTime) {
		set(cityName, flightType, flightTime, actualTime, 0, 0, new Text());
	}
	public CustomWritable(String cityName, int flightType, long flightTime, long actualTime) {
		set(new Text(cityName), flightType, flightTime, actualTime, 0, 0, new Text(""));
	}
        public CustomWritable(String cityName, int flightType, long flightTime, long actualTime, int fT, int aT, String date) {
                set(new Text(cityName), flightType, flightTime, actualTime, fT, aT, new Text(date));
        }

	public void set(Text cityName, int flightType, long flightTime, long actualTime, int fT, int aT, Text date) {
		this.cityName = cityName;
		this.flightType = flightType;
		this.flightTime = flightTime;
		this.actualTime = actualTime;
		this.fT = fT;
		this.aT = aT;
		this.date = date;
	}
	public Text getCityName() {
		return cityName;
	}
	public int getFlightType() {
		return flightType;
	}
	public long getFlightTime() {
		return flightTime;
	}
	public long getActualTime() {
		return actualTime;
	}
	public int getFT() {
		return fT;
	}
	public int getAT() {
		return aT;
	}
	public Text getDate() {
		return date;
	}
	@Override
	public void write(DataOutput out) throws IOException {
		cityName.write(out);
		out.writeInt(flightType);			
		out.writeLong(flightTime);		
		out.writeLong(actualTime);
		out.writeInt(fT);
		out.writeInt(aT);
		date.write(out);
	}
	@Override
	public void readFields(DataInput in) throws IOException {
		cityName.readFields(in);
		flightType = in.readInt();
		flightTime = in.readLong();
		actualTime = in.readLong();
		fT = in.readInt();
		aT = in.readInt();
		date.readFields(in);
	}
	@Override
	public int hashCode() {
		return flightType;
	}
	@Override
	public boolean equals(Object o) {
		return false;
	}
	@Override
	public String toString() {
		return cityName + "\t" + flightType + flightTime + actualTime;
	}
	@Override
	public int compareTo(CustomWritable tp) {
		return 0;
	}
}
