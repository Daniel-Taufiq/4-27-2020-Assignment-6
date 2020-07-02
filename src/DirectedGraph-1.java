import java.util.*;


/**
 * CISC 380 Algorithms Assignment 5
 * 
 * Represents a graph of nodes and edges in adjacency list format.
 * 
 * @author Daniel Taufiq
 * Due Date: 05/17/2020
 */

class DirectedGraph {

	private static final boolean DEBUG = false;
	private ArrayList<DirectedGraphNode> nodes;
	HashSet<DirectedGraphNode> initialStage = new HashSet<DirectedGraphNode>();
	HashSet<DirectedGraphNode> exploreStage = new HashSet<DirectedGraphNode>();
	HashSet<DirectedGraphNode> endStage = new HashSet<DirectedGraphNode>();


	/**
	 * Constructs a directed graph with the given adjacency matrix. The adjacency
	 * matrix is a 2d array of booleans representing the presence of edges in the
	 * graph.
	 *
	 * The graph will have a number of vertices equal to the length of the adjacency matrix.
	 * 
	 * An edge from vertex i to vertex j exists if adjacencyMatrix[i][j] is true.
	 * @param adjacencyMatrix a 2d boolean array representing an adjacency matrix.
	 */
	public DirectedGraph(boolean[][] adjacencyMatrix) {

		nodes = new ArrayList<DirectedGraphNode>();

		// populate the graph with nodes.
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			nodes.add(new DirectedGraphNode(i));
		}

