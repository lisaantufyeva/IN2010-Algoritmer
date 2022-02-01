import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.LinkedList;

class BalanceArray {

    public static void main(String[] args) throws IOException {
        LinkedList<Integer> list = new LinkedList<>();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            int x = Integer.parseInt(line);
            list.add(x);
        }
        traverser(list);

    }

    public static List<Integer> traverser(List<Integer> l) {

        if (l.isEmpty()) {
            return l;
        } else {
            int midten = (l.size() / 2);
            int verdi = l.get(midten);
            List<Integer> newList = new LinkedList<>();

            newList.add(verdi);
            newList.addAll(traverser2(l.subList(midten + 1, l.size())));
            newList.addAll(traverser2(l.subList(0, midten)));

            return newList;
        }
    }
}
