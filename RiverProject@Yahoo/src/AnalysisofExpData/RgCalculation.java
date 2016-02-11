package AnalysisofExpData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;

public class RgCalculation {

	public static String basicpath = "/home/t-tyabe/Data/KinugawaFlood/";

	public static void main(String args[]) throws IOException{

		File IDFile = new File(basicpath+"id_evac_yesno.csv");
		HashMap<String,String> IDs = getIDs(IDFile);
		System.out.println("got IDs:" + IDs.size());

		File res = new File(basicpath+"flood_result.csv");
		BufferedWriter bw = new BufferedWriter(new FileWriter(res,true));

		File in = new File(basicpath+"logsofIDwithhomesinArea20150910.csv");
		HashMap<String, HashMap<String, ArrayList<LonLat>>> alldata = getallData(in,IDs);
		System.out.println("size of map:" +alldata.size());
		
		for(String date : alldata.keySet()){
			String day = date.split("-")[2];
			String intday = null;
			if(day.equals("10")){
				intday = "10";
			}
			else{
				intday = "99";
			}
			for(String id : alldata.get(date).keySet()){
				
				Double rg = gyration(alldata.get(date).get(id));
				
				String points = String.valueOf(alldata.get(date).get(id).size());
				String strrg = String.valueOf(rg);
				if(rg<1000d){
					bw.write(id + "," + IDs.get(id) + "," + intday + "," + strrg + "," + points);
					bw.newLine();
				}
			}
		}
		bw.close();
		
		
	}

	public static Double gyration(ArrayList<LonLat> list){
		Double radius = 0d;
		Double tempsum = 0d;
		LonLat ave = average(list);
		for(LonLat p:list){
			tempsum = tempsum + Math.pow(p.distance(ave), 2);
		}
		radius = Math.pow(tempsum/list.size(), 0.5);
		return radius;
	}

	public static LonLat average(ArrayList<LonLat> list){
		Double lonave = 0d;
		Double latave = 0d;
		Double lonsum = 0d;
		Double latsum = 0d;
		for(LonLat p:list){
			lonsum = lonsum + p.getLon();
			latsum = latsum + p.getLat();
		}
		lonave = lonsum/list.size();
		latave = latsum/list.size();
		LonLat avepoint = new LonLat(lonave,latave);
		return avepoint;
	}

	public static HashMap<String,String> getIDs(File in) throws IOException{
		HashMap<String,String> targetIDs = new HashMap<String,String>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			targetIDs.put(tokens[0],tokens[1]);
		}
		br.close();
		return targetIDs;
	}

	public static HashMap<String, HashMap<String, ArrayList<LonLat>>> getallData(File in, HashMap<String,String> ids) throws NumberFormatException, IOException{

		HashMap<String, HashMap<String, ArrayList<LonLat>>> res = new HashMap<String,HashMap<String, ArrayList<LonLat>>>();

		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String id = tokens[0];		
			if(ids.keySet().contains(id)){
//				Integer time = getonlytimeinsecs(tokens[3]);
				String date = getonlydate(tokens[3]);

				Double lat = Double.parseDouble(tokens[2]);
				Double lon = Double.parseDouble(tokens[1]);
				LonLat p = new LonLat(lon,lat);
				if(res.containsKey(date)){
					if(res.get(date).containsKey(id)){
						res.get(date).get(id).add(p);
					}
					else{
						ArrayList<LonLat> temp = new ArrayList<LonLat>();
						temp.add(p);
						res.get(date).put(id,temp);
					}
				}
				else{
					ArrayList<LonLat> temp = new ArrayList<LonLat>();
					temp.add(p);
					HashMap<String, ArrayList<LonLat>> tmp2 = new HashMap<String, ArrayList<LonLat>>();
					tmp2.put(id, temp);
					res.put(date, tmp2);
				}
			}
		}
		br.close();
		return res;
	}

	public static Integer getonlytimeinsecs(String t){
		String[] x = t.split(" ");
		String time = x[1].substring(0,8);
		Integer hour = Integer.valueOf(time.split(":")[0]);
		Integer mins = Integer.valueOf(time.split(":")[1]);
		Integer secs = Integer.valueOf(time.split(":")[2]);
		Integer timeinsecs = hour*3600+mins*60+secs;
		return timeinsecs;
	}

	public static String getonlydate(String t){
		String[] x = t.split(" ");
		String date = x[0];
		return date;
	}

}
