/**
 * File: Main.java
 * Author: Sanskriti Tiwari
 * Course: CS3345 Data Structures & Algorithm Analysis
 * Description: Program entry point for the Navigation System. Allows interactive
 * user selection of indexing structures or executing performance benchmarks.
 */
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("==================================================");
        System.out.println(" Welcome to UTD Navigation System (Assignment 2)");
        System.out.println(" Author: Sanskriti Tiwari");
        System.out.println("==================================================");

        if (args.length > 0 && args[0].equalsIgnoreCase("benchmark")) {
            ExperimentRunner.runAllExperiments();
            return;
        }

        boolean running = true;
        LocationIndex currentIndex = new NavigationBST();
        String currentMethodName = "BST";

        while (running) {
            System.out.println("\n--- Navigation System Menu ---");
            System.out.println("Current Index: " + currentMethodName + " (Size: " + currentIndex.size() + ")");
            System.out.println("1. Select Tree Index (BST, AVL, Splay)");
            System.out.println("2. Select Hash Table Index");
            System.out.println("3. Add Location");
            System.out.println("4. Search Location by ID");
            System.out.println("5. Remove Location by ID");
            System.out.println("6. Show All Locations");
            System.out.println("7. Run Performance Benchmarks (All Datasets)");
            System.out.println("8. Exit");
            System.out.print("Select an option: ");

            String choiceStr = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(choiceStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 8.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("Select Tree Type: 1. BST | 2. AVL Tree | 3. Splay Tree");
                    String treeChoice = scanner.nextLine().trim();
                    if (treeChoice.equals("1")) { currentIndex = new NavigationBST(); currentMethodName = "BST"; }
                    else if (treeChoice.equals("2")) { currentIndex = new NavigationAVL(); currentMethodName = "AVL Tree"; }
                    else if (treeChoice.equals("3")) { currentIndex = new NavigationSplayTree(); currentMethodName = "Splay Tree"; }
                    else { System.out.println("Invalid choice."); break; }
                    System.out.println("Switched to " + currentMethodName);
                    break;

                case 2:
                    System.out.println("Select Hash Method: 1. Division | 2. Universal");
                    String hChoice = scanner.nextLine().trim();
                    NavigationHashTable.HashMethod hm = hChoice.equals("2") ? NavigationHashTable.HashMethod.UNIVERSAL : NavigationHashTable.HashMethod.DIVISION;

                    System.out.println("Select Collision Method: 1. Chaining | 2. Linear | 3. Quadratic | 4. Double");
                    String cChoice = scanner.nextLine().trim();
                    NavigationHashTable.CollisionMethod cm = NavigationHashTable.CollisionMethod.CHAINING;
                    String cName = "Chaining";
                    if (cChoice.equals("2")) { cm = NavigationHashTable.CollisionMethod.LINEAR; cName = "Linear Probing"; }
                    else if (cChoice.equals("3")) { cm = NavigationHashTable.CollisionMethod.QUADRATIC; cName = "Quadratic Probing"; }
                    else if (cChoice.equals("4")) { cm = NavigationHashTable.CollisionMethod.DOUBLE; cName = "Double Hashing"; }

                    currentIndex = new NavigationHashTable(hm, cm, 2003);
                    currentMethodName = "Hash - " + (hm == NavigationHashTable.HashMethod.DIVISION ? "Division" : "Universal") + "/" + cName;
                    System.out.println("Switched to " + currentMethodName);
                    break;

                case 3:
                    System.out.print("Enter Location ID: ");
                    try {
                        int id = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter Name: ");
                        String name = scanner.nextLine().trim();
                        if (name.isEmpty()) name = "Location " + id;
                        boolean added = currentIndex.insert(new Location(id, name));
                        System.out.println(added ? "Successfully added." : "Failed (Duplicate ID).");
                    } catch (NumberFormatException e) { System.out.println("Invalid ID."); }
                    break;

                case 4:
                    System.out.print("Enter ID to search: ");
                    try {
                        int id = Integer.parseInt(scanner.nextLine().trim());
                        Location loc = currentIndex.search(id);
                        System.out.println(loc != null ? "Found: " + loc : "Not found.");
                    } catch (NumberFormatException e) { System.out.println("Invalid ID."); }
                    break;

                case 5:
                    System.out.print("Enter ID to remove: ");
                    try {
                        int id = Integer.parseInt(scanner.nextLine().trim());
                        boolean removed = currentIndex.remove(id);
                        System.out.println(removed ? "Successfully removed." : "Not found.");
                    } catch (NumberFormatException e) { System.out.println("Invalid ID."); }
                    break;

                case 6:
                    currentIndex.display();
                    break;

                case 7:
                    ExperimentRunner.runAllExperiments();
                    break;

                case 8:
                    running = false;
                    System.out.println("Exiting. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
        scanner.close();
    }
}