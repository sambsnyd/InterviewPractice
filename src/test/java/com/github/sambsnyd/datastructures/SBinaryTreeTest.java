package com.github.sambsnyd.datastructures;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SBinaryTreeTest {

    @Test
    void testGetHeight() {
        var tree = new SBinaryTree<>( 1);
        Assertions.assertEquals(1, tree.getHeight());

        tree.setLeft(2);
        Assertions.assertEquals(2, tree.getHeight());

        tree.setRight(3);
        Assertions.assertEquals(2, tree.getHeight());

        tree.getLeft().setLeft(4);
        Assertions.assertEquals(3, tree.getHeight());
    }

    @Test
    void testIsBalanced() {
        var tree = new SBinaryTree<>(1);
        // (1)
        Assertions.assertTrue(tree.isBalanced(), "A single-element tree is balanced by definition");

        tree.setLeft(2);
        //    (1)
        //   /
        // (2)
        Assertions.assertTrue(tree.isBalanced(),
                "The left side being a single element deeper than the right is considered balanced");

        //     (1)
        //     /
        //   (2)
        //   /
        // (3)
        tree.getLeft().setLeft(3);
        Assertions.assertFalse(tree.isBalanced(),
                "The left side being two elements deeper than the right side is not balanced");

        //     (1)
        //     / \
        //   (2) (4)
        //   /
        // (3)
        tree.setRight(4);
        Assertions.assertTrue(tree.isBalanced(),
                "The left side is only one element deeper so it is balanced");

        //       (1)
        //       /  \
        //     (2)  (4)
        //     / \    \
        //   (3) (5)  (6)
        //   /          \
        // (7)          (8)
        tree.getLeft().setRight(5);
        tree.getRight().setRight(6);
        tree.getLeft().getLeft().setLeft(7);
        tree.getRight().getRight().setRight(8);
        Assertions.assertFalse(tree.isBalanced(), "Left and right sides are same height but right is unbalanced");
    }

    @Test
    void testBfsRecursive() {
        //      (1)
        //     /   \
        //   (3)    (2)
        //   /
        // (4)
        var tree = new SBinaryTree<>(1);
        tree.setRight(2);
        tree.setLeft(3);
        tree.getLeft().setLeft(4);

        var notFound = tree.breadthFirstSearchRecursive(it -> it == 5);
        Assertions.assertNull(notFound, "5 isn't in the tree so a search for it should return null");

        var match = tree.breadthFirstSearchRecursive(it -> it % 2 == 0);
        Assertions.assertNotNull(match, "Should find a matching item");
        Assertions.assertEquals(2, (int)match,
                "Should find the number '2' because of the breadth first traversal");
    }

    @Test
    void testDfsRecursive() {
        //      (1)
        //     /   \
        //   (3)    (2)
        //   /
        // (4)
        var tree = new SBinaryTree<>(1);
        tree.setRight(2);
        tree.setLeft(3);
        tree.getLeft().setLeft(4);

        var notFound = tree.depthFirstSearchRecursive(it -> it == 5);
        Assertions.assertNull(notFound, "5 isn't in the tree so a search for it should return null");

        var match = tree.depthFirstSearchRecursive(it -> it % 2 == 0);
        Assertions.assertNotNull(match, "Should find a matching item");
        Assertions.assertEquals(4, (int)match,
                "Should find the number '4' because of the depth first traversal");
    }

    @Test
    void traverseInorder() {
        //       (1)
        //     /    \
        //   (3)     (2)
        //   / \     /
        // (4) (5) (6)
        var tree = new SBinaryTree<>(1);
        tree.setRight(2);
        tree.setLeft(3);
        tree.getLeft().setLeft(4);
        tree.getLeft().setRight(5);
        tree.getRight().setLeft(6);

        var traversed = tree.traverseInorder();
        Assertions.assertIterableEquals(List.of(4,3,5,1,6,2), traversed);
    }
}
