package mainPackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class DavisPutnam {
	String[] backMatter; // Back Matter that should not be modified
	String[] runMatter; // Stuff that we will need to run the Davis-Putnam algorithm on.  We will convert this to a linked list.
	
	/*
	 * My Davis-Putnam Algorithm uses a linked-list because it's easier to remove elements than
	 * from an array.  The array above stores
	 */
	LinkedList<LinkedList<Integer>> endRunMatter; // Linked List to run Davis Putnam on
	int uniqueNumbers; // Number of atoms
	
	// Constructor reads in the filename passed in and then separates the back matter that will be used
	// later from the stuff that we need to run the Davis-Putnam on.
	public DavisPutnam(String textfile) {
		
		File file = new File(textfile);
		
		// First, count the number of lines in the textfile
		Scanner scanner;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.err.println("File not Found for "+textfile);
			e.printStackTrace();
			return;
		}
		int numLines = 1;
		while (scanner.hasNextLine()) {
			numLines++;
			scanner.nextLine();
		}
		
		// Then, a second scan of the data creates an array storing each line as a string.
		Scanner scanner2;
		try {
			scanner2 = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.err.println("File not Found for "+textfile);
			e.printStackTrace();
			return;
		}
		
		String[] input = new String[numLines];
		int index = 0;
		while(scanner2.hasNextLine()) {
			input[index] = scanner2.nextLine();
			input[index] = input[index] + '\n';
			index++;
		}
						
		uniqueNumbers = 0; //initialize number of atoms to 0, we will calculate later
		
		// Go through the array and then separate out the backMatter from the runMatter.
		for (int i=0; i<input.length; i++) {
			String line = input[i];
			if (line.equals("0\n")) {
				runMatter = Arrays.copyOfRange(input, 0, i);
				backMatter = Arrays.copyOfRange(input, i, input.length-1);
				break;
			}
		}
		
		if (backMatter == null || runMatter == null) {
			System.err.println("ERROR in parsing the inputs in Davis-Putnam.  You may not have included a 0 line separator");
		}
		
		if (runMatter == null) {
			System.err.println("Nothing to run the Davis-Putnam algorithm on");
			return;
		}
		
		// Translate the Run Matter into a linked-list
		endRunMatter = translateRunMatter();

	}
	
	// Call this to run the Davis-Putnam Algorithm
	public int[] runDavisPutnam() {
		int[] results = new int[uniqueNumbers];
		boolean noSolution = false;
		
		for (int k=0; k<results.length; k++) {
			results[k] = 0;
		}

		// Another function with a similar name (but it takes three inputs) will run the Davis-Putnam algorithm:
		results = runDavisPutnam(results, 0, endRunMatter);
		
		if (results == null) {
			System.err.println("Davis-Putnam failed on this input!");
			noSolution = true;
		}
		
		//Write to output file:
		
		String[] resultString = new String[results.length + backMatter.length];
		
		if (noSolution) {
			resultString = new String[1];
			resultString[0] = "NO SOLUTION";
		}
		else {
			for (int i=0; i<results.length; i++) {
				if (results[i] < 0) {
					resultString[i] = (i+1) + " F";
				}
				else {
					resultString[i] = (i+1) + " T";
				}
			}
			
			for (int j=0; j<backMatter.length; j++) {
				resultString[results.length + j] = backMatter[j];
			}
		}
		
		OutputWriter output = new OutputWriter("output2.txt", resultString);
				
		System.out.println("Success on Davis-Putnam");
		return results;
	}
	
	// This is only called from the method above and should not be called externally.
	private int[] runDavisPutnam(int[] results, int index, LinkedList<LinkedList<Integer>> translatedRunMatter) {
		
		if (translatedRunMatter == null) {
			return null;
		}
		
		int smallest = 999999999; // The smallest atom which we will choose if/when we have to make a choice
		
		// Once we've found the answer:
		if (translatedRunMatter.size() == 0) {
			return results;
		}
		
		// Pure literals stores the number of literals of each index's value in two arrays:
		/*
		 * pureLiterals for positive atoms
		 * pureLiteralsNegative for negative atoms
		 */
		int[] pureLiterals = new int[uniqueNumbers];
		for (int i=0; i<uniqueNumbers; i++) {
			pureLiterals[i] = 0;
		}
				
		int[] pureLiteralsNegative = new int[uniqueNumbers];
		for (int i=0; i<uniqueNumbers;i++) {
			pureLiteralsNegative[i] = 0;
		}
		
		for (LinkedList<Integer> element : translatedRunMatter) {
			//Empty Clause
			if (element.size() == 0) {
				return null;
			}
			//Singleton Clause
			else if (element.size() == 1) {
				int prop = element.get(0);
				results[Math.abs(prop)-1] = prop/Math.abs(prop); //Set to either 1 or negative one
				translatedRunMatter = propagate(translatedRunMatter, prop);
				return runDavisPutnam(results, index+1, translatedRunMatter);
			}
			// Pure Literals:
			for (Integer innerElement : element) {
				
				if (innerElement < 0) {
					int[] temp = pureLiterals;
					pureLiterals = pureLiteralsNegative;
					pureLiteralsNegative = temp;
				}
				
				// First, check for pure literals:
				if (pureLiterals[Math.abs(innerElement)-1] != -1) {
					pureLiterals[Math.abs(innerElement)-1] += 1;
					
					if (pureLiteralsNegative[Math.abs(innerElement)-1] > 0) {
						pureLiterals[Math.abs(innerElement)-1] = -1;
						pureLiteralsNegative[Math.abs(innerElement)-1] = -1;
					}
				}
				
				if (innerElement < 0) {
					int[] temp = pureLiterals;
					pureLiterals = pureLiteralsNegative;
					pureLiteralsNegative = temp;
				}
				
				// Then find the next choice point:
				if (Math.abs(innerElement) < smallest) {
					smallest = Math.abs(innerElement);
				}
			}
		}
		
		for (int i=0; i<uniqueNumbers; i++) {
			if (pureLiterals[i] > 0 || pureLiteralsNegative[i] > 0) {
				int literal = i+1;
				if (pureLiteralsNegative[i] > 0) {
					literal = -i-1;
				}
				results[Math.abs(literal)-1] = literal/Math.abs(literal);
				translatedRunMatter = propagate(translatedRunMatter, literal);
				return runDavisPutnam(results, index+1, translatedRunMatter);
			}
		}
		
		
		results[smallest-1] = 1;
		LinkedList<LinkedList<Integer>> backup = createCopy(translatedRunMatter);
		LinkedList<LinkedList<Integer>> temp1 = propagate(translatedRunMatter, smallest);
		int[] newResults = runDavisPutnam(results, index+1, temp1);
		if (newResults == null) {
			results[smallest-1] = -1;
			LinkedList<LinkedList<Integer>> temp2 = propagate(backup, -smallest);
			int[] newResults2 = runDavisPutnam(results, index+1, temp2);
			if (newResults2 != null) {
				return newResults2;
			}
		}
		return newResults;
	}
	
	// This makes a copy of the actual list, making a deep copy so it copies each integer and
	// creates a whole new linked list.  We need this to save the list when we make a choice
	// in case our choice was wrong then we'll go back to the previous state.
	LinkedList<LinkedList<Integer>> createCopy(LinkedList<LinkedList<Integer>> original) {
		LinkedList<LinkedList<Integer>> copy = new LinkedList<LinkedList<Integer>>();
		
		for (LinkedList<Integer> element : original) {
			LinkedList<Integer> newList = new LinkedList<Integer>();
			copy.add(newList);
			for (Integer i : element) {
				int newI = i;
				newList.add(newI);
			}
		}
		
		return copy;
	}
	
	// Removes chosenValue from the linkedList according to propagate rules for the Davis-Putnam algorithm
	LinkedList<LinkedList<Integer>> propagate(LinkedList<LinkedList<Integer>> translatedRunMatter, int chosenValue) {
		for (LinkedList<Integer> element : translatedRunMatter) {
			
			int p = element.indexOf(-chosenValue);
			if (p != -1) {
				element.remove(p);
			}
		}
		
		// Deals with removing rows with positive chosen values yo!
		
		int num = translatedRunMatter.size();
		LinkedList<Integer> list;
		
		while (num > 0) {
			num--;
			list = translatedRunMatter.get(num);
			if (list.contains(chosenValue)) {				
				translatedRunMatter.remove(num);
			}
			if (list.size() == 0) {
				return null;
			}
		}
		
		//Print out the current state of everything
		/*for (LinkedList<Integer> element : translatedRunMatter) {
			for (Integer innerElement : element) {
				System.out.print(innerElement + " ");
			}
			System.out.println();
		}*/
				
				
		return translatedRunMatter;
	}
	
	// Translates the runMatter array into a linked list:
	LinkedList<LinkedList<Integer>> translateRunMatter() {
		LinkedList<LinkedList<Integer>> translatedArray = new LinkedList<LinkedList<Integer>>();
		
		for (int i=0; i<runMatter.length; i++) {
			String line = runMatter[i];
			LinkedList<Integer> parsedLine = parse(line);
			translatedArray.add(parsedLine);
		}
		
		return translatedArray;
	}
	
	// Parses a string into a linked list of the integers contained in that string.
	LinkedList<Integer> parse(String line) {
		LinkedList<Integer> parsedLine = new LinkedList<Integer>();
		int parsedLineLength = 1;
		
		for (int i=0; i<line.length(); i++) {
			if (line.charAt(i) == ' ') {
				parsedLineLength ++;
			}
		}
				
		int currentStartIndex = 0;
		
		for (int i=0; i<line.length(); i++) {
			if (line.charAt(i) == ' ' || line.charAt(i) == '\n') {
				int number = Integer.parseInt(line.substring(currentStartIndex, i));
				if (Math.abs(number) > uniqueNumbers) {
					uniqueNumbers = Math.abs(number);
				}
				parsedLine.add(number);
				currentStartIndex = i+1;
			}
		}
				
		return parsedLine;
	}
}