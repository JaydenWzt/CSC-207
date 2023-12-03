package lab14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * The implementation of chained hash table
 * @param <T> The generic type of items to be stored in the hash table
 * @author Zitan Wang, Mingguang Wang
 */
public class HashTable<T> implements Collection<T> {

    /**
     * The constructor takes all three parameters
     * @param itr Containing items to be populated in
     * @param capacity The initial capacity of the hash table
     * @param loadFactor The load factor of the hash table
     */
    public HashTable(Iterable<T> itr, int capacity, double loadFactor)
    {
        if(itr == null)
            throw new NullPointerException();

        if(capacity <= 0 || loadFactor < 0.55)
            throw new IllegalArgumentException();

        InitialCapacity = capacity;
        CurrentCapacity = capacity;
        LoadFactor = loadFactor;

        HashTable = new LinkedList[capacity];

        for(int i = 0; i < HashTable.length; i++)
            HashTable[i] = new LinkedList();

        for(T t: itr)
        {
            if(t == null)
                throw new NullPointerException();
            add(t);
        }
    }

    /**
     * The constructor takes capacity and loadFactor
     * @param capacity The initial capacity of the hash table
     * @param loadFactor The load factor of the hash table
     */
    public HashTable(int capacity, double loadFactor)
    {
        if(capacity <= 0 || loadFactor < 0.55)
            throw new IllegalArgumentException();

        HashTable = new LinkedList[capacity];
        for(int i = 0; i < HashTable.length; i++)
            HashTable[i] = new LinkedList();

        InitialCapacity = capacity;
        CurrentCapacity = capacity;
        LoadFactor = loadFactor;
    }

    /**
     * The constructor takes only the iterable
     * @param itr Containing items to be populated in
     */
    public HashTable(Iterable<T> itr)
    {
        this(itr, 16, 0.75);
    }

    /**
     * The constructor takes only the capacity
     * @param capacity The initial capacity of the hash table
     */
    public HashTable(int capacity)
    {
        this(capacity, 0.75);
    }

    /**
     * The constructor takes only the load factor
     * @param loadFactor The load factor of the hash table
     */
    public HashTable(double loadFactor)
    {
        this(16, loadFactor);
    }

    /**
     * The constructor takes the iterable and the capacity
     * @param itr Containing items to be populated in
     * @param capacity The initial capacity of the hash table
     */
    public HashTable(Iterable<T> itr, int capacity)
    {
        this(itr, capacity, 0.75);
    }

    /**
     * The constructor takes the iterable and the load factor
     * @param itr Containing items to be populated in
     * @param loadFactor The load factor of the hash table
     */
    public HashTable(Iterable<T> itr, double loadFactor)
    {
        this(itr, 16, loadFactor);
    }

    /**
     * The constructor takes no parameters
     */
    public HashTable()
    {
        this(16, 0.75);
    }

    /**
     * The size of this hash table
     * @return The size
     */
    @Override
    public int size() {
        int count = 0;
        for(LinkedList<T> l: HashTable)
        {
            for(T t: l)
                count ++;
        }
        return count;
    }

    /**
     * Whether this hash table os empty or not
     * @return True if empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Determines whether this hash table contains this item
     * @param o Th item to be found
     * @return True if found, false otherwise
     */
    @Override
    public boolean contains(Object o) {
        if(o == null)
            throw new NullPointerException();

        int hashcode = o.hashCode()%CurrentCapacity;
        if(HashTable[hashcode] == null)
            return false;
        return HashTable[hashcode].contains(o);
    }

    /**
     * The iterator of this hash table
     * @return The iterator
     */
    @Override
    public Iterator<T> iterator() {
        if(HashTable == null)
            throw new NullPointerException();

        ArrayList<T> lst = new ArrayList<>();
        for(LinkedList<T> l: HashTable)
        {
            for(T t: l)
                lst.add(t);
        }
        return lst.iterator();
    }


    /**
     * Convert the hash table to array, kind of followed the order of hashcode
     * @return The array containing all items in this hash table
     */
    @Override
    public Object[] toArray() {
        Object arr[] = new Object [size()];
        int index = 0;
        for(LinkedList<T> l: HashTable)
        {
            for(T t: l)
            {
                arr[index] = t;
                index ++;
            }
        }
        return arr;
    }

    /**
     * Convert the hash table to array, kind of followed the order of hashcode
     * @param t1s The array that will contain items from the hash table after call
     * @return The array containing all items in this hash table
     * @param <T1> The generic type of t1s and return type
     */
    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        if(t1s == null)
            throw new NullPointerException();

        if(t1s.length > size())
            t1s = (T1[]) new Object [size()];

