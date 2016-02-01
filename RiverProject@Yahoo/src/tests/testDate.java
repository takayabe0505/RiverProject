package tests;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class testDate {

	protected static final SimpleDateFormat HMS  = new SimpleDateFormat("HH:mm:ss");//change time format
	protected static final SimpleDateFormat YMD = new SimpleDateFormat("yyyy-MM-dd");//change time format
	protected static final SimpleDateFormat FullDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//change time format

	public static void main(String args[]) throws ParseException{
		String time = "2016-01-30 11:10:30"; // yyyy-mm-dd HH:MM:SS
		
//		Date hms  = HMS.parse(time); NO 
//		Date ymd  = YMD.parse(time); NO
		Date date = FullDate.parse(time);
		
		System.out.println("1: "+time);
		System.out.println("2: "+date);
		
		/*
		 * IMPORTANT === (String --> Date) ==========================================================================
		 * 
		 * - you can parse String --> Date if the String fits the date format 
		 * => String stringdate = "2016-01-20 11:11:11" --> Date = FullDate.parse(stringdate) OK
		 * 
		 * - you cannot parse String --> Date if the String does not fit the date format
		 * 
		 */
		
		System.out.println("3: "+time);
		
		Date date2 = FullDate.parse(time);		
		System.out.println("4: "+date2);
				
		String backtostring  = FullDate.format(date2);
		String todiffstring  = HMS.format(date2);
		String todiffstring2 = YMD.format(date2);
		System.out.println("5: "+backtostring);
		System.out.println("6: "+todiffstring);
		System.out.println("7: "+todiffstring2);
		
		/*
		 * IMPORTANT === (Date --> String) ==========================================================================
		 * 
		 * - you can format Date --> String into any date format 
		 * 
		 */
	}
}
