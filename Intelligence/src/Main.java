/** 
 * This project implements a code that does various operations on AVL trees.
 * 
 * @author Mehmet Ali Özdemir
 * @since Date: 05.11.2023
 * 
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		String boss[];
		FamilyTree family;
		
		try {
			File file = new File("cases/inputs/small3.txt");
			Scanner reader = new Scanner(file);
			
			boss = reader.nextLine().split(" ");
			family = new FamilyTree(boss);
			
			while (reader.hasNextLine()) {
				String[] data = reader.nextLine().split(" ");
				functions(data, family);
			}
			
			reader.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * This function takes data determines which operation to do.
	 * 
	 * @param data array storing member name and gms
	 * @param family object of FamilyTree class.
	 */
	public static void functions(String[] data, FamilyTree family) {
		if (data[0].equals("INTEL_DIVIDE")) {
			int division = family.divide();
			System.out.println("Division Analysis Result: " + division);
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