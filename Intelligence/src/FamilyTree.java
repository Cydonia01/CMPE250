/**
 * This class contains methods and data fields of family tree.
 * 
 * @author Mehmet Ali Özdemir
 * @since Date: 04.11.2023
 */
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;

public class FamilyTree {
	private Node root;
	private FileWriter writer;
	
	FamilyTree(FileWriter writer) {
		root = null;
		this.writer = writer;
	}
	
	
	/**
	 * This method inserts a member to the family tree recursively. It also does rearrangements after inserting the member.
	 * 
	 * @param name name of the member
	 * @param gms gms of the member
	 * @return a node
	 * @throws IOException 
	 */
	public void insert(String name, double gms) throws IOException { root = insert(root, name, gms); }
	
	private Node insert(Node target, String name, double gms) throws IOException {
		if (target == null) {
			return new Node(name, gms);
		}
		
		if (gms < target.getGms()) {
			logIn(target.getName(), name);
			target.leftChild = insert(target.leftChild, name, gms);
		}
		
		if (gms > target.getGms()) {
			logIn(target.getName(), name);
			target.rightChild = insert(target.rightChild, name, gms);
		}
		
		target.setRank(nodeRank(target));
		
		int balance = balanceFactor(target);
		
		if (balance == 2 && balanceFactor(target.leftChild) == 1)
			return LLRotation(target);
		if (balance == 2 && balanceFactor(target.leftChild) == -1)
			return LRRotation(target);
		if (balance == -2 && balanceFactor(target.rightChild) == -1)
			return RRRotation(target);
		if (balance == -2 && balanceFactor(target.rightChild) == 1)
			return RLRotation(target);
		
		return target;
	}
	
	
	/**
	 * This method removes a member from the family tree. It also does rearrangements after removal.
	 * 
	 * @param name name of the member
	 * @param gms gms of the member
	 * @return a node
	 * @throws IOException 
	 */	
	public void delete(String name, double gms) throws IOException { root = delete(root, name, gms, false); }
	
