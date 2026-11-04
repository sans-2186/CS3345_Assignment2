/**
 * File: CS3345ToolKit.java
 * Author: Sanskriti Tiwari
 * Course: CS3345 Data Structures & Algorithm Analysis
 * Description: Performance measurement toolkit including Timer, Statistics, and Memory Monitor
 * based on Assignment 0 specifications.
 */
public class CS3345ToolKit {

    // Tracks execution time precisely using System.nanoTime()
    public static class Timer {
        private long startTime;
        private long elapsedTime;

        public void start() {
            startTime = System.nanoTime();
            elapsedTime = 0;
        }

        public void stop() {
            elapsedTime = System.nanoTime() - startTime;
        }

        public double getElapsedTimeMs() {
            return elapsedTime / 1_000_000.0;
        }
    }

    // Calculates minimum, maximum, and average values across 5 test trials
    public static class Statistics {
        private double[] values;

        public Statistics(double[] values) {
            this.values = values;
        }

        public double getMin() {
            if (values.length == 0) return 0.0;
            double min = values[0];
            for (double v : values) if (v < min) min = v;
            return min;
        }

        public double getMax() {
            if (values.length == 0) return 0.0;
            double max = values[0];
            for (double v : values) if (v > max) max = v;
            return max;
        }

        public double getAverage() {
            if (values.length == 0) return 0.0;
            double sum = 0.0;
            for (double v : values) sum += v;
            return sum / values.length;
        }
    }
}