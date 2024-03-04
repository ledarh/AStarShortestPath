import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

// Class DelivD does the work for deliverable DelivD of the Prog340

public class DelivD {

	File inputFile;
	File outputFile;
	PrintWriter output;
	Graph g; // distance graph
	Graph g2; // heuristic graph

	public DelivD(File in, Graph gr , Graph gr2) {
		inputFile = in;
		g = gr;
		g2 = gr2;

		// Get output file name.
		String inputFileName = inputFile.toString();
		String baseFileName = inputFileName.substring( 0, inputFileName.length()-4 ); // Strip off ".txt"
		String outputFileName = baseFileName.concat( "_out.txt" );
		outputFile = new File( outputFileName );
		if ( outputFile.exists() ) {    // For retests
			outputFile.delete();
		}

		try {
			output = new PrintWriter(outputFile);
		} catch (Exception x ) {
			System.err.format("Exception: %s%n", x);
			System.exit(0);
		}

		ArrayList<Node> listNode = g.getNodeList();
		ArrayList<Node> listNode2 = g2.getNodeList();
		Node nodeStart = null; // Start node from distance graph
		Node nodeGoal = null; // Goal node from distance graph
		Node nodeGoal2 = null; // Goal from heuristic graph

		// find Start node
		for (Node node : listNode) {
			if (node.getVal().equals("S")) {
				nodeStart = node;
				break;
			}
		}

		// find Goal node
		for (Node node : listNode) {
			if (node.getVal().equals("G")) {
				nodeGoal = node;
				break;
			}
		}

		// find Goal node (heuristic)
		if (nodeGoal != null) {
			String abbrevGoal = nodeGoal.getAbbrev();

			for (Node node2 : listNode2) {
				String abbrevGoal2 = node2.getAbbrev();

				if (abbrevGoal2.equals(abbrevGoal)) {
					nodeGoal2 = node2;
					break;
				}
			}
		}

		// safety checks
		if (nodeStart == null) {
			System.out.println("Start node not found");
			output.println("Start node not found");
		} else if (nodeGoal == null) {
			System.out.println("Goal node not found");
			output.println("Goal node not found");
		} else if (nodeGoal2 == null) {
			System.out.println("Goal2 node not found");
			output.println("Goal2 node not found");
		} else { // initial path set up
			Path pathStart = new Path();
			pathStart.setNode(nodeStart);
			pathStart.setValue(nodeStart.getAbbrev());
			pathStart.setDistance(0);
			pathStart.setHeuristic(0);
			pathStart.setFvalue(0);

			PriorityQueue<Path> queue = new PriorityQueue<>(new Path2Comparator()); // f-value comparator
			queue.add(pathStart); // add initial path (Starting node only) to queue to get adjacent nodes

			HashMap<String, Integer> mapDistance = new HashMap<>(); // min distance from Start to current Node according to Path

			// boilerplate output
			System.out.printf("Shortest Path from %s (%s) to %s (%s)\n", nodeStart.getName(), nodeStart.getAbbrev(),
					nodeGoal.getName(), nodeGoal.getAbbrev());
			output.printf("Shortest Path from %s (%s) to %s (%s)\n", nodeStart.getName(), nodeStart.getAbbrev(),
					nodeGoal.getName(), nodeGoal.getAbbrev());

			System.out.println();
			output.println();

			System.out.printf("%-40s %10s %10s %10s\n", "PATH", "DIST", "HEUR", "F-VALUE");
			output.printf("%-40s %10s %10s %10s\n", "PATH", "DIST", "HEUR", "F-VALUE");

			System.out.printf("%-40s %10s %10s %10s\n", pathStart.getValue(), pathStart.getDistance(), pathStart.getHeuristic(), pathStart.getFvalue());
			output.printf("%-40s %10s %10s %10s\n", pathStart.getValue(), pathStart.getDistance(), pathStart.getHeuristic(), pathStart.getFvalue());

			System.out.println();
			output.println();

			boolean execute = true;

			while (execute) { // path finding analysis
				Path pathFirst = queue.poll(); // poll queue for path in to iterate nodes

				if (pathFirst != null) {
					Node nodeFirst = pathFirst.getNode();
					String valueFirst = pathFirst.getValue();
					int distanceFirst = pathFirst.getDistance();

					if (!nodeFirst.getFinished()) {
						nodeFirst.setFinished(true);

						// add new paths
						for (Edge edge : nodeFirst.getOutgoingEdges()) {
							Node nodeNext = edge.getHead();

							if (!nodeNext.getFinished()) {
								String abbrevNext = nodeNext.getAbbrev();
								int distanceNew = distanceFirst + edge.getDist();
								Integer distanceOld = mapDistance.get(abbrevNext);

								if (distanceOld == null || distanceNew < distanceOld) {
									// found Goal -> exit
									if (nodeNext == nodeGoal) {
										System.out.printf("%-40s %10s\n", valueFirst + "-" + abbrevNext, distanceNew);
										output.printf("%-40s %10s\n", valueFirst + "-" + abbrevNext, distanceNew);

										execute = false;
										break;
									} else {
										mapDistance.put(abbrevNext, distanceNew);
										int heuristic = 0;

										for (Edge edge2 : nodeGoal2.getIncomingEdges()) {
											Node nodeTail2 = edge2.getTail();

											if (nodeTail2.getAbbrev().equals(abbrevNext)) {
												heuristic = edge2.getDist();
												break;
											}
										}

										if (heuristic != 0) {
											Path pathNew = new Path(); // tenative new path for left-side chart print out
											pathNew.setNode(nodeNext);
											pathNew.setValue(valueFirst + "-" + abbrevNext);
											pathNew.setDistance(distanceNew);
											pathNew.setHeuristic(heuristic);
											pathNew.setFvalue(distanceFirst + edge.getDist() + heuristic);
											queue.add(pathNew);

											System.out.printf("%-40s %10s %10s %10s\n", pathNew.getValue(), pathNew.getDistance(), pathNew.getHeuristic(), pathNew.getFvalue());
											output.printf("%-40s %10s %10s %10s\n", pathNew.getValue(), pathNew.getDistance(), pathNew.getHeuristic(), pathNew.getFvalue());
										}
									}
								} else {
									System.out.printf("%-40s %10s\n", valueFirst + "-" + abbrevNext, distanceNew);
									output.printf("%-40s %10s\n", valueFirst + "-" + abbrevNext, distanceNew);
								}
							}
						}

						System.out.println();
						output.println();
					}
				} else {
					System.out.println("Path not found");
					output.println("Path not found");

					execute = false;
				}
			}
		}

		output.flush();
	}
}


