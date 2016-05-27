package de.hpi.idd.dysni.avl;

import java.util.List;

/**
 * This class implements AVL trees nodes.
 * <p>
 * AVL tree nodes implement all the logical structure of the tree. Nodes are
 * created by the {@link AVLTree AVLTree} class.
 * </p>
 * <p>
 * The nodes are not independant from each other but must obey specific
 * balancing constraints and the tree structure is rearranged as elements are
 * inserted or deleted from the tree. The creation, modification and
 * tree-related navigation methods have therefore restricted access. Only the
 * order-related navigation, reading and delete methods are public.
 * </p>
 *
 * @see AVLTree
 */
public class Node<K extends Comparable<K>, V extends HasKey<K>> {

	/** Elements contained in the current node. */
	private Container<K, V> container;
	private K key;
	/** Left sub-tree. */
	private Node<K, V> left;
	private boolean needsRebalance;
	private Node<K, V> next;
	/** Parent tree. */
	private Node<K, V> parent;
	private Node<K, V> prev;
	/** Right sub-tree. */
	private Node<K, V> right;
	/** Skew factor. */
	private Skew skew;
	private final KeyComparator<K> comp;

	/**
	 * Build a node for a specified element.
	 *
	 * @param element
	 *            element
	 * @param parent
	 *            parent node
	 */
	Node(final K key, KeyComparator<K> comp) {
		setContainer(new Container<>(comp));
		this.key = key;
		this.comp = comp;
		left = null;
		right = null;
		parent = null;
		skew = Skew.BALANCED;
		prev = null;
		next = null;
	}

	List<V> add(final V newElement) {
		return container.add(newElement);
	}

	/**
	 * Delete the element from the tree. If the node is empty afterwards, the
	 * node is deleted from the tree
	 *
	 * @param element
	 * 
	 * @return true if the deleted element was the root of the tree, false
	 *         otherwise
	 */
	public boolean delete(final V element) {
		container.remove(element);
		if (container.isEmpty()) {
			if (parent == null && left == null && right == null) {
				// this was the last node, the tree is now empty
				container = null;
				return true;
			} else {
				Node<K, V> node;
				Node<K, V> child;
				boolean leftShrunk;
				if (left == null && right == null) {
					node = this;
					container = null;
					prev.setNext(next);
					leftShrunk = node == node.parent.left;
					child = null;
				} else {
					node = left != null ? prev : next;
					setContainer(node.container);
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
	 * Get the contained elements.
	 *
	 * @return elements contained in the node
	 */
	public Container<K, V> getContainer() {
		return container;
	}

	public K getKey() {
		return key;
	}

	/**
	 * Get the node whose element is the largest one in the tree rooted at this
	 * node.
	 *
	 * @return the tree node containing the largest element in the tree rooted
	 *         at this node or null if the tree is empty
	 * @see #getSmallest
	 */
	Node<K, V> getLargest() {
		Node<K, V> node = this;
		while (node.right != null) {
			node = node.right;
		}
		return node;
	}

	public Node<K, V> getLeft() {
		return left;
	}

	public Node<K, V> getNext() {
		return next;
	}

	public Node<K, V> getPrevious() {
		return prev;
	}

	public Node<K, V> getRight() {
		return right;
	}

	/**
	 * Get the node whose element is the smallest one in the tree rooted at this
	 * node.
	 *
	 * @return the tree node containing the smallest element in the tree rooted
	 *         at this node or null if the tree is empty
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
	 * @param newElement
	 *            element to insert
	 * @return true if the parent tree should be re-Skew.BALANCED
	 */
	List<V> insert(final V newElement) {
		if (newElement.getKey().compareTo(this.key) < 0) {
			// the inserted element is smaller than the node
			if (left == null) {
				setLeft(new Node<K, V>(newElement.getKey(), comp));
				left.setPrev(prev);
				setPrev(left);
				needsRebalance = rebalanceLeftGrown();
				return left.add(newElement);
			}
			List<V> candidates = left.insert(newElement);
			needsRebalance = left.needsRebalance ? rebalanceLeftGrown() : false;
			return candidates;
		}
		if (newElement.getKey().compareTo(this.key) == 0) {
			List<V> candidates = add(newElement);
			needsRebalance = false;
			return candidates;
		}
		// the inserted element is greater than the node
		if (right == null) {
			setRight(new Node<K, V>(newElement.getKey(), comp));
			right.setNext(next);
			setNext(right);
			needsRebalance = rebalanceRightGrown();
			return right.add(newElement);
		}
		List<V> candidates = right.insert(newElement);
		needsRebalance = right.needsRebalance ? rebalanceRightGrown() : false;
		return candidates;
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
				final Skew s = left.right.skew;
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
				final Skew s = right.left.skew;
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
				final Skew s = right.left.skew;
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
				final Skew s = left.right.skew;
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
		final Container<K, V> tmpElt = container;
		final K tmpSkv = key;
		final Node<K, V> tmpNext = next == right ? this : next;
		final Node<K, V> tmpPrev = prev;
		final Node<K, V> tmpRightNext = right.next;
		final Node<K, V> tmpRightPrev = right.prev == this ? right : right.prev;
		setContainer(right.container);
		key = right.key;
		setNext(tmpRightNext);
		setPrev(tmpRightPrev);
		right.setContainer(tmpElt);
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
		final Container<K, V> tmpElt = container;
		final K tmpSkv = key;
		final Node<K, V> tmpNext = next;
		final Node<K, V> tmpPrev = prev == left ? this : prev;
		final Node<K, V> tmpLeftNext = left.next == this ? left : left.next;
		final Node<K, V> tmpLeftPrev = left.prev;
		setContainer(left.container);
		key = left.key;
		setPrev(tmpLeftPrev);
		setNext(tmpLeftNext);
		left.setContainer(tmpElt);
		left.key = tmpSkv;
		left.setNext(tmpNext);
		left.setPrev(tmpPrev);
		final Node<K, V> tmpNode = left;
		setLeft(tmpNode.left);
		tmpNode.setLeft(tmpNode.right);
		tmpNode.setRight(right);
		setRight(tmpNode);
	}

	private void setContainer(Container<K, V> container) {
		this.container = container;
		container.setNode(this);
	}

	private void setLeft(Node<K, V> child) {
		left = child;
		if (child != null) {
			child.parent = this;
		}
	}

	private void setNext(final Node<K, V> next) {
		this.next = next;
		if (this.next != null) {
			this.next.prev = this;
		}
	}

	private void setPrev(final Node<K, V> prev) {
		this.prev = prev;
		if (this.prev != null) {
			this.prev.next = this;
		}
	}

	private void setRight(Node<K, V> child) {
		right = child;
		if (child != null) {
			child.parent = this;
		}
	}

	/**
	 * Get the number of elements of the tree rooted at this node.
	 *
	 * @return number of elements contained in the tree rooted at this node
	 */
	int size() {
		return 1 + (left == null ? 0 : left.size()) + (right == null ? 0 : right.size());
	}

	@Override
	public String toString() {
		return key.toString();
	}
}