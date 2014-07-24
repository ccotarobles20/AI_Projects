package mainPackage;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Scanner;

public class Learner {
	
	// Information from training set will be stored in these Hashtables.
	Hashtable<String, Integer> categoryCounts;
	Hashtable<String, Hashtable<String, Integer>> wordCounts;
	
	Hashtable<String, Integer> wordHasAppeared; // Keeps track of all words that have appeared in the biography being studied in the training set.

	public Learner() {
		categoryCounts = new Hashtable<String, Integer>();
		wordCounts = new Hashtable<String, Hashtable<String, Integer>>();
		wordHasAppeared = new Hashtable<String, Integer>();
	}
		
	/*
	 * Counts the number of each biography using a Hashtable to record the data:
	 */
	public Hashtable<String, Integer> countCategories(String[] data) {
				
		for (int i=0; i<data.length; i++) {
			if (data[i].equals("")) {
				i = i+2;
				if (categoryCounts.get(data[i]) != null) {
					categoryCounts.put(data[i], categoryCounts.get(data[i])+1);
				}
				else {
					categoryCounts.put(data[i], 1);
					wordCounts.put(data[i], new Hashtable<String, Integer>());
				}
			}
		}
		
		return categoryCounts;
	}
	
	/*
	 * Counts the number of each word in each type of biography using a Hashtable to record the data:
	 */
	public Hashtable<String, Hashtable<String, Integer>> countWords(String[] data) {
		
		Hashtable<String, Integer> toHash;
								
		for (int i=0; i<data.length; i++) {
			if (data[i].equals("")) {
				wordHasAppeared.clear();
				i = i+2;
				toHash = wordCounts.get(data[i]);
				while ((i+1 < data.length) && (!data[i+1].equals(""))) {
					i++;
					hashWords(toHash, data[i]);
				}
			}
		}
		
		return wordCounts;
	}
	
	private void hashWords(Hashtable<String, Integer> toHash, String line) {
		Scanner scan = new Scanner(line);
		
		while (scan.hasNext()) {
			String word = scan.next();
			
			word = filter(word); // filter out any periods, commas, extra spaces, other punctuation.
			
			// If the word is a stop word, skip it:
			if (stopWord(word)) {
				continue;
			}
			
			// If the word has already appeared in this biography, skip it:
			if (wordHasAppeared.get(word) != null) {
				continue;
			}
			
			//Otherwise, mark the word as appeared and then add it to the hashTable
			wordHasAppeared.put(word, 1);
			if (toHash.get(word) == null) {
				toHash.put(word, 1);
			}
			else {
				toHash.put(word, toHash.get(word)+1);
			}
			
		}
	}
	
	private String filter(String s) {
		
		StringBuilder sb = new StringBuilder();
		
		for (int i=0; i<s.length(); i++) {
			if (s.charAt(i) != ' ' && s.charAt(i) != ',' && s.charAt(i) != '.') {
				sb.append(s.charAt(i));
			}
		}
		
		s = sb.toString();
		
		return s;
	}
	
	/*
	 * 	Checks every word in stopWords.txt to see if the given word is a stopword.
	 * Could also convert this into a Hashtable to access faster, but then it would
	 * require bringing the entire stopword list into memory.
	 */
	private boolean stopWord(String word) {
		
		if (word.length() < 3) {
			return true;
		}
		
		File file = new File("stopWords.txt");
		Scanner scan;
		
		try {
			scan = new Scanner(file);
			String next;
			
			while (scan.hasNext()) {
				next = scan.next();
				if (next.equals(word)) {
					return true;
				}
			}
		}
		catch(IOException e) {
			System.err.println("Couldn't find the file named stopWords.txt");
		}
		
		return false;
	}
}