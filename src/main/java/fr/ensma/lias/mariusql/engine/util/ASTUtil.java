/*********************************************************************************
 * This file is part of MariusQL Project.
 * Copyright (C) 2014  LIAS - ENSMA
 *   Teleport 2 - 1 avenue Clement Ader
 *   BP 40109 - 86961 Futuroscope Chasseneuil Cedex - FRANCE
 * 
 * MariusQL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MariusQL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with MariusQL.  If not, see <http://www.gnu.org/licenses/>.
 **********************************************************************************/
package fr.ensma.lias.mariusql.engine.util;

import java.util.ArrayList;
import java.util.List;

import antlr.ASTFactory;
import antlr.collections.AST;
import antlr.collections.impl.ASTArray;

/**
 * Provides utility methods for AST traversal and manipulation.
 * 
 * @author Mickael BARON
 * @author Stéphane JEAN
 */
public final class ASTUtil {

	/**
	 * Private empty constructor. (or else checkstyle says: 'warning: Utility
	 * classes should not have a public or default constructor.')
	 */
	private ASTUtil() {
	}

	/**
	 * Creates a single node AST.
	 * 
	 * @param astFactory The factory.
	 * @param type       The node type.
	 * @param text       The node text.
	 * @return AST - A single node tree.
	 */
	public static AST create(final ASTFactory astFactory, final int type, final String text) {
		AST node = astFactory.create(type, text);
		return node;
	}

	/**
	 * Creates a single node AST as a sibling.
	 * 
	 * @param astFactory  The factory.
	 * @param type        The node type.
	 * @param text        The node text.
	 * @param prevSibling The previous sibling.
	 * @return AST - A single node tree.
	 */
	public static AST createSibling(final ASTFactory astFactory, final int type, final String text,
			final AST prevSibling) {
		AST node = astFactory.create(type, text);
		node.setNextSibling(prevSibling.getNextSibling());
		prevSibling.setNextSibling(node);
		return node;
	}

	/**
	 * Insert a single node AST.
	 * 
	 * @param node
	 * @param prevSibling
	 * @return
	 */
	public static AST insertSibling(final AST node, final AST prevSibling) {
		node.setNextSibling(prevSibling.getNextSibling());
		prevSibling.setNextSibling(node);
		return node;
	}

	/**
	 * Creates a 'binary operator' subtree, given the information about the parent
	 * and the two child nodex.
	 * 
	 * @param factory    The AST factory.
	 * @param parentType The type of the parent node.
	 * @param parentText The text of the parent node.
	 * @param child1     The first child.
	 * @param child2     The second child.
	 * @return AST - A new sub-tree of the form "(parent child1 child2)"
	 */
	public static AST createBinarySubtree(ASTFactory factory, int parentType, String parentText, AST child1,
			AST child2) {
		ASTArray array = createAstArray(factory, 3, parentType, parentText, child1);
		array.add(child2);
		return factory.make(array);
	}

	/**
	 * Creates a single parent of the specified child (i.e. a 'unary operator'
	 * subtree).
	 * 
	 * @param factory    The AST factory.
	 * @param parentType The type of the parent node.
	 * @param parentText The text of the parent node.
	 * @param child      The child.
	 * @return AST - A new sub-tree of the form "(parent child)"
	 */
	public static AST createParent(ASTFactory factory, int parentType, String parentText, AST child) {
		ASTArray array = createAstArray(factory, 2, parentType, parentText, child);
		return factory.make(array);
	}

	/**
	 * Create a tree.
	 * 
	 * @param factory        factory of node
	 * @param nestedChildren nested children
	 * @return the built tree
	 */
	public static AST createTree(ASTFactory factory, AST[] nestedChildren) {
		AST[] array = new AST[2];
		int limit = nestedChildren.length - 1;
		for (int i = limit; i >= 0; i--) {
			if (i != limit) {
				array[1] = nestedChildren[i + 1];
				array[0] = nestedChildren[i];
				factory.make(array);
			}
		}
		return array[0];
	}

