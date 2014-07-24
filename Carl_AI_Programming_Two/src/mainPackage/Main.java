package mainPackage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;

public class Main {
	
	public static void main(String[] args) {
				
		double[][] coordinates = null; //Array to store coordinates
		String line1 = ""; //Read line one of coordinates.txt into this string
		String line2 = ""; //Read second line into this one
		int numberOfPoints = 1;
		
		
		//SEPARATE TEXT FILE INTO TWO STRINGS (ASSUMING TWO LINES)
		try {
			File file = new File("Coordinates.txt");
			Scanner reader = new Scanner(file);
			
			reader.useDelimiter("\n");
			
			line1 = reader.next();
			line2 = reader.next();
			
			//Count the number of spaces in line 1, then that numberOfPoints+1 is the number of coordinates.
			for (int i=0;i<line1.length();i++) {
				if (line1.charAt(i) == ' ') {
					numberOfPoints++;
				}
			}
			
			coordinates = new double[numberOfPoints][2]; //Initialize coordinates to have size based on numberOfPoints
			
			coordinates = readLine(line1, coordinates, 0);
			coordinates = readLine(line2, coordinates, 1);
			
		}
		catch(IOException e) {
			e.printStackTrace();
		}
				
		double[][] shortest = travellingSalesman(coordinates);
		System.out.println("\n\nFinal Path:");
		double finalPath = printPath(shortest);
		System.out.println("\n\nLength = "+finalPath);
	}
	
	static double[][] travellingSalesman(double[][] coordinates) {

		double length = printPath(coordinates); //Print out the initial path, store length in double length
		System.out.println("\n\nLength = "+length); //Print out initial length
		
		//LOOP PORTION
		double maxChange = 0.0; //The maximum change from swapping two coordinates
		//The two coordinates you would swap for that maximum change
		int toSwap1 = -1;
		int toSwap2 = -1;
		
		while (maxChange==0) {
			for (int i=0;i<coordinates.length-1;i++) {
				for (int j=i+1;j<coordinates.length;j++) {

					//Calculate change in edge lengths from swapping each point i and j in coordinates
					double change = changeInEdge(coordinates, i, j);
					
					//If the potential swap is better (makes the path smaller than any other option) remember that swap.
					if (change > maxChange) {
						toSwap1 = i;
						toSwap2 = j;
						maxChange = change;
					}
				}
			}
			//Return only if no swap makes the path better
			if (maxChange == 0) {
				return coordinates;
			}
			//Otherwise swap the two points that decrease the path length the most and then loop again.
			else {
				double[] temp = coordinates[toSwap1];
				coordinates[toSwap1] = coordinates[toSwap2];
				coordinates[toSwap2] = temp;
				length -= maxChange;
				
				//UNCOMMENT THIS TO PRINT THE PATH AT EACH STAGE
				/*System.out.println("");
				printPath(coordinates);
				System.out.println("");
				*/
				
				System.out.println("\nSwapped "+toSwap1+" and "+toSwap2);
				System.out.println("\nLength = "+length);
				maxChange = 0.0; //reset the maxChange value to 0 after each loop
			}
		}
		
		return coordinates;
	}
	
	//Prints out the path by looping through the array.
	static double printPath(double[][] coordinates) {
		double length = 0;
		System.out.println("Path:");
		for (int i=0;i<coordinates.length-1;i++) {
			System.out.print(coordinates[i][0]+ " ");
			length += distance(coordinates[i], coordinates[i+1]);
		}
		System.out.println(coordinates[coordinates.length-1][0]+" ");
		for (int i=0;i<coordinates.length;i++) {
			System.out.print(coordinates[i][1]+" ");
		}
		length += distance(coordinates[coordinates.length-1], coordinates[0]);
		return length;
	}
	
	//Calculates the change of path length by swapping two edges.
	/*
	 * Needs to calculate change in only 2 edges if i and j are right next to each other.
	 * Otherwise, needs to calculate the change for 4 different edges.
	 * 
	 * Special Case refers to the case where i or j are at the end of the array, so i-1 or j+1 would result in NullPointerException
	 * Unique Case refers to the case where i and j are both at the end of the array (so j=coordinates[coordinates.length-1] and i=0)
	 */
	static double changeInEdge(double[][] coordinates, int i, int j) {
		double oldLength = 0;
		double newLength = 0;
		
		//UNIQUE CASE: (Where i and j are both at the ends of the array
		if (j==coordinates.length-1 && i==0) {
			oldLength += distance(coordinates[j-1], coordinates[j]);
			oldLength += distance(coordinates[i], coordinates[i+1]);
			newLength += distance(coordinates[j-1], coordinates[i]);
			newLength += distance(coordinates[j], coordinates[i+1]);
		}
		else {
			//Normal Cases/Also Special Cases
			if (i==0) {
				oldLength += distance(coordinates[coordinates.length-1], coordinates[i]);
				newLength += distance(coordinates[coordinates.length-1], coordinates[j]);
			}
			else {
				oldLength += distance(coordinates[i-1], coordinates[i]);
				newLength += distance(coordinates[i-1], coordinates[j]);
			}
			
			if (j==(coordinates.length-1)) {
				oldLength += distance(coordinates[j], coordinates[0]);
				newLength += distance(coordinates[i], coordinates[0]);
			}
			else {
				oldLength += distance(coordinates[j], coordinates[j+1]);
				newLength += distance(coordinates[i], coordinates[j+1]);
			}
			
			//If it's not a special case, ie i and j are not next to each other, we do more
			if (j-i != 1) {
				oldLength += distance(coordinates[i], coordinates[i+1]);
				oldLength += distance(coordinates[j-1], coordinates[j]);
				newLength += distance(coordinates[j], coordinates[i+1]);
				newLength += distance(coordinates[j-1], coordinates[i]);
			}
		}
		return oldLength-newLength;
	}
	
	//Calculate distance between two points
	static double distance(double[] a, double[] b) {
		double xLength = Math.abs(b[0]-a[0]);
		double yLength = Math.abs(b[1]-a[1]);
		
		return Math.sqrt(xLength*xLength + yLength*yLength);
	}
	
	static double[][] readLine(String line, double[][] coordinates, int j) {
				
		Scanner reader = new Scanner(line);
		
		int i=0; // will write to coordinate location coordinates[i][j]
		
		// READ EACH LINE AND STORE COORDINATES IN COORDINATES ARRAY
		while (reader.hasNext()) {
			if (reader.hasNextDouble()) {
				coordinates[i][j] = reader.nextDouble();
				i++;
			}
			else {
				System.out.println("Error on: "+reader.next());
				System.err.println("ERROR: Your input contained a non-number so we will ignore that!");
			}
		}
		
		return coordinates;
	}
}