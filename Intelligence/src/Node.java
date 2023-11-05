/**
 * This class is Node class which represents a member in family.
 * 
 * @author Mehmet Ali Özdemir
 * @since Date: 05.11.2023
 */
public class Node {
	private String name;
	private double gms;
	private int rank;
	Node leftChild;
	Node rightChild;
	
	Node(String name, double gms, int rank) {
		this.name = name;
		this.gms = gms;
		this.rank = rank;
		leftChild = null;
		rightChild = null;
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
