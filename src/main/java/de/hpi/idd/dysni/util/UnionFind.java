package de.hpi.idd.dysni.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Based on the implementation by
 * <a href="http://algs4.cs.princeton.edu/15uf/UF.java.html">Robert Sedgewick
 * and Kevin Wayne</a> from Princeton University
 *
 * <p>
 * For additional documentation, see
 * <a href="http://algs4.cs.princeton.edu/15uf">Section 1.5</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 * </p>
 *
 * @param <T>
 *            type of the elements
 */
public class UnionFind<T> {

	/**
	 * This class represents a node in the Union Find tree having a parent,
	 * multiple children and containing an element of type {@code T}
	 *
	 */
	private class Node {

		private final Collection<Node> children = new HashSet<>();
		private final T element;
		private Node parent;
		private byte rank;

		/**
		 * Constructs a new node containing the specified element.
		 *
		 * @param element element associated with this node
		 */
		public Node(T element) {
			this.element = element;
		}

		/**
		 * Adds a new child node to this node.
		 *
		 * @param child new child node
		 */
		private void addChild(Node child) {
			children.add(child);
		}

		/**
		 * Get all child nodes contained by this node.
		 *
		 * @return collection of child nodes
		 */
		public Collection<Node> getChildren() {
			return children;
		}

		/**
		 * Get the element contained by this node.
		 *
		 * @return element contained by this node
		 */
		public T getElement() {
			return element;
		}

		/**
		 * Get the parent node of this node.
		 *
		 * @return parent node of this node
		 */
		public Node getParent() {
			return parent;
		}

		/**
		 * Get the rank of this node. The rank is increased whenever two nodes
		 * of same rank are unified. Hence, the rank grows logarithmic and
		 * should never exceed 31.
		 *
		 * @return rank of this node in the tree
		 */
		public byte getRank() {
			return rank;
		}

		/**
		 * Increases rank of this node. The rank is increased whenever two nodes
		 * of same rank are unified. Hence, the rank grows logarithmic and
		 * should never exceed 31.
		 */
		public void increaseRank() {
			rank++;
		}

		/**
		 * Removes the specified child from this node.
		 *
		 * @param child
		 *            the child to be removed
		 */
		public void removeChild(Node child) {
			children.remove(child);
		}

		/**
		 * Sets the parent node of this node and updates the child relations
		 * accordingly.
		 *
		 * @param parent
		 *            the new parent node
		 */
		public void setParent(Node parent) {
			if (this.parent != null) {
				this.parent.removeChild(this);
			} else {
				roots.remove(this);
			}
			this.parent = parent;
			parent.addChild(this);
		}
	}

	/** Element-node mapping to retrieve the node */
	private final Map<T, Node> nodes = new HashMap<>();
	/** Nodes rooting a tree */
	private final Set<Node> roots = new HashSet<>();

	/**
	 * Returns true if the the two sites are in the same component.
	 *
	 * @param t
	 *            the element representing one site
	 * @param u
	 *            the element representing the other site
	 * @return <tt>true</tt> if the two sites <tt>t</tt> and <tt>u</tt> are in
	 *         the same component; <tt>false</tt> otherwise
	 */
	public boolean connected(T t, T u) {
		Node nodeT = find(t);
		Node nodeU = find(u);
		return !(nodeT == null || nodeU == null) && nodeT.equals(nodeU);
	}

	/**
	 * Returns the number of disjoint components.
	 *
	 * @return the number of components
	 */
	public int count() {
		return roots.size();
	}

	/**
	 * Returns the component identifier for the component containing site
	 * <tt>t</tt>.
	 *
	 * @param t
	 *            the element representing one site
	 * @return the component identifier for the component containing site
	 *         <tt>t</tt>
	 */
	private Node find(T t) {
		Node node = nodes.get(t);
		if (node == null) {
			return null;
		}
		while (node.getParent() != null) {
			// path compression by halving
			Node pp = node.getParent().getParent();
			if (pp != null) {
				node.setParent(pp);
			}
			node = node.getParent();
		}
		return node;
	}

	/**
	 * Returns the elements contained in the same component as <tt>t</tt>
	 *
	 * @param t
	 *            the element representing one site
	 * @return the elements contained in the same component as <tt>t</tt>
	 *         excluding <tt>t</tt>
	 */
	public Collection<T> getComponent(T t) {
		Collection<T> component = new HashSet<>();
		Stack<Node> todo = new Stack<>();
		Node node = find(t);
		if (node == null) {
			return component;
		}
		todo.add(node);
		while (!todo.isEmpty()) {
			Node elem = todo.pop();
			component.add(elem.getElement());
			todo.addAll(elem.getChildren());
		}
		component.remove(t);
		return component;
	}

	/**
	 * Get the nodes rooting the trees of the Union Find
	 *
	 * @return nodes rooting a tree
	 */
	public Set<T> getRoots() {
		return roots.stream().map(Node::getElement).collect(Collectors.toSet());
	}

	/**
	 * Add a new element to the Union find by creating a new tree having exactly
	 * one node.
	 *
	 * @param t
	 *            element to be inserted
	 */
	private void insert(T t) {
		Node node = new Node(t);
		roots.add(node);
		nodes.put(t, node);
	}

	/**
	 * Merges the component containing site <tt>t</tt> with the the component
	 * containing site <tt>u</tt>.
	 *
	 * @param t
	 *            the element representing one site
	 * @param u
	 *            the element representing the other site
	 */
	public void union(T t, T u) {
		if (!nodes.containsKey(t)) {
			insert(t);
		}
		if (!nodes.containsKey(u)) {
			insert(u);
		}
		Node rootT = find(t);
		Node rootU = find(u);
		if (rootT.equals(rootU)) {
			return;
		}
		// make root of smaller rank point to root of larger rank
		if (rootT.getRank() < rootU.getRank()) {
			rootT.setParent(rootU);
		} else if (rootT.getRank() > rootU.getRank()) {
			rootU.setParent(rootT);
		} else {
			rootU.setParent(rootT);
			rootT.increaseRank();
		}
	}
}