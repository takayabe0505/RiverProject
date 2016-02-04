package AnalysisofExpData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import jp.ac.ut.csis.pflow.geom.LonLat;

public class MovementLength {

	public static String basicpath = "/home/t-tyabe/Data/KinugawaFlood/";

	public static void main(String args[]) throws IOException{

		File IDFile = new File(basicpath+"IDswithhomesinArea.csv");
		HashSet<String> IDs = getIDs(IDFile);

		File res = new File(basicpath+"flood_length.csv");
		BufferedWriter bw = new BufferedWriter(new FileWriter(res));
		
		File in = new File(basicpath+"logsofIDwithhomesinArea20150910_from07.csv");
		HashMap<String, HashMap<String, HashMap<Integer,LonLat>>> alldata = getallData(in,IDs);
		for(String date : alldata.keySet()){
			for(String id : alldata.get(date).keySet()){
				Double distance = calculateLength(alldata.get(date).get(id));
				String points = String.valueOf(alldata.get(date).get(id).size());
				String strdis = String.valueOf(distance);
				bw.write(id + "," + date + "," + strdis + "," + points);
				bw.newLine();
			}
		}
		bw.close();
	}

	public static Double calculateLength(HashMap<Integer,LonLat> map){ //return km

		List<Entry<Integer, LonLat>> entries = new ArrayList<Entry<Integer, LonLat>>(map.entrySet());
		Collections.sort(entries, new Comparator<Entry<Integer, LonLat>>() {
			//î‰ärä÷êî
			@Override
			public int compare(Entry<Integer, LonLat> o1, Entry<Integer, LonLat> o2) {
				return o1.getKey().compareTo(o2.getKey());  
			}
		});

		//		System.out.println(entries);
		LonLat prevpoint = new LonLat(0,0);
		Double lengthsum = 0d;
		for (Entry<Integer, LonLat> e : entries) {
			if(prevpoint.getLon()==0){
				prevpoint = e.getValue();
			}
			else{
				lengthsum = lengthsum + prevpoint.distance(e.getValue());
				prevpoint = e.getValue();
			}
		}
		//		String res = String.valueOf(lengthsum/1000d);
		//		System.out.println("total length: "+ res);
		return lengthsum/1000d;
	}
	
	public static HashSet<String> getIDs(File in) throws IOException{
		HashSet<String> targetIDs = new HashSet<String>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			targetIDs.add(line);
		}
		br.close();
		return targetIDs;
	}

	public static HashMap<String, HashMap<String, HashMap<Integer,LonLat>>> getallData(File in, HashSet<String> ids) throws NumberFormatException, IOException{

		HashMap<String, HashMap<String, HashMap<Integer,LonLat>>> res = new HashMap<String,HashMap<String, HashMap<Integer,LonLat>>>();

		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split("\t");
			if(tokens.length>=5){
				if(!tokens[4].equals("null")){
					String id = tokens[0];		
					if(ids.contains(id)){
						Integer time = getonlytimeinsecs(tokens[4]);
						String date = getonlydate(tokens[4]);

						Double lat = Double.parseDouble(tokens[2]);
						Double lon = Double.parseDouble(tokens[3]);
						LonLat p = new LonLat(lon,lat);
						if(res.containsKey(date)){
							if(res.get(date).containsKey(id)){
								res.get(date).get(id).put(time, p);
							}
							else{
								HashMap<Integer,LonLat> temp = new HashMap<Integer,LonLat>();
								temp.put(time, p);
								res.get(date).put(id, temp);
							}
						}
						else{
							HashMap<Integer,LonLat> temp = new HashMap<Integer,LonLat>();
							temp.put(time, p);
							HashMap<String, HashMap<Integer,LonLat>> tmp2 = new HashMap<String, HashMap<Integer,LonLat>>();
							tmp2.put(id, temp);
							res.put(date, tmp2);
						}
					}
				}
			}
		}
		br.close();
		return res;
	}

	public static Integer getonlytimeinsecs(String t){
		String[] x = t.split("T");
		String time = x[1].substring(0,8);
		Integer hour = Integer.valueOf(time.split(":")[0]);
		Integer mins = Integer.valueOf(time.split(":")[1]);
		Integer secs = Integer.valueOf(time.split(":")[2]);
		Integer timeinsecs = hour*3600+mins*60+secs;
		return timeinsecs;
	}

	public static String getonlydate(String t){
		String[] x = t.split("T");
		String date = x[0];
		return date;
	}

}
