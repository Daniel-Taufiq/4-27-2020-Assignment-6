import edu.princeton.cs.algs4.EdgeWeightedGraph;

/**
 * CISC 380
 * Algorithms Assignment 6
 *
 * Tests the findPath method from the PrimMST class
 *
 *  This driver file contains a single example test case to get you started.
 *  You should include more test cases to ensure that your implemenetation works correctly.
 *  You do NOT need to submit this file.
 *
 *
 * @author YOUR NAME HERE
 * Due Date: xx/xx/xx
 */



public class PrimMSTDriver{


    public static void main(String[] args){
		EdgeWeightedGraph ewg = new EdgeWeightedGraph(5,9);
		PrimMST mst = new PrimMST(ewg);

		mst.findPath(1,3);

    }//main

}//class