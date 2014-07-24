package mainPackage;

public class CYKNode {
	
	String nonTerm;
	int startPhrase, endPhrase;

	String word;

	CYKNode left;
	CYKNode right;
	
	double prob;
	
	CYKNode(String nonTerm, int start, int end, String word, CYKNode left, CYKNode right, double prob) {
		this.nonTerm = nonTerm;
		endPhrase = end;
		startPhrase = start;
		this.word = word;
		this.left = left;
		this.right = right;
		this.prob = prob;
	}
	
	void printNode() {
		System.out.println(word+"("+startPhrase+"-"+endPhrase+") : "+nonTerm + " : "+prob);
	}

	void printTree(int indent) {
		System.out.println(""); //Pushes to the next line
		for (int i=0;i<indent;i++) {
			System.out.print(" ");
		}
		System.out.print(nonTerm);
		if (word != null) {
			System.out.print(" "+word);
		}
		else if (left != null && right != null) {
			left.printTree(indent+3);
			right.printTree(indent+3);
		}
	}
}