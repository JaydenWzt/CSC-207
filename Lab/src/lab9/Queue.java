package lab9;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A queue data structure.
 * @author Zitan Wang, Mingguang Wang
 * @param <T> The type of data stored in the queue.
 */
public class Queue<T> implements IQueue<T> {

	/**
	 * The default constructor
	 */
	public Queue()
	{
		QueueArr = new ArrayList<T>();
		First = -1;
	}
	
	/**
	 * Constructor
	 * @param i The Iterable with data to be added to the Queue
	 */
	public Queue(Iterable<T> i)
	{
		QueueArr = new ArrayList<T>();
		for(T t: i)
		{
			if(First == -1)
				First = 1;
			QueueArr.add(t);
		}
	}
	
	/**
	 * Returns the iterator of this Queue
	 */
	@Override
	public Iterator<T> iterator() {
		return QueueArr.iterator();
	}

	/**
	 * Adds {@code t} to the queue.
	 * @param t The item to add to the queue.
	 * @return Returns true if this queue was changed as a result of this call and false otherwise.
	 */
	@Override
	public boolean Enqueue(T t) {
		int count = Count();
		QueueArr.add(t);
		return Count() == count + 1;
	}

	/**
	 * Adds every element of {@code c} to the queue.
	 * @param c The elements to add to the queue.
	 * @return Returns true if this queue was changed as a result of this call and false otherwise.
	 * @throws NullPointerException Thrown if {@code c} is null.
	 */
	@Override
	public boolean EnqueueAll(Iterable<? extends T> c) {
		int count = Count();
		for(T t: c)
			QueueArr.add(t);
		return Count() > count;
	}

	/**
	 * Gets and removes the front item from the queue.
	 * @return Returns the front item of the queue.
	 * @throws NoSuchElementException Thrown if the queue is empty.
	 */
	@Override
	public T Dequeue() {
		if(First == -1)
			throw new NoSuchElementException();
		return QueueArr.remove(First);
	}

	/**
	 * Gets the front of the queue without removing it.
	 * @return Returns the front item of the queue.
	 * @throws NoSuchElementException Thrown if the queue is empty.
	 */
	@Override
	public T Front() {
		if(First == -1)
			throw new NoSuchElementException();
		return QueueArr.get(First);
	}

	/**
	 * Clears the queue.
	 */
	@Override
	public void Clear() {
		QueueArr = new ArrayList<T>();
		First = -1;
	}

	/**
	 * The number of items in the queue.
	 */
	@Override
	public int Count() {
		return QueueArr.size();
	}

	/**
	 * Determines if the queue is empty.
	 * @return Returns true if the queue is empty and false otherwise.
	 */
	@Override
	public boolean IsEmpty() {
		return First == -1;
	}
	
	protected int First; // The index of the first element (0 is have elements, -1 id empty)
	protected ArrayList<T> QueueArr; //The back up ArrayList

}
