package mainPackage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class OutputWriter {
	
	public OutputWriter(String filename, Object data) {
		
		// Output Writer which is capable of translating either:
		/*
		 *  A : An array of strings into a textfile by writing the strings all in order.
		 *  B : A linked list of strings into a textfile by also writing the strings in order.
		 */
		
		File file = new File("output1.txt");
		
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			
			FileWriter writer = new FileWriter(file);
			BufferedWriter buffWriter = new BufferedWriter(writer);
		
			if (data instanceof String[]) {
				String[] dataArray = (String[]) data;
				for (int element=0; element<dataArray.length; element++) {
					buffWriter.write(dataArray[element]);
					buffWriter.newLine();
				}
				
				buffWriter.close();
			}
			else if (data instanceof LinkedList<?>) {
				LinkedList<String> dataList = (LinkedList<String>) data;
				for (String line : dataList) {
					buffWriter.write(line);
					buffWriter.newLine();
				}
				
				buffWriter.close();

			}
			else {
				System.err.println("Invalid data type was passed into OutputWriter");
			}
		}
		catch (IOException e) {
			System.err.println("Error with outputting to file!");
		}
	}
}
