package mainPackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Hashtable;

public class Main {
		
	public static void main(String[] args) {
		
		// GET N, THE VALUE WHERE WE'LL SPLIT TRAINING AND TEST SETS:
		System.out.println("Specify an n value below:");
		Scanner readInput = new Scanner(System.in);
		int n = readInput.nextInt();
		
		System.out.println("Thanks!");
		System.out.println("\nBringing Data into Memory...");
		
		// NOW READ IN THE CORPUS:
		FileToLineArray fla = new FileToLineArray();
		String[] data = fla.convert("smallCorpus.txt");
		
		String[] trainData = fla.splitToTrain(data, n);
		String[] testData = fla.splitToTest(data, n);
		
		//Print all the training and test data seperated accordingly.
		/*
		System.out.println("\nTraining:");
		for (String s : trainData) {
			System.out.println(s);
		}
		
		System.out.println("\nTesting:");
		for (String s : testData) {
			System.out.println(s);
		}*/
		
		System.out.println("\nLearning...");
				
		// NOW CREATE THE HASHTABLES FOR THE COUNTING:
		Learner l = new Learner();
		Hashtable<String, Integer> catHash = l.countCategories(trainData);
		Hashtable<String, Hashtable<String, Integer>> wordHash = l.countWords(trainData);
		
		//Print the Hashtables
		/*
		System.out.println(catHash.toString());
		System.out.println(wordHash.toString());*/
		
		System.out.println("\nCalculating size of test data...");
		int testSize = fla.getBioNumbers(testData);
		
		System.out.println("Test data has "+(testSize-1)+" biographies in it.  Classifying...\n");
		Classifier classifier = new Classifier(testSize, catHash, wordHash);
		classifier.classifyData(testData);
		
		System.out.println("\nDONE!");
		System.out.println("Check output.txt for results");
	}
}