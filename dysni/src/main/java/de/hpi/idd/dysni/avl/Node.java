package de.hpi.idd.dysni.avl;

import java.util.ArrayList;
import java.util.Collection;

/**
 * this class implements Braided AVL trees nodes.
 * <p>
 * Braided AVL tree nodes implement all the logical structure of the tree. Nodes
 * are created by the {@link BraidedAVLTree} class.
 * </p>
 * <p>
 * The nodes are not independent from each other but must obey specific
 * balancing constraints and the tree structure is rearranged as elements are
 * inserted or deleted from the tree. The creation, modification and
 * tree-related navigation methods have therefore restricted access. Only the
 * order-related navigation, reading and delete methods are public.
 * </p>
 *
 * @see BraidedAVLTree
 */
public class Node<K extends Comparable<K>, V> {

	/** Enum for tree skew factor. */
	enum Skew {
		/** Code for Skew.BALANCED trees. */
		BALANCED,
		/** Code for left high trees. */
		LEFT_HIGH,
		/** Code for right high trees. */
		RIGHT_HIGH
	}

	/** Elements contained in the current node. */
	private final Collection<V> elements;
	private final K key;
	/** Left sub-tree. */
	private Node<K, V> left;
	/** Next node */
	private Node<K, V> next;
	/** Parent tree. */
	private Node<K, V> parent;
	/** Previous node */
	private Node<K, V> prev;
	/** Right sub-tree. */
	private Node<K, V> right;
	/** Skew factor. */
	private Skew skew;
	/** The tree the node belongs to. Needed to keep track of root of tree */
	private final BraidedAVLTree<K, V> tree;

	/**
	 * Build a node for a specified element.
	 *
	 * @param key
	 *            key
	 * @param element
	 *            element
	 * @param tree
	 *            the tree the node belongs to
	 */
	Node(BraidedAVLTree<K, V> tree, K key, V element) {
		this.tree = tree;
		if (key == null) {
			throw new NullPointerException("Key must not be null");
		}
		elements = new ArrayList<>();
		this.key = key;
		if (element != null) {
			elements.add(element);
		}
		left = null;
		right = null;
		parent = null;
		skew = Skew.BALANCED;
		prev = null;
		next = null;
	}

	/**
	 * Checks whether a node contains an element.
	 *
	 * @param element
	 *            to look for
	 * @return true if node contains element
	 */
	public boolean contains(V element) {
		return elements.contains(element);
	}

	/**
	 * Delete the element from the tree. If the node is empty afterwards, the
	 * node is deleted from the tree
	 *
	 * @param element
	 *            element to delete
	 *
	 */
	void delete(V element) {
		elements.remove(element);
		if (elements.isEmpty()) {
			if (parent == null && left == null && right == null) {
				// this was the last node, the tree is now empty
				tree.root = null;
			} else {
				final Node<K, V> start;
				Node<K, V> child;
				boolean leftShrunk;
				if (left == null && right == null) {
					start = parent;
					leftShrunk = start.isLeft(this);
					child = null;
				} else {
					final Node<K, V> node;
					if (left == null) {
						node = next;
						child = node.right;
					} else {
						node = prev;
						child = node.left;
					}
					if (node != left) {
						node.setLeft(left);
					}
					if (node != right) {
						node.setRight(right);
					}
					start = node.parent;
					leftShrunk = start.isLeft(node);
					exchange(node);
				}
				if (prev != null) {
					prev.setNext(next);
				} else {
					next.setPrev(prev);
				}
				if (leftShrunk) {
					start.setLeft(child);
				} else {
					start.setRight(child);
				}
				Node<K, V> node = start;
				Node<K, V> nextParent = node.parent;
				while (leftShrunk ? node.rebalanceLeftShrunk() : node.rebalanceRightShrunk()) {
					if (nextParent == null) {
						return;
					}
					leftShrunk = nextParent.isLeft(start);
					node = nextParent;
					nextParent = node.parent;
				}
			}
		}
	}

