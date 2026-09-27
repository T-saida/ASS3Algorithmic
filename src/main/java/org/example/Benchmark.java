package org.example;

import java.util.Random;

public class Benchmark {
    private static final int[] N_VALUES = {100, 1000, 10000, 100000};

    public static void main(String[] args) {
        System.out.println("=== RUNNING BENCHMARKS ===\n");
        runWorkload1();
        runWorkload2();
        runWorkload3();
        runWorkload4();
    }

    private static void runWorkload1() {
        System.out.println("--- Workload 1: Random Access ---");
        for (int n : N_VALUES) {
            long totalTimeArray = 0;
            long totalTimeList = 0;

            for (int run = 0; run < 5; run++) {
                Random rng = new Random(42);
                DynamicArray<Integer> da = new DynamicArray<>();
                LinkedList<Integer> ll = new LinkedList<>();

                for (int i = 0; i < n; i++) {
                    int val = rng.nextInt();
                    da.add(val);
                    ll.add(val);
                }

                int[] indices = new int[10000];
                for (int i = 0; i < 10000; i++) indices[i] = rng.nextInt(n);

                long start = System.nanoTime();
                for (int idx : indices) da.get(idx);
                totalTimeArray += (System.nanoTime() - start);

                start = System.nanoTime();
                for (int idx : indices) ll.get(idx);
                totalTimeList += (System.nanoTime() - start);
            }

            System.out.printf("n=%-6d | DynamicArray get(): %8.2f ms | LinkedList get(): %8.2f ms%n",
                    n, (totalTimeArray / 5.0) / 1e6, (totalTimeList / 5.0) / 1e6);
        }
        System.out.println();
    }

    private static void runWorkload2() {
        System.out.println("--- Workload 2: Search ---");
        for (int n : N_VALUES) {
            long totalTimeArray = 0;
            long totalTimeList = 0;

            for (int run = 0; run < 5; run++) {
                Random rng = new Random(42);
                DynamicArray<Integer> da = new DynamicArray<>();
                LinkedList<Integer> ll = new LinkedList<>();

                for (int i = 0; i < n; i++) {
                    int val = rng.nextInt();
                    da.add(val);
                    ll.add(val);
                }

                int[] searchVals = new int[1000];
                for (int i = 0; i < 1000; i++) searchVals[i] = rng.nextInt();

                long start = System.nanoTime();
                for (int val : searchVals) da.contains(val);
                totalTimeArray += (System.nanoTime() - start);

                start = System.nanoTime();
                for (int val : searchVals) ll.contains(val);
                totalTimeList += (System.nanoTime() - start);
            }

            System.out.printf("n=%-6d | DynamicArray search: %8.2f ms | LinkedList search: %8.2f ms%n",
                    n, (totalTimeArray / 5.0) / 1e6, (totalTimeList / 5.0) / 1e6);
        }
        System.out.println();
    }

    private static void runWorkload3() {
        System.out.println("--- Workload 3: Insertion and Removal ---");
        for (int n : N_VALUES) {
            long timeArrayInsert0 = 0, timeListInsert0 = 0;

            for (int run = 0; run < 5; run++) {
                Random rng = new Random(42);
                DynamicArray<Integer> da = new DynamicArray<>();
                LinkedList<Integer> ll = new LinkedList<>();

                for (int i = 0; i < n; i++) {
                    int val = rng.nextInt();
                    da.add(val);
                    ll.add(val);
                }

                long start = System.nanoTime();
                for (int i = 0; i < 1000; i++) da.add(0, 999);
                timeArrayInsert0 += (System.nanoTime() - start);

                start = System.nanoTime();
                for (int i = 0; i < 1000; i++) ll.add(0, 999);
                timeListInsert0 += (System.nanoTime() - start);
            }

            System.out.printf("n=%-6d | Insert at 0 (Array): %8.2f ms | Insert at 0 (List): %8.2f ms%n",
                    n, (timeArrayInsert0 / 5.0) / 1e6, (timeListInsert0 / 5.0) / 1e6);
        }
        System.out.println();
    }

    private static void runWorkload4() {
        System.out.println("--- Workload 4: Priority Processing (MinHeap) ---");
        for (int n : N_VALUES) {
            long totalInsertTime = 0;
            long totalExtractTime = 0;
            long totalComparisons = 0;

            for (int run = 0; run < 5; run++) {
                Random rng = new Random(42);
                MinHeap heap = new MinHeap();
                int[] data = new int[n];
                for (int i = 0; i < n; i++) data[i] = rng.nextInt();

                long start = System.nanoTime();
                for (int val : data) heap.insert(val);
                totalInsertTime += (System.nanoTime() - start);

                start = System.nanoTime();
                int prev = Integer.MIN_VALUE;
                for (int i = 0; i < n; i++) {
                    int min = heap.extractMin();
                    if (min < prev) throw new IllegalStateException("Heap order violated!");
                    prev = min;
                }
                totalExtractTime += (System.nanoTime() - start);
                totalComparisons += heap.comparisonCount;
            }

            System.out.printf("n=%-6d | Insert time: %8.2f ms | Extract time: %8.2f ms | Avg Comparisons: %d%n",
                    n, (totalInsertTime / 5.0) / 1e6, (totalExtractTime / 5.0) / 1e6, totalComparisons / 5);
        }
        System.out.println();
    }
}