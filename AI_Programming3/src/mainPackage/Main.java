package mainPackage;

public class Main {
	public static void main(String[] args) {
		
		System.out.println("Converting Front-End to CNF...");
		FrontEnd fd = new FrontEnd("input.txt");
		System.out.println("Done converting.");
		System.out.println("Writing to output file...");
		fd.writeOutput();
		System.out.println("Done writing to output file.  Open filename output1.txt for results\n\n");
		
		System.out.println("Preparing to run Davis-Putnam...");
		DavisPutnam dp = new DavisPutnam("output1.txt");
		System.out.println("Running Davis-Putnam...");
		int[] results = dp.runDavisPutnam();
		System.out.println("Open filename output2.txt for results\n\n");
		
		System.out.println("Preparing Back End...");
		BackEnd bd = new BackEnd("output2.txt");
		System.out.println("Running Back End...\n");
		bd.runBackEnd();
		System.out.println("\nCOMPLETE!");
	}
}