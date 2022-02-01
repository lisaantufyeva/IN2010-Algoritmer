import java.util.HashMap;
import java.util.Scanner;

public class Katten {
    static HashMap<String, String> tree;

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        String start = input.next();

        input.nextLine();
        tree = new HashMap<>();
        while (true) {
            String s = input.next();
            if (s.equals("-1")) {
                break;
            } else {
                String[] noder = input.nextLine().split(" ");
                for (int i = 0; i < noder.length; i++) {
                    tree.put(noder[i], s);
                }
            }
        }
        findPath(start);

        input.close();

    }

    static void findPath(String start) {
        System.out.println(start);
        if (tree.containsKey(start)) {
            findPath(tree.get(start));

        }
    }
}
