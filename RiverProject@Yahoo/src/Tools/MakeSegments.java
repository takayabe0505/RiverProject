package Tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MakeSegments {

	//	public static String basicpath = "c:/users/yabetaka/Desktop/";
	public static String basicpath = "c:/users/t-tyabe/Desktop/";
	public static String lvl = "3";
	public static Double ht  = 17d;
	public static Double ht2  = 22d;
	public static Double normal = 16d;
	public static Double normal2 = 20d;
	public static Double bunsan = 1.5d;

	public static void main(String args[]) throws IOException{

		File in = new File(basicpath+"office_exit_diff_rain_final.csv");
		File out = new File(basicpath+"rain_level1and4.csv");

		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;

		int count1 = 0; int count2 = 0;
		int count3 = 0; int count4 = 0; int count5 = 0;
		int count6 = 0; int count7 = 0; int count8 = 0;

		while((line=br.readLine())!=null){
			String[] ts = line.split(",");
			String diff = ts[1];
			String level = ts[3];
			Double hittime = Double.parseDouble(ts[4]);
			Double normaltime = Double.parseDouble(ts[5]);
			Double normalbunsan = Double.parseDouble(ts[6]);
			//			Double disastertime = Double.parseDouble(ts[7]);
			Double distance = Double.parseDouble(ts[8])*100;

			if((hittime>=ht)&&(hittime<=ht2)){
				bw.write("1,"+diff);
				bw.newLine();
				if(level.equals("4")){
					bw.write("2,"+diff);
					bw.newLine();
					if((normaltime>=normal)&&(normaltime<=normal2)){
						bw.write("3,"+diff);
						bw.newLine();
					}
					else if(normaltime<=normal){
						bw.write("4,"+diff);
						bw.newLine();
					}
					else if(normaltime>=normal2){
						bw.write("5,"+diff);
						bw.newLine();
					}
				}
				else if (level.equals("1")){
					bw.write("6, " + diff);
					bw.newLine();
				}
			}
		}
		bw.close();
		br.close();
		System.out.println("done properly");
		System.out.println("1:"+count1);
		System.out.println("2:"+count2);
		System.out.println("3:"+count3);
		System.out.println("4:"+count4);
		System.out.println("5:"+count5);
		System.out.println("6:"+count6);
		System.out.println("7:"+count7);
		System.out.println("8:"+count8);

	}
}
