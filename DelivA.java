import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

// Class DelivA does the work for deliverable DelivA of the Prog340

public class DelivA {

	File inputFile;
	File outputFile;
	PrintWriter output;
	Graph g;

	public DelivA(File in, Graph gr) {
		inputFile = in;
		g = gr;

		// Get output file name.
		String inputFileName = inputFile.toString();
		String baseFileName = inputFileName.substring(0, inputFileName.length() - 4); // Strip off ".txt"
		String trimmedFileName = in.getName().replaceFirst("[.][^.]+$", "");
		String outputFileName = baseFileName.concat("_out.txt");
		outputFile = new File(outputFileName);
		if (outputFile.exists()) { // For retests
			outputFile.delete();
		}

		try {
			output = new PrintWriter(outputFile);
		} catch (Exception x) {
			System.err.format("Exception: %s%n", x);
			System.exit(0);
		}

		System.out.println("Graph " + trimmedFileName + ":\n");
		output.println("Graph " + trimmedFileName + ":\n");

		System.out.println("There are " + gr.getNodeList().size() + " nodes in the graph.\n");
		output.println("There are " + gr.getNodeList().size() + " nodes in the graph.\n");

		System.out.println("There are " + gr.getEdgeList().size() + " edges in the graph.\n");
		output.println("There are " + gr.getEdgeList().size() + " edges in the graph.\n");

		/*
		 * Nodes
		 */
		ArrayList<Node> mostOutgoing = getMostOutgoing(gr.getNodeList());
		if (mostOutgoing.size() > 1) {
			StringBuilder abbrevs = new StringBuilder();
			mostOutgoing.forEach((Node n) -> abbrevs.append(n.getAbbrev() + ", "));
			abbrevs.delete(abbrevs.length() - 3, abbrevs.length());
			abbrevs.append("and " + mostOutgoing.get(mostOutgoing.size() - 1).getAbbrev() + " ");
			System.out.println("Nodes " + abbrevs.toString() + "have the most outgoing edges " + "("
					+ mostOutgoing.get(0).getOutgoingEdges().size() + ").\n");
			output.println("Nodes " + abbrevs.toString() + "have the most outgoing edges " + "("
					+ mostOutgoing.get(0).getOutgoingEdges().size() + ").\n");
		} else if (mostOutgoing.size() == 1) {
			System.out.println("Node " + mostOutgoing.get(0).getAbbrev() + " has the most outgoing edges " + "("
					+ mostOutgoing.get(0).getOutgoingEdges().size() + ").\n");
			output.println("Node " + mostOutgoing.get(0).getAbbrev() + " has the most outgoing edges " + "("
					+ mostOutgoing.get(0).getOutgoingEdges().size() + ").\n");
		} else {
			try {
				throw new Exception("Unable to find node with the most outgoing edges.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/*
		 * Edges
		 */
		ArrayList<Edge> longestEdgeList = getLargestEdge(gr.getEdgeList());
		if (longestEdgeList.size() > 1) {
			StringBuilder longestEdgesStr = new StringBuilder();
			longestEdgeList.forEach(
					(Edge e) -> longestEdgesStr.append(e.getTail().getAbbrev() + e.getHead().getAbbrev() + ", "));
			longestEdgesStr.delete(longestEdgesStr.length() - 6, longestEdgesStr.length());
			longestEdgesStr
					.append(" and " + longestEdgeList.get(longestEdgeList.size() - 1).getTail().getAbbrev()
							+ longestEdgeList.get(longestEdgeList.size() - 1).getHead().getAbbrev() + " ");
			System.out.println("The longest edges are edges " + longestEdgesStr.toString() + "("
					+ longestEdgeList.get(0).getDist() + ").\n");
			output.println("The longest edges are edges " + longestEdgesStr.toString() + "("
					+ longestEdgeList.get(0).getDist() + ").\n");
		} else if (longestEdgeList.size() == 1) {
			System.out.println("The longest edge is edge " + longestEdgeList.get(0).getTail().getAbbrev()
					+ longestEdgeList.get(0).getHead().getAbbrev() + " ("
					+ longestEdgeList.get(0).getDist() + ").\n");
			output.println("The longest edge is edge " + longestEdgeList.get(0).getTail().getAbbrev()
					+ longestEdgeList.get(0).getHead().getAbbrev() + " ("
					+ longestEdgeList.get(0).getDist() + ").\n");
		} else {
			try {
				throw new Exception("Unable to find the longest edge.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		output.flush();
	}

	public ArrayList<Node> getMostOutgoing(ArrayList<Node> nodeList) {
		Iterator<Node> itr = nodeList.iterator();
		ArrayList<Node> mostOutgoingEdges = new ArrayList<Node>(Collections.singletonList(itr.next()));
		Node max = mostOutgoingEdges.get(0);
		while (itr.hasNext()) {
			Node curr = itr.next();
			if (max.getOutgoingEdges().size() < curr.getOutgoingEdges().size()) {
				max = curr;
				mostOutgoingEdges = new ArrayList<Node>(); // reset list
				mostOutgoingEdges.add(curr);
			} else if (max.getOutgoingEdges().size() == curr.getOutgoingEdges().size()) {
				mostOutgoingEdges.add(curr);
			}

		}
		return mostOutgoingEdges;
	}

	public ArrayList<Edge> getLargestEdge(ArrayList<Edge> edgeList) {
		Iterator<Edge> itr = edgeList.iterator();
		ArrayList<Edge> longestEdgeList = new ArrayList<Edge>(Collections.singletonList(itr.next()));
		Edge longest = longestEdgeList.get(0); // current longest Edge
		while (itr.hasNext()) {
			Edge curr = itr.next();
			if (longest.getDist() < curr.getDist()) {
				longest = curr;
				longestEdgeList = new ArrayList<Edge>(); // new longest, reset list
				longestEdgeList.add(longest);
			} else if ((longest.getDist() == curr.getDist()) && (true)) {
				longestEdgeList.add(curr);
			}
		}
		return longestEdgeList;
	}

}