		// connect the nodes based on the adjacency matrix
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			for (int j = 0; j < adjacencyMatrix[i].length; j++) {
				if (adjacencyMatrix[i][j]) {
					this.connect(i, j);
				}//if
				// THIS CODE BELOW MAKES CYCLE
				if(i == 3 && j == 0)
				{
					this.connect(3, 0);
				}
			}
		}

	}// constructor

	/**
	 * Constructs a directed graph with the given adjacency list. The
	 * adjacency list is a 2d array of integers where the array at index i
	 * represents the ids of child vertices of vertex i.
	 *
	 * The graph will have a number vertices equal to the length of the adjacency list.
	 * 
	 * Only values between 0 (inclusive) and the length of the adjacency list (exclusive) are valid.
	 * 
	 * @param adjacencyList a 2d integer array representing an adjacency list of the vertices.
	 */
	public DirectedGraph(int[][] adjacencyList) {
		nodes = new ArrayList<DirectedGraphNode>();

		// populates the graph with nodes.
		for (int i = 0; i < adjacencyList.length; i++) {
			nodes.add(new DirectedGraphNode(i));
		}

		// connect the nodes based on the adjacency list.
		for (int i = 0; i < adjacencyList.length; i++) {
			for (int j = 0; j < adjacencyList[i].length; j++) {

				// if the value in the array is a valid node id, connect them.
				if (-1 < adjacencyList[i][j] && adjacencyList[i][j] < adjacencyList.length) {
					this.connect(i, adjacencyList[i][j]);
				}
			}

		}

	}// constructor

	/**
	 * Determines if a cycle exists in the directed graph.
	 * 
	 * @return true, if there are no cycles, false otherwise.
	 */
	public boolean isAcyclic() {
		/*
			initialStage = unvisited vertices
			exploreStage = vertices being visited
			endStage = visited vertices

			finding vertex in greySet = cycle found, return false
		 */
		for(int i = 0; i < this.nodes.size(); i++)
		{
			initialStage.add(nodes.get(i));
		}
		while(initialStage.size() > 0)
		{
			DirectedGraphNode current = null;
			current = nodes.iterator().next();
			for(int j = 0; j < endStage.size()+1; j++)
			{
				current = nodes.get(j);
			}
			// found cycle means graph is not acyclic
			if(findCycle(current) == true)
			{
				return false;
			}
		}
		return true;
		//YOUR CODE HERE
	}// isAcyclic

	private boolean findCycle(DirectedGraphNode current)
	{
		// move current Node from initialStage to exploreStage and set to explore
		moveToSet(current, initialStage, exploreStage);

		for(DirectedGraphNode neighbor : current.getOutgoingNodes())
		{
			//if in endStage set means already explored so continue.
			if (endStage.contains(neighbor) == true) {
				continue;
			}
			//if in exploreStage then cycle found.
			if (exploreStage.contains(neighbor) == true) {
				return true;
			}
			// return true if we found a cycle
			if(findCycle(neighbor) == true) {
				return true;
			}
		}
		//move vertex from exploreStage set to endStage set when done exploring.
		moveToSet(current, exploreStage, endStage);

		return false;
	}

	private void moveToSet(DirectedGraphNode current, HashSet<DirectedGraphNode> source, HashSet<DirectedGraphNode> dest)
	{
		source.remove(current);
		dest.add(current);
	}


	/**
	 * Retrieves the number of nodes in the Graph.
	 * 
	 * @return the number of nodes in the graph.
	 */
	public int getGraphSize() {
		return this.nodes.size();
	}// getGraphSize


	/**
	 * Returns a string representation of all the nodes in the graph. The string
	 * displays all of the data fields in the node.
	 *
	 * @return a string representation of the graph.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		// for every node
		for (int i = 0; i < this.nodes.size(); i++) {
			// append the string representation to the result.
			DirectedGraphNode current = this.nodes.get(i);
			sb.append(String.format("%-8s vCover: %3d In: %3d Out: %3d Pre: %3d Post: %3d\n", current.toString(),current.getCoverSize(),current.getOutDegree(),current.getInDegree(),current.getPreNumber(), current.getPostNumber()));
		}
		return sb.toString();
	}// toString

	/**
	 * adds the node other as a neighbor to root.
	 *
	 * @param root  the data of the node to receive a neighbor
	 * @param other the data of the node to be added
	 */
	private void connect(int root, int other) {

		if (0 > root || root >= this.getGraphSize()) {
			throw new ArrayIndexOutOfBoundsException("Cannot connect nonexistent root with value: " + root
					+ ". Valid Nodes are between 0 and " + (this.nodes.size() - 1) + ".");
		}

		if (0 > other || other >= this.getGraphSize()) {
			throw new ArrayIndexOutOfBoundsException("Cannot connect nonexistent root with value: " + other
					+ ". Valid Nodes are between 0 and " + (this.nodes.size() - 1) + ".");

		}

		DirectedGraphNode rootNode = findNode(root);
		DirectedGraphNode otherNode = findNode(other);

		// add the other node to the root
		rootNode.getOutgoingNodes().add(otherNode);
		otherNode.incrementInDegree();

	}// connect

	/**
	 * Finds a node in the graph, if it exists.
	 * 
	 * @throws ArrayIndedOutOfBoundsException if the node does not exist.
	 * @return a DirectedGraphNode with the given data.
	 * 
	 */
	private DirectedGraphNode findNode(int data) {
		if(0 <= data && data < this.nodes.size()){
			return nodes.get(data);
		}else{
			return null;
		}
		

	}// findNode

	/**
	 * Representation of a vertex of the graph, uniquely identified by the data.
	 */
	private static class DirectedGraphNode {

		private int data;
		private int inDegree;

		private Integer coverSize;
		private int preNumber;
		private int postNumber;

		private LinkedList<DirectedGraphNode> outgoingNodes;

		public DirectedGraphNode(int data) {

			this.data = data;
			this.coverSize = null;
			this.outgoingNodes = new LinkedList<DirectedGraphNode>();
			this.inDegree = 0;
			this.preNumber = -1;
			this.postNumber = -1;

		}// constructor

		/**
		 * increments the in degree. 
		 * 
		 */
		public void incrementInDegree() {
			this.inDegree++;
		}

		/**
		 * sets the post number for this nodes pre number.
		 * @param newVal the new value
		 */
		public void setPreNumber(int newVal) {
			this.preNumber = newVal;
		}
		/**
		 * 
		 * sets the post number for this nodes post number.
		 * @param newVal the new value
		 */
		public void setPostNumber(int newVal) {
			this.postNumber = newVal;
		}

		/**
		 * returns this node's pre number
		 * @return the pre number of this node.
		 */
		public int getPreNumber() {
			return this.preNumber;
		}

		/**
		 * returns this node's post number
		 * @return the post number of this node.
		 */
		public int getPostNumber() {
			return this.postNumber;
		}
		/**
		 * returns this node's in degree.
		 * This is the amount of nodes that this node has as a parent.
		 * @return the in degree of this node.
		 */
		public int getInDegree() {
			return this.inDegree;
		}//getInDegree

		/**
		 * returns this node's out degree.
		 * This is the amount of children nodes this node has.
		 * @return the out degree of this node.
		 */
		public int getOutDegree() {
			return this.outgoingNodes.size();
		}

		/**
		 * getter method for the data of the node. This should uniquely identify the
		 * node.
		 * 
		 * @return the data within this node.
		 */
		public int getData() {
			return this.data;
		}// getData

		/**
		 * getter method for the cover size of the node.
		 * 
		 * @return the size of the vertex cover stored in this Node, or null if the
		 *         cover size hasn't been set yet.
		 * 
		 */
		public Integer getCoverSize() {
			return this.coverSize;
		}// isCovered

		/**
		 * retrieves a reference to a list of this node's outgoingNodes.
		 * 
		 * @return a LinkedList of nodes that are connected to this node.
		 * 
		 */
		public List<DirectedGraphNode> getOutgoingNodes() {
			return this.outgoingNodes;
		}// getNeighbors

		/**
		 * returns a string representation of the node. Displays the current data of the
		 * node, and a list of the data of all of its outgoingNodes.
		 * 
		 * @return a string representation of the node.
		 */
		public String toString() {
			StringBuilder sb = new StringBuilder();

			sb.append(this.getData() + ":[");

			for (int i = 0; i < this.outgoingNodes.size(); i++) {
				if (i == this.outgoingNodes.size() - 1) {
					sb.append(outgoingNodes.get(i).getData());
				} else {
					sb.append(outgoingNodes.get(i).getData() + ", ");
				} // else

			} // for
			sb.append("]");
			return sb.toString();

		}// toString

	}// class DirectedGraphNode

}// class