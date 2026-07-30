/*
Kruskal's MST algorithm needs edges sorted by weight before the greedy selection starts.
--> The algorithm doesn't care HOW they get sorted — BubbleSort, MergeSort, QuickSort all produce the same result.
--> So the sorting step is a swappable detail.

Solution?
--> Pull the sorting step behind a SortingAlgo interface.
--> KruskalMST holds a reference to it and delegates — it has no idea which algorithm runs.
*/

interface SortingAlgo {
    void sort(int[] edges);
}

class BubbleSort implements SortingAlgo {
    public void sort(int[] edges) {
        System.out.println("Sorting edges with Bubble Sort");
    }
}

class MergeSort implements SortingAlgo {
    public void sort(int[] edges) {
        System.out.println("Sorting edges with Merge Sort");
    }
}

class QuickSort implements SortingAlgo {
    public void sort(int[] edges) {
        System.out.println("Sorting edges with Quick Sort");
    }
}

class KruskalMST {
    private SortingAlgo sortingAlgo;

    public KruskalMST(SortingAlgo sortingAlgo) {
        this.sortingAlgo = sortingAlgo;
    }

    public void setSortingAlgo(SortingAlgo strategy) {
        this.sortingAlgo = strategy;
    }

    public void findMST(int[] edges) {
        sortingAlgo.sort(edges); // KruskalMST has no idea how this sorts
        System.out.println("... greedy edge selection runs here");
    }
}

public class Sorting {
    public static void main(String[] args) {
        int[] edges = {10, 6, 5, 15, 4};

        KruskalMST kruskal = new KruskalMST(new BubbleSort());
        kruskal.findMST(edges);

        // Swap strategy at runtime — KruskalMST code doesn't change at all
        kruskal.setSortingAlgo(new MergeSort());
        kruskal.findMST(edges);

        kruskal.setSortingAlgo(new QuickSort());
        kruskal.findMST(edges);
    }
}
