package lattice;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Lattice {

	private List<Node> nodes;
	
	public static final String LATTICE_DOT = "lattice.dot";

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public Lattice(List<Node> nodes) {
		this.nodes = nodes;
	}
	
	public Lattice() {
		this.nodes = new ArrayList<Node>();
	}
	
	public List<Node> getNodesPerName(Set<String> names) {
		List<Node> returnList = new ArrayList<Node>();
		for (Node node : nodes) {
			for (String name : names) {
				if (node.getName().equals(name)) {
					returnList.add(node);
				}
			}
		}
		return returnList;
	}
	
	public Node getNodePerName(String name) {
		for (Node node : nodes) {
			if (node.getName().equals(name)) {
				return node;
			}
		}
		return null;
	}

	public boolean leq(Node node1, Node node2){
		System.out.println("Node 1: " + node1.getName());
		System.out.println("Node 2: " + node2.getName());
		if(node1.getName().equals(node2.getName())){
			return true;
		}
		//Naive check if there is any path from node1 to node2. If there is leq = true
		for (Transition transition : node1.getOutgoingTransitions()){
			Node target = transition.getTarget();
			System.out.println("Target: " + target.getName());
			if(node2.getName().equals(target.getName())){
				return true;
			}
			else {
				leq(target, node2);
			}
		}
		return false;
	}
	
	public Node getBottom() {
		List<Node> allNodes = new ArrayList<Node>();
		allNodes.addAll(nodes);
		for (Node node : nodes) {
			for (Transition transition : node.getOutgoingTransitions()) {
				if (allNodes.contains(transition.getTarget())) {
					allNodes.remove(transition.getTarget());
				}
			}
		}
		if (allNodes.size() == 1) {
			return allNodes.get(0);
		} else {
			return null;
		}
	}
}