	/**
	 * Finds the first node of the specified type in the chain of children.
	 * 
	 * @param parent The parent
	 * @param type   The type to find.
	 * @return The first node of the specified type, or null if not found.
	 */
	public static AST findTypeInChildren(AST parent, int type) {
		AST n = parent.getFirstChild();
		while (n != null && n.getType() != type)
			n = n.getNextSibling();
		return n;
	}

	/**
	 * Finds all nodes of the specified type in the chain of children.
	 * 
	 * @param parent The parent
	 * @param type   The type to find.
	 * @return List af all the node of the specified type.
	 */
	public static List<AST> findAllTypeInChildren(final AST parent, final int type) {
		List<AST> res = new ArrayList<AST>();
		AST n = parent.getFirstChild();
		while (n != null) {
			if (n.getType() == type) {
				res.add(n);
			}
			n = n.getNextSibling();
		}
		return res;
	}

	/**
	 * Returns the last direct child of 'n'.
	 * 
	 * @param n The parent
	 * @return The last direct child of 'n'.
	 */
	public static AST getLastChild(final AST n) {
		return getLastSibling(n.getFirstChild());
	}

	/**
	 * Returns the last sibling of 'a'.
	 * 
	 * @param a The sibling.
	 * @return The last sibling of 'a'.
	 */
	private static AST getLastSibling(AST a) {
		AST last = null;
		while (a != null) {
			last = a;
			a = a.getNextSibling();
		}
		return last;
	}

	/**
	 * Returns the 'list' representation with some brackets around it for debugging.
	 * 
	 * @param n The tree.
	 * @return The list representation of the tree.
	 */
	public static String getDebugString(AST n) {
		StringBuilder buf = new StringBuilder();
		buf.append("[ ");
		buf.append((n == null) ? "{null}" : n.toStringTree());
		buf.append(" ]");
		return buf.toString();
	}

	/**
	 * Find the previous sibling in the parent for the given child.
	 * 
	 * @param parent the parent node
	 * @param child  the child to find the previous sibling of
	 * @return the previous sibling of the child
	 */
	public static AST findPreviousSibling(AST parent, AST child) {
		AST prev = null;
		AST n = parent.getFirstChild();
		while (n != null) {
			if (n.equals(child)) { // Fix problem n == child
				return prev;
			}
			prev = n;
			n = n.getNextSibling();
		}
		throw new IllegalArgumentException("Child not found in parent!");
	}

	/**
	 * Makes the child node a sibling of the parent, reconnecting all siblings.
	 * 
	 * @param parent the parent
	 * @param child  the child
	 */
	public static void makeSiblingOfParent(final AST parent, final AST child) {
		AST prev = findPreviousSibling(parent, child);
		if (prev != null) {
			prev.setNextSibling(child.getNextSibling());
		} else { // child == parent.getFirstChild()
			parent.setFirstChild(child.getNextSibling());
		}
		child.setNextSibling(parent.getNextSibling());
		parent.setNextSibling(child);
	}

	/**
	 * Get the string representing this node and its sibling.
	 * 
	 * @param n the node
	 * @return the string representing this node and its sibling
	 */
	public static String getPathText(final AST n) {
		StringBuilder buf = new StringBuilder();
		getPathText(buf, n);
		return buf.toString();
	}

	/**
	 * @param buf buffer
	 * @param n   node
	 */
	private static void getPathText(final StringBuilder buf, final AST n) {
		AST firstChild = n.getFirstChild();
		// If the node has a first child, recurse into the first child.
		if (firstChild != null) {
			getPathText(buf, firstChild);
		}
		// Append the text of the current node.
		buf.append(n.getText());
		// If there is a second child (RHS), recurse into that child.
		if (firstChild != null && firstChild.getNextSibling() != null) {
			getPathText(buf, firstChild.getNextSibling());
		}
	}