        int index = 0;
        for(LinkedList<T> l: HashTable)
        {
            for(T t: l)
            {
                t1s[index] = (T1)t;
                index ++;
            }
        }
        return t1s;
    }

    /**
     * Add the item to the hash table
     * @param t The item
     * @return True if added successfully, false otherwise
     * @throws Exception Thrown if t is null
     */
    @Override
    public boolean add(T t) {
        if(t == null)
            throw new NullPointerException();

        int hashcode = Math.abs(t.hashCode()%CurrentCapacity);
        int count = size();
        HashTable[hashcode].add(t);
        Expand();
        return count + 1 == size();
    }

    /**
     * Remove the item to the hash table
     * @param o The item
     * @return True if removed successfully, false otherwise
     * @throws Exception Thrown if o is null
     */
    @Override
    public boolean remove(Object o) {
        if(o == null)
            throw new NullPointerException();

        int hashcode = o.hashCode()%CurrentCapacity;
        int count = size();
        if(HashTable[hashcode] == null)
            return false;
        HashTable[hashcode].remove(o);
        Contract();
        return count - 1 == size();
    }

    /**
     * Determine whether the hash table contain all items in collection
     * @param collection The collection os items to be found
     * @return true if all contained in the hash table, false otherwise
     * @throws Exception Thrown if collection is null
     */
    @Override
    public boolean containsAll(Collection<?> collection) {
        if(collection == null)
            throw new NullPointerException();

        for(Object c: collection)
        {
            if(!contains(c))
                return false;
        }
        return true;
    }

    /**
     * Add the hash table with all items in collection
     * @param collection The collection os items to be added
     * @return true if at least one was added successfully, false otherwise
     * @throws Exception Thrown if collection is null
     */
    @Override
    public boolean addAll(Collection<? extends T> collection) {
        if(collection == null)
            throw new NullPointerException();

        int count = size();
        for(T t: collection)
            add(t);
        return count < size();
    }

    /**
     * Delete the hash table with all items in collection
     * @param collection The collection os items to be deleted
     * @return true if at least one was deleted successfully, false otherwise
     * @throws Exception Thrown if collection is null
     */
    @Override
    public boolean removeAll(Collection<?> collection) {
        if(collection == null)
            throw new NullPointerException();

        int count = size();
        for(Object o: collection)
            remove(o);
        return count > size();
    }

    /**
     * Delete all the item in the hash table if not included in collection
     * @param collection The collection of item should remain in the hash table
     * @return true if all removed
     */
    @Override
    public boolean retainAll(Collection<?> collection) {
        if(collection == null)
            throw new NullPointerException();

        for(int i = 0; i < CurrentCapacity; i++)
        {
            for(int j = 0; j < HashTable[i].size(); j++)
            {
                if(!collection.contains(HashTable[i].get(i)))
                {
                    HashTable[i].remove(i);
                    i--;
                }
            }
        }
        return true;
    }

    /**
     * Clear this hash table
     */
    @Override
    public void clear() {
        HashTable = new LinkedList[InitialCapacity];
        for(int i = 0; i < HashTable.length; i++)
            HashTable[i] = new LinkedList();
        CurrentCapacity = InitialCapacity;
    }

    /**
     * It  determines the load of the hash table (size/capacity)
     * @return The load
     */
    public double Load()
    {
        return size()/(double)CurrentCapacity;
    }

    /**
     * It determines the current capacity of the hash table’s backing array
     * @return The capacity
     */
    public int Capacity()
    {
        return CurrentCapacity;
    }

    /**
     * It takes a parameter of the generic type and returns either the item in the hash table which equals it or null if no such item is found
     * @param obj The item to be found
     * @return either the item in the hash table  or null if no such item is found
     */
    public T get(T obj)
    {
        if(obj == null)
            throw new NullPointerException();

        int hashcode = Math.abs(obj.hashCode()%CurrentCapacity);
        for(T t: HashTable[hashcode])
        {
            if(t.equals(obj))
                return t;
        }
        return null;
    }

    /**
     * This is called after each deletion
     */
    protected void Contract()
    {
        if(Load() > 0.25 * LoadFactor || InitialCapacity == CurrentCapacity)
            return;

        Iterator<T> data = iterator();
        HashTable = new LinkedList[CurrentCapacity/2];
        CurrentCapacity = CurrentCapacity/2;
        for(int i = 0; i < HashTable.length; i++)
            HashTable[i] = new LinkedList();
        for (Iterator<T> it = data; it.hasNext(); ) {
            T t = it.next();
            add(t);
        }
    }

    /**
     * This is called after each addition
     */
    protected void Expand()
    {
        if(Load() < LoadFactor)
            return;

        Iterator<T> data = iterator();
        HashTable = new LinkedList[CurrentCapacity * 2];
        CurrentCapacity = CurrentCapacity * 2;
        for(int i = 0; i < HashTable.length; i++)
            HashTable[i] = new LinkedList();
        for (Iterator<T> it = data; it.hasNext(); ) {
            T t = it.next();
            add(t);
        }


    }

    //The backing data sctructure
    protected LinkedList<T> HashTable[];

    //The load factor of this hash table
    protected final double LoadFactor;

    //The initial capacity of this hash table
    protected final int InitialCapacity;

    //The current capacity if this hash table
    protected int CurrentCapacity;
}
