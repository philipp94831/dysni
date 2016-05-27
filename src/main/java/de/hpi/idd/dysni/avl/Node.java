package de.hpi.idd.dysni.avl;

import de.hpi.idd.dysni.util.SelfGeneric;

/**
 * this class implements AVL trees nodes.
 * <p>
 * AVL tree nodes implement all the logical structure of the tree. Nodes are
 * created by the {@link AVLTree AVLTree} class.
 * </p>
 * <p>
 * The nodes are not independent from each other but must obey specific
 * balancing constraints and the tree structure is rearranged as elements are
 * inserted or deleted from the tree. The creation, modification and
 * tree-related navigation methods have therefore restricted access. Only the
 * order-related navigation, reading and delete methods are public.
 * </p>
 *
 * @see AVLTree
 */
public abstract class Node<K extends Comparable<K>, V, C extends Container<V>, N extends Node<K, V, C, N>>
		implements SelfGeneric<N> {

	/** Elements contained in the current node. */
	C container;
	K key;
	/** Left sub-tree. */
	N left;
	N next;
	/** Parent tree. */
	N parent;
	N prev;
	/** Right sub-tree. */
	N right;
	/** Skew factor. */
	Skew skew;

	/**
	 * Build a node for a specified element.
	 *
	 * @param element
	 *            element
	 * @param container
	 *            container to hold the elements
	 */
	protected Node(final K key, final V element, final C container) {
		setContainer(container);
		this.key = key;
		add(element);
		left = null;
		right = null;
		parent = null;
		skew = Skew.BALANCED;
		prev = null;
		next = null;
	}

	private void add(final V newElement) {
		container.add(newElement);
	}

	protected abstract N createNode(K key, V newElement);

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
	public boolean delete(final V element) {
		container.remove(element);
		if (container.isEmpty()) {
			if (parent == null && left == null && right == null) {
				// this was the last node, the tree is now empty
				container = null;
				return true;
			} else {
				N node;
				N child;
				boolean leftShrunk;
				if (left == null && right == null) {
					node = getThis();
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
	public C getContainer() {
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
	N getLargest() {
		N node = getThis();
		while (node.right != null) {
			node = node.right;
		}
		return node;
	}

	public N getLeft() {
		return left;
	}

	public N getNext() {
		return next;
	}

	public N getPrevious() {
		return prev;
	}

	public N getRight() {
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
	N getSmallest() {
		N node = getThis();
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
	boolean insert(final K key, final V newElement) {
		if (key.compareTo(this.key) < 0) {
			// the inserted element is smaller than the node
			if (left == null) {
				setLeft(createNode(key, newElement));
				left.setPrev(prev);
				setPrev(left);
				return rebalanceLeftGrown();
			}
			return left.insert(key, newElement) && rebalanceLeftGrown();
		}
		if (key.compareTo(this.key) == 0) {
			add(newElement);
			return false;
		}
		// the inserted element is greater than the node
		if (right == null) {
			setRight(createNode(key, newElement));
			right.setNext(next);
			setNext(right);
			return rebalanceRightGrown();
		}
		return right.insert(key, newElement) && rebalanceRightGrown();
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
	boolean rebalanceLeftShrunk() {
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
	boolean rebalanceRightGrown() {
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
	boolean rebalanceRightShrunk() {
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
	void rotateCCW() {
		final C tmpElt = container;
		final K tmpSkv = key;
		final N tmpNext = next == right ? getThis() : next;
		final N tmpPrev = prev;
		final N tmpRightNext = right.next;
		final N tmpRightPrev = right.prev == getThis() ? right : right.prev;
		setContainer(right.container);
		key = right.key;
		setNext(tmpRightNext);
		setPrev(tmpRightPrev);
		right.setContainer(tmpElt);
		right.key = tmpSkv;
		right.setPrev(tmpPrev);
		right.setNext(tmpNext);
		final N tmpNode = right;
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
	void rotateCW() {
		final C tmpElt = container;
		final K tmpSkv = key;
		final N tmpNext = next;
		final N tmpPrev = prev == left ? getThis() : prev;
		final N tmpLeftNext = left.next == getThis() ? left : left.next;
		final N tmpLeftPrev = left.prev;
		setContainer(left.container);
		key = left.key;
		setPrev(tmpLeftPrev);
		setNext(tmpLeftNext);
		left.setContainer(tmpElt);
		left.key = tmpSkv;
		left.setNext(tmpNext);
		left.setPrev(tmpPrev);
		final N tmpNode = left;
		setLeft(tmpNode.left);
		tmpNode.setLeft(tmpNode.right);
		tmpNode.setRight(right);
		setRight(tmpNode);
	}

	protected void setContainer(final C container) {
		this.container = container;
	}

	void setLeft(final N child) {
		left = child;
		if (child != null) {
			child.parent = getThis();
		}
	}

	void setNext(final N next) {
		this.next = next;
		if (getThis().next != null) {
			this.next.prev = getThis();
		}
	}

	void setPrev(final N prev) {
		this.prev = prev;
		if (this.prev != null) {
			this.prev.next = getThis();
		}
	}

	void setRight(final N child) {
		right = child;
		if (child != null) {
			child.parent = getThis();
		}
	}

	/**
	 * Get the number of elements of the tree rooted at this node.
	 *
	 * @return number of elements contained in the tree rooted at node
	 */
	int size() {
		return 1 + (left == null ? 0 : left.size()) + (right == null ? 0 : right.size());
	}

	@Override
	public String toString() {
		return key.toString();
	}
}