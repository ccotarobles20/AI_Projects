package mainPackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class BackEnd {
	
	LinkedList<String> data;
	LinkedList<String> key;
	
	String[] solutionArray;
	String[] nodeNames;
	
	boolean noSolution = false;
	
	public BackEnd(String filename) {
		
		File file = new File(filename);
		boolean writeToData = true;
		
		data = new LinkedList<String>();
		key = new LinkedList<String>();
		
		try {
			
			Scanner sc = new Scanner(file);
			String nl;
			
			while (sc.hasNextLine()) {
				nl = sc.nextLine();
				
				if (nl.equals("NO SOLUTION")) {
					noSolution = true;
					break;
				}
				
				if (nl.equals("0")) {
					writeToData = false;
				}
				else if (writeToData) {
					data.add(nl);
				}
				else {
					key.add(nl);
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void runBackEnd() {
		
		if (noSolution) {
			System.out.println("NO SOLUTION");
			return;
		}
				
		int numberOfSteps = Integer.parseInt(key.get(1));
		int numberOfNodes = Integer.parseInt(key.get(3));
		
		String names = key.get(7);
		nodeNames = new String[numberOfNodes];
		
		Scanner sc = new Scanner(names);
		String next = "";
		int nextIndex = 1;
		nodeNames[0] = "GOAL";
		while (sc.hasNext()) {
			next = sc.next();
			if (!next.equals("GOAL")) {
				nodeNames[nextIndex] = next;
				nextIndex++;
			}
		}
		
		solutionArray = new String[numberOfSteps];
		int index = 0;
		
		for (int i=0; i<numberOfSteps*numberOfNodes; i++) {
			char value = data.get(i).charAt(data.get(i).length()-1);
			if (value == 'T') {
				String answer = "At square" + decode(i+1, numberOfNodes, numberOfSteps);
				solutionArray[index] = answer;
				index++;
			}
		}
		
		for (int element=0; element<solutionArray.length; element++) {
			System.out.println(solutionArray[element]);
		}

		
		//Write to output file:
		
		OutputWriter output = new OutputWriter("output3.txt", solutionArray);		
		
		System.out.println("Open filename output3.txt for results");

	}
	
	public String decode(int val, int numberOfNodes, int numberOfSteps) {
		String returnString = "";
				
		returnString += " " + Stringify(val%numberOfNodes);
		
		returnString += " at time " + (Math.ceil((double)val/(double)numberOfNodes)-1);
		
		return returnString;
	}
	
	public String Stringify(int number) {
		return nodeNames[number];
	}
}