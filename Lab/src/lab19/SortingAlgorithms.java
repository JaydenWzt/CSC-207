package lab19;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import lab11.RedBlackTree;
import lab13.PriorityQueue;

/**
 * Pure comparison-based sorting algorithms.
 * @author Dawn Nye
 */
public final class SortingAlgorithms
{
    /**
     * No one will make this forbidden class.
     * If only static classes were a thing in Java (the inner static class doesn't count).
     */
    private SortingAlgorithms()
    {return;}

    /**
     * Performs a bubble sort of {@code arr}.
     * When finished, this algorithm leaves {@code arr} in ascending order according to {@code cmp}.
     * @param <T> The type of data being sorted.
     * @param arr The array to sort.
     * @param cmp The means by which items are compared.
     * @throws NullPointerException Thrown if {@code arr} or {@code cmp} is null.
     * @implNote This sort must run in O(n) best-case time and O(n^2) average and worst-case time.
     */
    public static <T> void BubbleSort(T[] arr, Comparator<? super T> cmp)
    {
        if (arr == null || cmp == null)
            throw new NullPointerException();

        boolean swapped = true;
        while (swapped)//continue to loop until no swaps are made
        {
            swapped = false;
            for (int i = 0; i < arr.length - 1; i++)
            {
                if (cmp.compare(arr[i], arr[i + 1]) > 0)//swap the two adjacent elements if out of order
                {
                    T temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                    swapped = true;//mark that a swap was made
                }
            }
        }
    }

    /**
     * Performs an heap sort of {@code arr}.
     * When finished, this algorithm leaves {@code arr} in ascending order according to {@code cmp}.
     * @param <T> The type of data being sorted.
     * @param arr The array to sort.
     * @param cmp The means by which items are compared.
     * @throws NullPointerException Thrown if {@code arr} or {@code cmp} is null.
     * @implNote This sort must run in O(n log n) worst-case time.
     */
    public static <T> void HeapSort(T[] arr, Comparator<? super T> cmp)
    {
        if (arr == null || cmp == null)
            throw new NullPointerException();

        PriorityQueue<T> heap = new PriorityQueue<T>((Comparator<T>) cmp);
        for(T item: arr)//add all items to the heap
            heap.Enqueue(item);

        for(int i = 0; i < arr.length; i++)//put elements back into the array in order
        {
            arr[i] = heap.Dequeue();
        }
    }

