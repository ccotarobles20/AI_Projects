package mainPackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.LinkedList;

public class FrontEnd {
	
	int numberOfNodes;
	int numberOfTreasures;
	int numberOfSteps;
	
	LinkedList<String> nodeNames;
	LinkedList<String> treasureNames;
	
	LinkedList<String> propositions;
	
	public FrontEnd(String filename) {
		
		numberOfNodes = 0;
		numberOfTreasures = 0;
		numberOfSteps = 0;
		propositions = new LinkedList<String>();
		
		// FOR SCANNING THE LATTER HALF ONLY!!!
		int currentNode = 0;		
		String nextToAdd = "";
		String toAdd = "";
		
		// Keeps track of what is next and which treasures and tolls on each square
		// initialize all to 0, then if in a currentNode's list the treasure appears, get
		// that treasure's slot and set the value to 1.  We initialize these to 0 later because we
		// don't know the size needed for the arrays yet.
		
		int[][] treasureList = null;
		int[][] tollList = null;
		int[][] nextList = null;
				
		/* Set mode to
		 *   1: to add elements to treasures
		 *   2: to add elements to tolls
		 *   3: to add elements to next
		 */
		int mode = 0;
		
		int scanningStep = 0;  // Which step we are on: a. reading the nodes b. reading treasures c. number of steps d. the puzzle specification
				
		File file = new File(filename);
		
		try {
			Scanner scanner = new Scanner(file);
			
			String beingScanned = "";
			nodeNames = new LinkedList<String>();
			treasureNames = new LinkedList<String>();
			
			while (scanner.hasNext()) {
				
				toAdd = beingScanned;
				
				beingScanned = scanner.next();
				
				nextToAdd = beingScanned;

				if (scanningStep == 0) {
					numberOfNodes++;
					nodeNames.add(beingScanned);
				
					if (beingScanned.equals("GOAL")) {
						scanningStep++;
					}
				}
				else if (scanningStep == 1) {
					numberOfTreasures++;
					treasureNames.add(beingScanned);
					
					if (scanner.hasNextInt()) {
						scanningStep++;
					}
				}
				else if (scanningStep == 2) {
					numberOfSteps = Integer.parseInt(beingScanned);
					
					treasureList = new int[numberOfNodes][numberOfTreasures];
					tollList = new int[numberOfNodes][numberOfTreasures];
					nextList = new int[numberOfNodes][numberOfNodes];
					
					//Initialize all lists to 0.
					for (int i=0; i<numberOfNodes; i++) {
						for (int j=0; j<numberOfTreasures; j++) {
							treasureList[i][j] = 0;
							tollList[i][j] = 0;
						}
						
						for (int k=0; k<numberOfNodes; k++) {
							nextList[i][k] = 0;
						}
			 		}
					
					scanningStep++;
				}
				// Now do puzzle specification:
				else if (scanningStep == 3) {															
					if (nextToAdd.equals("TREASURES")) {
						currentNode = encodeNode(toAdd);
						mode = 0;
					}
					else if (toAdd.equals("TREASURES")) {
						mode = 1;
					}
					else if (toAdd.equals("TOLLS")) {
						mode = 2;
					}
					else if (toAdd.equals("NEXT")) {
						mode = 3;
					}
					else {
						if (mode == 1) {
							treasureList[currentNode][encodeTreasure(toAdd)] = 1;
						}
						else if (mode == 2) {
							tollList[currentNode][encodeTreasure(toAdd)] = 1;
						}
						else if (mode == 3) {
							nextList[currentNode][encodeNode(toAdd)] = 1;
						}
					}					
				}
			}
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		
		nextList[currentNode][encodeNode(nextToAdd)] = 1;
		
		// Print out how many nodes, treasures, steps
		System.out.println("Number of Nodes: "+numberOfNodes);
		System.out.println("Number of Treasures: "+numberOfTreasures);
		System.out.println("Number of Steps: "+numberOfSteps);
				
		numberOfSteps++; //Add one because I screwed up before with this.
		
		// PROPOSITION 1:
		for (int time=0; time<numberOfSteps; time++) {
			for (int node=0; node<numberOfNodes-1; node++) {
				for (int node2=node+1; node2<numberOfNodes; node2++) {
					propositions.add(Not(At(node, time)) + " " + Not(At(node2, time)));
				}
			}
		}
				
		// PROPOSITION 3: THE PLAYER MUST MOVE ON EDGES
		for (int node=0; node<numberOfNodes; node++) {
			for (int time=0; time<numberOfSteps-1;time++) {
				String nextProp = Not(At(node, time)) + "";
				for (int node2=0; node2<numberOfNodes; node2++) {
					if (nextList[node][node2] == 1) {
						nextProp = nextProp + " " + (At(node2, time+1));
					}
				}
				propositions.add(nextProp);
			}
		}
		
		// PROPOSITION 4:
		for (int time=1; time<numberOfSteps; time++) {
			for (int node=0; node<numberOfNodes; node++) {
				for (int toll=0; toll<numberOfTreasures; toll++) {
					if (tollList[node][toll] == 1) {
						propositions.add(Not(At(node, time)) + " " + Has(toll, time-1));
					}
				}
			}
		}
		
		
		// PROPOSITION 2:
		for (int time=0; time<numberOfSteps; time++) {
			for (int treasure=0; treasure<numberOfTreasures; treasure++) {
				propositions.add(Not(Has(treasure, time)) + " " + Not(Available(treasure, time)));
			}
		}
		
		// PROPOSITION 5:
		for (int time=0; time<numberOfSteps-1; time++) {
			for (int treasure=0; treasure<numberOfTreasures; treasure++) {
				for (int node=0; node<numberOfNodes; node++) {
					if (treasureList[node][treasure] == 1) {
						propositions.add(Not(Available(treasure, time)) + " " + Not(At(node, time+1)) + " " + Has(treasure, time+1));
					}
				}
			}
		}
		
		
		// PROPOSITION 6:
		for (int time=0; time<numberOfSteps; time++) {
			for (int toll=0; toll<numberOfTreasures; toll++) {
				for (int node=0; node<numberOfNodes;node++) {
					if (tollList[node][toll] == 1) {
						propositions.add(Not(At(node, time)) + " " + Not(Has(toll, time)));
					}
				}
			}
		}
		
		// PROPOSITION 7:
		for (int time=0; time<numberOfSteps-1; time++) {
			for (int treasure=0; treasure<numberOfTreasures; treasure++) {
				for (int node=0; node<numberOfNodes; node++) {
					if (treasureList[node][treasure] == 0) {
						propositions.add(Not(Available(treasure, time)) + " " + Not(At(node, time+1)) + " " + Available(treasure, time+1));
					}
				}
			}
		}
		
		// PROPOSITION 8:
		for (int time=0; time<numberOfSteps-1; time++) {
			for (int treasure=0; treasure<numberOfTreasures; treasure++) {
				propositions.add(Available(treasure, time) + " " + Not(Available(treasure, time+1)));
			}
		}
		
		// PROPOSITION 9:
		for (int time=0; time<numberOfSteps-1; time++) {
			for (int treasure=0; treasure<numberOfTreasures; treasure++) {
				propositions.add(Available(treasure, time) + " " + Has(treasure, time) + " " + Not(Has(treasure, time+1)));
			}
		}		
		// PROPOSITION 10:
		for (int time=0; time<numberOfSteps-1; time++) {
			for (int treasure=0; treasure<numberOfTreasures; treasure++) {
				for (int node=0; node<numberOfNodes; node++) {
					if (tollList[node][treasure] == 0) {
						propositions.add(Not(Has(treasure, time)) + " " + Not(At(node, time+1)) + " " + Has(treasure, time+1));
					}
				}
			}
		}
				
		// PROPOSITION 11:
		propositions.add(""+At(0, 0));  // Must be at start at time 0
		
		// PROPOSITION 12:
		for (int treasure=0; treasure<numberOfTreasures; treasure++) {
			propositions.add(Available(treasure, 0)+"");
		}
		
		//PROPOSITION 13:
		propositions.add(""+At(numberOfNodes-1, numberOfSteps-1)); //Must be at goal at time k
		
		// BACK MATTER:
		propositions.add("0");
		propositions.add(numberOfSteps+"");
		propositions.add(numberOfNodes+"");
		propositions.add(numberOfTreasures+"");
		
		String nodeNamesString = "";
		
		for (String name : nodeNames) {
			nodeNamesString = nodeNamesString + name + " ";
		}
		
		propositions.add(nodeNamesString);
		
		return;
	}
	
	// The encoding for a node in the nextList
	private int encodeNode(String node) {
				
		for (int i=0 ; i<nodeNames.size(); i++) {
			if (nodeNames.get(i).equals(node)) {
				return i;
			}
		}
		
		System.err.println("Node not in list");
		return 0;		
	}
	
	// The encoding for a treasure or toll in treasureList or tollList
	private int encodeTreasure(String treasure) {
		
		for (int i=0; i<treasureNames.size(); i++) {
			if (treasureNames.get(i).equals(treasure)) {
				return i;
			}
		}
		
		System.err.println("Treasure not found in list");
		return 0;		
	}
	
	public void writeOutput() {
		
		OutputWriter writer = new OutputWriter("output1.txt", propositions);
	}
	
	private int At(int node, int time) {
		int encodedVal;
		
		encodedVal = (numberOfNodes*time) + node;
		
		return encodedVal+1;
	}
	
	private int Not(int val) {
		return -val;
	}
	
	private int Has(int treasure, int time) {
		int encodedVal;
		
		encodedVal = (numberOfNodes*numberOfSteps) + (numberOfTreasures*time) + treasure;
		
		return encodedVal+1;
	}
	
	private int Available(int treasure, int time) {
		int encodedVal;
		
		encodedVal = (numberOfNodes*numberOfSteps) + (numberOfTreasures*numberOfSteps) + (numberOfTreasures*time) + treasure;
		
		return encodedVal+1;
	}
}