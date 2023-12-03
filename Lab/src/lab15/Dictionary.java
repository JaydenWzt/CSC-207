package lab15;

import lab14.HashTable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * The implementation of Dictionary using hashtable as backing data structure
 * @param <K> The generic type representing the type of keys
 * @param <V> THe generic type representing the type of values
 * @author Zitan Wang
 */
public class Dictionary<K,V> implements IMap<K,V>{

    /**
     * The constructor
     */
    public Dictionary()
    {
        Table = new HashTable<>();
    }

    /**
     * Adds a key-value pair to the map if the key does not already exist.
     *
     * @param key   The key to add.
     * @param value The value to map {@code key} to.
     * @return Returns true if the key-value pair could be added and false otherwise.
     * @throws NullPointerException Thrown if {@code key} is null and the map does not permit null keys.
     */
    @Override
    public boolean Add(K key, V value) {
        if(key == null)
             throw new NullPointerException();
        Pair p = new Pair(key, value);
        if(Table.contains(p))
        {
            return false;
        }
        else
        {
            Table.add(p);
            return true;
        }
    }

    /**
     * Puts a key-value pair into the map.
     * If {@code key} is already in the map, then its mapping is overwritten with this new mapping.
     *
     * @param key   The key to add.
     * @param value The value to map key to.
     * @return Returns the old value associated with {@code key} if any exists or {@code value} otherwise.
     * @throws NullPointerException Thrown if {@code key} is null and the map does not permit null keys.
     */
    @Override
    public V Put(K key, V value) {
        if(key == null)
            throw new NullPointerException();
        Pair<K, V> p = new Pair(key, value);
        Pair<K, V> prev = Table.get(p);
        if(prev != null)
            Table.remove(p);
        Table.add(p);

        if(prev != null)
            return prev.E2;
        else
            return value;

    }

    /**
     * Removes the map entry with key {@code key} if it exists.
     *
     * @param key The key to remove.
     * @return Returns true if the key was removed and false otherwise.
     */
    @Override
    public boolean Remove(K key) {
        Pair<K, V> p = new Pair(key, null);
        if(Table.contains(p))
        {
            Table.remove(p);
            return true;
        }
        return false;
    }

    /**
     * Gets the value associated with {@code key}.
     *
     * @param key The key to search for.
     * @return Returns the value associated with {@code key}.
     * @throws NoSuchElementException Thrown if the key is not in the map.
     */
    @Override
    public V Get(K key) {
        Pair<K, V> p = new Pair(key, null);
        if(Table.contains(p))
            return Table.get(p).E2;
        else
            throw new NoSuchElementException();
    }

    /**
     * Gets the value associated with {@code key}.
     *
     * @param key The key to search for.
     * @return Returns the value associated with {@code key} or null if it does not exist.
     * @implNote Note that if this map permits null entry values, the value returned may be a null value and indistinguishable from an absent value.
     */
    @Override
    public V TryGet(K key) {
        Pair<K, V> p = new Pair(key, null);
        if(Table.contains(p))
            return Table.get(p).E2;
        else
            return null;
    }

    /**
     * Determines if the map contains the key {@code k}.
     *
     * @param key
     * @return Returns true if the map contains the key and false otherwise.
     */
    @Override
    public boolean Contains(K key) {
        return Table.contains(new Pair<K, V>(key, null));
    }

    /**
     * Clears the map of all entries.
     */
    @Override
    public void Clear() {
        Table.clear();
    }

    /**
     * Determines the number of entries in the map.
     */
    @Override
    public int Count() {
        return Table.size();
    }

    /**
     * Obtains the keys in the map.
     *
     * @return Returns the keys in the map. They are guaranteed to appear in the same order as their values as obtained from {@code Values}.
     */
    @Override
    public Iterable<K> Keys() {
        return new Iterable<K>()
        {
            public Iterator<K> iterator()
            {
                return new Iterator<K>()
                {
                    public boolean hasNext()
                    {
                        return itr.hasNext();
                    }

                    public K next()
                    {
                        if(!hasNext())
                            throw new NoSuchElementException();
                        return itr.next().E1;
                    }
                    Iterator<Pair<K, V>> itr = Table.iterator();
                };
            }
        };
    }

    /**
     * Obtains the values in the map.
     *
     * @return Returns the values in the map. They are guaranteed to appear in the same order as their keys as obtained from {@code Keys}.
     */
    @Override
    public Iterable<V> Values() {
        return new Iterable<V>()
        {
            public Iterator<V> iterator()
            {
                return new Iterator<V>()
                {
                    public boolean hasNext()
                    {
                        return itr.hasNext();
                    }

                    public V next()
                    {
                        if(!hasNext())
                            throw new NoSuchElementException();
                        return itr.next().E2;
                    }
                    Iterator<Pair<K, V>> itr = Table.iterator();
                };
            }
        };
    }

    //The backing data structure
    HashTable<Pair<K, V>> Table;

    /**
     * The inner class represeting a pair in dictionary
     * @param <K> The generic type representing the type of keys
     * @param <V> THe generic type representing the type of values
     * @author Zitan Wang
     */
    public static class Pair<K, V>
    {
        /**
         * The constructor
         * @param element1 The first element: key
         * @param element2 The second element: value
         */
        public Pair(K element1, V element2)
        {
            E1 = element1;
            E2 = element2;
        }


        /**
         * Compare two objects
         * @param obj The object to be compared
         * @return True if obj is Pair and the first entry is the same, false otherwise
         */
        @Override
        public boolean equals(Object obj) {
            if(obj == null)
                return false;

            if(this == obj)
                return true;

            if(obj instanceof Pair)
            {
                Pair p = (Pair)obj;
                return Objects.equals(E1, p.E1);
            }

            return false;
        }


        /**
         * The hashcode is of a pair is the hashcode of element 1, the key
         * @return
         */
        @Override
        public int hashCode() {
            return E1.hashCode();
        }

        //The key of this pair
        K E1;
        //The value of this pair
        V E2;
    }

}
