/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.hpi.idd.dysni.avl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class implements AVL trees.
 *
 * <p>
 * The purpose of this class is to sort elements while allowing duplicate
 * elements (i.e. such that {@code a.equals(b)} is true). The {@code SortedSet}
 * interface does not allow this, so a specific class is needed. Null elements
 * are not allowed.
 * </p>
 *
 * <p>
 * Since the {@code equals} method is not sufficient to differentiate elements,
 * the {@link #delete delete} method is implemented using the equality operator.
 * </p>
 *
 * <p>
 * In order to clearly mark the methods provided here do not have the same
 * semantics as the ones specified in the {@code SortedSet} interface, different
 * names are used ({@code add} has been replaced by {@link #insert insert} and
 * {@code remove} has been replaced by {@link #delete delete}).
 * </p>
 *
 * <p>
 * This class is based on the C implementation Georg Kraml has put in the public
 * domain. Unfortunately, his
 * <a href="www.purists.org/georg/avltree/index.html">page</a> seems not to
 * exist any more.
 * </p>
 *
 * @param <T>
 *            the type of the elements
 *
 * @since 3.0 to be out of scope of Apache Commons Math
 */
public class AVLTree<U extends Comparable<U>, T extends Element<U>> implements Iterable<AVLTree<U, T>.Node> {

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
	public class Node {

		/** Elements contained in the current node. */
		private List<T> elements = new ArrayList<>();
		/** Left sub-tree. */
		private Node left;
		private Node next;
		/** Parent tree. */
		private Node parent;
		private Node prev;
		/** Right sub-tree. */
		private Node right;
		/** Skew factor. */
		private Skew skew;
		private U skv;

		/**
		 * Build a node for a specified element.
		 *
		 * @param element
		 *            element
		 * @param parent
		 *            parent node
		 */
		Node(final T element, final Node parent) {
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
		 * Delete the node from the tree.
		 *
		 * @param element
		 */
		public void delete(final T element) {
			elements.remove(element);
			if (elements.isEmpty()) {
				if (parent == null && left == null && right == null) {
					// this was the last node, the tree is now empty
					elements = null;
					top = null;
				} else {
					Node node;
					Node child;
					boolean leftShrunk;
					if (left == null && right == null) {
						node = this;
						elements = null;
						prev.next = next;
						next.prev = prev;
						leftShrunk = node == node.parent.left;
						child = null;
					} else {
						node = left != null ? left.getLargest() : right.getSmallest();
						elements = node.elements;
						skv = node.skv;
						if (left != null) {
							prev = node.prev;
							prev.next = this;
						} else {
							next = node.next;
							next.prev = this;
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
							return;
						}
						leftShrunk = node == node.parent.left;
						node = node.parent;
					}
				}
			}
		}

		/**
		 * Get the contained element.
		 *
		 * @return element contained in the node
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
		Node getLargest() {
			Node node = this;
			while (node.right != null) {
				node = node.right;
			}
			return node;
		}

		public Node getNext() {
			return next;
		}

		public Node getPrevious() {
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
		Node getSmallest() {
			Node node = this;
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
					left = new Node(newElement, this);
					left.prev = prev;
					left.next = this;
					if (left.prev != null) {
						left.prev.next = left;
					}
					prev = left;
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
				right = new Node(newElement, this);
				right.prev = this;
				right.next = next;
				next = right;
				if (right.next != null) {
					right.next.prev = right;
				}
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
			final List<T> tmpElt = elements;
			final U tmpSkv = skv;
			final Node tmpPrev = prev;
			elements = right.elements;
			skv = right.skv;
			next = right.next;
			next.prev = this;
			prev = right;
			prev.next = this;
			right.elements = tmpElt;
			right.skv = tmpSkv;
			right.prev = tmpPrev;
			if (right.prev != null) {
				right.prev.next = right;
			}
			final Node tmpNode = right;
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
			final Node tmpNext = next;
			elements = left.elements;
			skv = left.skv;
			prev = left.prev;
			prev.next = this;
			next = left;
			next.prev = this;
			left.elements = tmpElt;
			left.skv = tmpSkv;
			left.next = tmpNext;
			if (left.next != null) {
				left.next.prev = left;
			}
			final Node tmpNode = left;
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

	/** Enum for tree skew factor. */
	private enum Skew {
		/** Code for Skew.BALANCED trees. */
		BALANCED,
		/** Code for left high trees. */
		LEFT_HIGH,
		/** Code for right high trees. */
		RIGHT_HIGH;
	}

	/** Top level node. */
	private Node top;

	/**
	 * Build an empty tree.
	 */
	public AVLTree() {
		top = null;
	}

	/**
	 * Delete an element from the tree.
	 * <p>
	 * The element is deleted only if there is a node {@code n} containing
	 * exactly the element instance specified, i.e. for which
	 * {@code n.getElement() == element}. This is purposely <em>different</em>
	 * from the specification of the {@code java.util.Set} {@code remove} method
	 * (in fact, this is the reason why a specific class has been developed).
	 * </p>
	 *
	 * @param element
	 *            element to delete (silently ignored if null)
	 * @return true if the element was deleted from the tree
	 */
	public boolean delete(final T element) {
		if (element != null) {
			final Node node = find(element);
			if (node.elements.contains(element)) {
				node.delete(element);
				return true;
			}
			return false;
		}
		return false;
	}

	public Node find(final T element) {
		for (Node node = top; node != null;) {
			if (node.skv.compareTo(element.getSKV()) < 0) {
				if (node.right == null) {
					return null;
				}
				node = node.right;
			} else if (node.skv.compareTo(element.getSKV()) > 0) {
				if (node.left == null) {
					return null;
				}
				node = node.left;
			} else {
				return node;
			}
		}
		return null;
	}

	/**
	 * Get the node whose element is the largest one in the tree.
	 *
	 * @return the tree node containing the largest element in the tree or null
	 *         if the tree is empty
	 * @see #getSmallest
	 * @see #getNotSmaller
	 * @see #getNotLarger
	 * @see Node#getPrevious
	 * @see Node#getNext
	 */
	public Node getLargest() {
		return top == null ? null : top.getLargest();
	}

	/**
	 * Get the node whose element is the smallest one in the tree.
	 *
	 * @return the tree node containing the smallest element in the tree or null
	 *         if the tree is empty
	 * @see #getLargest
	 * @see #getNotSmaller
	 * @see #getNotLarger
	 * @see Node#getPrevious
	 * @see Node#getNext
	 */
	public Node getSmallest() {
		return top == null ? null : top.getSmallest();
	}

	public Node getRoot() {
		return top;
	}

	/**
	 * Insert an element in the tree.
	 *
	 * @param element
	 *            element to insert (silently ignored if null)
	 */
	public void insert(final T element) {
		if (element != null) {
			if (top == null) {
				top = new Node(element, null);
			} else {
				top.insert(element);
			}
		}
	}

	/**
	 * Check if the tree is empty.
	 *
	 * @return true if the tree is empty
	 */
	public boolean isEmpty() {
		return top == null;
	}

	@Override
	public Iterator<AVLTree<U, T>.Node> iterator() {
		return new AVLTreeIterator<U, T>(getSmallest());
	}

	/**
	 * Get the number of elements of the tree.
	 *
	 * @return number of elements contained in the tree
	 */
	public int size() {
		return top == null ? 0 : top.size();
	}
}
