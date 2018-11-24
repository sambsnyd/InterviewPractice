package com.github.sambsnyd.datastructures;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SBinaryTree<T> {

    private T value;
    private SBinaryTree<T> left;
    private SBinaryTree<T> right;

    public SBinaryTree(T value) {
        this(null, value, null);
    }

    public SBinaryTree(SBinaryTree<T> left, T value, SBinaryTree<T> right) {
        Preconditions.checkNotNull(value);
        this.left = left;
        this.value = value;
        this.right = right;
    }

    public int getHeight() {
        int leftHeight = 0;
        if(left != null) {
            leftHeight = left.getHeight();
        }
        int rightHeight = 0;
        if(right != null) {
            rightHeight = right.getHeight();
        }
        return 1 + Math.max(leftHeight, rightHeight);
    }

    public boolean isBalanced() {
        int leftHeight = 0;
        boolean leftBalanced = true;
        if(left != null) {
            leftHeight = left.getHeight();
            leftBalanced = left.isBalanced();
        }
        int rightHeight = 0;
        boolean rightBalanced = true;
        if(right != null) {
            rightHeight = right.getHeight();
            rightBalanced = right.isBalanced();
        }
        return Math.abs(leftHeight - rightHeight) <= 1 && leftBalanced && rightBalanced;
    }

    public T breadthFirstSearchRecursive(Predicate<T> searchFun) {
        if(searchFun.test(value)) {
            return value;
        }
        return breadthFirstSearchRecursive(searchFun, List.of(left, right));
    }
    private static <T> T breadthFirstSearchRecursive(Predicate<T> searchFun, List<SBinaryTree<T>> nodes ) {
        var withoutNulls = nodes.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if(withoutNulls.size() == 0) {
            return null;
        }

        return withoutNulls.stream()
                .filter(it -> searchFun.test(it.value))
                .map(it -> it.value)
                .findFirst()
                .orElse(breadthFirstSearchRecursive(searchFun,
                                withoutNulls.stream()
                                        .flatMap(it -> Stream.of(it.left, it.right))
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toList())));
    }

    public T depthFirstSearchRecursive(Predicate<T> searchFun) {
        if(searchFun.test(value)) {
            return value;
        }
        if(left != null) {
            T maybeFound = left.depthFirstSearchRecursive(searchFun);
            if(maybeFound != null) {
                return maybeFound;
            }
        }
        if(right != null) {
            T maybeFound = right.depthFirstSearchRecursive(searchFun);
            if(maybeFound != null) {
                return maybeFound;
            }
        }
        return null;
    }

    /**
     * Traverses left -> root -> right
     *     (1)
     *     / \
     *   (2) (3)
     *   / \
     * (4) (5)
     *
     * Would return [4,2,5,1,3]
     * So a BST would return its elements in smallest->largest (ascending) order
     */
    public List<T> traverseInorder() {
        return traverseInorder(new ArrayList<>(), this);
    }
    private static <T> List<T> traverseInorder(List<T> aggregator, SBinaryTree<T> tree) {
        if(tree.left != null) {
            aggregator.addAll(traverseInorder(new ArrayList<>(), tree.left));
        }
        aggregator.add(tree.value);
        if(tree.right != null) {
            aggregator.addAll(traverseInorder(new ArrayList<>(), tree.right));
        }
        return aggregator;
    }
    private static <T> Stream<T> traverseInorderS(Stream<T> aggregator, SBinaryTree<T> tree) {
        if(tree.left != null) {
            aggregator = Stream.concat(aggregator, traverseInorderS(Stream.of(), tree.left));
        }
        aggregator = Stream.concat(aggregator, Stream.of(tree.value));
        if(tree.right != null) {
            aggregator = Stream.concat(aggregator, traverseInorderS(Stream.of(), tree.right));
        }
        return aggregator;
    }

    /**
     * Traverses root -> left-> right
     *     (1)
     *     / \
     *   (2) (3)
     *   / \
     * (4) (5)
     *
     * Would return [1,2,3,4,5]
     * A BST would return its elements in non-decreasing order
     * Effectively a breadth-first traversal
     */
    public List<T> traversePreorder() {
        return traverseInorder(new ArrayList<>(), this);
    }
    private static <T> List<T> traversePreorder(List<T> aggregator, SBinaryTree<T> tree) {
        aggregator.add(tree.value);
        if(tree.left != null) {
            aggregator.addAll(traversePreorder(new ArrayList<>(), tree.left));
        }
        if(tree.right != null) {
            aggregator.addAll(traversePreorder(new ArrayList<>(), tree.right));
        }
        return aggregator;
    }

    // Setters and getters
    public SBinaryTree<T> getLeft() {
        return left;
    }

    public void setLeft(SBinaryTree<T> left) {
        this.left = left;
    }
    public void setLeft(T left) {
        setLeft(new SBinaryTree<>(left));
    }

    public SBinaryTree<T> getRight() {
        return right;
    }
    public void setRight(T right) {
        setRight(new SBinaryTree<>(right));
    }

    public void setRight(SBinaryTree<T> right) {
        this.right = right;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        Preconditions.checkNotNull(value);
        this.value = value;
    }
}
