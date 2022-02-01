import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;

class Teque {
    public LinkedList<Integer> list = new LinkedList<>();
    public int itemsNumber;

    public void push_back(int x) {
        itemsNumber++;
        list.addLast(x);
    }

    public void push_front(int x) {
        itemsNumber++;
        list.addFirst(x);
    }

    public void push_middle(int x) {
        int mid = (itemsNumber + 1) / 2;
        list.add(mid, x);
        itemsNumber++;
    }

    public int get(int i) {
        return list.get(i);
    }

    public int size() {
        return itemsNumber;
    }

    public static void main(String[] args) throws IOException {
        Teque t = new Teque();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        for (int i = 0; i < N; i++) {
            String[] line = br.readLine().split(" ");
            String cmd = line[0].trim();
            int x = Integer.parseInt(line[1]);

            if (cmd.equals("push_back")) {
                t.push_back(x);
            } else if (cmd.equals("push_front")) {
                t.push_front(x);
            } else if (cmd.equals("push_middle")) {
                t.push_middle(x);
            } else if (cmd.equals("get")) {
                System.out.println(t.get(x));
            }

        }

    }

}
