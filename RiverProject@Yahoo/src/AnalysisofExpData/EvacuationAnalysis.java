package AnalysisofExpData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.GeometryChecker;
import jp.ac.ut.csis.pflow.geom.LonLat;
import jp.ac.ut.csis.pflow.geom.STPoint;

public class EvacuationAnalysis {

	public static String basicpath = "/home/t-tyabe/Data/";
	static File shapedir = new File(basicpath+"Kinugawa_Ibaragi_shp/Kinugawa_Ibaragi_shp");
	static GeometryChecker gchecker = new GeometryChecker(shapedir);

	protected static final SimpleDateFormat HMS  = new SimpleDateFormat("HH:mm:ss");//change time format
	protected static final SimpleDateFormat YMD = new SimpleDateFormat("yyyy-MM-dd");//change time format
	protected static final SimpleDateFormat FullDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//change time format

	public static void main(String args[]) throws IOException, NumberFormatException, ParseException{
		String hitdate = "20150512";
		String hittime = "23:57:00";
		File in = new File(basicpath+"logsofIDwithhomesinArea"+hitdate+"_limitedtimerange.csv");

		HashMap<String, HashMap<String,ArrayList<STPoint>>> id_alllogs = MapofIDanditsLogs(in);

		evacuationcheck(id_alllogs, hittime);

	}

	public static void evacuationcheck(HashMap<String, HashMap<String,ArrayList<STPoint>>> map, String hittime) throws IOException{
		File out = new File(basicpath+"ID_date_beforeandafterhittime.csv");
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		for(String id : map.keySet()){
			
		}
		bw.close();
	}

	public static void calculatemovement(HashMap<String, ArrayList<STPoint>> map){

	}
	
	


	public static HashMap<String, HashMap<String,ArrayList<STPoint>>> MapofIDanditsLogs(File in) throws NumberFormatException, IOException, ParseException{
		HashMap<String, HashMap<String,ArrayList<STPoint>>> map = new  HashMap<String, HashMap<String,ArrayList<STPoint>>>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String id = tokens[0];
			LonLat p  = new LonLat(Double.parseDouble(tokens[1]),Double.parseDouble(tokens[2]));
			String time = tokens[3]; // yyyy-mm-dd HH:MM:SS
			Date date = FullDate.parse(time);
			String ymd = tokens[3].substring(0,10); //yyyy-mm-dd
			STPoint stp = new STPoint(date,p.getLon(),p.getLat());
			if(map.containsKey(id)){
				if(map.get(id).containsKey(ymd)){
					map.get(id).get(ymd).add(stp);
				}
				else{
					ArrayList<STPoint> tmp = new ArrayList<STPoint>();
					tmp.add(stp);
					map.get(id).put(ymd, tmp);
				}
			}
			else{
				ArrayList<STPoint> tmp = new ArrayList<STPoint>();
				tmp.add(stp);
				HashMap<String,ArrayList<STPoint>> temp = new HashMap<String,ArrayList<STPoint>>();
				temp.put(ymd, tmp);
				map.put(id, temp);
			}
		}
		br.close();
		return map;
	}

}
