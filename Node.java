import java.util.*;

// A node of a graph for the Spring 2018 ICS 340 program

public class Node implements Comparator<Node> {

	String name;
	String val; // The value of the Node
	String abbrev; // The abbreviation for the Node
	Boolean isStart;
	Boolean isGoal;
	Boolean finished; // if the Node has been visited/finished
	ArrayList<Edge> outgoingEdges;
	ArrayList<Edge> incomingEdges;

	public Node(String theAbbrev) {
		setAbbrev(theAbbrev);
		val = null;
		name = null;
		isStart = false;
		isGoal = false;
		finished = false;
		outgoingEdges = new ArrayList<Edge>();
		incomingEdges = new ArrayList<Edge>();
	}

	public String getAbbrev() {
		return abbrev;
	}

	public String getName() {
		return name;
	}

	public String getVal() {
		return val;
	}

	public boolean getFinished() {
		return finished;
	}

	public boolean getIsStart() {
		return isStart;
	}

	public boolean getIsGoal() {
		return isGoal;
	}

	public ArrayList<Edge> getOutgoingEdges() {
		return outgoingEdges;
	}

	public ArrayList<Edge> getIncomingEdges() {
		return incomingEdges;
	}

	public void setAbbrev(String theAbbrev) {
		abbrev = theAbbrev;
	}

	public void setIsStart(Boolean start) {
		isStart = start;
	}

	public void setIsGoal(Boolean goal) {
		isGoal = goal;
	}

	public void setName(String theName) {
		name = theName;
	}

	public void setVal(String theVal) {
		val = theVal;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public void addOutgoingEdge(Edge e) {
		outgoingEdges.add(e);
	}

	public void addIncomingEdge(Edge e) {
		incomingEdges.add(e);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Node)) {
			return false;
		}
		Node node = (Node) o;
		return Objects.equals(name, node.name) && Objects.equals(val, node.val) && Objects.equals(abbrev, node.abbrev)
				&& Objects.equals(finished, node.finished) && Objects.equals(outgoingEdges, node.outgoingEdges)
				&& Objects.equals(incomingEdges, node.incomingEdges);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, val, abbrev, finished, outgoingEdges, incomingEdges);
	}

	@Override
	public int compare(Node o1, Node o2) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'compare'");
	}

}
