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
	private Collection<V> elements;
	private K key;
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

	/**
	 * Build a node for a specified element.
	 *
	 * @param key
	 *            key
	 * @param element
	 *            element
	 */
	Node(K key, V element) {
		if (key == null || element == null) {
			throw new NullPointerException("Key and element must not be null");
		}
		elements = new ArrayList<>();
		this.key = key;
		elements.add(element);
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
	 * @return true if the deleted element was the root of the tree, false
	 *         otherwise
	 */
	boolean delete(V element) {
		elements.remove(element);
		if (elements.isEmpty()) {
			if (parent == null && left == null && right == null) {
				// this was the last node, the tree is now empty
				elements = null;
				return true;
			} else {
				Node<K, V> node;
				Node<K, V> child;
				boolean leftShrunk;
				if (left == null && right == null) {
					node = this;
					elements = null;
					prev.setNext(next);
					leftShrunk = node == node.parent.left;
					child = null;
				} else {
					node = left != null ? prev : next;
					elements = node.elements;
					key = node.key;
					if (left != null) {
						setPrev(node.prev);
					} else {
						setNext(node.next);
					}
					leftShrunk = node == node.parent.left;
					child = node.left != null ? node.left : node.right;
				}
				node = node.parent;
				if (leftShrunk) {
					node.setLeft(child);
				} else {
					node.setRight(child);
				}
				while (leftShrunk ? node.rebalanceLeftShrunk() : node.rebalanceRightShrunk()) {
					if (node.parent == null) {
						return false;
					}
					leftShrunk = node == node.parent.left;
					node = node.parent;
				}
			}
		}
		return false;
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
	public Node<K, V> getLeft() {
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
	public Node<K, V> getRight() {
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
	boolean insert(K key, V element) {
		if (key.compareTo(this.key) < 0) {
			// the inserted element is smaller than the node
			if (left == null) {
				setLeft(new Node<>(key, element));
				left.setPrev(prev);
				setPrev(left);
				return rebalanceLeftGrown();
			}
			return left.insert(key, element) && rebalanceLeftGrown();
		}
		if (key.compareTo(this.key) == 0) {
			elements.add(element);
			return false;
		}
		// the inserted element is greater than the node
		if (right == null) {
			setRight(new Node<>(key, element));
			right.setNext(next);
			setNext(right);
			return rebalanceRightGrown();
		}
		return right.insert(key, element) && rebalanceRightGrown();
	}

	/**
	 * Re-balance the instance as left sub-tree has grown.
	 *
	 * @return true if the parent tree should be reSkew.BALANCED too
	 */
	private boolean rebalanceLeftGrown() {
		switch (skew) {
		case LEFT_HIGH:
			if (left.skew == Skew.LEFT_HIGH) {
				rotateCW();
				skew = Skew.BALANCED;
				right.skew = Skew.BALANCED;
			} else {
				Skew s = left.right.skew;
				left.rotateCCW();
				rotateCW();
				switch (s) {
				case LEFT_HIGH:
					left.skew = Skew.BALANCED;
					right.skew = Skew.RIGHT_HIGH;
					break;
				case RIGHT_HIGH:
					left.skew = Skew.LEFT_HIGH;
					right.skew = Skew.BALANCED;
					break;
				default:
					left.skew = Skew.BALANCED;
					right.skew = Skew.BALANCED;
				}
				skew = Skew.BALANCED;
			}
			return false;
		case RIGHT_HIGH:
			skew = Skew.BALANCED;
			return false;
		default:
			skew = Skew.LEFT_HIGH;
			return true;
		}
	}

	/**
	 * Re-balance the instance as left sub-tree has shrunk.
	 *
	 * @return true if the parent tree should be reSkew.BALANCED too
	 */
	private boolean rebalanceLeftShrunk() {
		switch (skew) {
		case LEFT_HIGH:
			skew = Skew.BALANCED;
			return true;
		case RIGHT_HIGH:
			if (right.skew == Skew.RIGHT_HIGH) {
				rotateCCW();
				skew = Skew.BALANCED;
				left.skew = Skew.BALANCED;
				return true;
			} else if (right.skew == Skew.BALANCED) {
				rotateCCW();
				skew = Skew.LEFT_HIGH;
				left.skew = Skew.RIGHT_HIGH;
				return false;
			} else {
				Skew s = right.left.skew;
				right.rotateCW();
				rotateCCW();
				switch (s) {
				case LEFT_HIGH:
					left.skew = Skew.BALANCED;
					right.skew = Skew.RIGHT_HIGH;
					break;
				case RIGHT_HIGH:
					left.skew = Skew.LEFT_HIGH;
					right.skew = Skew.BALANCED;
					break;
				default:
					left.skew = Skew.BALANCED;
					right.skew = Skew.BALANCED;
				}
				skew = Skew.BALANCED;
				return true;
			}
		default:
			skew = Skew.RIGHT_HIGH;
			return false;
		}
	}

	/**
	 * Re-balance the instance as right sub-tree has grown.
	 *
	 * @return true if the parent tree should be reSkew.BALANCED too
	 */
	private boolean rebalanceRightGrown() {
		switch (skew) {
		case LEFT_HIGH:
			skew = Skew.BALANCED;
			return false;
		case RIGHT_HIGH:
			if (right.skew == Skew.RIGHT_HIGH) {
				rotateCCW();
				skew = Skew.BALANCED;
				left.skew = Skew.BALANCED;
			} else {
				Skew s = right.left.skew;
				right.rotateCW();
				rotateCCW();
				switch (s) {
				case LEFT_HIGH:
					left.skew = Skew.BALANCED;
					right.skew = Skew.RIGHT_HIGH;
					break;
				case RIGHT_HIGH:
					left.skew = Skew.LEFT_HIGH;
					right.skew = Skew.BALANCED;
					break;
				default:
					left.skew = Skew.BALANCED;
					right.skew = Skew.BALANCED;
				}
				skew = Skew.BALANCED;
			}
			return false;
		default:
			skew = Skew.RIGHT_HIGH;
			return true;
		}
	}

	/**
	 * Re-balance the instance as right sub-tree has shrunk.
	 *
	 * @return true if the parent tree should be reSkew.BALANCED too
	 */
	private boolean rebalanceRightShrunk() {
		switch (skew) {
		case RIGHT_HIGH:
			skew = Skew.BALANCED;
			return true;
		case LEFT_HIGH:
			if (left.skew == Skew.LEFT_HIGH) {
				rotateCW();
				skew = Skew.BALANCED;
				right.skew = Skew.BALANCED;
				return true;
			} else if (left.skew == Skew.BALANCED) {
				rotateCW();
				skew = Skew.RIGHT_HIGH;
				right.skew = Skew.LEFT_HIGH;
				return false;
			} else {
				Skew s = left.right.skew;
				left.rotateCCW();
				rotateCW();
				switch (s) {
				case LEFT_HIGH:
					left.skew = Skew.BALANCED;
					right.skew = Skew.RIGHT_HIGH;
					break;
				case RIGHT_HIGH:
					left.skew = Skew.LEFT_HIGH;
					right.skew = Skew.BALANCED;
					break;
				default:
					left.skew = Skew.BALANCED;
					right.skew = Skew.BALANCED;
				}
				skew = Skew.BALANCED;
				return true;
			}
		default:
			skew = Skew.LEFT_HIGH;
			return false;
		}
	}

	/**
	 * Perform a counter-clockwise rotation rooted at the instance.
	 * <p>
	 * The skew factor are not updated by this method, they <em>must</em> be
	 * updated by the caller
	 * </p>
	 */
	private void rotateCCW() {
		final Collection<V> tmpElt = elements;
		final K tmpSkv = key;
		final Node<K, V> tmpNext = next == right ? this : next;
		final Node<K, V> tmpPrev = prev;
		final Node<K, V> tmpRightNext = right.next;
		final Node<K, V> tmpRightPrev = right.prev == this ? right : right.prev;
		elements = right.elements;
		key = right.key;
		setNext(tmpRightNext);
		setPrev(tmpRightPrev);
		right.elements = tmpElt;
		right.key = tmpSkv;
		right.setPrev(tmpPrev);
		right.setNext(tmpNext);
		final Node<K, V> tmpNode = right;
		setRight(tmpNode.right);
		tmpNode.setRight(tmpNode.left);
		tmpNode.setLeft(left);
		setLeft(tmpNode);
	}

	/**
	 * Perform a clockwise rotation rooted at the instance.
	 * <p>
	 * The skew factor are not updated by this method, they <em>must</em> be
	 * updated by the caller
	 * </p>
	 */
	private void rotateCW() {
		final Collection<V> tmpElt = elements;
		final K tmpSkv = key;
		final Node<K, V> tmpNext = next;
		final Node<K, V> tmpPrev = prev == left ? this : prev;
		final Node<K, V> tmpLeftNext = left.next == this ? left : left.next;
		final Node<K, V> tmpLeftPrev = left.prev;
		elements = left.elements;
		key = left.key;
		setPrev(tmpLeftPrev);
		setNext(tmpLeftNext);
		left.elements = tmpElt;
		left.key = tmpSkv;
		left.setNext(tmpNext);
		left.setPrev(tmpPrev);
		final Node<K, V> tmpNode = left;
		setLeft(tmpNode.left);
		tmpNode.setLeft(tmpNode.right);
		tmpNode.setRight(right);
		setRight(tmpNode);
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