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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import jp.ac.ut.csis.pflow.geom.GeometryChecker;
import jp.ac.ut.csis.pflow.geom.LonLat;
import Tools.FileHandling;

public class ExtractIDswithHomesinArea {

	public static String basicpath = "/home/t-tyabe/Data/";
	static File shapedir = new File(basicpath+"Kinugawa_Ibaragi_shp/Kinugawa_Ibaragi_shp");
	static GeometryChecker gchecker = new GeometryChecker(shapedir);

	protected static final SimpleDateFormat HMS  = new SimpleDateFormat("HH:mm:ss");//change time format
	protected static final SimpleDateFormat YMD = new SimpleDateFormat("yyyy-MM-dd");//change time format
	protected static final SimpleDateFormat FullDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//change time format
	
	public static void main(String args[]) throws ParseException, IOException{
		
		String hitdate = "20150910";
		Integer minimumpoints = 10; 
		HashMap<String, Integer> IDstobeTested = new HashMap<String, Integer>();
		run(hitdate, IDstobeTested,minimumpoints);
		
	}
	
	public static void run(String hitdate, HashMap<String, Integer> ids, Integer min) throws ParseException, IOException{
		HashSet<String> targetDates = getTargetDates(hitdate);
		
		for(String date : targetDates){
			FileHandling.extractfromcommand(hitdate);
			File gpslogs = new File(basicpath+"grid/0/tmp/ktsubouc/gps_"+date+".csv");	
			ReadoutOnlyNightLogs(gpslogs,ids);
		}

		HashSet<String> IDswithhomesinArea = new HashSet<String>();
		for(String id : ids.keySet()){
			if(ids.get(id)>min){
				IDswithhomesinArea.add(id);
			}
		}
		System.out.println("number of IDs with homes in Area are: " + IDswithhomesinArea.size());
		writeout(IDswithhomesinArea);
	}
	
	public static void writeout(HashSet<String> set) throws IOException{
		File out = new File(basicpath+"IDswithhomesinArea.csv");
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		for(String id : set){
			bw.write(id);
			bw.newLine();
		}
		bw.close();
	}

	public static HashSet<String> getTargetDates(String disDate) 
			throws ParseException, IOException{
		HashSet<String> res = new HashSet<String>();
		String year = disDate.substring(0,4);
		String month = disDate.substring(4,6);
		for(int i=1; i<=28; i++){
			String day = String.valueOf(i);
			Date d = YMD.parse(year+"-"+month+"-"+day);
			String youbi = (new SimpleDateFormat("u")).format(d);
			if(!((youbi.equals("6"))||(youbi.equals("7")))){
					String date = YMD.format(d);
					res.add(date);
			}
		}
		return res;
	}
	
	public static void ReadoutOnlyNightLogs(File in, HashMap<String, Integer> ids) throws IOException, ParseException{
		
		Date startdate = HMS.parse("00:00:00");
		Date finishdate = HMS.parse("08:00:00");
		
		BufferedReader br = new BufferedReader(new FileReader(in));
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
							Date dt = HMS.parse(time);
							Double lat = Double.parseDouble(tokens[2]);
							Double lon = Double.parseDouble(tokens[3]);
							if((dt.after(startdate))&&(dt.before(finishdate))){
								if(AreaOverlap(new LonLat(lon,lat)).equals("yes")){
									if(ids.containsKey(id)){
										int count = ids.get(id)+1;
										ids.put(id, count);
									}
									else{
										ids.put(id,1);
									}
								}
							}
						}
					}
				}
				prevline = line;
			}
		}
		br.close();
	}
	
	public static String AreaOverlap(LonLat point){
		List<String> zonecodeList = gchecker.listOverlaps("A31_001",point.getLon(),point.getLat());
		if(zonecodeList == null || zonecodeList.isEmpty()) {
			return "no";
		}
		else{
			return "yes";
		}
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
