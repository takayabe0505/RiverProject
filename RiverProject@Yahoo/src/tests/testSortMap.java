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
	
		Map<String, Integer> map = new HashMap<String, Integer>(){    //TreeMap �ł��ǂ�
		    {
		        put("11", 13);
		        put("3", 10);
		        put("6",  6);
		        put("1", 99);
		        put("9",  9);
		    }
		};
		
		//Map.Entry �̃��X�g�����
		List<Entry<String, Integer>> entries = new ArrayList<Entry<String, Integer>>(map.entrySet());

		//Comparator �� Map.Entry �̒l���r
		Collections.sort(entries, new Comparator<Entry<String, Integer>>() {
			//��r�֐�
			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
				return o1.getKey().compareTo(o2.getKey());  
			}
		});

		//�m�F�p
		for (Entry<String, Integer> e : entries) {
			System.out.println(e.getKey() + " = " + e.getValue());
		}
	}
}
