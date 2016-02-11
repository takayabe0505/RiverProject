package CreateDataforExp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;

public class DivideLogsbyID {

	public static String basicpath = "/home/t-tyabe/Data/KinugawaFlood/";

	public static void main(String args[]) throws ParseException, IOException{
		File IDFile = new File(basicpath+"id_home_real.csv");
		File alllogs = new File(basicpath+"logsofIDwithhomesinArea20150910_from07_onlyVictims.csv");
		run(alllogs,IDFile);
	}

	public static void run(File alllogs, File IDfile) throws ParseException, IOException{
		HashSet<String> IDset = getIDMap(IDfile);
		File outpath = new File(basicpath+"byIDs/");
		outpath.mkdir();
		for(String id : IDset){
			File out = new File(basicpath+"byIDs/"+id+".csv");
			ReadoutOnlyLogsofID(alllogs,out,id);
			System.out.println("done "+id);
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

	public static void ReadoutOnlyLogsofID(File in, File out, String ids) throws IOException, ParseException{
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out,true));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split(",");
			String id = tokens[0];		
			if(ids.equals(id)){
//				String time = tokens[3];
//				Double lat = Double.parseDouble(tokens[2]);
//				Double lon = Double.parseDouble(tokens[1]);
				bw.write(line);
				bw.newLine();
			}
		}
		br.close();
		bw.close();
	}

	public static String getfulltime(String t){
		String[] x = t.split("T");
		String time = x[1].substring(0,8);
		String res = x[0]+ " " + time;
		return res;
	}
}
