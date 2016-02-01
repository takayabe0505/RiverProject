package CreateDataforExp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;

public class GetAllDataforIDsinArea {

	public static String basicpath = "/home/t-tyabe/Data/";

	public static void main(String args[]) throws ParseException, IOException{
		String hitdate = "20150910";
		File IDFile = new File(basicpath+"IDswithhomesinArea.csv");
		run(hitdate,IDFile);
	}

	public static void run(String hitdate, File IDfile) throws ParseException, IOException{
		HashSet<String> targetDates = ExtractIDswithHomesinArea.getTargetDates(hitdate);
		HashSet<String> IDset = getIDMap(IDfile);
		File out = new File(basicpath+"logsofIDwithhomesinArea"+hitdate+".csv");
		for(String date : targetDates){
//			FileHandling.extractfromcommand(date);
			File gpslogs = new File(basicpath+"grid/0/tmp/ktsubouc/gps_"+date+".csv");	
			ReadoutOnlyLogsofID(gpslogs,out,IDset);
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

	public static void ReadoutOnlyLogsofID(File in, File out, HashSet<String> ids) throws IOException, ParseException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
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
							Double lat = Double.parseDouble(tokens[2]);
							Double lon = Double.parseDouble(tokens[3]);
							bw.write(id+","+lon+","+lat+","+time);
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

	public static String getfulltime(String t){
		String[] x = t.split("T");
		String time = x[1].substring(0,8);
		String res = x[0]+ " " + time;
		return res;
	}
}
