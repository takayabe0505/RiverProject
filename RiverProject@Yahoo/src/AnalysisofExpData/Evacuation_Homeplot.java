package AnalysisofExpData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import jp.ac.ut.csis.pflow.geom.LonLat;

public class Evacuation_Homeplot {

	public static String basicpath = "/home/t-tyabe/Data/KinugawaFlood/";
	
	public static void main(String args[]) throws IOException{	
		File IDFile = new File(basicpath+"id_evac_yesno.csv");
		HashMap<String,String> IDs = getIDs(IDFile);
		System.out.println("got IDs:" + IDs.size());
		
		File HomepFile = new File(basicpath+"id_home_overlaparea.csv");
		HashMap<String,LonLat> ID_P = getHomeP(HomepFile);
		
		File out = new File(basicpath+"id_evac_yesno_homep.csv");
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		for(String id : IDs.keySet()){
			if(ID_P.containsKey(id)){
				bw.write(id + "," + IDs.get(id) + ","+ ID_P.get(id).getLon() + ","+ ID_P.get(id).getLat() );
				bw.newLine();
			}
		}
		bw.close();
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

	public static HashMap<String,LonLat> getHomeP(File in) throws IOException{
		HashMap<String,LonLat> targetIDs = new HashMap<String,LonLat>();
		BufferedReader br = new BufferedReader(new FileReader(in));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			Double lon = Double.parseDouble(tokens[1]);
			Double lat = Double.parseDouble(tokens[2]);
			
			targetIDs.put(tokens[0],new LonLat(lon,lat));
		}
		br.close();
		return targetIDs;
	}
}

