package CreateMobMap;

import java.io.File;
import java.util.List;

import jp.ac.ut.csis.pflow.geom.GeometryChecker;
import jp.ac.ut.csis.pflow.geom.LonLat;

public class testOverlap {

	public static String basicpath = "/home/t-tyabe/Data/";
	static File shapedir = new File(basicpath+"Kinugawa_Ibaragi_shp");
	static GeometryChecker gchecker = new GeometryChecker(shapedir);

	
	public static void main(String args[]){
		
		Double lon = 139.71931458;
		Double lat =  36.14314262;
		
		if(AreaOverlap(new LonLat(lon,lat))==true){
			System.out.println("yeah");
		}
		else{
			System.out.println("nope");
		}
	}
	
	public static Boolean AreaOverlap(LonLat point){
		List<String> zonecodeList = gchecker.listOverlaps("A31_001",point.getLon(),point.getLat());
		if(zonecodeList == null || zonecodeList.isEmpty()) {
			return false;
		}
		else{
			return true;
		}
	}
	
}
