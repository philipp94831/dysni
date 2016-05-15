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
 * @param <V>
 *            the type of the elements
 * @param <K>
 *            the type of the skv of the elements
 */
public class AVLTree<K extends Comparable<K>, V extends Element<K>> implements Iterable<Node<K, V>> {

	/** Top level node. */
	private Node<K, V> top;

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
	 * {@code n.getElements().contains(element)}. This is purposely
	 * <em>different</em> from the specification of the {@code java.util.Set}
	 * {@code remove} method (in fact, this is the reason why a specific class
	 * has been developed).
	 * </p>
	 *
	 * @param element
	 *            element to delete (silently ignored if null)
	 * @return true if the element was deleted from the tree
	 */
	public boolean delete(final V element) {
		if (element != null) {
			final Node<K, V> node = find(element);
			if (node.getContainer().contains(element)) {
				if (node.delete(element)) {
					top = null;
				}
				return true;
			}
			return false;
		}
		return false;
	}

	public Node<K, V> find(final V element) {
		for (Node<K, V> node = top; node != null;) {
			if (node.getKey().compareTo(element.getKey()) < 0) {
				if (node.getRight() == null) {
					return null;
				}
				node = node.getRight();
			} else if (node.getKey().compareTo(element.getKey()) > 0) {
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
	 * Get the node whose element is the largest one in the tree.
	 *
	 * @return the tree node containing the largest element in the tree or null
	 *         if the tree is empty
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
	 * Get the node whose element is the smallest one in the tree.
	 *
	 * @return the tree node containing the smallest element in the tree or null
	 *         if the tree is empty
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
	 * @param element
	 *            element to insert (silently ignored if null)
	 * @return candidates that are in the adaptive similarity window
	 */
	public List<V> insert(final V element) {
		if (element != null) {
			if (top == null) {
				top = new Node<K, V>(element.getKey(), element.getComparator());
				return top.add(element);
			} else {
				return top.insert(element);
			}
		}
		return null;
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
	public Iterator<Node<K, V>> iterator() {
		return new AVLTreeIterator<K, V>(getSmallest());
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
