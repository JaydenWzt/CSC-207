package lab13;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

import lab11.RedBlackTree;
import lab12.HeapTree;

import lab9.IQueue;

public class PriorityQueue<T> implements IQueue<T>{

    public PriorityQueue (Comparator<T> cmp) {
        if(cmp==null) {
            throw new NullPointerException();
        }
        this.cmp = cmp;
        this.heap=new HeapTree<T>(this.cmp);
    }

    public PriorityQueue (Iterable<? extends T> seed, Comparator<T> cmp) {

        if(cmp==null) {
            throw new NullPointerException();
        }

        if(seed==null) {
            throw new NullPointerException();
        }

        this.cmp =cmp;
        this.heap=new HeapTree<T>(seed, this.cmp);
    }


    public Iterator iterator() {
        ArrayList<T> lst = new ArrayList<>();
        Iterator<T> itr = heap.iterator();
        for (Iterator<T> it = itr; it.hasNext(); ) {
            T t = it.next();
            lst.add(t);
        }
        lst.sort(cmp);
        return lst.iterator();

    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean Enqueue(Object t) {
        if(t==null) {
            throw new NullPointerException();
        }
        int num= heap.Count();
        heap.Add((T)t);

        if(num==(heap.Count()-1)) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean EnqueueAll(Iterable<? extends T>  c) {
        // TODO Auto-generated method stub
        if(c==null) {
            throw new NullPointerException();
        }

        int num=this.heap.Count();

        for(Object o: c) {
            Enqueue(o);
        }

        if(this.heap.Count()>num) {
            return true;
        }
        else {
            return false;
        }

    }

    @Override
    public T Dequeue() {
        if(this.heap.IsEmpty()) {
            throw new NoSuchElementException();
        }

        T temp = heap.Root();

        this.heap.Remove(temp);

        return temp;
    }

    @Override
    public T Front() {

        if(this.heap.IsEmpty()) {
            throw new NoSuchElementException();
        }

        return heap.Root();
    }

    @Override
    public void Clear() {
        this.heap=new HeapTree<T>(this.cmp);
    }

    @Override
    public int Count() {
        return heap.Count();
    }

    @Override
    public boolean IsEmpty() {
        return heap.IsEmpty();
    }




    protected Comparator<T> cmp=null;
    protected HeapTree<T> heap=null;


}