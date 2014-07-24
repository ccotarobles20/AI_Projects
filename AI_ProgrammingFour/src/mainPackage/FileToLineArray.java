package mainPackage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class FileToLineArray {
	
	public FileToLineArray() {
		
	}
	
	// Convert a given file into an array of strings, each string is a line from the file,
	// remove extra empty lines.
	public String[] convert(String filename) {
		File corpusFile = new File(filename);
		Scanner readCorpus;
		String line;
		// below set to true every time you access an empty line to trim multiple empty lines
		boolean recentEmptyLine = true;
		
		// We will eventually return an array of this size below:
		int arraySize = 1;
		
		// First Scan to find the size of the array that we need
		try {
			readCorpus = new Scanner(corpusFile);
			
			while (readCorpus.hasNextLine()) {
				
				line = readCorpus.nextLine();
				
				if (line.equals("")) {
					if (!recentEmptyLine) {
						arraySize++;
						recentEmptyLine = true;
					}
				}
				else {
					arraySize++;
					recentEmptyLine = false;
				}
			}
		}
		catch(IOException e) {
			System.err.println("Couldn't read input file!!  Maybe you have the wrong name");
		}
		
		String[] outputArray = new String[arraySize];
		outputArray[0] = ""; // inserting a blank line at the top will make future code easier the way I designed it.
		int nextIndex = 1; //next index in the outputArray to write to.
		recentEmptyLine = true;
		
		// Second Scan to populate the array
		try {
			readCorpus = new Scanner(corpusFile);
			
			while (readCorpus.hasNextLine()) {
				
				line = readCorpus.nextLine();
				line = line.trim();
				line = line.toLowerCase();
				
				if (line.equals("")) {
					if (!recentEmptyLine) {
						outputArray[nextIndex] = line;
						nextIndex++;
						recentEmptyLine = true;
					}
				}
				else {
					outputArray[nextIndex] = line;
					nextIndex++;
					recentEmptyLine = false;
				}
			}
		}
		catch(IOException e) {
			System.err.println("Couldn't read input file!!  Maybe you have the wrong name");
		}
		
		return outputArray;
	}
	
	/*
	 * Given a list of biographies, data, and an integer n, returns the appropriate array with
	 * the first n biographies.
	 */
	public String[] splitToTrain(String[] data, int n) {
		int counted = 0;
		String[] output;
		for (int i=0; i<data.length; i++) {
			if (data[i].equals("")) {
				counted++;
				if (counted > n) {
					output = Arrays.copyOfRange(data, 0, i);
					return output;
				}
			}
		}
		
		System.err.println("Your chosen n value is larger than the number of entries in the corpus!");
		return data;
	}
	
	/*
	 * Given a list of biographies called data, and an integer n, returns the array with the test
	 * set (ie every biography after the first n biographies.
	 */
	public String[] splitToTest(String[] data, int n) {
		int counted = 0;
		String[] output;
		for (int i=0; i<data.length; i++) {
			if (data[i].equals("")) {
				counted++;
				if (counted > n) {
					output = Arrays.copyOfRange(data, i, data.length);
					return output;
				}
			}
		}
		
		System.err.println("Your chosen n value is larger than the number of entries in the corpus!");
		output = new String[0];
		return output;
	}
	
	/*
	 * Given a string array that contains the information for several biographies, counts the
	 * empty lines to determine how many biographies there are.
	 */
	public int getBioNumbers(String[] data) {
		int bioNumbers = 0;
		if (data[0] != "") {
			bioNumbers++;
		}
		
		for (String s : data) {
			if (s.equals("")) {
				bioNumbers++;
			}
		}
		
		return bioNumbers;
	}
}