package CreateDataforExp;

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

public class GetShortLengthDataforIDsinArea {

	public static String basicpath = "/home/t-tyabe/Data/";
	protected static final SimpleDateFormat HMS  = new SimpleDateFormat("HH:mm:ss");//change time format
	public static Integer bufferhours = 6;

	public static void main(String args[]) throws ParseException, IOException{
		String hitdate = "20150910";
		String hittime = "12:50:00";
		File IDFile = new File(basicpath+"IDswithhomesinArea.csv");
		run(hitdate, hittime, IDFile);
	}

	public static void run(String hitdate, String hittime, File IDfile) throws ParseException, IOException{
		HashSet<String> targetDates = ExtractIDswithHomesinArea.getTargetDates(hitdate);
		HashSet<String> IDset = getIDMap(IDfile);
		File out = new File(basicpath+"logsofIDwithhomesinArea"+hitdate+"_limitedtimerange.csv");
		for(String date : targetDates){
//			FileHandling.extractfromcommand(date);
			File gpslogs = new File(basicpath+"grid/0/tmp/ktsubouc/gps_"+date+".csv");	
			ReadoutOnlyLogsofID(gpslogs,out,IDset,hittime);
		}
	}

	public static HashSet<String> getIDMap(File in) throws IOException{
		HashSet<String> set = new HashSet<String>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			set.add(line);
		}
		br.close();
		return set;
	}

	public static void ReadoutOnlyLogsofID(File in, File out, HashSet<String> ids,String hittime) throws IOException, ParseException{

		Integer starthour = Integer.valueOf(hittime.substring(0,2))-bufferhours;
		Integer endhour = Integer.valueOf(hittime.substring(0,2))+bufferhours;
		String starttime = String.format("%02d", starthour)+hittime.substring(2,8);
		String endtime = String.format("%02d", endhour)+hittime.substring(2,8);
		Date start = HMS.parse(starttime);
		Date end   = HMS.parse(endtime);

		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out,true));
		String line = null;
		String prevline = null;
		while((line=br.readLine())!=null){
			if(SameLogCheck(line,prevline)==true){
				String[] tokens = line.split("\t");
				if(tokens.length>=5){
					if(!tokens[4].equals("null")){
						String id = tokens[0];		
						if(ids.contains(id)){
							String time = getfulltime(tokens[4]);
							String hms = getHMS(tokens[4]);
							Date HMStime = HMS.parse(hms);
							if((HMStime.after(start))&&(HMStime.before(end))){
								Double lat = Double.parseDouble(tokens[2]);
								Double lon = Double.parseDouble(tokens[3]);
								bw.write(id+","+lon+","+lat+","+time);
								bw.newLine();
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
