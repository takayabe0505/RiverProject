package CreateMobMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import jp.ac.ut.csis.pflow.geom.GeometryChecker;

public class ExtractDataforVictims {

	public static String basicpath = "/home/t-tyabe/Data/";
	static File shapedir = new File(basicpath+"Kinugawa_Ibaragi_shp");
	static GeometryChecker gchecker = new GeometryChecker(shapedir);
	protected static final SimpleDateFormat SDF_TS  = new SimpleDateFormat("HH:mm:ss");//change time format
	protected static final SimpleDateFormat FullDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//change time format

	public static void main(String args[]) throws IOException, ParseException{

		String hitdate = "20150512";
		String hittime = "23:30:00"; // XX:XX:XX shape 

		//output boxes
		HashSet<String> victimID = new HashSet<String>();
		File dataofvictims = new File(basicpath+"gpsdataofpeopleinFloodEvent.csv");
		//

		Integer starthour = Integer.valueOf(hittime.substring(0, 2))-3;
		Integer endhour = Integer.valueOf(hittime.substring(0, 2))+3;
		String starttime = String.format("%02d", starthour)+":00:00";
		String endtime = String.format("%02d", endhour)+":00:00";

		System.out.println(
				"Date:"+hitdate+
				"Starttime"+starttime+
				"Endtime"+endtime);
		
		if(endhour<=23){
			NoOverlapMidnight(hitdate, starttime, endtime, victimID, dataofvictims);
		}
		else{
			OverlapMidnight(hitdate, starttime, endtime, victimID, dataofvictims);
		}
	}

	public static void NoOverlapMidnight
	(String hitdate, String starttime, String endtime, HashSet<String> victimID, File dataofvictims) throws IOException, ParseException{
//		FileHandling.extractfromcommand(hitdate);
		extract_writeoutIDs(hitdate,starttime,endtime,victimID);
		extract_dataofvictims(hitdate,victimID,dataofvictims);	
	}

	public static void OverlapMidnight
	(String hitdate, String starttime, String endtime, HashSet<String> victimID, File dataofvictims) throws IOException, ParseException{
//		FileHandling.extractfromcommand(hitdate);
		extract_writeoutIDs(hitdate,starttime,"23:59:59",victimID);

		String hitdateplusone = String.valueOf(Integer.valueOf(hitdate)+1);
//		FileHandling.extractfromcommand(hitdateplusone);
		extract_writeoutIDs(hitdate,"00:00:00",endtime,victimID);

		System.out.println("Size of HashMap:"+victimID.size());
		
		extract_dataofvictims(hitdate,victimID,dataofvictims);
		extract_dataofvictims(hitdateplusone,victimID,dataofvictims);

	}


	public static void extract_writeoutIDs(String hitdate, String start, String end, HashSet<String> res) throws IOException, ParseException{
		File gpslogs = new File(basicpath+"grid/0/tmp/ktsubouc/gps_"+hitdate+".csv");	

		Date startdate = SDF_TS.parse(start);
		Date finishdate = SDF_TS.parse(end);

		File output  = new File("/home/t-tyabe/Data/IDofpeopleinFloodEvent.csv");
		BufferedReader br = new BufferedReader(new FileReader(gpslogs));
		BufferedWriter bw = new BufferedWriter(new FileWriter(output));
		String line = null;
		String prevline = null;
		while((line=br.readLine())!=null){
			if(SameLogCheck(line,prevline)==true){
				String[] tokens = line.split("\t");
				if(tokens.length>=5){
					if(!tokens[4].equals("null")){
						String id = tokens[0];
						if(!id.equals("null")){
							String time = getHMS(tokens[4]);
							Date dt = SDF_TS.parse(time);
							Double lat = Double.parseDouble(tokens[2]);
							Double lon = Double.parseDouble(tokens[3]);
							if((dt.after(startdate))&&(dt.before(finishdate))){ //
								if(gchecker.checkOverlap(lon, lat)==true){
									res.add(id);
									bw.write(id);
									bw.newLine();
								}
							}
						}
					}
				}
				prevline = line;
			}
		}
		br.close();
		bw.close();
	}

	public static void extract_dataofvictims(String hitdate, HashSet<String> IDofvictim, File out) throws IOException{
		File gpslogs = new File(basicpath+"grid/0/tmp/ktsubouc/gps_"+hitdate+".csv");	

		BufferedReader br = new BufferedReader(new FileReader(gpslogs));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out,true));
		String line = null;
		String prevline = null;
		while((line=br.readLine())!=null){
			if(SameLogCheck(line,prevline)==true){
				String[] tokens = line.split("\t");
				if(tokens.length>=5){
					if(!tokens[4].equals("null")){
						String id = tokens[0];
						//						String time = getHMS(tokens[4]);
						String ftime = getfulltime(tokens[4]);
						Double lat = Double.parseDouble(tokens[2]);
						Double lon = Double.parseDouble(tokens[3]);
						if(IDofvictim.contains(id)){ 
							bw.write(id + "\t" + lat + "\t" + lon + "\t" + ftime);
							bw.newLine();
						}
					}
				}
				prevline = line;
			}
		}
		br.close();
		bw.close();
	}

	public static boolean SameLogCheck(String line, String prevline){
		if(line.equals(prevline)){
			return false;
		}
		else{
			return true;
		}
	}

	public static String getHMS(String t){
		String[] x = t.split("T");
		String time = x[1].substring(0,8);
		//		String res = x[0]+ " " + time;
		return time;
	}

	public static String getfulltime(String t){
		String[] x = t.split("T");
		String time = x[1].substring(0,8);
		String res = x[0]+ " " + time;
		return res;
	}

}
