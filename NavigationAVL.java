/**
 * File: NavigationAVL.java
 * Author: Sanskriti Tiwari
 * Course: CS3345 Data Structures & Algorithm Analysis
 * Description: Implements a self-balancing AVL Tree maintaining height balance factor
 * between -1 and +1 using LL, RR, LR, and RL rotations after insertions and deletions.
 */
public class NavigationAVL implements LocationIndex {

    private static class Node {
        Location data;
        Node left, right;
        int height;

        Node(Location data) {
            this.data = data;
            this.height = 1;
        }
    }

    private Node root;
    private int count;

    public NavigationAVL() {
        this.root = null;
        this.count = 0;
    }

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    // Calculates the height difference between left and right subtrees to ensure it stays between -1 and +1.
    private int getBalance(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    // Performs tree rotations (LL, RR, LR, RL) to restore balance after insertions and deletions
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    @Override
    public synchronized boolean insert(Location location) {
        if (location == null) return false;
        int oldSize = count;
        root = insertRec(root, location);
        return count > oldSize;
    }

    private Node insertRec(Node node, Location location) {
        if (node == null) {
            count++;
            return new Node(location);
        }

        int cmp = Integer.compare(location.getLocationID(), node.data.getLocationID());
        if (cmp < 0) node.left = insertRec(node.left, location);
        else if (cmp > 0) node.right = insertRec(node.right, location);
        else return node;

        node.height = 1 + Math.max(height(node.left), height(node.right));
        int balance = getBalance(node);

        // LL
        if (balance > 1 && location.getLocationID() < node.left.data.getLocationID()) return rightRotate(node);
        // RR
        if (balance < -1 && location.getLocationID() > node.right.data.getLocationID()) return leftRotate(node);
        // LR
        if (balance > 1 && location.getLocationID() > node.left.data.getLocationID()) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        // RL
        if (balance < -1 && location.getLocationID() < node.right.data.getLocationID()) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    @Override
    public synchronized Location search(int locationID) {
        Node curr = root;
        while (curr != null) {
            int cmp = Integer.compare(locationID, curr.data.getLocationID());
            if (cmp < 0) curr = curr.left;
            else if (cmp > 0) curr = curr.right;
            else return curr.data;
        }
        return null;
    }

    @Override
    public synchronized boolean remove(int locationID) {
        int oldSize = count;
        root = deleteRec(root, locationID);
        return count < oldSize;
    }

    private Node deleteRec(Node root, int id) {
        if (root == null) return null;

        int cmp = Integer.compare(id, root.data.getLocationID());
        if (cmp < 0) root.left = deleteRec(root.left, id);
        else if (cmp > 0) root.right = deleteRec(root.right, id);
        else {
            count--;
            if ((root.left == null) || (root.right == null)) {
                Node temp = root.left != null ? root.left : root.right;
                if (temp == null) root = null;
                else root = temp;
            } else {
                Node temp = findMin(root.right);
                root.data = temp.data;
                root.right = deleteRec(root.right, temp.data.getLocationID());
                count++;
            }
        }

        if (root == null) return null;

        root.height = Math.max(height(root.left), height(root.right)) + 1;
        int balance = getBalance(root);

        // LL & LR & RR & RL balance checks
        if (balance > 1 && getBalance(root.left) >= 0) return rightRotate(root);
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        if (balance < -1 && getBalance(root.right) <= 0) return leftRotate(root);
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    private Node findMin(Node node) {
        Node current = node;
        while (current.left != null) current = current.left;
        return current;
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