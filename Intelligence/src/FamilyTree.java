/**
 * This class contains methods and data fields of family tree.
 * 
 * @author Mehmet Ali Özdemir
 * @since Date: 05.11.2023
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;

public class FamilyTree {
	private Node root;
	private HashMap<Integer, String> rankMap;
	
	FamilyTree(String[] boss) {
		root = new Node(null, 0, 0);
		root.setName(boss[0]);
		root.setGms(Double.parseDouble(boss[1]));
		this.rankMap = new HashMap<Integer,String>();
	}
	
	
	/**
	 * This method inserts a member to family tree recursively. It also does rearrangements after inserting the member.
	 * 
	 * @param name name of the member
	 * @param gms gms of the member
	 * @return a node
	 */
	public Node insert(String name, double gms) { return insert(root, name, gms); }
	
	public Node insert(Node target, String name, double gms) {
		if (target == null) {
			Node temp = new Node(name, gms, 1);
			return temp;
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
		
		return rearrange(target);
	}
	
	
	/**
	 * This method removes a member from the family tree. It also does rearrangements after removal.
	 * 
	 * @param name name of the member
	 * @param gms gms of the member
	 * @return a node
	 */	
	public Node delete(String name, double gms) { return delete(root, name, gms); }
	
	public Node delete(Node target, String name, double gms) {
		Node chosen;
		
		if (target == null)
			return null;
		
		if (target.leftChild == null && target.rightChild == null) {
			if (target == root)
				root = null;
			return null;
		}
		
		if (gms < target.getGms())
			target.leftChild = delete(target.leftChild, name, gms);
		
		else if (gms > target.getGms())
			target.rightChild = delete(target.rightChild, name, gms);
		
		else {
			if (target.leftChild != null && target.rightChild != null) {
				chosen = findLeftMost(target.rightChild);
				logOut(target.getName(), chosen.getName());
				target.setName(chosen.getName());
				target.setGms(chosen.getGms());
				target.rightChild = delete(target.rightChild, name, chosen.getGms());
			}
			else if (target.rightChild != null) {
				logOut(target.getName(), target.rightChild.getName());
				target.setName(target.rightChild.getName());
				target.setGms(target.rightChild.getGms());
				target.rightChild = delete(target.rightChild, name, target.rightChild.getGms());
			}
			else if (target.leftChild != null) {
				logOut(target.getName(), target.leftChild.getName());
				target.setName(target.leftChild.getName());
				target.setGms(target.leftChild.getGms());
				target.leftChild = delete(target.leftChild, name, target.leftChild.getGms());
			}
			else {
				logOut(target.getName(), "nobody");
			}
		}
		
		target.setRank(nodeRank(target));
		
		return rearrange(target);
	}
	
	
	/**
	 * This method finds the leftmost child of an element. In other words, the member who has minimum gms.
	 * It is used only to find the leftmost child of the right child of a node.
	 * 
	 * @param target starting node
	 * @return wanted node
	 */
	private Node findLeftMost(Node target) {
		while (target != null && target.leftChild != null)
			target = target.leftChild;
		return target;
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
	 * This method finds the height of a node in the tree.
	 * 
	 * @param target root node
	 * @return height
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
	 * This method rearranges the tree according to balance factor.
	 * 
	 * @param target any node
	 * @return a node
	 */
	private Node rearrange(Node target) {
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
	 * This method finds the intel about target members. After using findPath method it traverses reversely
	 * on these arrays to find the lowest ranking member superior of both members.
	 * 
	 * @param firstMemberName
	 * @param firstMemberGms
	 * @param secondMemberName
	 * @param secondMemberGms
	 */
	public void intelTarget(String firstMemberName, double firstMemberGms, String secondMemberName, double secondMemberGms) {
		ArrayList<Node> firstMemberPath = findPath(firstMemberName, firstMemberGms);
		ArrayList<Node> secondMemberPath = findPath(secondMemberName, secondMemberGms);
		
		
		int index;
		int a = firstMemberPath.size();
		int b = secondMemberPath.size();
		if (a <= b)
			index = a - 1;
		else
			index = b - 1;
		
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
	 * @return array including ancestors of the member.
	 */
	private ArrayList<Node> findPath(String name, double gms) {
		Node target = root;
		ArrayList<Node> path = new ArrayList<>();
		
		while(target.getGms() != gms) {
			path.add(target);
			
			if (gms < target.getGms())
				target = target.leftChild;
			
			if (gms > target.getGms())
				target = target.rightChild;
		}
		
		if (path.isEmpty())
			path.add(target);
		
		return path;
	}
	
	
	/**
	 * This method divides the family.
	 * @return
	 */
	public int divide() { return divide(root); }
	
	public int divide(Node root) {
		int[] result = partition(root);
		return Math.max(result[0], result[1]);
	}
	
	private int[] partition(Node root) {
		if (root == null) {
			return new int[2];
		}
		
		int[] left = partition(root.leftChild);
	    int[] right = partition(root.rightChild);
	    int[] result = new int[2];

	    result[0] = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
	    result[1] = left[0] + right[0] + 1;
	    
	    return result;
	}
	
	
	/**
	 * This method finds the rank of a member.
	 * 
	 * @param gms gms of the member
	 * @return rank of the member
	 */
	private int findRank(double gms) {
		Node target = root;
		
		while (target != null) {
			if (gms < target.getGms())
				target = target.leftChild;
			
			else if (gms > target.getGms())
				target = target.rightChild;
			
			else
				return target.getRank();
		}
		return -1;
	}
	
	
	/**
	 * This method prints the name of the members who has the same rank with the target member.
	 * 
	 * @param gms gms of the member
	 */
	public void monitorRanks(double gms) {
		Node target = root;
		String result = "Rank Analysis Result: ";
		int rank = findRank(gms);
		
		Queue<Node> membersQueue = new LinkedList<>();
		membersQueue.add(target);
		
		if (this.rankMap.containsKey(rank))
			System.out.println(this.rankMap.get(rank));
		
		else {
			while (!membersQueue.isEmpty()) {
				target = membersQueue.poll();
				
				if (target.getRank() < rank)
					break;
				
				if (target.getRank() == rank)
					result += String.format("%s %.3f ", target.getName(), target.getGms());
					
				if (target.leftChild != null)
					membersQueue.add(target.leftChild);

				if (target.rightChild != null)
					membersQueue.add(target.rightChild);
			}
			rankMap.put(rank, result);
			System.out.println(result);
		}
	}
	
	
	/**
	 * This method prints who welcomes a member.
	 * 
	 * @param superiorName
	 * @param inferiorName
	 */
	private void logIn(String superiorName, String inferiorName) {
		System.out.println(superiorName + " welcomed " + inferiorName);
	}
	
	
	/**
	 * This method prints who left and who replaced it.
	 * 
	 * @param goneMemberName
	 * @param replacingMemberName
	 */
	private void logOut(String goneMemberName, String replacingMemberName) {
		System.out.println(goneMemberName + " left the family, replaced by " + replacingMemberName);
	}
	
	
	/**
	 * This method prints the superior of the targeted members.
	 * 
	 * @param superior
	 */
	private void logIntel(Node superior) {
		System.out.println(String.format("Target Analysis Result: %s %.3f", superior.getName(), superior.getGms()));
	}
}
