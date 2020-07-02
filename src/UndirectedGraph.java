import java.util.*;

public class UndirectedGraph {

    private ArrayList<UndirectedGraphNode> nodes;
    //private Queue<UndirectedGraphNode> queue = new LinkedList<>();


	/**
	 * Constructs an undirected graph with the given adjacency matrix. The adjacency
	 * matrix is a 2d array of booleans representing the presence of edges in the
	 * graph.
	 *
	 * The graph will have a number of vertices equal to the length of the adjacency matrix.
	 * 
	 * An edge from vertex i to vertex j exists if adjacencyMatrix[i][j] is true.
	 * @param adjacencyMatrix a 2d boolean array representing an adjacency matrix.
	 */
	public UndirectedGraph(boolean[][] adjacencyMatrix) {

		nodes = new ArrayList<UndirectedGraphNode>();

		// populate the graph with nodes.
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			nodes.add(new UndirectedGraphNode(i));
		}

		// connect the nodes based on the adjacency matrix
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			for (int j = 0; j < adjacencyMatrix[i].length; j++) {
				if (adjacencyMatrix[i][j]) {
					this.connect(i, j);
				}//if
				//MODIFIED
//				if(i == 2 && j == 4)
//				{
//					this.connect(2, 4);
//				}
//				if(i == 0 && j == 3)
//				{
//					this.connect(0, 3);
//				}
//				if(i == 4 && j == 5)
//				{
//					this.connect(4, 5);
//				}
//				if(i == 0 && j == 6)
//				{
//					this.connect(0, 6);
//				}
//				if(i == 4 && j == 6)
//				{
//					this.connect(4, 6);
//				}
			}
        }

    }// constructor
    
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
			UndirectedGraphNode current = this.nodes.get(i);
			sb.append(String.format("Node %-8d Adjacent Nodes %3s\n", current.getData(), this.getArrayData(current.getAdjacentNodes())));
		}
		return sb.toString();
    }// toString
    
    private String getArrayData(LinkedList<UndirectedGraphNode> output) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (output.size() > 0) {
            sb.append(output.get(0).data);
            for (int i = 1; i < output.size(); i++) {
                sb.append(", " + output.get(i).data);
            }
        }
        sb.append("]");
        return sb.toString();
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
	 * adds the node other as a neighbor to root.
	 *
	 * //@param root  the data of the node to receive a neighbor
	 * //@param other the data of the node to be added
	 */
	private void connect(int nodeA, int nodeB) {

		if (0 > nodeA || nodeA >= this.getGraphSize()) {
			throw new ArrayIndexOutOfBoundsException("Cannot connect nonexistent root with value: " + nodeA
					+ ". Valid Nodes are between 0 and " + (this.nodes.size() - 1) + ".");
		}

		if (0 > nodeB || nodeB >= this.getGraphSize()) {
			throw new ArrayIndexOutOfBoundsException("Cannot connect nonexistent root with value: " + nodeB
					+ ". Valid Nodes are between 0 and " + (this.nodes.size() - 1) + ".");

		}

		UndirectedGraphNode connectNodeA = findNode(nodeA);
		UndirectedGraphNode connectNodeB = findNode(nodeB);

        // add the other node to the root
        if (!connectNodeA.hasAdjacentNode(connectNodeB)) {
            connectNodeA.addAdjacentNode(connectNodeB);
        }
        if (!connectNodeB.hasAdjacentNode(connectNodeA)) {
            connectNodeB.addAdjacentNode(connectNodeA);
        }
        

	}// connect
    
    private UndirectedGraphNode findNode(int data) {
		if(0 <= data && data < this.nodes.size()){
			return nodes.get(data);
		}else{
			return null;
		}
    }// findNode
    
    /**
     * This method takes data for a starting node and ending node and returns the number of unique shortest paths between them.
     * For example, if the the shortest path between nodes 1 and 5 is 3, and there is only ONE path of length 3, this method will 
     * return 1.  However, if there are 4 different paths that lead from 1 to 5 that are all length 3, this method will return 4.
	 * If there is not a path between start and end, this method should return 0
	 * 
     * @param start
     * @param end
     * @return
     */
    public int numShortestPaths(int start, int end) {

		//ADD YOUR CODE HERE
		// queue to add elements to
		Queue<UndirectedGraphNode> queue = new LinkedList<>();
		// distance array corresponding to each node from the start vertex
		//TODO maybe dist should re
		int[] dist = new int[nodes.size()];

		// initialize start node with distance zero (explicit)
		for(int i = 0; i < dist.length; i++)
		{
			if (nodes.get(i).data == start)
			{
				dist[i] = 0;
				queue.add(nodes.get(i));	// add start node to queue
			}
			else
			{
				dist[i] = 2*dist.length;	// set all other nodes to a large value
			}
		}
		int min = 2*dist.length;
		int counter = 0;
        return numShortestPaths(queue, dist, end, start, min, counter, true, true);
    }

    // O(m+n)
    private int numShortestPaths(Queue<UndirectedGraphNode> queue, int[] dist, int end, int start, int min, int counter, boolean checkMin, boolean increment)
	{
		int numShortestPaths = 0;
		// loop as long as queue is not empty
		while(queue.peek() != null)
		{
			//pop node out of queue and use as current node
			UndirectedGraphNode w = queue.poll();

			// BFS: traverse neighboring nodes
			for(int i = 0; i < w.getAdjacentNodes().size(); i++)
			{
				increment = true;
				// if adjacent node has large distance, add to queue
				UndirectedGraphNode z = w.getAdjacentNodes().get(i);
				//UndirectedGraphNode z = w.getAdjacentNodes().iterator().next();	// get the next adjacent node

				// since it's undirected graph, we dont want to go backwards AND did not reach target
				if(dist[w.getData()] + 1 <= dist[z.getData()] && z.getData() != end && increment == true)
				{
					queue.add(w.getAdjacentNodes().get(i));
					dist[z.getData()] = dist[w.getData()] + 1;
					if(start != w.getData())
					{
						dist[z.getData()] = dist[w.getData()] + 1;
					}
					increment = false;
				}
				// don't go backwards, but reached target and make sure we don't update it to a bigger dist than previously found
				// one time check because our first node to reach will be the first to update
				if(dist[w.getData()] + 1 < dist[z.getData()] && z.getData() == end && dist[w.getData()] + 1 < min && checkMin == true)
				{
					checkMin = false;
					min = dist[w.getData()]+1;
					counter++;
					//queue.add(w.getAdjacentNodes().get(i));
					dist[z.getData()] = dist[w.getData()]+1;
				}
				// update counter if node reacher target with same min as found by another node
				else if(dist[w.getData()] + 1 <= dist[z.getData()] && z.getData() == end && dist[w.getData()] + 1 == min)
				{
					counter++;
					//queue.add(w.getAdjacentNodes().get(i));
					dist[z.getData()] = dist[w.getData()] + 1;
				}
				// we found a smaller min, reset stuff
				else if(dist[w.getData()] + 1 < dist[z.getData()] && z.getData() == end && dist[w.getData()] + 1 <= min)
				{
					counter = 1;
					min = dist[w.getData()]+1;
				}
			}
		}

		return counter;
	}
    
    
    private static class UndirectedGraphNode {
        private int data;
        private LinkedList<UndirectedGraphNode> adjacentNodes;

        public UndirectedGraphNode(int data) {
            this.data = data;
            this.adjacentNodes = new LinkedList<UndirectedGraphNode>();
        }

        public int getData() {
            return this.data;
        }

        public LinkedList<UndirectedGraphNode> getAdjacentNodes() {
            return this.adjacentNodes;
        }

        public void addAdjacentNode(UndirectedGraphNode newNode) {
            this.adjacentNodes.add(newNode);
        }

        public boolean hasAdjacentNode(UndirectedGraphNode node) {
            for (int i = 0; i < this.adjacentNodes.size(); i++) {
                if (this.adjacentNodes.get(i).equals(node)) {
                    return true;
                }
            }
            return false;
        }
    }

}