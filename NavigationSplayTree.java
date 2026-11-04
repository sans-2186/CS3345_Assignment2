/**
 * File: NavigationSplayTree.java
 * Author: Sanskriti Tiwari
 * Course: CS3345 Data Structures & Algorithm Analysis
 * Description: Implements a self-adjusting Splay Tree using Zig, Zig-Zig, and Zig-Zag
 * operations. Successful searches move the accessed node to the root.
 */
public class NavigationSplayTree implements LocationIndex {

    private static class Node {
        Location data;
        Node left, right;

        Node(Location data) {
            this.data = data;
        }
    }

    private Node root;
    private int count;

    public NavigationSplayTree() {
        this.root = null;
        this.count = 0;
    }

    private Node rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        y.right = x;
        return y;
    }

    private Node leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }

    // Executes Zig, Zig-Zig, and Zig-Zag operations to rotate a node up to the root.
    private Node splay(Node root, int key) {
        if (root == null || root.data.getLocationID() == key) return root;

        if (key < root.data.getLocationID()) {
            if (root.left == null) return root;
            if (key < root.left.data.getLocationID()) {
                root.left.left = splay(root.left.left, key);
                root = rightRotate(root);
            } else if (key > root.left.data.getLocationID()) {
                root.left.right = splay(root.left.right, key);
                if (root.left.right != null) root.left = leftRotate(root.left);
            }
            return (root.left == null) ? root : rightRotate(root);
        } else {
            if (root.right == null) return root;
            if (key < root.right.data.getLocationID()) {
                root.right.left = splay(root.right.left, key);
                if (root.right.left != null) root.right = rightRotate(root.right);
            } else if (key > root.right.data.getLocationID()) {
                root.right.right = splay(root.right.right, key);
                root = leftRotate(root);
            }
            return (root.right == null) ? root : leftRotate(root);
        }
    }

    @Override
    public synchronized boolean insert(Location location) {
        if (location == null) return false;
        int id = location.getLocationID();
        if (root == null) {
            root = new Node(location);
            count++;
            return true;
        }

        root = splay(root, id);
        int cmp = Integer.compare(id, root.data.getLocationID());
        if (cmp == 0) return false;

        Node newNode = new Node(location);
        if (cmp < 0) {
            newNode.right = root;
            newNode.left = root.left;
            root.left = null;
        } else {
            newNode.left = root;
            newNode.right = root.right;
            root.right = null;
        }
        root = newNode;
        count++;
        return true;
    }

    // Calls the splay operation so that successful searches automatically pull the location to the root
    @Override
    public synchronized Location search(int locationID) {
        if (root == null) return null;
        root = splay(root, locationID);
        if (root.data.getLocationID() == locationID) return root.data;
        return null;
    }

    @Override
    public synchronized boolean remove(int locationID) {
        if (root == null) return false;
        root = splay(root, locationID);
        if (root.data.getLocationID() != locationID) return false;

        if (root.left == null) root = root.right;
        else {
            Node temp = root.right;
            root = splay(root.left, locationID);
            root.right = temp;
        }
        count--;
        return true;
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