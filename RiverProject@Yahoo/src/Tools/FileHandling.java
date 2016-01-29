package Tools;

import java.io.File;

public class FileHandling {
	
	public static void extractfromcommand(String date){
		ProcessBuilder pb = new ProcessBuilder("tar", "zxvf",
				"/tmp/bousai_data/gps_"+date+".tar.gz", 
				"-C","/home/t-tyabe/Data/");
		pb.inheritIO();
		try {
			Process process = pb.start();
			process.waitFor();
//			System.out.println(pb.redirectInput());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
//		System.out.println("=======done=======");
		File out = new File("/home/t-tyabe/Data/grid/0/tmp/ktsubouc/gps_"+date);
		out.renameTo(new File("/home/t-tyabe/Data/grid/0/tmp/ktsubouc/gps_"+date+".csv"));
	}
	
}
