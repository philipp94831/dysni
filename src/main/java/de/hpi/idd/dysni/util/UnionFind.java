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
	 * multiple children and containing an element of type {@code U}
	 *
	 * @param <T>
	 *            type of elements contained
	 *
	 */
	private static class Node<T> {

		private static <T> Node<T> constructTopNode() {
			return new Node<T>();
		}

		private final Collection<Node<T>> children = new HashSet<>();
		private final T element;
		private Node<T> parent;
		private byte rank;

		/**
		 * Constructs top node
		 */
		private Node() {
			this.element = null;
		}

		/**
		 * Constructs a new node containing the specified element.
		 *
		 * @param element
		 *            element associated with this node
		 */
		public Node(T element) {
			if (element == null) {
				throw new NullPointerException("Element must not be null");
			}
			this.element = element;
		}

		/**
		 * Adds a new child node to this node.
		 *
		 * @param child
		 *            new child node
		 */
		private void addChild(Node<T> child) {
			children.add(child);
		}

		/**
		 * Get all child nodes contained by this node.
		 *
		 * @return collection of child nodes
		 */
		public Collection<Node<T>> getChildren() {
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
		public Node<T> getParent() {
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
		 * Node is considered root if its parent is a top node
		 *
		 * @return true if node's parent is top node, false otherwise
		 *
		 * @see #isTopNode()
		 */
		public boolean isRoot() {
			return !isTopNode() && parent.isTopNode();
		}

		/**
		 * Node is considered top node if its element is null
		 *
		 * @return true if node's element is null, false otherwise
		 */
		private boolean isTopNode() {
			return element == null;
		}

		/**
		 * Removes the specified child from this node.
		 *
		 * @param child
		 *            the child to be removed
		 */
		public void removeChild(Node<T> child) {
			children.remove(child);
		}

		/**
		 * Sets the parent node of this node and updates the child relations
		 * accordingly.
		 *
		 * @param parent
		 *            the new parent node
		 */
		public void setParent(Node<T> parent) {
			if (this.parent != null) {
				this.parent.removeChild(this);
			}
			this.parent = parent;
			parent.addChild(this);
		}
	}

	/** Element-node mapping to retrieve the node for an element */
	private final Map<T, Node<T>> nodes = new HashMap<>();
	/** Virtual root node of the tree, practically ignored */
	private final Node<T> top = Node.constructTopNode();

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
		Node<T> nodeT = find(t);
		Node<T> nodeU = find(u);
		return !(nodeT == null || nodeU == null) && nodeT.equals(nodeU);
	}

	/**
	 * Returns the number of disjoint components.
	 *
	 * @return the number of components
	 */
	public int count() {
		return top.getChildren().size();
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
	private Node<T> find(T t) {
		Node<T> node = nodes.get(t);
		if (node == null) {
			return null;
		}
		while (!node.isRoot()) {
			// path compression by halving
			Node<T> p = node.getParent();
			if (!p.isRoot()) {
				node.setParent(p.getParent());
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
		Stack<Node<T>> todo = new Stack<>();
		Node<T> node = find(t);
		if (node == null) {
			return component;
		}
		todo.add(node);
		while (!todo.isEmpty()) {
			Node<T> elem = todo.pop();
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
		return top.getChildren().stream().map(Node::getElement).collect(Collectors.toSet());
	}

	/**
	 * Add a new element to the Union find by creating a new tree having exactly
	 * one node.
	 *
	 * @param t
	 *            element to be inserted
	 */
	private void insert(T t) {
		Node<T> node = new Node<>(t);
		node.setParent(top);
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
		Node<T> rootT = find(t);
		Node<T> rootU = find(u);
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