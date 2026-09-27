package org.example;

public class Tests {
    public static void main(String[] args) {
        testDynamicArray();
        testLinkedList();
        testMinHeap();
        System.out.println("ALL TESTS PASSED SUCCESSFULLY!");
    }

    private static void testDynamicArray() {
        DynamicArray<Integer> da = new DynamicArray<>();
        da.add(10);
        da.add(20);
        da.add(30);
        assert da.get(0) == 10;
        assert da.get(2) == 30;
        assert da.contains(20);
        assert !da.contains(99);
        da.add(1, 15);
        assert da.get(1) == 15;
        assert da.remove(1) == 15;
        assert da.size() == 3;
    }

    private static void testLinkedList() {
        LinkedList<Integer> ll = new LinkedList<>();
        ll.add(100);
        ll.add(200);
        assert ll.get(0) == 100;
        assert ll.contains(200);
        ll.add(0, 50);
        assert ll.get(0) == 50;
        assert ll.remove(0) == 50;
        assert ll.size() == 2;
    }

    private static void testMinHeap() {
        MinHeap heap = new MinHeap();
        heap.insert(15);
        heap.insert(5);
        heap.insert(20);
        heap.insert(1);
        assert heap.peekMin() == 1;
        assert heap.extractMin() == 1;
        assert heap.extractMin() == 5;
        assert heap.extractMin() == 15;
        assert heap.extractMin() == 20;
    }
}