	public static boolean hasExactlyOneChild(final AST n) {
		return n != null && n.getFirstChild() != null && n.getFirstChild().getNextSibling() == null;
	}

	/**
	 * @param n to this node
	 * @param s next node
	 */
	public static void appendSibling(AST n, final AST s) {
		while (n.getNextSibling() != null) {
			n = n.getNextSibling();
		}
		n.setNextSibling(s);
	}

	/**
	 * Inserts the child as the first child of the parent, all other children are
	 * shifted over to the 'right'.
	 * 
	 * @param parent the parent
	 * @param child  the new first child
	 */
	public static void insertChild(final AST parent, final AST child) {
		if (parent.getFirstChild() == null) {
			parent.setFirstChild(child);
		} else {
			AST n = parent.getFirstChild();
			parent.setFirstChild(child);
			child.setNextSibling(n);
		}
	}

	/**
	 * Remove the child of the parent.
	 * 
	 * @param parent the parent
	 * @param child  the child
	 */
	public static void removeChild(final AST parent, final AST child) {
		if (parent.getFirstChild().equals(child)) {
			parent.setFirstChild(child.getNextSibling());
		} else {
			AST previousSiblingOfChild = ASTUtil.findPreviousSibling(parent, child);
			previousSiblingOfChild.setNextSibling(child.getNextSibling());
		}
	}

	/**
	 * Filters nodes out of a tree.
	 */
	public static interface FilterPredicate {
		/**
		 * Returns true if the node should be filtered out.
		 * 
		 * @param n The node.
		 * @return true if the node should be filtered out, false to keep the node.
		 */
		boolean exclude(AST n);
	}

	/**
	 * A predicate that uses inclusion, rather than exclusion semantics.
	 */
	public abstract static class IncludePredicate implements FilterPredicate {
		public final boolean exclude(AST node) {
			return !include(node);
		}

		public abstract boolean include(AST node);
	}

	/**
	 * @param factory    factory
	 * @param size       size
	 * @param parentType parentType
	 * @param parentText parentText
	 * @param child1     child1
	 * @return the AstArray
	 */
	private static ASTArray createAstArray(final ASTFactory factory, final int size, final int parentType,
			final String parentText, final AST child1) {
		ASTArray array = new ASTArray(size);
		array.add(factory.create(parentType, parentText));
		array.add(child1);
		return array;
	}

	/**
	 * @param root      root node
	 * @param predicate predicate
	 * @return the list or children
	 */
	public static List<AST> collectChildren(final AST root, final FilterPredicate predicate) {
		List<AST> children = new ArrayList<AST>();
		collectChildren(children, root, predicate);
		return children;
	}

	/**
	 * @param children  children
	 * @param root      root
	 * @param predicate predicate
	 */
	private static void collectChildren(final List<AST> children, final AST root, final FilterPredicate predicate) {
		for (AST n = root.getFirstChild(); n != null; n = n.getNextSibling()) {
			if (predicate == null || !predicate.exclude(n)) {
				children.add(n);
			}
			collectChildren(children, n, predicate);
		}
	}

	/**
	 * @param node node
	 * @return the direct children of this node
	 */
	public static List<AST> collectDirectChildren(final AST node) {
		List<AST> children = new ArrayList<AST>();
		for (AST n = node.getFirstChild(); n != null; n = n.getNextSibling()) {
			children.add(n);
		}
		return children;
	}

	/**
	 * @param node node
	 * @return the direct children of this node
	 */
	public static <T extends AST> List<T> collectDirectChildren(final AST node, Class<T> classType) {
		List<T> children = new ArrayList<T>();
		for (AST n = node.getFirstChild(); n != null; n = n.getNextSibling()) {
			if (classType.isInstance(n)) {
				children.add(classType.cast(n));
			}
		}
		return children;
	}
}
