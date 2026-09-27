# Assignment 2: Algorithmic Analysis, Correctness and Performance Trade-offs

## 1. Project Overview
This repository contains implementations and algorithmic analysis for three fundamental data structures:
* **Dynamic Array** (`DynamicArray.java`)
* **Doubly Linked List** (`LinkedList.java`)
* **Min-Heap** (`MinHeap.java`)

The primary objective is to evaluate theoretical asymptotic complexity against empirical measurements across various workloads ($n \in \{100, 1000, 10000, 100000\}$), prove algorithmic correctness via loop invariants, and analyze lower-level performance characteristics such as CPU cache locality.

---

## 2. Asymptotic Analysis

### Complexity Table

| Data Structure | Operation | Best Case | Average Case | Worst Case | Auxiliary Space |
| :--- | :--- | :---: | :---: | :---: | :---: |
| **Dynamic Array** | `get(i)` | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | $O(1)$ |
| | `add(x)` | $\Theta(1)$ | $\Theta(1)$ (amortized) | $\Theta(n)$ | $O(1)$ |
| | `add(0, x)` | $\Theta(n)$ | $\Theta(n)$ | $\Theta(n)$ | $O(1)$ |
| | `remove(0)` | $\Theta(n)$ | $\Theta(n)$ | $\Theta(n)$ | $O(1)$ |
| | `contains(x)` | $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $O(1)$ |
| **Linked List** | `get(i)` | $\Theta(1)$ (at head/tail) | $\Theta(n)$ | $\Theta(n)$ | $O(1)$ |
| | `add(x)` | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | $O(1)$ |
| | `add(0, x)` | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | $O(1)$ |
| | `remove(0)` | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | $O(1)$ |
| | `contains(x)` | $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $O(1)$ |
| **Min-Heap** | `insert(x)` | $\Theta(1)$ | $O(\log n)$ | $\Theta(\log n)$ | $O(1)$ |
| | `peekMin()` | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | $O(1)$ |
| | `extractMin()` | $\Theta(\log n)$ | $\Theta(\log n)$ | $\Theta(\log n)$ | $O(1)$ |

### Justification of Practical Differences
1. **`get(i)`:** `DynamicArray` achieves true $\Theta(1)$ access time due to contiguous memory allocation and direct pointer arithmetic. `LinkedList` requires sequential traversal from the head or tail node, yielding $O(n)$ time complexity.
2. **Operations at `index = 0`:** Prepending or removing at index 0 in a `LinkedList` requires updating only $1-2$ reference pointers ($\Theta(1)$). In contrast, `DynamicArray` must shift all $n$ existing elements in memory ($\Theta(n)$).

---

## 3. Algorithmic Correctness Proofs

### Proof 1: `DynamicArray.contains(x)` (Linear Search)

#### Loop Invariant
At the start of each iteration $i$ of the `for` loop, the target element $x$ is not present in the subarray `array[0 .. i-1]`.

#### Proof Steps
1. **Initialization:** Before the first iteration ($i = 0$), the subarray `array[0 .. -1]` is empty. The claim that $x$ is not in an empty subarray is trivially true.
2. **Maintenance:** Assume the invariant holds before iteration $i$, meaning $x \notin \text{array}[0 .. i-1]$. During iteration $i$, the algorithm checks `array[i]`:
   * If `array[i].equals(x)` is true, the method returns `true`, correctly confirming element presence.
   * If `array[i].equals(x)` is false, we establish $x \neq \text{array}[i]$. Combining this with the assumption, $x \notin \text{array}[0 .. i]$. Incrementing $i$ to $i + 1$ preserves the invariant for the next iteration.
3. **Termination:** The loop terminates either when $x$ is found (returning `true`) or when $i = \text{size}$. When $i = \text{size}$, the invariant proves that $x \notin \text{array}[0 .. \text{size}-1]$.
4. **Correctness at Termination:** The invariant guarantees that $x$ is not present anywhere in the array. Returning `false` is provably correct.

