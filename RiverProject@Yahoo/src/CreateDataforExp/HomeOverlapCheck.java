package CreateDataforExp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import jp.ac.ut.csis.pflow.geom.GeometryChecker;
import jp.ac.ut.csis.pflow.geom.LonLat;

public class HomeOverlapCheck {

	public static String basicpath = "/home/t-tyabe/Data/";
	static File shapedir = new File(basicpath+"Kinugawa_Ibaragi_shp/Kinugawa_Ibaragi_shp");
	static GeometryChecker gchecker = new GeometryChecker(shapedir);

	public static void main(String args[]) throws ParseException, IOException{

		File in = new File(basicpath+"id_home.csv");
		File out = new File(basicpath+"id_home_real.csv");
		CheckOverlap(in, out);

	}


	public static void CheckOverlap(File in, File out) throws IOException, ParseException{

		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;
		while((line=br.readLine())!=null){
			String[] tokens = line.split("\t");
			String id = tokens[0];
			Double lat = Double.parseDouble(tokens[2]);
			Double lon = Double.parseDouble(tokens[3]);
			if(AreaOverlap(new LonLat(lon,lat)).equals("yes")){
				bw.write(id);
				bw.newLine();
			}

		}
		br.close();
		bw.close();
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

}
