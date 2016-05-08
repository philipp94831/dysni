package de.hpi.idd.dysni.avl;

import java.util.ArrayList;
import java.util.List;

import de.hpi.idd.dysni.avl.AVLTree.Skew;

/**
 * This class implements AVL trees nodes.
 * <p>
 * AVL tree nodes implement all the logical structure of the tree. Nodes are
 * created by the {@link AVLTree AVLTree} class.
 * </p>
 * <p>
 * The nodes are not independant from each other but must obey specific
 * balancing constraints and the tree structure is rearranged as elements
 * are inserted or deleted from the tree. The creation, modification and
 * tree-related navigation methods have therefore restricted access. Only
 * the order-related navigation, reading and delete methods are public.
 * </p>
 *
 * @see AVLTree
 */
public class Node<U extends Comparable<U>, T extends Element<U>> {

	/** Elements contained in the current node. */
	List<T> elements = new ArrayList<>();
	/** Left sub-tree. */
	Node<U, T> left;
	private Node<U, T> next;
	/** Parent tree. */
	private Node<U, T> parent;
	private Node<U, T> prev;
	/** Right sub-tree. */
	Node<U, T> right;
	/** Skew factor. */
	private AVLTree.Skew skew;
	U skv;

	/**
	 * Build a node for a specified element.
	 *
	 * @param element
	 *            element
	 * @param parent
	 *            parent node
	 */
	Node(final T element, final Node<U, T> parent) {
		elements.add(element);
		skv = element.getSKV();
		left = null;
		right = null;
		this.parent = parent;
		skew = Skew.BALANCED;
		prev = null;
		next = null;
	}

	/**
	 * Delete the element from the tree. If the node is empty afterwards, the node is deleted from the tree
	 *
	 * @param element
	 * 
	 * @return true if the deleted element was the root of the tree, false otherwise 
	 */
	public boolean delete(final T element) {
		elements.remove(element);
		if (elements.isEmpty()) {
			if (parent == null && left == null && right == null) {
				// this was the last node, the tree is now empty
				elements = null;
				return true;
			} else {
				Node<U, T> node;
				Node<U, T> child;
				boolean leftShrunk;
				if (left == null && right == null) {
					node = this;
					elements = null;
					prev.setNext(next);
					leftShrunk = node == node.parent.left;
					child = null;
				} else {
					node = left != null ? left.getLargest() : right.getSmallest();
					elements = node.elements;
					skv = node.skv;
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
					node.left = child;
				} else {
					node.right = child;
				}
				if (child != null) {
					child.parent = node;
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
	public List<T> getElements() {
		return elements;
	}

	/**
	 * Get the node whose element is the largest one in the tree rooted at
	 * this node.
	 *
	 * @return the tree node containing the largest element in the tree
	 *         rooted at this node or null if the tree is empty
	 * @see #getSmallest
	 */
	Node<U, T> getLargest() {
		Node<U, T> node = this;
		while (node.right != null) {
			node = node.right;
		}
		return node;
	}

	public Node<U, T> getNext() {
		return next;
	}

	public Node<U, T> getPrevious() {
		return prev;
	}

	public U getSKV() {
		return skv;
	}

	/**
	 * Get the node whose element is the smallest one in the tree rooted at
	 * this node.
	 *
	 * @return the tree node containing the smallest element in the tree
	 *         rooted at this node or null if the tree is empty
	 * @see #getLargest
	 */
	Node<U, T> getSmallest() {
		Node<U, T> node = this;
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
	boolean insert(final T newElement) {
		if (newElement.getSKV().compareTo(this.skv) < 0) {
			// the inserted element is smaller than the node
			if (left == null) {
				left = new Node<U, T>(newElement, this);
				left.setPrev(prev);
				setPrev(left);
				return rebalanceLeftGrown();
			}
			return left.insert(newElement) ? rebalanceLeftGrown() : false;
		}
		if (newElement.getSKV().compareTo(this.skv) == 0) {
			elements.add(newElement);
			return false;
		}
		// the inserted element is greater than the node
		if (right == null) {
			right = new Node<U, T>(newElement, this);
			right.setNext(next);
			setNext(right);
			return rebalanceRightGrown();
		}
		return right.insert(newElement) ? rebalanceRightGrown() : false;
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
				final AVLTree.Skew s = left.right.skew;
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
				final AVLTree.Skew s = right.left.skew;
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
				final AVLTree.Skew s = right.left.skew;
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
				final AVLTree.Skew s = left.right.skew;
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
		final List<T> tmpElt = elements;
		final U tmpSkv = skv;
		final Node<U, T> tmpNext = next == right? this : next;
		final Node<U, T> tmpPrev = prev;
		final Node<U, T> tmpRightNext = right.next;
		final Node<U, T> tmpRightPrev = right.prev == this? right : right.prev;
		elements = right.elements;
		skv = right.skv;
		setNext(tmpRightNext);
		setPrev(tmpRightPrev);
		right.elements = tmpElt;
		right.skv = tmpSkv;
		right.setPrev(tmpPrev);
		right.setNext(tmpNext);
		final Node<U, T> tmpNode = right;
		right = tmpNode.right;
		tmpNode.right = tmpNode.left;
		tmpNode.left = left;
		left = tmpNode;
		if (right != null) {
			right.parent = this;
		}
		if (left.left != null) {
			left.left.parent = left;
		}
	}

	/**
	 * Perform a clockwise rotation rooted at the instance.
	 * <p>
	 * The skew factor are not updated by this method, they <em>must</em> be
	 * updated by the caller
	 * </p>
	 */
	private void rotateCW() {
		final List<T> tmpElt = elements;
		final U tmpSkv = skv;
		final Node<U, T> tmpNext = next;
		final Node<U, T> tmpPrev = prev == left? this : prev;
		final Node<U, T> tmpLeftNext = left.next == this? left : left.next;
		final Node<U, T> tmpLeftPrev = left.prev;
		elements = left.elements;
		skv = left.skv;
		setPrev(tmpLeftPrev);
		setNext(tmpLeftNext);
		left.elements = tmpElt;
		left.skv = tmpSkv;
		left.setNext(tmpNext);
		left.setPrev(tmpPrev);
		final Node<U, T> tmpNode = left;
		left = tmpNode.left;
		tmpNode.left = tmpNode.right;
		tmpNode.right = right;
		right = tmpNode;
		if (left != null) {
			left.parent = this;
		}
		if (right.right != null) {
			right.right.parent = right;
		}
	}

	private void setPrev(final Node<U, T> prev) {
		this.prev = prev;
		if (this.prev != null) {
			this.prev.next = this;
		}
	}

	private void setNext(final Node<U, T> next) {
		this.next = next;
		if (this.next != null) {
			this.next.prev = this;
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
		return skv.toString();
	}
}