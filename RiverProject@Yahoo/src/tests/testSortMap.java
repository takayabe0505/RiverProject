package tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class testSortMap {
	
	public static void main(String args[]){
	
		Map<String, Integer> map = new HashMap<String, Integer>(){    //TreeMap でも良い
		    {
		        put("11", 13);
		        put("3", 10);
		        put("6",  6);
		        put("1", 99);
		        put("9",  9);
		    }
		};
		
		//Map.Entry のリストを作る
		List<Entry<String, Integer>> entries = new ArrayList<Entry<String, Integer>>(map.entrySet());

		//Comparator で Map.Entry の値を比較
		Collections.sort(entries, new Comparator<Entry<String, Integer>>() {
			//比較関数
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o1.getKey().compareTo(o2.getKey());  
			}
		});

		//確認用
		for (Entry<String, Integer> e : entries) {
			System.out.println(e.getKey() + " = " + e.getValue());
		}
	}
}
