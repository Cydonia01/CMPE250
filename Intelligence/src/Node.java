/**
 * This class is Node class which represents a member in family.
 * 
 * @author Mehmet Ali Özdemir
 * @since Date: 04.11.2023
 */
public class Node {
	private String name;
	private double gms;
	private int rank;
	Node leftChild;
	Node rightChild;
	
	Node (String name, double gms) {
		this.name = name;
		this.gms = gms;
		this.leftChild = null;
		this.rightChild = null;
		this.rank = 1;
	}
	
	public double getGms() {
		return this.gms;
	}
	
	public void setGms(double gms) {
		this.gms = gms;
	}
	
	public int getRank() {
		return this.rank;
	}
	
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}