	private Node delete(Node target, String name, double gms, boolean printed) throws IOException {
		if (target == null)
			return null;
		
		if (gms < target.getGms())
			target.leftChild = delete(target.leftChild, name, gms, printed);
		
		else if (gms > target.getGms())
			target.rightChild = delete(target.rightChild, name, gms, printed);
		
		else {
			if (target.rightChild == null && target.leftChild == null) {
				if (!printed) {
					logOut(target.getName(), "nobody");
					printed = true;
				}
				return null;
			}
			else if (target.rightChild == null) {
				if (!printed) {
					logOut(target.getName(), target.leftChild.getName());
					printed = true;
				}
				
				target.setName(target.leftChild.getName());
				target.setGms(target.leftChild.getGms());
				target.leftChild = delete(target.leftChild, name, target.leftChild.getGms(), printed);
			}
			else if (target.leftChild == null) {
				if (!printed) {
					logOut(target.getName(), target.rightChild.getName());
					printed = true;
				}
				
				target.setName(target.rightChild.getName());
				target.setGms(target.rightChild.getGms());
				target.rightChild = delete(target.rightChild, name, target.rightChild.getGms(), printed);
			}
			else {
				Node chosen = findLeftMost(target.rightChild);
				if (!printed) {
					logOut(target.getName(), chosen.getName());
					printed = true;
				}
				
				target.setName(chosen.getName());
				target.setGms(chosen.getGms());
				target.rightChild = delete(target.rightChild, name, chosen.getGms(), printed);
			}
		}
		
		target.setRank(nodeRank(target));
		
		int balance = balanceFactor(target);
		
		if (balance == 2 && balanceFactor(target.leftChild) >= 0)
			return LLRotation(target);
		if (balance == 2 && balanceFactor(target.leftChild) < 0)
			return LRRotation(target);
		if (balance == -2 && balanceFactor(target.rightChild) <= 0)
			return RRRotation(target);
		if (balance == -2 && balanceFactor(target.rightChild) > 0)
			return RLRotation(target);
		
		return target;
	}
	
	
	/**
	 * This method finds the leftmost child of an element. In other words, the member who has minimum gms.
	 * It is used only to find the leftmost child of the right child of a node.
	 * 
	 * @param target starting node
	 * @return wanted node
	 */
	private Node findLeftMost(Node currentNode) {
		while (currentNode.leftChild != null)
			currentNode = currentNode.leftChild;
		return currentNode;
	}
	
	
	/**
	 * This method does the single left rotation to rearrange the tree.
	 * 
	 * @param target imbalancing node.
	 * @return node
	 */
	private Node LLRotation(Node target) {
		Node targetLeft = target.leftChild;
		Node targetLeftRight = targetLeft.rightChild;
		
		targetLeft.rightChild = target;
		target.leftChild = targetLeftRight;
		
		target.setRank(nodeRank(target));
		targetLeft.setRank(nodeRank(targetLeft));
		
		if (root == target)
			root = targetLeft;
			
		return targetLeft;
	}
	
	
	/**
	 * This method does the left right double rotation to rearrange the tree.
	 * 
	 * @param target imbalancing node.
	 * @return node
	 */
	private Node LRRotation(Node target) {
		Node targetLeft = target.leftChild;
		Node targetLeftRight = targetLeft.rightChild;
		
		targetLeft.rightChild = targetLeftRight.leftChild;
		target.leftChild = targetLeftRight.rightChild;
		
		targetLeftRight.leftChild = targetLeft;
		targetLeftRight.rightChild = target;
		
		targetLeft.setRank(nodeRank(targetLeft));
		target.setRank(nodeRank(target));
		targetLeftRight.setRank(nodeRank(targetLeftRight));
		
		if (root == target)
			root = targetLeftRight;
			
		return targetLeftRight;
	}
	
	
	/**
	 * This method does the single right rotation to rearrange the tree.
	 * 
	 * @param target imbalancing node.
	 * @return node
	 */
	private Node RRRotation(Node target) {
		Node targetRight = target.rightChild;
		Node targetRightLeft = targetRight.leftChild;
		
		targetRight.leftChild = target;
		target.rightChild = targetRightLeft;
		
		target.setRank(nodeRank(target));
		targetRight.setRank(nodeRank(targetRight));
		
		if (root == target)
			root = targetRight;
			
		return targetRight;
	}
	
	
	/**
	 * This method does the right left double rotation to rearrange the tree.
	 * 
	 * @param target imbalancing node.
	 * @return node
	 */
	private Node RLRotation(Node target) {
		Node targetRight = target.rightChild;
		Node targetRightLeft = targetRight.leftChild;
		
		targetRight.leftChild = targetRightLeft.rightChild;
		target.rightChild = targetRightLeft.leftChild;
		
		targetRightLeft.leftChild = target;
		targetRightLeft.rightChild = targetRight;
		
		targetRight.setRank(nodeRank(targetRight));
		target.setRank(nodeRank(target));
		targetRightLeft.setRank(nodeRank(targetRightLeft));
		
		if (root == target)
			root = targetRightLeft;
			
		return targetRightLeft;
	}
	
	
	/**
	 * This method finds the rank (height) of a node in the tree.
	 * 
	 * @param target root node
	 * @return rank (height)
	 */
	private int nodeRank(Node target) {
		int rankLeft, rankRight;
		
		if (target == null)
			return 0;
		
		rankLeft = (target != null && target.leftChild != null)?target.leftChild.getRank():0;
		rankRight = (target != null && target.rightChild != null)?target.rightChild.getRank():0;
		
		return rankLeft > rankRight ? rankLeft + 1:rankRight + 1;
	}
	
	
	/**
	 * This method calculates the balance factor.
	 * 
	 * @param target any node
	 * @return balance factor
	 */
	private int balanceFactor(Node target) {
		int rankLeft, rankRight;

		rankLeft = (target != null && target.leftChild != null)?target.leftChild.getRank():0;
		rankRight = (target != null && target.rightChild != null)?target.rightChild.getRank():0;
		
		return rankLeft - rankRight;
	}
	
	
	/**
	 * This method finds the intel about target members. After using findPath method it traverses reversely
	 * on these arrays to find the lowest ranking member superior of both members.
	 * 
	 * @param firstMemberName
	 * @param firstMemberGms
	 * @param secondMemberName
	 * @param secondMemberGms
	 * @throws IOException 
	 */
	public void intelTarget(String firstMemberName, double firstMemberGms, String secondMemberName, double secondMemberGms) throws IOException {
		ArrayList<Node> firstMemberPath = findPath(firstMemberName, firstMemberGms);
		ArrayList<Node> secondMemberPath = findPath(secondMemberName, secondMemberGms);

		int index;

		if (firstMemberPath.size() <= secondMemberPath.size())
			index = firstMemberPath.size() - 1;
		else
			index = secondMemberPath.size() - 1;
		
		for (int i = index; i >= 0; i--) {
			if (firstMemberPath.get(i).getName().equals(secondMemberPath.get(i).getName())) {
				logIntel(firstMemberPath.get(i));
				break;
			}
		}
	}
	
	
	/**
	 * This method finds the all ancestors of a member.
	 * 
	 * @param name name of the member
	 * @param gms gms of the member
	 * @return array including ancestors of the member and member itself.
	 */
	private ArrayList<Node> findPath(String name, double gms) {
		Node currentNode = root;
		ArrayList<Node> path = new ArrayList<>();
		
		while(true) {
			path.add(currentNode);
			
			if (gms < currentNode.getGms())
				currentNode = currentNode.leftChild;
			
			else if (gms > currentNode.getGms())
				currentNode = currentNode.rightChild;
			
			else if (gms == currentNode.getGms()) {
				break;
			}
		}
		
		if (path.isEmpty())
			path.add(currentNode);
		
		return path;
	}
	
	
	/**
	 * These methods divide the family. It finds number of members counted when the root is counted and when it is not counted.
	 * It does this operation to the roots of subtrees, too. After storing these numbers, it returns the maximum of these numbers.
	 * The first element of result array stores the maximum number of members when root is not counted, the other stores the maximum
	 * number of members when root is counted.
	 * 
	 * @return maximum number of members divided.
	 */
	public int divide() { return divide(root); }
	