---

### Proof 2: `MinHeap.extractMin()` (`siftDown` Procedure)

#### Loop Invariant
At the start of each iteration of the `siftDown(i)` loop:
1. Every node in the tree **except possibly** node $i$ satisfies the min-heap property ($A[\text{parent}] \le A[\text{child}]$).
2. The subtrees rooted at node $i$'s children satisfy the min-heap property.

#### Proof Steps
1. **Initialization:** When `extractMin()` removes the root and replaces it with the last element, only root index $i = 0$ may violate the min-heap property. The left and right subtrees retain their valid min-heap structures. The invariant holds.
2. **Maintenance:** The algorithm compares `heap[i]` with its children `heap[left]` and `heap[right]`, identifying `minIndex`:
   * If `i == minIndex`, `heap[i]` is smaller than or equal to both children, so the min-heap property is satisfied and the loop terminates.
   * If `i != minIndex`, swapping `heap[i]` and `heap[minIndex]` restores the min-heap property at node $i$. The violation shifts down to `minIndex`. Setting $i = \text{minIndex}$ preserves the invariant for the next iteration.
3. **Termination:** The loop terminates when node $i$ is smaller than or equal to its children or when $i$ becomes a leaf node.
4. **Correctness at Termination:** Upon termination, node $i$ satisfies the min-heap property. Since no other nodes violate the property, the entire binary tree is a valid Min-Heap.

---

## 4. Experimental Setup

* **Benchmark Harness:** Custom benchmarking harness (`Benchmark.java`) measuring execution time in nanoseconds via `System.nanoTime()`.
* **Repetitions:** Each test workload is executed 5 times; reported values represent average execution times.
* **Deterministic Input:** Random data is generated using a fixed seed (`new Random(42)`).
* **Isolation:** Timers strictly measure data structure operations; data generation and setup phases are excluded.
* **Input Sizes:** $n \in \{100, 1000, 10000, 100000\}$.

---

## 5. Experimental Results

### Workload 1: Random Access
* **Operations:** $m = 10\,000$ calls to `get(index)` with random indices.

| $n$ | DynamicArray Avg Time (ms) | LinkedList Avg Time (ms) | Theoretical (Array) | Theoretical (List) |
| :--- | :--- | :--- | :--- | :--- |
| 100 | 0.36 | 1.04 | $\Theta(1)$ | $\Theta(n)$ |
| 1 000 | 0.06 | 4.65 | $\Theta(1)$ | $\Theta(n)$ |
| 10 000 | 0.02 | 50.87 | $\Theta(1)$ | $\Theta(n)$ |
| 100 000 | 0.20 | 523.02 | $\Theta(1)$ | $\Theta(n)$ |

---

### Workload 2: Search
* **Operations:** $m = 1\,000$ calls to `contains(value)` with random target values.

| $n$ | DynamicArray Search Time (ms) | LinkedList Search Time (ms) | Theoretical Complexity |
| :--- | :--- | :--- | :--- |
| 100 | 1.18 | 0.99 | $O(n)$ |
| 1 000 | 1.90 | 2.58 | $O(n)$ |
| 10 000 | 10.43 | 24.04 | $O(n)$ |
| 100 000 | 112.00 | 239.13 | $O(n)$ |

---

### Workload 3: Insertion at Index 0
* **Operations:** $m = 1\,000$ insertions at index 0.

| $n$ | Insert at 0 (Array) (ms) | Insert at 0 (List) (ms) | Theoretical (Array) | Theoretical (List) |
| :--- | :--- | :--- | :--- | :--- |
| 100 | 0.37 | 0.11 | $\Theta(n)$ | $\Theta(1)$ |
| 1 000 | 0.35 | 0.07 | $\Theta(n)$ | $\Theta(1)$ |
| 10 000 | 1.37 | 0.05 | $\Theta(n)$ | $\Theta(1)$ |
| 100 000 | 11.42 | 0.02 | $\Theta(n)$ | $\Theta(1)$ |

