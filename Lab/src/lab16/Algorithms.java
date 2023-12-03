package lab16;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import lab12.HeapTree;
import lab15.Dictionary;

/**
 * A collection of algorithms.
 * @author Zitan Wang
 */
public class Algorithms
{
    /**
     * Counts the number of each item in {@code items}.
     * For example, if {@code items} contains {a,b,c,a,a,b}, then it would create the mapping {a -> 3,b -> 2,c -> 1}.
     * @param <T> The type of things to count.
     * @param items The items to count. These entries must not be null.
     * @return Returns a dictionary containing the count of each each item in {@code items}.
     * @throws NullPointerException Thrown if {@code items} is null or {@code items} contains a null value.
     * @implSpec This algorithm runs in average case O(n) time.
     */
    public static <T> Dictionary<T,Integer> Count(Iterable<? extends T> items)
    {
        if(items == null)
            throw new NullPointerException();

        Dictionary<T, Integer> dictionary = new Dictionary<>();
        for(T t: items)//for every element in items
        {
            if(t == null)
                throw new NullPointerException();

            if(!dictionary.Contains(t)) //if no counted, add new one ine
            {
                dictionary.Add(t, 1);
            }
            else //if already counted, increment the count by 1
            {
                int count = dictionary.Get(t);
                dictionary.Remove(t);
                dictionary.Add(t, count + 1);
            }
        }
        return dictionary;
    }

    /**
     * Determines if there is a cycle in a function {@code func} starting from {@code initial_input}.
     * For instance, if {@code func} is {@code π sin(x)} starting from x = 0, then we obtain
     * <ul>
     * 	<li>π sin(0) = π,</li>
     * 	<li>π sin(π) = -π,</li>
     * 	<li>π sin(-π) = -π.</li>
     * </ul>
     * In this example, the cycle length is 1 because we end at a fixed point -π.
     * <br><br>
     * Another example would be the inverse sign function (returns 1 if x is positive, 0 if x is 0, and -1 if x is negative), {@code -sgn(x)}.
     * Starting from x = 1, we obtain
     * <ul>
     * 	<li>-sgn(1) = -1,</li>
     * 	<li>-sgn(-1) = 1.</li>
     * </ul>
     * Here we have a cycle length of 2.
     * If we had started from x = 2, we would still have a cycle length of 2, but it would take one attempt longer to discover.
     * If we had started from x = 0 instead, we would have a cycle length of 1 since 0 is a fixed point of {@code -sgn(x)}.
     * <br><br>
     * In contrast, the function {@code f(x) = x + 1} has no cycle since no sequence of inputs will take us to an input that we've already visited.
     * <br><br>
     * Note that user is responsible for ensuring that equality checks on types {@code T} succeed.
     * If {@code T} is {@code Double}, for instance, the user must be careful of floating point errors in function calculation.
     * @param <T> The input/output type of the function.
     * @param func The function to search for a cycle within.
     * @param initial_input The initial input to the function.
     * @param max_depth The maximum number of input-output combinations to search. The initial input and its output counts as the first.
     * @return Returns true if a cycle is found or false if there is no (discovered) cycle.
     * @throws NullPointerException Thrown if {@code func} is null or if {@code initial_input} is null.
     * @throws IllegalArgumentException Thrown if {@code max_depth} is nonpositive.
     * @implSpec
     * Suppose the cycle length is n (if no cycle will be discovered, then n = 0), the number of evaluations prior to the cycle is m, and {@code func} can be evaluated in averge case O(f) time.
     * Then the runtime of this algorithm is average case O((n + m)f).
     * In the first example above, m = 2 and n = 1.
     * In the second example, m = 0 and n = 2.
     */
    public static <T> boolean HasCycle(SingleInputFunction<T,T> func, T initial_input, int max_depth)
    {
        if(func == null || initial_input == null)
            throw new NullPointerException();
        if(max_depth <= 0)
            throw new IllegalArgumentException();

        ArrayList<T> lst = new ArrayList<>();
        for(int i = 0; i < max_depth; i++)//if a previous element occured later, then cycle
        {
            lst.add(initial_input);
            initial_input = func.eval(initial_input);
            if(lst.contains(initial_input))
                return true;
        }
        return false;
    }

