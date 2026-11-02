/**
 * File: NavigationBST.java
 * Author: Sanskriti Tiwari
 * Course: CS3345 Data Structures & Algorithm Analysis
 * Description: Implements an unbalanced Binary Search Tree (BST) for location indexing.
 * Handles insertion, search, deletion across all three cases (leaf, one child, two children),
 * and in-order traversal display without balancing.
 */
public class NavigationBST implements LocationIndex {

    private static class Node {
        Location data;
        Node left, right;

        Node(Location data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    private Node root;
    private int count;

    public NavigationBST() {
        this.root = null;
        this.count = 0;
    }

    @Override
    public synchronized boolean insert(Location location) {
        if (location == null) return false;
        int oldSize = count;
        root = insertRec(root, location);
        return count > oldSize;
    }

    private Node insertRec(Node current, Location location) {
        if (current == null) {
            count++;
            return new Node(location);
        }
        int cmp = Integer.compare(location.getLocationID(), current.data.getLocationID());
        if (cmp < 0) {
            current.left = insertRec(current.left, location);
        } else if (cmp > 0) {
            current.right = insertRec(current.right, location);
        }
        return current;
    }

    @Override
    public synchronized Location search(int locationID) {
        Node curr = root;
        while (curr != null) {
            int cmp = Integer.compare(locationID, curr.data.getLocationID());
            if (cmp < 0) {
                curr = curr.left;
            } else if (cmp > 0) {
                curr = curr.right;
            } else {
                return curr.data;
            }
        }
        return null;
    }

    @Override
    public synchronized boolean remove(int locationID) {
        int oldSize = count;
        root = deleteRec(root, locationID);
        return count < oldSize;
    }

    private Node deleteRec(Node current, int locationID) {
        if (current == null) return null;

        int cmp = Integer.compare(locationID, current.data.getLocationID());
        if (cmp < 0) {
            current.left = deleteRec(current.left, locationID);
        } else if (cmp > 0) {
            current.right = deleteRec(current.right, locationID);
        } else {
            count--;
            // Case 1 & 2: Leaf or one child
            if (current.left == null) return current.right;
            if (current.right == null) return current.left;

            // Case 3: Two children - find successor
            Node successor = findMin(current.right);
            current.data = successor.data;
            current.right = deleteRec(current.right, successor.data.getLocationID());
            count++; 
        }
        return current;
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public void display() {
        if (root == null || count == 0) {
            System.out.println("No locations available.");
            return;
        }
        inOrderDisplay(root);
    }

    private void inOrderDisplay(Node node) {
        if (node != null) {
            inOrderDisplay(node.left);
            System.out.println(node.data);
            inOrderDisplay(node.right);
        }
    }
}