import java.util.PriorityQueue;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

class BalanceHeap {
    public static void main(String[] args) throws IOException {
        PriorityQueue<Integer> queue = new PriorityQueue<Integer>();
        // PriorityQueue <Integer> nyQueue = new PriorityQueue<Integer>();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            int x = Integer.parseInt(line);
            queue.add(x);
        }
        traverser(queue);

    }

    public static void traverser(PriorityQueue<Integer> q) {
        if (q.size() == 0) {
            return;
        } else {
            PriorityQueue<Integer> left = new PriorityQueue<Integer>();
            int key = (q.size() / 2);

            for (int i = 0; i < key; i++) {
                int next = q.poll();
                left.offer(next);
            }

            int midten = q.poll();
            System.out.println(midten);

            traverser(q);
            traverser(left);
            return;
        }
    }
}