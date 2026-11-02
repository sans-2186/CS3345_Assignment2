/**
 * File: LocationIndex.java
 * Author: Sanskriti Tiwari
 * Course: CS3345 Data Structures & Algorithm Analysis
 * Description: Common interface defining core operations for all location indexing
 * data structures (BST, AVL Tree, Splay Tree, Hash Tables).
 */
public interface LocationIndex {
    boolean insert(Location location);
    Location search(int locationID);
    boolean remove(int locationID);
    int size();
    void display();
}