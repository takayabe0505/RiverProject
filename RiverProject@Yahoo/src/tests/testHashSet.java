package tests;

import java.util.HashSet;

public class testHashSet {

	public static void main(String args[]){
		
		HashSet<String> victimID = new HashSet<String>();
		method1(victimID);
		method2(victimID);
		
		System.out.println(victimID);
	}
	
	public static void method1(HashSet<String> victimID){
		victimID.add("007");
		victimID.add("James Bond");
	}
	
	public static void method2(HashSet<String> victimID){
		victimID.add("James Bond");
		victimID.add("James Bond 007");
	}
}
