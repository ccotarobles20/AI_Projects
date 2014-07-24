package mainPackage;

import java.util.Hashtable;
import java.util.Scanner;

public class Classifier {
	
	// Hashtables for the data from the training sets:
	Hashtable<String, Integer> categoryHash;
	Hashtable<String, Hashtable<String, Integer>> wordHash;
	
	// The Hashtable alreadySeen stores any strings seen in the biography currently being looked at
	// to avoid duplicate recordings.
	Hashtable<String, Integer> wordHasAppeared;
	
	Probabilities prob;
	Object[] categories; //An array containing all the keys in categoryHash
	String[] output; // The output we will write.
	
	public Classifier(int n, Hashtable<String, Integer> C, Hashtable<String, Hashtable<String, Integer>> W) {
		categoryHash = C;
		wordHash = W;
		wordHasAppeared = new Hashtable<String, Integer>();
		prob = new Probabilities(categoryHash, wordHash);
		categories = categoryHash.keySet().toArray();
		output = new String[n*3+1-3]; // Each biography has three lines (Name and prediction, probabilities, and then a blank line separator.  The +1 is for the total probability at the bottom.
		for (int i=0; i<output.length; i++) {
			output[i] = "";
		}
	}
	
	public void classifyData(String[] data) {
		String line;
		
		double Prob = 0;
		int originalIndex; // Used when calculating the probabilities of each category for a biography, so we can easily go back to the start of the list of words.
		
		double BestProb = 0;
		String bestCategory = ""; // The best category (one with highest probability) for a biography.
		
		int outputIndex = 0; //The line we'll write the output to.
		int guessesRight = 0;
		int totalGuesses = 0;
		
		// RawProbs remembers the probabilities for each category for a biography so we can then write those probablities to the output when we're finished calculating:
		double[] rawProbs = new double[categories.length];
		int rawIndex = 0;
				
		for (int i=0; i<data.length; i++) {

			line = data[i];
			
			if (line.equals("")) {
				// First, write the name of the person who's biography this is to the output:
				output[outputIndex] = data[i+1] + ".";
				
				// Incrememt i by 2 (skipping over the answer category) to look at the words in the bio.
				i = i+2;
				
				// Remember where this bio starts, the original index:
				originalIndex = i;
				
				BestProb = 999999999;
				bestCategory = "";
				rawIndex = 0; // Raw index stores...
				
				// Next, calculate the probability for each category on this biography:
				for (Object cat : categories) {
					Prob = 0;
					i = originalIndex;
					wordHasAppeared.clear();
					while ((i+1 < data.length) && (!data[i+1].equals(""))) {
						i++;
						Prob = Prob + L(data[i], (String)cat);
					}
					Prob = Prob + prob.L((String)cat);
					rawProbs[rawIndex] = Prob;
					rawIndex++;
					//System.out.println("Calculation: "+P+" for bio with CAT: "+(String)cat);
					if (Prob < BestProb) {
						BestProb = Prob;
						bestCategory = (String)cat;
					}
				}
				
				// Write all the calculated probabilities to the output:
				recordProbabilities(BestProb, rawProbs, outputIndex+1);
				
				// Increment the number of guesses by 1.
				totalGuesses++;
				
				// Write out the results (what our guess was and if it was right or wrong).  If it's right, increment the number of correct guesses by 1.
				output[outputIndex] = output[outputIndex] + "  Prediction: " + bestCategory + ".";
				if (bestCategory.equals(data[originalIndex])) {
					output[outputIndex] = output[outputIndex] + "  Right.";
					guessesRight++;
				}
				else {
					output[outputIndex] = output[outputIndex] + "  Wrong.";
				}
				outputIndex+=3;
			}
		}
		
		//Write the final results
		output[output.length-1] = "Overall accuracy: "+guessesRight+" out of "+totalGuesses+" = "+roundDoubleToTwoPlaces((double)guessesRight/(double)totalGuesses);
		
		//Write the output array to a text file and also to the console:
		writeOutput();
	}
	/*
	 * Writes the final probabilities to the array
	 */
	private void recordProbabilities(double m, double[] c, int outputWriteIndex) {
		String tab = "";
		
		double s = 0;
				
		for (int i=0; i<c.length; i++) {
			if (c[i]-m < 7) {
				c[i] = (Math.pow(10, (m-c[i])));
				s += c[i];
			}
			else {
				c[i] = 0;
			}
		}
		
		for (int i=0; i<c.length; i++) {
			c[i] = roundDoubleToTwoPlaces(c[i]/s);
			output[outputWriteIndex] = output[outputWriteIndex] + tab + categories[i] + ": "+c[i];
			if (i == 0) {
				tab = "     ";
			}

		}
	}
	
	/*
	 * Round a decimal to two places:
	 */
	private double roundDoubleToTwoPlaces(double num) {
		num = num * 100.0;
		num = Math.round(num);
		num = num/100.0;
		return num;
	}
	
	/*
	 * Write the output to a text file and also the console
	 */
	private void writeOutput() {
		//Write to external text file.
		OutputWriter o = new OutputWriter("output.txt", output);
		
		// Also write to the console
		for (String s : output) {
			System.out.println(s);
		}
	}
	
	public double L(String line, String category) {
		
		// For each W, compute L(W|C) then return return the sum of all those values:
				
		Scanner sc = new Scanner(line);
		String next;
		double cur = 0;
				
		while (sc.hasNext()) {
			next = sc.next();
			next = filter(next);
			if (!inTrainingSet(next)) {
				continue;
			}
			else if (wordHasAppeared.get(next) != null) {
				continue;
			}
			wordHasAppeared.put(next, 1);
			cur = cur + prob.L(next, category);
		}
				
		return cur;
	}
	
	/*
	 * Answers the question: is this word in the training set?
	 */
	private boolean inTrainingSet(String word) {
		for (Object cat : categories) {
			if (wordHash.get((String)cat).get(word) != null) {
				return true;
			}
		}
		return false;
	}
	
	// Filter for punctuation/excess spaces:
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
}
