/** 
 * This project implements a code that does various operations on AVL trees.
 * 
 * @author Mehmet Ali Özdemir
 * @since Date: 04.11.2023
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		String boss[];
		FamilyTree family;
		try {
			File inputFile = new File(args[0]);
			FileWriter writer = new FileWriter(args[1]);
			Scanner reader = new Scanner(inputFile);
			
			boss = reader.nextLine().split(" ");
			family = new FamilyTree(writer);
			family.insert(boss[0], Double.parseDouble(boss[1]));
			
			while (reader.hasNextLine()) {
				String[] data = reader.nextLine().split(" ");
				functions(data, family, writer);
			}
			reader.close();
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * This function takes data determines which operation to do.
	 * 
	 * @param data array storing member name and gms
	 * @param family object of FamilyTree class.
	 * @throws IOException 
	 */
	public static void functions(String[] data, FamilyTree family, FileWriter writer) throws IOException {
		if (data[0].equals("INTEL_DIVIDE")) {
			int division = family.divide();
			writer.write("Division Analysis Result: " + division + "\n");
		}
		else if (data[0].equals("INTEL_TARGET")) {
			String firstMemberName = data[1];
			String secondMemberName = data[3];
			
			double firstMemberGms = Double.parseDouble(data[2]);
			double secondMemberGms = Double.parseDouble(data[4]);
			
			family.intelTarget(firstMemberName, firstMemberGms, secondMemberName, secondMemberGms);
		}
		else {
			String name = data[1];
			double gms = Double.parseDouble(data[2]);
			
			if (data[0].equals("MEMBER_IN"))
				family.insert(name, gms);
			
			if (data[0].equals("MEMBER_OUT"))
				family.delete(name, gms);
			
			if (data[0].equals("INTEL_RANK"))
				family.monitorRanks(gms);
		}
	}
}