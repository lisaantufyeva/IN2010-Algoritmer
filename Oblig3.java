import java.util.Arrays;
import java.util.Random;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

class Oblig3 {
    public static void main(String[] args) throws Exception {

        // String filename = args[0];
        Scanner scanner = new Scanner(System.in);
        String filename = scanner.nextLine();
        File file = new File(filename);
        BufferedReader in = new BufferedReader(new FileReader(file));
        int[] A = in.lines().mapToInt(i -> Integer.parseInt(i)).toArray();
        in.close();

        Oblig3Runner.runAlgsPart1(A, filename);
        Oblig3Runner.runAlgsPart2(A, filename);
    }
}

class Insertion extends Sorter {

    void sort() {
        int n = A.length;
        for (int i = 1; i < n; i++) {
            int j = i;
            while (gt(j, 0) && gt(A[j - 1], A[j])) {
                swap(j - 1, j);
                j = j - 1;
            }
        }
    }

    String algorithmName() {
        return "insertion";
    }
}

class Selection extends Sorter {

    void sort() {
        int n = A.length;
        for (int i = 0; i < n; i++) {
            int key = i;
            for (int j = i + 1; j < n; j++) {
                if (lt(A[j], A[key])) {
                    key = j;
                }
            }
            if (!eq(i, key)) {
                swap(i, key);
            }
        }
    }

    String algorithmName() {
        return "selection";
    }
}

class Quick extends Sorter {

    void sort() {
        sorter(0, A.length - 1);
    }

    void sorter(int low, int high) {

        if (low < high) {
            int p = partition(A, low, high);
            sorter(low, p - 1);
            sorter(p + 1, high);
        }
    }

    String algorithmName() {
        return "quick";
    }

    int partition(int[] a, int low, int high) {
        int p = new Random().nextInt(high - low) + low;

        swap(p, high);
        int pivot = a[high];

        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (lt(a[j], pivot)) {
                i++;
                swap(i, j);
            }
        }
        swap(i + 1, high);
        return (i + 1);

    }
}

class HeapSort extends Sorter {

    void sort() {
        int n = A.length;
        buildMaxHeap(A, n);
        for (int i = n - 1; i >= 0; i--) {
            swap(0, i);
            bubbleDown(A, 0, i);
        }
    }

    String algorithmName() {
        return "heap";
    }

    void buildMaxHeap(int[] a, int n) {
        for (int i = n / 2; i >= 0; i--) {
            bubbleDown(a, i, n);
        }
    }

    void bubbleDown(int[] a, int i, int n) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (lt(left, n) && lt(a[largest], a[left])) {
            largest = left;
        }
        if (lt(right, n) && lt(a[largest], a[right])) {
            largest = right;
        }
        if (!eq(i, largest)) {
            swap(i, largest);
            bubbleDown(a, largest, n);
        }
    }

}

abstract class Sorter {
    // Keep copy of the original array
    int original[];
    // The array which is to be sorted
    int A[];
    // The number of elements of A
    int n = 0;
    // Counters for comparisons and swaps
    int comparisons = 0;
    int swaps = 0;

    // Set true when a sort exceeds Oblig3Runner.TIME_LIMIT_MS
    // When true, we discard the call to run() (i.e. return immediately)
    boolean discard = false;

    void initializePart1(int[] A) {
        this.original = A;
        this.n = A.length;
        // We clone, so that A may be safely passed to multiple sorters
        this.A = Arrays.copyOfRange(original, 0, n);
    }

    void initializePart2(int[] A) {
        this.original = A;
        this.n = 0;
        // We clone, so that A may be safely passed to multiple sorters
        this.A = Arrays.copyOfRange(original, 0, n);
    }

    // For the students to implement in an appropriate subclass
    abstract void sort();

    // Necessary for output
    abstract String algorithmName();

    // A swapping method that counts
    void swap(int i, int j) {
        swaps++;
        int tmp = A[i];
        A[i] = A[j];
        A[j] = tmp;
    }

    // Comparisons that count
    boolean lt(int a, int b) {
        comparisons++;
        return a < b;
    }

    boolean leq(int a, int b) {
        comparisons++;
        return a <= b;
    }

    boolean gt(int a, int b) {
        comparisons++;
        return a > b;
    }

    boolean geq(int a, int b) {
        comparisons++;
        return a >= b;
    }

    boolean eq(int a, int b) {
        comparisons++;
        return a == b;
    }

    // For Oblig3Runner

    // Sort and return the time it consumed in microseconds
    long sortTimed() {
        long t = System.nanoTime();
        sort();
        return (System.nanoTime() - t) / 1000;
    }

    // Run a sorting and return a description of the execution as a CSV row
    String run() {
        String fmt = runStringFormat();
        if (discard) {
            String res = String.format(fmt, 0, 0, 0);
            return res.replace("0", " ");
        }
        long timeus = sortTimed();
        long timems = timeus / 1000;

        if (timems > Oblig3Runner.TIME_LIMIT_MS) {
            discard = true;
            System.out.println("\nGiving up on " + algorithmName() + "\n");
        }

        String res = String.format(fmt, comparisons, swaps, timeus);
        return res;
    }

    // Reset counters and restore the original array (up to n)
    void reset() {
        comparisons = 0;
        swaps = 0;
        this.A = Arrays.copyOfRange(original, 0, n);
    }

    // Reset with a higher n
    void resetAndIncBy(int increment) {
        n += increment;
        reset();
    }

    // Run, reset and increment
    String runResetAndIncBy(int increment) {
        String res = run();
        resetAndIncBy(increment);
        return res;
    }

    // Generate the header for this sorting algorithm
    String headerString() {
        String name = this.algorithmName();
        String headerfmt = "%s_cmp, %s_swaps, %s_time";
        return String.format(headerfmt, name, name, name);
    }

    // Generate a format string for printing results of a run
    String runStringFormat() {
        String name = this.algorithmName();
        int cmplen = name.length() + "_cmp".length();
        int swaplen = name.length() + "_swaps".length();
        int timelen = name.length() + "_time".length();
        String fmt = "%%%dd, %%%dd, %%%dd";
        return String.format(fmt, cmplen, swaplen, timelen);
    }
}