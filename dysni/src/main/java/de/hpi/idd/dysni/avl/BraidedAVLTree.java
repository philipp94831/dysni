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

import java.util.Iterator;

/**
 * This class implements AVL trees.
 *
 * <p>
 * Based on the implementation by <a href=
 * "https://commons.apache.org/proper/commons-math/jacoco/org.apache.commons.math3.geometry.partitioning.utilities/AVLTree.java.html">
 * Apache</a>
 * </p>
 *
 * <p>
 * This data structure is basically an AVL Tree where nodes have pointers to
 * their in-order neighbors such that neighbor access is possible in constant
 * time. Furthermore, this tree allows only one node per key but stores the
 * elements per node in a list such that multiple elements can be associated
 * with a node.
 * </p>
 *
 * <p>
 * The idea for this data structure is based on the
 * <a href="http://www.stephenvrice.com/images/AVL_SCS.pdf">work</a> of Stephen
 * V. Rice
 * </p>
 *
 * @param <K>
 *            the type of the key of the elements
 * @param <V>
 *            the type of the elements
 */
public class BraidedAVLTree<K extends Comparable<K>, V> implements Iterable<Node<K, V>> {

	private static class AVLTreeIterator<K extends Comparable<K>, V> implements Iterator<Node<K, V>> {

		private Node<K, V> next;

		public AVLTreeIterator(Node<K, V> smallest) {
			next = smallest;
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public Node<K, V> next() {
			if (next == null) {
				throw new IllegalStateException("There is no next element");
			}
			Node<K, V> ret = next;
			next = next.getNext();
			return ret;
		}
	}

	/** Top level node. */
	private Node<K, V> top;

	/**
	 * Delete an element from the tree.
	 * <p>
	 * The element is deleted only if there is a node {@code n} containing
	 * exactly the element instance specified, i.e. for which
	 * {@code n.contains(element)}. This is purposely <em>different</em> from
	 * the specification of the {@code java.util.Set} {@code remove} method (in
	 * fact, this is the reason why a specific class has been developed).
	 * </p>
	 *
	 * @param element
	 *            element to delete (silently ignored if null)
	 * @return true if the element was deleted from the tree
	 */
	public boolean delete(K key, V element) {
		if (element != null) {
			Node<K, V> node = find(key);
			if (node == null) {
				return false;
			}
			if (node.contains(element)) {
				if (node.delete(element)) {
					top = null;
				}
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * Find the node with the specified key
	 *
	 * @param key
	 *            the key which's node should be retrieved
	 * @return node with the specified key, null if no node is found
	 */
	public Node<K, V> find(K key) {
		for (Node<K, V> node = top; node != null;) {
			if (node.getKey().compareTo(key) < 0) {
				if (node.getRight() == null) {
					return null;
				}
				node = node.getRight();
			} else if (node.getKey().compareTo(key) > 0) {
				if (node.getLeft() == null) {
					return null;
				}
				node = node.getLeft();
			} else {
				return node;
			}
		}
		return null;
	}

	/**
	 * Get the node whose key is the largest one in the tree.
	 *
	 * @return the tree node having the largest key in the tree or null if the
	 *         tree is empty
	 * @see #getSmallest
	 * @see Node#getPrevious
	 * @see Node#getNext
	 */
	public Node<K, V> getLargest() {
		return top == null ? null : top.getLargest();
	}

	Node<K, V> getRoot() {
		return top;
	}

	/**
	 * Get the node whose key is the smallest one in the tree.
	 *
	 * @return the tree node having the smallest key in the tree or null if the
	 *         tree is empty
	 * @see #getLargest
	 * @see Node#getPrevious
	 * @see Node#getNext
	 */
	public Node<K, V> getSmallest() {
		return top == null ? null : top.getSmallest();
	}

	/**
	 * Insert an element in the tree.
	 *
	 * @param key
	 *            the element's sorting key (silently ignored if null)
	 * @param element
	 *            element to insert (silently ignored if null)
	 */
	public void insert(K key, V element) {
		if (key != null || element != null) {
			if (top == null) {
				top = new Node<>(key, element);
			} else {
				top.insert(key, element);
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

	/**
	 * @return iterator to iterate over the tree's nodes in-order.
	 */
	@Override
	public Iterator<Node<K, V>> iterator() {
		return new AVLTreeIterator<>(getSmallest());
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