    /**
     * Determines the first unique item in {@code items}.
     * For example, if {@code items} contains {a,b,c,a,a,b,d}, then it would return c.
     * @param <T> The type of things to search.
     * @param items The items to search for a unique element in. These entries must not be null.
     * @return Returns the first unique item in {@code items} or null if no such item exists.
     * @throws NullPointerException Thrown if {@code items} is null or {@code items} contains a null value.
     * @implSpec This algorithm runs in average case O(n) time.
     */
    public static <T> T FirstUniqueItem(Iterable<? extends T> items)
    {
        if(items == null)
            throw new NullPointerException();

        Dictionary<T, Integer> dictionary = Algorithms.Count(items);
        for(T t: items)//is one element occured only in time in count, then unique
        {
            if(dictionary.Get(t) == 1)
                return t;
        }
        return null;
    }

    /**
     * Determines the {@code k} smallest elements of {@code items}.
     * For example, if {@code k} = 3, {@code items} contains {1,3,6,2,7}, and {@code cmp} is the natural ordering, then the {@code k} smallest elements are 1, 2, and 3.
     * These elements are guaranteed to appear in ascending order.
     * @param <T> The type of things to search.
     * @param items The items to search for small elements in. These entries can be null if {@code cmp} knows how to compare null elements.
     * @param cmp The means by which elements are compared.
     * @param k The number of small elements we want to obtain.
     * @return Returns an iterable list of {@code k} smallest elements of {@code items}. These must be iterated in ascending order.
     * @throws NullPointerException Thrown if {@code items} or {@code cmp} is null.
     * @throws IllegalArgumentException Thrown if {@code k} is negative or if it is larger than the number of elements in {@code items}.
     * @implSpec The algorithm runs in worst case O(n + k log n) time.
     */
    public static <T> Iterable<T> KthSmallestElements(Iterable<? extends T> items, Comparator<T> cmp, int k)
    {
        if(items == null || cmp == null)
            throw new NullPointerException();
        if(k < 0)
            throw new IllegalArgumentException();

        HeapTree<T> tree = new HeapTree<>(items, cmp);

        if(k > tree.Count())
            throw new IllegalArgumentException();

        return new Iterable<T>()
        {
            public Iterator<T> iterator()
            {
                return new Iterator<T>()
                {
                    public boolean hasNext()
                    {
                        if(!tree.IsEmpty() && index < k)
                            return true;
                        else
                            return false;
                    }

                    public T next()
                    {
                        if(!hasNext())
                            throw new NoSuchElementException();
                        else
                        {
                            index++;
                            T t = tree.Root();
                            tree.Remove(t);
                            return t;
                        }
                    }
                    int index = 0;
                };
            }
        };
    }

    /**
     * Determines the majority element of {@code items}.
     * A <b>majority element</b> of a set is an element that appears more than {@code |items|}/2 times.
     * For example, if {@code items} contains {a,b,c,a,a,b}, then a is <b>not</b> the majority element because it only appears 3 times, and 3 ≯ 6/2.
     * However, if {@code items} contains {a,b,c,a,a,b,a}, then a <b>is</b> the majority element because it appears 4 times, and 4 ≯ 7/2.
     * @param <T> The type of things to search.
     * @param items The items to search for a majority element in. These entries must not be null.
     * @return Returns the unique majority element if one exists or null otherwise.
     * @throws NullPointerException Thrown if {@code items} is null or {@code items} contains a null value.
     * @implSpec This algorithm runs in average case O(n) time.
     */
    public static <T> T MajorityElement(Iterable<? extends T> items)
    {
        if(items == null)
            throw new NullPointerException();

        Dictionary<T, Integer> dictionary = Algorithms.Count(items);

        int size = 0;
        for(T t: items)
            size ++;

        for(T t: items)//if one element have count more than half, then majority
        {
            if((double)dictionary.Get(t) > (double)(0.5 * size))
                return t;
        }
        return null;


    }

    /**
     * Describes a single-input single-output function.
     * @author Dawn Nye
     * @param <I> The input type.
     * @param <O> The output type.
     */
    @FunctionalInterface public interface SingleInputFunction<I,O>
    {
        /**
         * Evaluates this function with the given input.
         * @param input The input to this function.
         * @return Returns the calculated output of this function input {@code input}.
         */
        public O eval(I input);
    }
}