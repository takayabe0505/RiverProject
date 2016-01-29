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
import Tools.FileHandling;

public class ExtractIDs {

	public static String basicpath = "/home/t-tyabe/Data/";
	static File shapedir = new File(basicpath+"Kinugawa_Ibaragi_shp");
	static GeometryChecker gchecker = new GeometryChecker(shapedir);
	protected static final SimpleDateFormat SDF_TS  = new SimpleDateFormat("HH:mm:ss");//change time format

	public static void main(String args[]) throws IOException, ParseException{

		String date = "20140727";
		String start = "10:00:00"; // XX:XX:XX shape 
		String end = "23:59:00";
		
		FileHandling.extractfromcommand(date);
		File gpslogs = new File(basicpath+"grid/0/tmp/ktsubouc/gps_"+date+".csv");
		
		HashSet<String> ids_victims = extract_writeoutIDs(gpslogs,start,end); //extract the IDs of the victims
		
		extract_dataofvictims(gpslogs, ids_victims); //extract all the data on that day of the victims
	}

	public static HashSet<String> extract_writeoutIDs(File gpslogs, String start, String end) throws IOException, ParseException{

		Date startdate = SDF_TS.parse(start);
		Date finishdate = SDF_TS.parse(end);

		HashSet<String> IDset = new HashSet<String>();
		File output  = new File("/home/t-tyabe/Data/gpsdataofpeopleinFloodEvent.csv");
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
						String time = getHMS(tokens[4]);
						Date dt = SDF_TS.parse(time);
						Double lat = Double.parseDouble(tokens[2]);
						Double lon = Double.parseDouble(tokens[3]);
						if((dt.after(startdate))&&(dt.before(finishdate))){ //
							if(gchecker.checkOverlap(lon, lat)==true){
								IDset.add(id);
							}
							bw.write(id + "\t" + lat + "\t" + lon + "\t" + time);
							bw.newLine();
						}
					}
				}
				prevline = line;
			}
		}
		br.close();
		bw.close();
		return IDset;
	}

	public static void extract_dataofvictims(File gpslogs, HashSet<String> IDofvictim) throws IOException{
		File out = new File(basicpath+"gpsdataofvictims_whole.csv");
		BufferedReader br = new BufferedReader(new FileReader(gpslogs));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;
		String prevline = null;
		while((line=br.readLine())!=null){
			if(SameLogCheck(line,prevline)==true){
				String[] tokens = line.split("\t");
				if(tokens.length>=5){
					if(!tokens[4].equals("null")){
						String id = tokens[0];
						String time = getHMS(tokens[4]);
						Double lat = Double.parseDouble(tokens[2]);
						Double lon = Double.parseDouble(tokens[3]);
						if(IDofvictim.contains(id)){ 
							bw.write(id + "\t" + lat + "\t" + lon + "\t" + time);
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

}