	/**
	 * Place a node at the position where this node currently is. May update the
	 * trees root.
	 *
	 * @param node
	 *            the new node to be placed
	 */
	private void exchange(Node<K, V> node) {
		if (parent != null) {
			if (parent.isLeft(this)) {
				parent.setLeft(node);
			} else if (parent.isRight(this)) {
				parent.setRight(node);
			} else {
				throw new IllegalStateException("Node is neither left nor right node of parent");
			}
		} else {
			node.parent = null;
			tree.root = node;
		}
	}

	/**
	 * Retrieve the elements stored in the node.
	 *
	 * @return all elements contained by the node
	 */
	public Collection<V> getElements() {
		return elements;
	}

	/**
	 * Get the key of the node.
	 *
	 * @return key
	 */
	public K getKey() {
		return key;
	}

	/**
	 * Get the node whose key is the largest one in the tree rooted at this
	 * node.
	 *
	 * @return the tree node having the largest key in the tree rooted at this
	 *         node or null if the tree is empty
	 * @see #getSmallest
	 */
	Node<K, V> getLargest() {
		Node<K, V> node = this;
		while (node.right != null) {
			node = node.right;
		}
		return node;
	}

	/**
	 * Get the left sub-tree of this node
	 *
	 * @return left sub-tree
	 */
	Node<K, V> getLeft() {
		return left;
	}

	/**
	 * Get the in-order next node
	 *
	 * @return next element of this node
	 */
	public Node<K, V> getNext() {
		return next;
	}

	/**
	 * Get the in-order previous node
	 *
	 * @return previous element of this node
	 */
	public Node<K, V> getPrevious() {
		return prev;
	}

	/**
	 * Get the right sub-tree of this node
	 *
	 * @return right sub-tree
	 */
	Node<K, V> getRight() {
		return right;
	}

	/**
	 * Get the node whose key is the smallest one in the tree rooted at this
	 * node.
	 *
	 * @return the tree node having the smallest key in the tree rooted at this
	 *         node or null if the tree is empty
	 * @see #getLargest
	 */
	Node<K, V> getSmallest() {
		Node<K, V> node = this;
		while (node.left != null) {
			node = node.left;
		}
		return node;
	}

	/**
	 * Insert an element in a sub-tree.
	 *
	 * @param key
	 *            sorting key of the element
	 * @param element
	 *            element to insert
	 * @return true if the parent tree should be re-Skew.BALANCED
	 */
	Node<K, V> insert(K key, V element) {
		final Node<K, V> newNode;
		if (key.compareTo(this.key) < 0) {
			// the inserted element is smaller than the node
			if (left == null) {
				newNode = new Node<>(tree, key, element);
				setLeft(newNode);
				left.setPrev(prev);
				setPrev(left);
				Node<K, V> node = this;
				Node<K, V> nextParent = parent;
				boolean isLeft = true;
				boolean needsRebalance = true;
				while (needsRebalance) {
					needsRebalance = isLeft ? node.rebalanceLeftGrown() : node.rebalanceRightGrown();
					if (nextParent == null) {
						break;
					}
					isLeft = nextParent.isLeft(node);
					node = nextParent;
					nextParent = node.parent;
				}
				return newNode;
			}
			return left.insert(key, element);
		}
		if (key.compareTo(this.key) == 0) {
			elements.add(element);
			return this;
		}
		// the inserted element is greater than the node
		if (right == null) {
			newNode = new Node<>(tree, key, element);
			setRight(newNode);
			right.setNext(next);
			setNext(right);
			Node<K, V> node = this;
			Node<K, V> nextParent = parent;
			boolean isLeft = false;
			boolean needsRebalance = true;
			while (needsRebalance) {
				needsRebalance = isLeft ? node.rebalanceLeftGrown() : node.rebalanceRightGrown();
				if (nextParent == null) {
					break;
				}
				isLeft = nextParent.isLeft(node);
				node = nextParent;
				nextParent = node.parent;
			}
			return newNode;
		}
		return right.insert(key, element);
	}

	/**
	 * Check whether a node is the left child of this node.
	 *
	 * @param node
	 *            The potential left child.
	 * @return true if the node is this node's left child, false otherwise.
	 */
	private boolean isLeft(Node<K, V> node) {
		return left == node;
	}

