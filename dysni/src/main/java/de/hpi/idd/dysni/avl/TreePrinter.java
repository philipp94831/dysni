package de.hpi.idd.dysni.avl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class to print binary tree. Based on <a href=
 * "http://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram">michal.kreuzmann</a>
 */
public class TreePrinter {

	private static <T> boolean isAllElementsNull(List<T> list) {
		for (T object : list) {
			if (object != null) {
				return false;
			}
		}

		return true;
	}

	private static <T extends Comparable<?>> int maxLevel(Node<?, ?> node) {
		if (node == null) {
			return 0;
		}

		return Math.max(TreePrinter.maxLevel(node.getLeft()), TreePrinter.maxLevel(node.getRight())) + 1;
	}

	public static void print(BraidedAVLTree<?, ?> tree) {
		printNode(tree.getRoot());
	}

	static <T extends Comparable<?>> void printNode(Node<?, ?> root) {
		int maxLevel = TreePrinter.maxLevel(root);

		printNodeInternal(Collections.singletonList(root), 1, maxLevel);
	}

	private static <T extends Comparable<?>> void printNodeInternal(List<Node<?, ?>> nodes, int level, int maxLevel) {
		if (nodes.isEmpty() || TreePrinter.isAllElementsNull(nodes)) {
			return;
		}

		int floor = maxLevel - level;
		int endgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
		int firstSpaces = (int) Math.pow(2, (floor)) - 1;
		int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

		TreePrinter.printWhitespaces(firstSpaces);

		List<Node<?, ?>> newNodes = new ArrayList<>();
		for (Node<?, ?> node : nodes) {
			if (node != null) {
				System.out.print(node);
				newNodes.add(node.getLeft());
				newNodes.add(node.getRight());
			} else {
				newNodes.add(null);
				newNodes.add(null);
				System.out.print(" ");
			}

			TreePrinter.printWhitespaces(betweenSpaces);
		}
		System.out.println("");

		for (int i = 1; i <= endgeLines; i++) {
			for (Node<?, ?> node : nodes) {
				TreePrinter.printWhitespaces(firstSpaces - i);
				if (node == null) {
					TreePrinter.printWhitespaces(endgeLines + endgeLines + i + 1);
					continue;
				}

				if (node.getLeft() != null) {
					System.out.print("/");
				} else {
					TreePrinter.printWhitespaces(1);
				}

				TreePrinter.printWhitespaces(i + i - 1);

				if (node.getRight() != null) {
					System.out.print("\\");
				} else {
					TreePrinter.printWhitespaces(1);
				}

				TreePrinter.printWhitespaces(endgeLines + endgeLines - i);
			}

			System.out.println("");
		}

		printNodeInternal(newNodes, level + 1, maxLevel);
	}

	private static void printWhitespaces(int count) {
		for (int i = 0; i < count; i++) {
			System.out.print(" ");
		}
	}

}
