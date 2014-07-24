package mainPackage;

import java.util.Hashtable;

public class Probabilities {
	
	// Hashtables for training data:
	Hashtable<String, Integer> categoryCounts;
	Hashtable<String, Hashtable<String, Integer>> wordCounts;
	
	double e; // Laplacian corrector
	
	public Probabilities(Hashtable<String, Integer> C, Hashtable<String, Hashtable<String, Integer>> W) {
		categoryCounts = C;
		wordCounts = W;
		e = 0.1;
	}
	
	// LOG Probabilities:
	public double L(String C) {
		return (-Math.log10(Prob(C)));
	}
	
	public double L(String W, String C) {
		double retVal = (-Math.log10(Prob(W, C)));
		return retVal;
	}
	
	// Non-Log Probabilities:
	private double Prob(String C) {
		
		double sol = (Freq(C)+e)/(1+categoryCounts.size()*e);
		return sol;
	}
	
	private double Prob(String W, String C) {

		double sol = (Freq(W, C) + e)/(1+2*e);
		return sol;
		
	}
	
	// Frequency counts
	private double Freq(String W, String C) {
		int occC = categoryCounts.get(C);
		int occW;
		
		try {
			occW = wordCounts.get(C).get(W);
		}
		catch(NullPointerException e) {
			occW = 0;
		}
		
		return ((double)occW/(double)occC);
	}
	
	private double Freq(String C) {
		int occ = categoryCounts.get(C);
		int total = 0;
		Object[] values = categoryCounts.values().toArray();
		for (Object o : values) {
			total += (Integer)o;
		}
				
		return ((double)occ/(double)total);
	}
}