	/**
	 * Check whether a node is the right child of this node.
	 *
	 * @param node
	 *            The potential right child.
	 * @return true if the node is this node's right child, false otherwise.
	 */
	private boolean isRight(Node<K, V> node) {
		return right == node;
	}

	/**
	 * Get the number of nodes of the tree rooted at this node.
	 *
	 * @return number of nodes contained in the tree rooted at node
	 */
	int nodes() {
		return 1 + (left == null ? 0 : left.nodes()) + (right == null ? 0 : right.nodes());
	}

	/**
	 * Print the tree to the console.
	 */
	public void print() {
		TreePrinter.printNode(this);
	}

	/**
	 * Re-balance the instance as left sub-tree has grown.
	 *
	 * @return true if the parent tree should be reSkew.BALANCED too
	 */
	private boolean rebalanceLeftGrown() {
		Node<K, V> node = this;
		switch (skew) {
		case LEFT_HIGH:
			if (left.skew == Skew.LEFT_HIGH) {
				node = rotateCW();
				node.skew = Skew.BALANCED;
				node.right.skew = Skew.BALANCED;
			} else {
				Skew s = left.right.skew;
				left.rotateCCW();
				node = rotateCW();
				switch (s) {
				case LEFT_HIGH:
					node.left.skew = Skew.BALANCED;
					node.right.skew = Skew.RIGHT_HIGH;
					break;
				case RIGHT_HIGH:
					node.left.skew = Skew.LEFT_HIGH;
					node.right.skew = Skew.BALANCED;
					break;
				default:
					node.left.skew = Skew.BALANCED;
					node.right.skew = Skew.BALANCED;
				}
				node.skew = Skew.BALANCED;
			}
			return false;
		case RIGHT_HIGH:
			node.skew = Skew.BALANCED;
			return false;
		default:
			node.skew = Skew.LEFT_HIGH;
			return true;
		}
	}

	/**
	 * Re-balance the instance as left sub-tree has shrunk.
	 *
	 * @return true if the parent tree should be reSkew.BALANCED too
	 */
	private boolean rebalanceLeftShrunk() {
		Node<K, V> node = this;
		switch (skew) {
		case LEFT_HIGH:
			node.skew = Skew.BALANCED;
			return true;
		case RIGHT_HIGH:
			if (right.skew == Skew.RIGHT_HIGH) {
				node = rotateCCW();
				node.skew = Skew.BALANCED;
				node.left.skew = Skew.BALANCED;
				return true;
			} else if (right.skew == Skew.BALANCED) {
				node = rotateCCW();
				node.skew = Skew.LEFT_HIGH;
				node.left.skew = Skew.RIGHT_HIGH;
				return false;
			} else {
				Skew s = right.left.skew;
				right.rotateCW();
				node = rotateCCW();
				switch (s) {
				case LEFT_HIGH:
					node.left.skew = Skew.BALANCED;
					node.right.skew = Skew.RIGHT_HIGH;
					break;
				case RIGHT_HIGH:
					node.left.skew = Skew.LEFT_HIGH;
					node.right.skew = Skew.BALANCED;
					break;
				default:
					node.left.skew = Skew.BALANCED;
					node.right.skew = Skew.BALANCED;
				}
				node.skew = Skew.BALANCED;
				return true;
			}
		default:
			node.skew = Skew.RIGHT_HIGH;
			return false;
		}
	}

	/**
	 * Re-balance the instance as right sub-tree has grown.
	 *
	 * @return true if the parent tree should be reSkew.BALANCED too
	 */
	private boolean rebalanceRightGrown() {
		Node<K, V> node = this;
		switch (skew) {
		case LEFT_HIGH:
			node.skew = Skew.BALANCED;
			return false;
		case RIGHT_HIGH:
			if (right.skew == Skew.RIGHT_HIGH) {
				node = rotateCCW();
				node.skew = Skew.BALANCED;
				node.left.skew = Skew.BALANCED;
			} else {
				Skew s = right.left.skew;
				right.rotateCW();
				node = rotateCCW();
				switch (s) {
				case LEFT_HIGH:
					node.left.skew = Skew.BALANCED;
					node.right.skew = Skew.RIGHT_HIGH;
					break;
				case RIGHT_HIGH:
					node.left.skew = Skew.LEFT_HIGH;
					node.right.skew = Skew.BALANCED;
					break;
				default:
					node.left.skew = Skew.BALANCED;
					node.right.skew = Skew.BALANCED;
				}
				node.skew = Skew.BALANCED;
			}
			return false;
		default:
			node.skew = Skew.RIGHT_HIGH;
			return true;
		}
	}

