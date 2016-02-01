package Tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CutDatabyDate {

	public static String basicpath = "/home/t-tyabe/Data/";

	public static void main(String args[]) throws IOException{
		
		String startday = "07";
		
		File in = new File(basicpath+"logsofIDwithhomesinArea20150910.csv");
		File out = new File(basicpath+"logsofIDwithhomesinArea20150910_from"+startday+".csv");
		
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter bw = new BufferedWriter(new FileWriter(out));
		String line = null;
		while((line=br.readLine())!=null){
			String[] ts = line.split(",");
			String date = ts[3]; //yyyy-mm-dd hh:mm:ss
			if(date.contains("2015-09-07")||date.contains("2015-09-08")||date.contains("2015-09-09")||date.contains("2015-09-10")){
				bw.write(line);
				bw.newLine();
			}
		}
		br.close();
		bw.close();
	}
	
}
