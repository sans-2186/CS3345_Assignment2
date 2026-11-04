/**
 * File: NavigationHashTable.java
 * Author: Sanskriti Tiwari
 * Course: CS3345 Data Structures & Algorithm Analysis
 * Description: Implements hash-based indexing supporting Division and Universal hashing,
 * along with 6 required configurations: Chaining, Linear Probing, Quadratic Probing, and Double Hashing.
 */
public class NavigationHashTable implements LocationIndex {

    public enum HashMethod { DIVISION, UNIVERSAL }
    public enum CollisionMethod { CHAINING, LINEAR, QUADRATIC, DOUBLE }

    private HashMethod hashMethod;
    private CollisionMethod collisionMethod;
    private int capacity;
    private int size;

    private static class HashNode {
        Location data;
        HashNode next;
        HashNode(Location data) { this.data = data; }
    }
    private HashNode[] tableChaining;
    private Location[] tableArray;
    private boolean[] deletedArray;

    private static final long P = 10000019L;
    private static final long A = 31L;
    private static final long B = 17L;

    public NavigationHashTable(HashMethod hashMethod, CollisionMethod collisionMethod, int capacity) {
        this.hashMethod = hashMethod;
        this.collisionMethod = collisionMethod;
        this.capacity = capacity;
        this.size = 0;

        if (collisionMethod == CollisionMethod.CHAINING) {
            tableChaining = new HashNode[capacity];
        } else {
            tableArray = new Location[capacity];
            deletedArray = new boolean[capacity];
        }
    }

    //Implements either Division Hashing (k (mod m)) or Universal Hashing (((a * k + b) (mod p) mod m))
    private int hash(int k) {
        long key = Math.abs((long) k);
        if (hashMethod == HashMethod.DIVISION) {
            return (int) (key % capacity);
        } else {
            return (int) (((A * key + B) % P) % capacity);
        }
    }

    // Used specifically for Double Hashing collision resolution to calculate the step size.
    private int secondaryHash(int k) {
        long key = Math.abs((long) k);
        int r = 1;
        for (int i = capacity - 2; i > 1; i--) {
            if (isPrime(i)) { r = i; break; }
        }
        return (int) (r - (key % r));
    }

    private boolean isPrime(int n) {
        if (n <= 1) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    // Implements the logic for Chaining (using custom linked nodes) and Open Addressing (Linear, Quadratic, and Double Hashing using arrays)
    @Override
    public synchronized boolean insert(Location location) {
        if (location == null) return false;
        int id = location.getLocationID();

        if (collisionMethod == CollisionMethod.CHAINING) {
            int idx = hash(id);
            HashNode curr = tableChaining[idx];
            while (curr != null) {
                if (curr.data.getLocationID() == id) return false;
                curr = curr.next;
            }
            HashNode newNode = new HashNode(location);
            newNode.next = tableChaining[idx];
            tableChaining[idx] = newNode;
            size++;
            return true;
        } else {
            int hashVal = hash(id);
            int step = (collisionMethod == CollisionMethod.DOUBLE) ? secondaryHash(id) : 1;
            int firstDeleted = -1;

            for (int i = 0; i < capacity; i++) {
                int idx = hashVal;
                if (collisionMethod == CollisionMethod.LINEAR) idx = (hashVal + i) % capacity;
                else if (collisionMethod == CollisionMethod.QUADRATIC) idx = (int) ((hashVal + (long) i * i) % capacity);
                else if (collisionMethod == CollisionMethod.DOUBLE) idx = (int) ((hashVal + (long) i * step) % capacity);

                if (tableArray[idx] == null) {
                    if (!deletedArray[idx]) {
                        int target = (firstDeleted != -1) ? firstDeleted : idx;
                        tableArray[target] = location;
                        size++;
                        return true;
                    } else if (firstDeleted == -1) {
                        firstDeleted = idx;
                    }
                } else if (tableArray[idx].getLocationID() == id) {
                    return false;
                }
            }
            if (firstDeleted != -1) {
                tableArray[firstDeleted] = location;
                size++;
                return true;
            }
            return false;
        }
    }

    @Override
    public synchronized Location search(int locationID) {
        if (collisionMethod == CollisionMethod.CHAINING) {
            int idx = hash(locationID);
            HashNode curr = tableChaining[idx];
            while (curr != null) {
                if (curr.data.getLocationID() == locationID) return curr.data;
                curr = curr.next;
            }
            return null;
        } else {
            int hashVal = hash(locationID);
            int step = (collisionMethod == CollisionMethod.DOUBLE) ? secondaryHash(locationID) : 1;

            for (int i = 0; i < capacity; i++) {
                int idx = hashVal;
                if (collisionMethod == CollisionMethod.LINEAR) idx = (hashVal + i) % capacity;
                else if (collisionMethod == CollisionMethod.QUADRATIC) idx = (int) ((hashVal + (long) i * i) % capacity);
                else if (collisionMethod == CollisionMethod.DOUBLE) idx = (int) ((hashVal + (long) i * step) % capacity);

                if (tableArray[idx] == null && !deletedArray[idx]) return null;
                if (tableArray[idx] != null && tableArray[idx].getLocationID() == locationID) return tableArray[idx];
            }
            return null;
        }
    }

    @Override
    public synchronized boolean remove(int locationID) {
        if (collisionMethod == CollisionMethod.CHAINING) {
            int idx = hash(locationID);
            HashNode curr = tableChaining[idx], prev = null;
            while (curr != null) {
                if (curr.data.getLocationID() == locationID) {
                    if (prev == null) tableChaining[idx] = curr.next;
                    else prev.next = curr.next;
                    size--;
                    return true;
                }
                prev = curr;
                curr = curr.next;
            }
            return false;
        } else {
            int hashVal = hash(locationID);
            int step = (collisionMethod == CollisionMethod.DOUBLE) ? secondaryHash(locationID) : 1;

            for (int i = 0; i < capacity; i++) {
                int idx = hashVal;
                if (collisionMethod == CollisionMethod.LINEAR) idx = (hashVal + i) % capacity;
                else if (collisionMethod == CollisionMethod.QUADRATIC) idx = (int) ((hashVal + (long) i * i) % capacity);
                else if (collisionMethod == CollisionMethod.DOUBLE) idx = (int) ((hashVal + (long) i * step) % capacity);

                if (tableArray[idx] == null && !deletedArray[idx]) return false;
                if (tableArray[idx] != null && tableArray[idx].getLocationID() == locationID) {
                    tableArray[idx] = null;
                    deletedArray[idx] = true;
                    size--;
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void display() {
        if (size == 0) {
            System.out.println("No locations available.");
            return;
        }
        if (collisionMethod == CollisionMethod.CHAINING) {
            for (int i = 0; i < capacity; i++) {
                HashNode curr = tableChaining[i];
                while (curr != null) {
                    System.out.println("Bucket " + i + ": " + curr.data);
                    curr = curr.next;
                }
            }
        } else {
            for (int i = 0; i < capacity; i++) {
                if (tableArray[i] != null) {
                    System.out.println("Index " + i + ": " + tableArray[i]);
                }
            }
        }
    }
}