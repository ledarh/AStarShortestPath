import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.PriorityQueue;

// Class DelivC does the work for deliverable DelivC of the Prog340

public class DelivC {

	File inputFile;
	File outputFile;
	PrintWriter output;
	Graph g;

	public DelivC(File in, Graph gr) {
		inputFile = in;
		g = gr;

		// Get output file name.
		String inputFileName = inputFile.toString();
		String baseFileName = inputFileName.substring(0, inputFileName.length() - 4); // Strip off ".txt"
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

		// isExitIfFoundGoal came about after debugging why my program wasn't continuing
		// through 2 depth levels
		boolean isExitIfFoundGoal = false; // default False for output consistent with DelivC spec sheet.

		ArrayList<Node> listNode = g.getNodeList();
		ArrayList<Edge> listEdge = g.getEdgeList();
		Node nodeStart = null;
		Node nodeGoal = null;

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

		// safety checks
		if (nodeStart == null) {
			System.out.println("Start node not found");
			output.println("Start node not found");
		} else if (nodeGoal == null) {
			System.out.println("Goal node not found");
			output.println("Goal node not found");
		} else { // initial path set up
			Path pathStart = new Path();
			pathStart.setNode(nodeStart);
			pathStart.setValue(nodeStart.getAbbrev());
			pathStart.setDistance(0);

			PriorityQueue<Path> queue = new PriorityQueue<>(new PathComparator());
			queue.add(pathStart); // add initial path (Starting node only) to queue to get adjacent nodes

			// boilerplate output
			System.out.printf("Shortest Path from %s (%s) to %s (%s)\n", nodeStart.getName(), nodeStart.getAbbrev(),
					nodeGoal.getName(), nodeGoal.getAbbrev());
			output.printf("Shortest Path from %s (%s) to %s (%s)\n", nodeStart.getName(), nodeStart.getAbbrev(),
					nodeGoal.getName(), nodeGoal.getAbbrev());

			System.out.println();
			output.println();

			System.out.printf("%-40s%7s    %-20s%15s    %-20s\n", "PATH", "DIST", "CITY", "MIN_DIST", "PATH");
			output.printf("%-40s%7s    %-20s%15s    %-20s\n", "PATH", "DIST", "CITY", "MIN_DIST", "PATH");

			System.out.printf("%-40s%7s\n", pathStart.getValue(), pathStart.getDistance());
			output.printf("%-40s%7s\n", pathStart.getValue(), pathStart.getDistance());

			boolean execute = true;

			while (execute) { // path finding analysis
				Path p = queue.poll(); // poll queue for path in to iterate nodes

				if (p != null) {
					Node nodeFirst = p.getNode();

					// 'right side' output of chart
					if (nodeFirst == nodeGoal) {
						System.out.printf("%s%-20s%15s    %-20s\n", " ".repeat(51), nodeFirst.getName(),
								p.getDistance(), p.getValue());
						output.printf("%s%-20s%15s    %-20s\n", " ".repeat(51), nodeFirst.getName(),
								p.getDistance(), p.getValue());

						execute = false; // breaking factor for output aligned with Deliv C spec sheet
					} else if (!nodeFirst.getFinished()) {
						System.out.printf("%s%-20s%15s    %-20s\n", " ".repeat(51), nodeFirst.getName(),
								p.getDistance(), p.getValue());
						output.printf("%s%-20s%15s    %-20s\n", " ".repeat(51), nodeFirst.getName(),
								p.getDistance(), p.getValue());

						nodeFirst.setFinished(true);

						// add new paths
						for (Edge edge : listEdge) {
							Node nodeHead = edge.getHead();
							Node nodeTail = edge.getTail();

							if (nodeHead == nodeFirst && !nodeTail.getFinished()) {
								Path pathNew = new Path(); // tenative new path for left-side chart print out
								pathNew.setNode(nodeTail);
								pathNew.setValue(p.getValue() + "-" + nodeTail.getAbbrev());
								pathNew.setDistance(p.getDistance() + edge.getDist());
								queue.add(pathNew);

								// 'left side' output of chart
								System.out.printf("%-40s%7s\n", pathNew.getValue(), pathNew.getDistance());
								output.printf("%-40s%7s\n", pathNew.getValue(), pathNew.getDistance());

								// found Goal -> exit
								if (isExitIfFoundGoal && nodeTail == nodeGoal) {
									System.out.printf("%s%-20s%15s    %-20s\n", " ".repeat(51), nodeTail.getName(),
											pathNew.getDistance(), pathNew.getValue());
									output.printf("%s%-20s%15s    %-20s\n", " ".repeat(51), nodeTail.getName(),
											pathNew.getDistance(), pathNew.getValue());

									execute = false;
									break;
								}
							}
						}
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
