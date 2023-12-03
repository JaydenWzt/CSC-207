package lab13;

import lab12.HeapTree;

import java.util.LinkedList;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        PriorityQueue<Integer> P = new PriorityQueue<Integer>((a, b) -> a.compareTo(b));
        /*


        for (int i = 5; i >= 0; i--) {
            P.Enqueue(i);
        }

        for (int i = 5; i >= 0; i--) {
            P.Dequeue();
         */

        Random rand = new Random();


        // Test a battery of random trees
        // We'll probably run into every Add/Remove case this way without having to think about it
        for (int i = 0; i < 10000; i++) {
            int len = 10;//rand.nextInt(10);
            LinkedList<Integer> l = new LinkedList<Integer>();

            // Add a bunch of random data
            for (int j = 0; j < len; j++) {
                int k = rand.nextInt(100);

                if (P.Enqueue(k)) {
                    l.add(k); // We'll save this for later
                }
            }

            // Now let's randomly remove everything from the tree
            while (!l.isEmpty()) {
                int k = rand.nextInt(l.size());
                P.Dequeue();
            }
        }


        return;
    }
}