---

### Workload 4: Priority Processing (MinHeap)
* **Operations:** $n$ insertions followed by $n$ extractions.

| $n$ | Insert Total Time (ms) | Extract Total Time (ms) | Total Comparisons | Complexity per Op |
| :--- | :--- | :--- | :--- | :--- |
| 100 | 0.03 | 0.09 | 1 069 | $O(\log n)$ |
| 1 000 | 0.09 | 0.19 | 17 322 | $O(\log n)$ |
| 10 000 | 0.66 | 1.62 | 239 284 | $O(\log n)$ |
| 100 000 | 3.07 | 11.94 | 3 059 283 | $O(\log n)$ |

---

## 6. Plots and Visual Analysis

Place generated charts in `results/plots/`:
* `results/plots/time_vs_n.png` (Execution Time vs. $n$)
* `results/plots/ops_vs_n.png` (Comparisons vs. $n$)

<img width="600" height="371" alt="B (Insert Time) и C (Extract Time)" src="https://github.com/user-attachments/assets/72fe8700-19a6-4172-bdd0-3ce9bbf82c59" />

<img width="600" height="371" alt="D (Total Comparisons)" src="https://github.com/user-attachments/assets/11fb1f8e-29e4-44c8-a194-e13261aeb403" />


1. **Execution Time vs. $n$ (`time_vs_n.png`)**
   * As $n$ increases from 100 to 100,000, both `insert` and `extractMin` operations show scaling consistent with $O(\log n)$ per operation.
   * `extractMin` execution time grows faster than `insert` time because `siftDown` requires up to 2 comparisons per heap level (comparing children then parent), whereas `siftUp` performs only 1 comparison per step.

2. **Total Comparisons vs. $n$ (`ops_vs_n.png`)**
   * The total comparison count demonstrates clear $O(n \log n)$ growth for $n$ operations.
   * At $n = 100\,000$, the total comparisons reach approximately $3.06 \times 10^6$, perfectly validating the theoretical bound on real binary tree height operations.
---

## 7. Performance and Design Analysis

1. **Impact of Scaling $n$:**
   * In **Workload 1**, `LinkedList.get()` execution time increases dramatically (from $1.04\text{ ms}$ to $523.02\text{ ms}$) due to $O(n)$ node traversal costs, whereas `DynamicArray` remains flat at sub-millisecond levels.
   * In **Workload 3**, `LinkedList` achieves prepending in $0.02\text{ ms}$ at $n=100\,000$, outperforming `DynamicArray` ($11.42\text{ ms}$) due to avoiding $O(n)$ array element shifting.

2. **Memory Layout and CPU Cache Effects:**
   * Although search operations in both structures share $O(n)$ asymptotic complexity, `DynamicArray` is **~2x faster** than `LinkedList` at $n = 100\,000$ ($112.00\text{ ms}$ vs $239.13\text{ ms}$).
   * `DynamicArray` stores elements in contiguous memory blocks, enabling CPU prefetching and high L1/L2 cache hit ratios. `LinkedList` nodes are dynamically allocated across the heap, causing frequent CPU cache misses during traversal.

3. **Design Recommendations:**
   * **Use Dynamic Array** when frequent random access (`get(i)`) or append operations are needed, and dataset size fluctuates dynamically.
   * **Use Linked List** when frequent insertions or removals at the beginning/end of the collection are required without random access indexing.
   * **Use Min-Heap** for priority processing scenarios requiring fast extraction of minimal/maximal elements in $O(\log n)$ time.

---

## 8. Conclusion
This assignment demonstrated the synergy between theoretical algorithm analysis, mathematical correctness proofs, and empirical performance benchmarks. While Big-O analysis accurately predicts growth trends, hardware factors like memory locality significantly influence real-world performance.
