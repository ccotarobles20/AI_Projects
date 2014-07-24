package mainPackage;

import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		
		String sentence = "";
		int sentenceLength;
		String parsed_sentence[] = null;
		Ruler ruler = new Ruler();
		boolean quit = false;
		CYKNode best[][][];
		CYKNode secondBest[][][];
		
		int grammar = 9; //Number of different types of grammar (ie noun, verb VerbAndObject, VPWithPPList, etc.  See legend halfway down the page
		
		while (!quit) {
			
			sentenceLength = 1;
			
			//Scanner for input
			Scanner read = new Scanner(System.in).useDelimiter("\n");
			System.out.println("\nType a new sentence (type 'quit' to exit): ");

			sentence = read.next();
			//--------------------
					
			for (int i=0;i<sentence.length();i++) {
				if (sentence.charAt(i) == ' ') {
					sentenceLength++;
				}
			}

			Parser parser = new Parser();
			parsed_sentence = parser.parse(sentenceLength, sentence);
			
			if (parsed_sentence[0].equals("quit")) {
				quit = true;
				break;
			}
		
		
			best = new CYKNode[grammar][sentenceLength][sentenceLength];
			secondBest = new CYKNode[grammar][sentenceLength][sentenceLength];
			
			//Legend for best/secondBest chart:
				// Values for first dimension of the array
				/*
				 * 0 : S
				 * 1 : NP
				 * 2 : PP
				 * 3 : PPList
				 * 4 : VerbAndObject
				 * 5 : VPWithPPList
				 * 6 : Noun
				 * 7 : Prep
				 * 8 : Verb
				 */
			// END OF LEGEND
				
			//  CYK-PARSE ALGORITHMS (PROVIDED BY PROFESSOR ERNEST DAVIS):
		
			for (int i=0;i<sentenceLength;i++) {
				String word = parsed_sentence[i];
				for (int j=0;j<grammar;j++) {
					String pos = ruler.key(j);
					best[j][i][i] = new CYKNode(pos, i, i, word, null, null, ruler.checkRule(pos, word));
					secondBest[j][i][i] = new CYKNode(pos, i, i, word, null, null, ruler.checkRule(pos, word));
					//chart[j][i][i].printNode();
				}
			}
			
				
		
			for (int length=2;length<sentenceLength+1;length++) {		//	length == length of phrase
				for (int i=0;i<sentenceLength-length+1;i++) {		// 	i == start of phrase
					int j=i+length-1;								//	j == end of phrase
					for (int nonTerm=0;nonTerm<grammar;nonTerm++) {
						String ntString = ruler.key(nonTerm);
						best[nonTerm][i][j] = new CYKNode(ntString, i, j, null, null, null, 0.0);
						secondBest[nonTerm][i][j] = new CYKNode(ntString, i, j, null, null, null, 0.0);
						for (int k=i;k<j;k++) {
							for (int child1=1;child1<grammar;child1++) { //child1 = # of nonTerms
								String sChild1 = ruler.key(child1);
								for (int child2=1;child2<grammar;child2++) { //child2 = # of nonTerms
									String sChild2 = ruler.key(child2);
									double newProb = ruler.checkRule(ntString, sChild1, sChild2) * best[child1][i][k].prob * best[child2][k+1][j].prob;
									if (newProb > best[nonTerm][i][j].prob) {
										secondBest[nonTerm][i][j].left = best[nonTerm][i][j].left;
										secondBest[nonTerm][i][j].right = best[nonTerm][i][j].right;
										secondBest[nonTerm][i][j].prob = best[nonTerm][i][j].prob;
										
										best[nonTerm][i][j].left = best[child1][i][k];
										best[nonTerm][i][j].right = best[child2][k+1][j];
										best[nonTerm][i][j].prob = newProb;
									}
									else if (newProb > secondBest[nonTerm][i][j].prob) {
										secondBest[nonTerm][i][j].left = best[child1][i][k];
										secondBest[nonTerm][i][j].right = best[child2][k+1][j];
										secondBest[nonTerm][i][j].prob = newProb;
									}
								}
							}
						}
					}
				}
			}
			
			if (best[0][0][sentenceLength-1].prob == 0.0) {
				System.err.println("This sentence could not be parsed");
			}
			else {
				best[0][0][sentenceLength-1].printTree(0);
				System.out.println("\nBest probability: "+best[0][0][sentenceLength-1].prob);
				if (secondBest[0][0][sentenceLength-1].prob > 0) {
					secondBest[0][0][sentenceLength-1].printTree(0);
					System.out.println("\nSecond Best probability: " + secondBest[0][0][sentenceLength-1].prob);
				}
				else {
					System.out.println("No other parses found");
				}
			}
		}
	}
}