    /**
     * Performs an insertion sort of {@code arr}.
     * When finished, this algorithm leaves {@code arr} in ascending order according to {@code cmp}.
     * @param <T> The type of data being sorted.
     * @param arr The array to sort.
     * @param cmp The means by which items are compared.
     * @throws NullPointerException Thrown if {@code arr} or {@code cmp} is null.
     * @implNote This sort must run in O(n) best-case time and O(n^2) average and worst-case time.
     */
    public static <T> void InsertionSort(T[] arr, Comparator<? super T> cmp)
    {
        if (arr == null || cmp == null)
            throw new NullPointerException();

        for(int i = 1; i < arr.length; i++)//loop through all elements
        {
            T temp = arr[i];
            int j = i - 1;
            while(j >= 0 && cmp.compare(arr[j], temp) > 0)//move elements to the right until the correct position is found
            {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = temp;//insert the element into the correct position
        }
    }

    /**
     * Performs an merge sort of {@code arr}.
     * When finished, this algorithm leaves {@code arr} in ascending order according to {@code cmp}.
     * @param <T> The type of data being sorted.
     * @param arr The array to sort.
     * @param cmp The means by which items are compared.
     * @throws NullPointerException Thrown if {@code arr} or {@code cmp} is null.
     * @implNote This sort must run in O(n log n) worst-case time.
     */
    public static <T> void MergeSort(T[] arr, Comparator<? super T> cmp)
    {
        if (arr == null || cmp == null)
            throw new NullPointerException();

        MergeSortHelper(arr, cmp, 0, arr.length - 1);

    }

    /**
     * The helper method for the merge sort algorithm, that recursively splits the array into smaller arrays
     * @param arr The array to sort
     * @param cmp The means by which items are compared
     * @param l The left index of the array
     * @param r The right index of the array
     * @param <T> The type of data being sorted
     */
    private static <T> void MergeSortHelper(T[] arr, Comparator<? super T> cmp, int l, int r)
    {
        if(l >= r)
            return;
        int m = (l + r) / 2;
        MergeSortHelper(arr, cmp, l, m);//sort the left half
        MergeSortHelper(arr, cmp, m + 1, r);//sort the right half
        Merge(arr, cmp, l, m, r);//merge the two halves
    }

    /**
     * The helper method for the merge sort algorithm, that merges two sorted arrays into one sorted array
     * @param arr The array to sort
     * @param cmp The means by which items are compared
     * @param l The left index of the array
     * @param m The middle index of the array
     * @param r The right index of the array
     * @param <T> The type of data being sorted
     */
    private static <T> void Merge(T[] arr, Comparator<? super T> cmp, int l, int m, int r)
    {
        //find the sizes of the two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        //create temporary arrays
        T [] L = (T[]) new Object[n1];
        T [] R = (T[]) new Object[n2];

        //copy the two halves into temporary arrays
        for(int i = 0; i < n1; i++)
            L[i] = arr[l + i];
        for(int j = 0; j < n2; j++)
            R[j] = arr[m + 1 + j];

        int i = 0;
        int j = 0;
        int k = l;

        while(i < n1 && j < n2)//merge the two arrays
        {
            if(cmp.compare(L[i], R[j]) <= 0)
            {
                arr[k] = L[i];
                i++;
            }
            else
            {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        while(i < n1)//copy the remaining elements of L
        {
            arr[k] = L[i];
            i++;
            k++;
        }
        while(j < n2)//copy the remaining elements of R
        {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    /**
     * Performs an recursive quick sort of {@code arr}.
     * When finished, this algorithm leaves {@code arr} in ascending order according to {@code cmp}.
     * @param <T> The type of data being sorted.
     * @param arr The array to sort.
     * @param cmp The means by which items are compared.
     * @throws NullPointerException Thrown if {@code arr} or {@code cmp} is null.
     * @implNote This sort must run in O(n log n) average-case time.
     */
    public static <T> void QuickSort(T[] arr, Comparator<? super T> cmp)
    {
        QuickSortHelper(arr, cmp, 0, arr.length-1);//call the helper method
    }

    /**
     * The helper method for the quick sort algorithm, that recursively splits the array into smaller arrays
     * @param arr The array to sort
     * @param cmp The means by which items are compared
     * @param l The left index of the array
     * @param r The right index of the array
     * @param <T> The type of data being sorted
     */
    private static <T> void QuickSortHelper(T[] arr, Comparator<? super T> cmp, int l, int r)
    {
        Random rand = new Random();
        if(l >= r)
            return;
        int p = Partition(arr, cmp, l, r);
        QuickSortHelper(arr, cmp, l, p - 1);//sort the left half
        QuickSortHelper(arr, cmp, p + 1, r);//sort the right half
    }

    /**
     * The helper method for the quick sort algorithm, that partitions the array into two halves
     * @param arr The array to sort
     * @param cmp The means by which items are compared
     * @param l The left index of the array
     * @param r The right index of the array
     * @param <T> The type of data being sorted
     * @return The index of the pivot
     */
    private static <T> int Partition(T[] arr, Comparator<? super T> cmp, int l, int r)
    {
        T pivot = arr[r];
        int i = l - 1;
        for(int j = l; j <= r - 1; j++)//iterate through the array
        {
            if(cmp.compare(arr[j], pivot) < 0)
            {
                i++;
                Swap(arr, i, j);
            }
        }
        Swap(arr, i + 1, r);//swap the pivot with the element at the index i + 1
        return i + 1;
    }

    /**
     * Swaps the elements at the given indices in {@code arr}.
     * @param arr The array in which to swap elements.
     * @param index1 The index of the first element to swap.
     * @param index2 The index of the second element to swap.
     * @param <T> The type of data being sorted.
     */
    private static <T> void Swap(T[] arr, int index1, int index2)
    {
        if(arr == null)
            throw new NullPointerException();
        if(index1 < 0 || index2 < 0 || index1 >= arr.length || index2 >= arr.length)
            throw new IndexOutOfBoundsException();

        // Swap the elements
        T temp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = temp;
    }

    /**
     * Performs a selection sort of {@code arr}.
     * When finished, this algorithm leaves {@code arr} in ascending order according to {@code cmp}.
     * @param <T> The type of data being sorted.
     * @param arr The array to sort.
     * @param cmp The means by which items are compared.
     * @throws NullPointerException Thrown if {@code arr} or {@code cmp} is null.
     * @implNote This sort must run in O(n^2) worst-case time.
     */
    public static <T> void SelectionSort(T[] arr, Comparator<? super T> cmp)
    {
        for(int i = 0; i < arr.length; i++)//goes through the array
        {
            T min = arr[i];
            int index = i;
            for(int j = i; j < arr.length; j++)//finds the minimum value
            {
                if(cmp.compare(arr[j], min) < 0)//if the value is less than the minimum value
                {
                    min = arr[j];
                    index = j;
                }
            }
            Swap(arr, i, index);//swaps the minimum value with the current index
        }
    }

    /**
     * Performs a tree sort of {@code arr}.
     * When finished, this algorithm leaves {@code arr} in ascending order according to {@code cmp}.
     * @param <T> The type of data being sorted.
     * @param arr The array to sort.
     * @param cmp The means by which items are compared.
     * @throws NullPointerException Thrown if {@code arr} or {@code cmp} is null.
     * @implNote This sort must run in O(n log n) worst-case time.
     */
    public static <T> void TreeSort(T[] arr, Comparator<? super T> cmp)
    {
        if(arr == null || cmp == null)
            throw new NullPointerException();

        RedBlackTree<T> tree = (RedBlackTree<T>) new RedBlackTree<>(cmp);//creates a new tree
        for(T element: arr)//adds the elements to the tree
            tree.Add(element);
        Iterator<T> iter = tree.iterator();
        int i = 0;
        while(iter.hasNext())//adds the elements back to the array
            arr[i++] = iter.next();
    }
}