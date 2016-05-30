package de.hpi.idd.dysni.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Based on the implementation by Robert Sedgewick and Kevin Wayne from
 * Princeton University
 *
 * <p>
 * For additional documentation, see
 * <a href="http://algs4.cs.princeton.edu/15uf">Section 1.5</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class UnionFind<T> {

	private class Node<U> {

		private final Collection<Node<U>> children = new HashSet<>();
		private final U element;
		private Node<U> parent;
		private byte rank;

		public Node(U element) {
			this.element = element;
		}

		private void addChild(Node<U> child) {
			children.add(child);
		}

		public Collection<Node<U>> getChildren() {
			return children;
		}

		public U getElement() {
			return element;
		}

		public Node<U> getParent() {
			return parent;
		}

		public byte getRank() {
			return rank;
		}

		public void increaseRank() {
			rank++;
		}

		private void removeChild(Node<U> child) {
			children.remove(child);
		}

		public void setParent(Node<U> parent) {
			if (this.parent != null) {
				this.parent.removeChild(this);
			} else {
				roots.remove(this);
			}
			this.parent = parent;
			parent.addChild(this);
		}
	}

	private final Map<T, Node<T>> nodes = new HashMap<>();
	private final Set<Node<T>> roots = new HashSet<>();

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
	 * Returns the number of components.
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
	 *            the integer representing one site
	 * @return the component identifier for the component containing site
	 *         <tt>t</tt>
	 */
	private Node<T> find(T t) {
		Node<T> node = nodes.get(t);
		if (node == null) {
			return null;
		}
		while (node.getParent() != null) {
			// path compression by halving
			Node<T> pp = node.getParent().getParent();
			if (pp != null) {
				node.setParent(pp);
			}
			node = node.getParent();
		}
		return node;
	}

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

	public Set<T> getRoots() {
		return roots.stream().map(Node::getElement).collect(Collectors.toSet());
	}

	private void insert(T t) {
		Node<T> node = new Node<>(t);
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