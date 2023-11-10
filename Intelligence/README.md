# Intelligence Services (An AVL Tree Project)

In this project, we are an intelligence service and we have the list of criminals and their criminal values. We shape those list into a binary tree and make operations on it.  

## Operations

- Member in: A new member to add to the hierarchy. Insert a new node to the tree.
- Member out: A member leaves the organisation. Remove a node from the tree.
- Reorganisation: After each member in and member out operation, the criminals performs an organisation such that the tree structure is always balanced. (The height difference of two nodes is at most 1) This makes the tree an AVL tree.
- Intel Target: Given 2 members, find the lowest ranking member which is superior to both of the members. Simply, lowest common ancestor of these nodes is needed. Solved using Depth First Search(DFS) with recursion.
- Intel Rank: Given a member, find all the members which has the same rank. Solved using Breadth First Search(BFS) with Queue data structure.
- Intel Divide: Find the maximum number of members such that no two members are directly superior or inferior to each other. Solved using Depth First Search(DFS) using recursion.

## Performance

- Member in and out operations execute in O(logn) complexity.  
- Intel Target operation also executes in O(logn) complexity.  
- Intel Rank operation has O(n) complexity.  
- Intel Divide operation also has O(n) complexity.  
  
I didn't upload the testcases because they exceeded the 100 mb limit of Github. But they execute fairly quick. (around 2 to 10 seconds for extremely large inputs like 871k lines of input)