	public int divide(Node root) {
		int[] result = subTree(root);
		return Math.max(result[0], result[1]);
	}
	
	private int[] subTree(Node root) {
		if (root == null) {
			return new int[2];
		}
		
		int[] leftSub = subTree(root.leftChild);
	    int[] rightSub = subTree(root.rightChild);
	    int[] result = new int[2];

	    result[0] = Math.max(leftSub[0], leftSub[1]) + Math.max(rightSub[0], rightSub[1]);
	    result[1] = leftSub[0] + rightSub[0] + 1;
	    
	    return result;
	}
	
	
	/**
	 * This method prints the name of the members who has the same rank with the target member 
	 * by breadth first search.
	 * 
	 * @param gms gms of the member
	 * @throws IOException 
	 */
	public void monitorRanks(double gms) throws IOException {
		Queue<Node> membersQueue = new LinkedList<>();
		ArrayList<String> result = new ArrayList<>();

		int queueLength;
		boolean found = false;
		
		writer.write("Rank Analysis Result:");
		
		membersQueue.offer(root);
		
		while (!membersQueue.isEmpty()) {
			queueLength = membersQueue.size();
			result.clear();
			
			for (int i = 0; i < queueLength; i++) {
				Node target = membersQueue.poll();
				
				if (target != null) {
					if (target.getGms() == gms)
						found = true;
					
					result.add(String.format(Locale.US, " %s %.3f", target.getName(), target.getGms()));
					
					if (target.leftChild != null)
						membersQueue.add(target.leftChild);
					
					if (target.rightChild != null)
						membersQueue.add(target.rightChild);							
				}
			}
			
			if (found)
				break;
		}
		
		for (String str: result)
			writer.write(str);
		writer.write("\n");
	}
	
	
	/**
	 * This method writes who welcomes a member.
	 * 
	 * @param superiorName
	 * @param inferiorName
	 * @throws IOException 
	 */
	private void logIn(String superiorName, String inferiorName) throws IOException {
		writer.write(superiorName + " welcomed " + inferiorName + "\n");
	}
	
	
	/**
	 * This method writes who left and who replaced it.
	 * 
	 * @param goneMemberName
	 * @param replacingMemberName
	 * @throws IOException 
	 */
	private void logOut(String goneMemberName, String replacingMemberName) throws IOException {
		writer.write(goneMemberName + " left the family, replaced by " + replacingMemberName + "\n");
	}
	
	
	/**
	 * This method writes the superior of the targeted members.
	 * 
	 * @param superior
	 * @throws IOException 
	 */
	private void logIntel(Node superior) throws IOException {
		writer.write(String.format(Locale.US, "Target Analysis Result: %s %.3f\n", superior.getName(), superior.getGms()));
	}
}