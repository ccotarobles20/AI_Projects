package mainPackage;

public class Parser {
	String[] parse(int sentenceLength, String sentence) {
		
		String end[] = new String[sentenceLength];
		String currentString = "";
		int currentInt = 0;
		
		sentence = sentence.toLowerCase();
		
		for (int i=0;i<sentence.length();i++) {
			if (sentence.charAt(i) == ' ') {
				end[currentInt] = currentString;
				currentString = "";
				currentInt++;
			}
			else {
				currentString = currentString+sentence.charAt(i);
			}
		}
		
		currentString = currentString.substring(0, currentString.length()-1); //Clip the excess newline character added to the end.
		end[currentInt] = currentString;
		return end;
	}
}