	/**
	 * Re-balance the instance as right sub-tree has shrunk.
	 *
	 * @return true if the parent tree should be reSkew.BALANCED too
	 */
	private boolean rebalanceRightShrunk() {
		Node<K, V> node = this;
		switch (skew) {
		case RIGHT_HIGH:
			node.skew = Skew.BALANCED;
			return true;
		case LEFT_HIGH:
			if (left.skew == Skew.LEFT_HIGH) {
				node = rotateCW();
				node.skew = Skew.BALANCED;
				node.right.skew = Skew.BALANCED;
				return true;
			} else if (left.skew == Skew.BALANCED) {
				node = rotateCW();
				node.skew = Skew.RIGHT_HIGH;
				node.right.skew = Skew.LEFT_HIGH;
				return false;
			} else {
				Skew s = left.right.skew;
				left.rotateCCW();
				node = rotateCW();
				switch (s) {
				case LEFT_HIGH:
					node.left.skew = Skew.BALANCED;
					node.right.skew = Skew.RIGHT_HIGH;
					break;
				case RIGHT_HIGH:
					node.left.skew = Skew.LEFT_HIGH;
					node.right.skew = Skew.BALANCED;
					break;
				default:
					node.left.skew = Skew.BALANCED;
					node.right.skew = Skew.BALANCED;
				}
				node.skew = Skew.BALANCED;
				return true;
			}
		default:
			node.skew = Skew.LEFT_HIGH;
			return false;
		}
	}

	/**
	 * Perform a counter-clockwise rotation rooted at the instance.
	 * <p>
	 * The skew factor are not updated by this method, they <em>must</em> be
	 * updated by the caller
	 * </p>
	 *
	 * @return Node that is now located at this position in the tree.
	 */
	private Node<K, V> rotateCCW() {
		final Node<K, V> tmpRight = right;
		exchange(tmpRight);
		setRight(tmpRight.left);
		tmpRight.setLeft(this);
		return tmpRight;
	}

	/**
	 * Perform a clockwise rotation rooted at the instance.
	 * <p>
	 * The skew factor are not updated by this method, they <em>must</em> be
	 * updated by the caller
	 * </p>
	 *
	 * @return Node that is now located at this position in the tree.
	 */
	private Node<K, V> rotateCW() {
		final Node<K, V> tmpLeft = left;
		exchange(tmpLeft);
		setLeft(tmpLeft.right);
		tmpLeft.setRight(this);
		return tmpLeft;
	}

	/**
	 * Set the left child of this node and update the new child's parent
	 *
	 * @param left
	 *            new child node
	 */
	private void setLeft(Node<K, V> left) {
		this.left = left;
		if (left != null) {
			left.parent = this;
		}
	}

	/**
	 * Set the next in-order node and update the pointers accordingly
	 *
	 * @param next
	 *            next in-order node
	 */
	private void setNext(Node<K, V> next) {
		this.next = next;
		if (this.next != null) {
			this.next.prev = this;
		}
	}

	/**
	 * Set the previous in-order node and update the pointers accordingly
	 *
	 * @param prev
	 *            previous in-order node
	 */
	private void setPrev(Node<K, V> prev) {
		this.prev = prev;
		if (this.prev != null) {
			this.prev.next = this;
		}
	}

	/**
	 * Set the right child of this node and update the new child's parent
	 *
	 * @param right
	 *            new child node
	 */
	private void setRight(Node<K, V> right) {
		this.right = right;
		if (right != null) {
			right.parent = this;
		}
	}

	/**
	 * Get the number of elements of the tree rooted at this node.
	 *
	 * @return number of elements contained in the tree rooted at node
	 */
	int size() {
		return elements.size() + (left == null ? 0 : left.size()) + (right == null ? 0 : right.size());
	}

	@Override
	public String toString() {
		return key.toString();
	}
}