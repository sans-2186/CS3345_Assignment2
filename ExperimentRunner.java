/**
 * File: ExperimentRunner.java
 * Author: Sanskriti Tiwari
 * Course: CS3345 Data Structures & Algorithm Analysis
 * Description: Automates loading datasets, executing 5 performance trials for insertion,
 * search, and deletion across all 9 data structures, and generating formatted results.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExperimentRunner {

    private static final String[] SCALES = {"small", "medium", "large"};
    private static final int[] CAPACITIES = {2003, 10007, 20011};

    public static void runAllExperiments() {
        System.out.println("==================================================");
        System.out.println(" CS3345 Navigation System - Assignment 2 Benchmarks");
        System.out.println("==================================================");

        String[] dataStructures = {
            "BST", "AVL Tree", "Splay Tree",
            "Hash - Division/Chaining", "Hash - Division/Linear",
            "Hash - Division/Quadratic", "Hash - Division/Double",
            "Hash - Universal/Chaining", "Hash - Universal/Double"
        };

        for (int s = 0; s < SCALES.length; s++) {
            String scale = SCALES[s];
            int capacity = CAPACITIES[s];

            System.out.println("\n--------------------------------------------------");
            System.out.println(" Dataset Scale: " + scale.toUpperCase());
            System.out.println("--------------------------------------------------");

            List<Integer> insertIDs = loadIDs(scale + "_insert.txt");
            List<Integer> searchIDs = loadIDs(scale + "_search.txt");
            List<Integer> deleteIDs = loadIDs(scale + "_delete.txt");

            if (insertIDs.isEmpty()) {
                System.out.println("Dataset files not found in working directory for " + scale + ". Skipping benchmark.");
                continue;
            }

            for (String dsName : dataStructures) {
                runBenchmarkForStructure(dsName, insertIDs, searchIDs, deleteIDs, capacity);
            }
        }
    }

    private static List<Integer> loadIDs(String filename) {
        List<Integer> ids = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) ids.add(Integer.parseInt(line));
            }
        } catch (IOException e) {
            // File not found handled gracefully
        }
        return ids;
    }

    private static LocationIndex createDataStructure(String dsName, int capacity) {
        switch (dsName) {
            case "BST": return new NavigationBST();
            case "AVL Tree": return new NavigationAVL();
            case "Splay Tree": return new NavigationSplayTree();
            case "Hash - Division/Chaining":
                return new NavigationHashTable(NavigationHashTable.HashMethod.DIVISION, NavigationHashTable.CollisionMethod.CHAINING, capacity);
            case "Hash - Division/Linear":
                return new NavigationHashTable(NavigationHashTable.HashMethod.DIVISION, NavigationHashTable.CollisionMethod.LINEAR, capacity);
            case "Hash - Division/Quadratic":
                return new NavigationHashTable(NavigationHashTable.HashMethod.DIVISION, NavigationHashTable.CollisionMethod.QUADRATIC, capacity);
            case "Hash - Division/Double":
                return new NavigationHashTable(NavigationHashTable.HashMethod.DIVISION, NavigationHashTable.CollisionMethod.DOUBLE, capacity);
            case "Hash - Universal/Chaining":
                return new NavigationHashTable(NavigationHashTable.HashMethod.UNIVERSAL, NavigationHashTable.CollisionMethod.CHAINING, capacity);
            case "Hash - Universal/Double":
                return new NavigationHashTable(NavigationHashTable.HashMethod.UNIVERSAL, NavigationHashTable.CollisionMethod.DOUBLE, capacity);
            default: return new NavigationBST();
        }
    }

    private static void runBenchmarkForStructure(String dsName, List<Integer> insertIDs, List<Integer> searchIDs, List<Integer> deleteIDs, int capacity) {
        CS3345ToolKit.Timer timer = new CS3345ToolKit.Timer();

        // 5 Trials for Insertion
        double[] insertTimes = new double[5];
        for (int trial = 0; trial < 5; trial++) {
            LocationIndex index = createDataStructure(dsName, capacity);
            List<Location> prepped = new ArrayList<>();
            for (int id : insertIDs) prepped.add(new Location(id, "Location " + id));
            timer.start();
            for (Location loc : prepped) index.insert(loc);
            timer.stop();
            insertTimes[trial] = timer.getElapsedTimeMs();
        }
        CS3345ToolKit.Statistics insertStats = new CS3345ToolKit.Statistics(insertTimes);

        // 5 Trials for Search
        double[] searchTimes = new double[5];
        for (int trial = 0; trial < 5; trial++) {
            LocationIndex index = createDataStructure(dsName, capacity);
            for (int id : insertIDs) index.insert(new Location(id, "Location " + id));
            timer.start();
            for (int id : searchIDs) index.search(id);
            timer.stop();
            searchTimes[trial] = timer.getElapsedTimeMs();
        }
        CS3345ToolKit.Statistics searchStats = new CS3345ToolKit.Statistics(searchTimes);

        // 5 Trials for Deletion
        double[] deleteTimes = new double[5];
        for (int trial = 0; trial < 5; trial++) {
            LocationIndex index = createDataStructure(dsName, capacity);
            for (int id : insertIDs) index.insert(new Location(id, "Location " + id));
            timer.start();
            for (int id : deleteIDs) index.remove(id);
            timer.stop();
            deleteTimes[trial] = timer.getElapsedTimeMs();
        }
        CS3345ToolKit.Statistics deleteStats = new CS3345ToolKit.Statistics(deleteTimes);

        System.out.printf("[%-28s] Insert Avg: %6.2f ms | Search Avg: %6.2f ms | Delete Avg: %6.2f ms\n",
                dsName, insertStats.getAverage(), searchStats.getAverage(), deleteStats.getAverage());
    }
}