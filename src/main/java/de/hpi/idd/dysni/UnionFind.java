package de.hpi.idd.dysni;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;

/******************************************************************************
 *  Compilation:  javac UF.java
 *  Execution:    java UF < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/15uf/tinyUF.txt
 *                http://algs4.cs.princeton.edu/15uf/mediumUF.txt
 *                http://algs4.cs.princeton.edu/15uf/largeUF.txt
 *
 *  Weighted quick-union by rank with path compression by halving.
 *
 *  % java UF < tinyUF.txt
 *  4 3
 *  3 8
 *  6 5
 *  9 4
 *  2 1
 *  5 0
 *  7 2
 *  6 1
 *  2 components
 *
 ******************************************************************************/

/**
 * The <tt>UF</tt> class represents a <em>union-find data type</em> (also known
 * as the <em>disjoint-sets data type</em>). It supports the <em>union</em> and
 * <em>find</em> operations, along with a <em>connected</em> operation for
 * determining whether two sites are in the same component and a <em>count</em>
 * operation that returns the total number of components.
 * <p>
 * The union-find data type models connectivity among a set of <em>N</em> sites,
 * named 0 through <em>N</em> &ndash; 1. The <em>is-connected-to</em> relation
 * must be an <em>equivalence relation</em>:
 * <ul>
 * <p>
 * <li><em>Reflexive</em>: <em>p</em> is connected to <em>p</em>.
 * <p>
 * <li><em>Symmetric</em>: If <em>p</em> is connected to <em>q</em>, then
 * <em>q</em> is connected to <em>p</em>.
 * <p>
 * <li><em>Transitive</em>: If <em>p</em> is connected to <em>q</em> and
 * <em>q</em> is connected to <em>r</em>, then <em>p</em> is connected to
 * <em>r</em>.
 * </ul>
 * <p>
 * An equivalence relation partitions the sites into
 * <em>equivalence classes</em> (or <em>components</em>). In this case, two
 * sites are in the same component if and only if they are connected. Both sites
 * and components are identified with integers between 0 and <em>N</em> &ndash;
 * 1. Initially, there are <em>N</em> components, with each site in its own
 * component. The <em>component identifier</em> of a component (also known as
 * the <em>root</em>, <em>canonical element</em>, <em>leader</em>, or
 * <em>set representative</em>) is one of the sites in the component: two sites
 * have the same component identifier if and only if they are in the same
 * component.
 * <ul>
 * <p>
 * <li><em>union</em>(<em>p</em>, <em>q</em>) adds a connection between the two
 * sites <em>p</em> and <em>q</em>. If <em>p</em> and <em>q</em> are in
 * different components, then it replaces these two components with a new
 * component that is the union of the two.
 * <p>
 * <li><em>find</em>(<em>p</em>) returns the component identifier of the
 * component containing <em>p</em>.
 * <p>
 * <li><em>connected</em>(<em>p</em>, <em>q</em>) returns true if both
 * <em>p</em> and <em>q</em> are in the same component, and false otherwise.
 * <p>
 * <li><em>count</em>() returns the number of components.
 * </ul>
 * <p>
 * The component identifier of a component can change only when the component
 * itself changes during a call to <em>union</em>&mdash;it cannot change during
 * a call to <em>find</em>, <em>connected</em>, or <em>count</em>.
 * <p>
 * This implementation uses weighted quick union by rank with path compression
 * by halving. Initializing a data structure with <em>N</em> sites takes linear
 * time. Afterwards, the <em>union</em>, <em>find</em>, and <em>connected</em>
 * operations take logarithmic time (in the worst case) and the <em>count</em>
 * operation takes constant time. Moreover, the amortized time per
 * <em>union</em>, <em>find</em>, and <em>connected</em> operation has inverse
 * Ackermann complexity. For alternate implementations of the same API, see
 * {@link QuickUnionUF}, {@link QuickFindUF}, and {@link WeightedQuickUnionUF}.
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

	private int count = 0; // number of components
	private Map<T, T> parent = new HashMap<>(); // parent[i] = parent of i
	private Map<T, Byte> rank = new HashMap<>(); // rank[i] = rank of subtree rooted at i (never more than 31)
	private Map<T, Collection<T>> children = new HashMap<>();

	/**
	 * Returns true if the the two sites are in the same component.
	 *
	 * @param p
	 *            the integer representing one site
	 * @param q
	 *            the integer representing the other site
	 * @return <tt>true</tt> if the two sites <tt>p</tt> and <tt>q</tt> are in
	 *         the same component; <tt>false</tt> otherwise
	 * @throws IndexOutOfBoundsException
	 *             unless both <tt>0 &le; p &lt; N</tt> and
	 *             <tt>0 &le; q &lt; N</tt>
	 */
	public boolean connected(T p, T q) {
		return find(p).equals(find(q));
	}

	/**
	 * Returns the number of components.
	 *
	 * @return the number of components (between <tt>1</tt> and <tt>N</tt>)
	 */
	public int count() {
		return count;
	}

	/**
	 * Returns the component identifier for the component containing site
	 * <tt>p</tt>.
	 *
	 * @param p
	 *            the integer representing one site
	 * @return the component identifier for the component containing site
	 *         <tt>p</tt>
	 * @throws IndexOutOfBoundsException
	 *             unless <tt>0 &le; p &lt; N</tt>
	 */
	public T find(T p) {
		while (p != getParent(p)) {
			setParent(p, getParent(getParent(p))); // path compression by halving
			p = getParent(p);
		}
		return p;
	}
	
	private T getParent(T t) {
		return parent.getOrDefault(t, t);
	}

	/**
	 * Merges the component containing site <tt>p</tt> with the the component
	 * containing site <tt>q</tt>.
	 *
	 * @param p
	 *            the integer representing one site
	 * @param q
	 *            the integer representing the other site
	 * @throws IndexOutOfBoundsException
	 *             unless both <tt>0 &le; p &lt; N</tt> and
	 *             <tt>0 &le; q &lt; N</tt>
	 */
	public void union(T p, T q) {
		if(!parent.containsKey(p)) {
			count++;
		}
		if(!parent.containsKey(q)) {
			count++;
		}
		T rootP = find(p);
		T rootQ = find(q);
		if (rootP == rootQ) {
			return;
		}
		// make root of smaller rank point to root of larger rank
		if (getRank(rootP) < getRank(rootQ)) {
			setParent(rootP, rootQ);
		} else if (getRank(rootP) > getRank(rootQ)) {
			setParent(rootQ, rootP);
		} else {
			setParent(rootQ, rootP);
			increaseRank(rootP);
		}
		count--;
	}

	private void increaseRank(T rootP) {
		rank.put(rootP, (byte) (getRank(rootP) + 1));
	}

	private void setParent(T rootP, T rootQ) {
		getChildren(getParent(rootP)).remove(rootP);
		parent.put(rootP, rootQ);
		addChild(rootQ, rootP);
	}

	private void addChild(T rootQ, T rootP) {
		getChildren(rootQ).add(rootP);		
	}
	
	public Collection<T> getComponent(T p) {
		Collection<T> component = new HashSet<>();
		Stack<T> todo = new Stack<>();
		todo.add(find(p));
		while(!todo.isEmpty()) {
			T elem = todo.pop();
			component.add(elem);
			todo.addAll(getChildren(elem));
		}
		component.remove(p);
		return component;
	}

	private Collection<T> getChildren(T parent) {
		Collection<T> c = children.get(parent);
		if(c == null) {
			c = new HashSet<>();
			children.put(parent, c);
		}
		return c;
	}

	private int getRank(T t) {
		return rank.getOrDefault(t, (byte) 0);
	}
}