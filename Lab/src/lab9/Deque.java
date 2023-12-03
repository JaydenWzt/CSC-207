package lab9;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A double-ended queue.
 * Allows addition and removal from either the front or the back of the queue.
 * This, of course, allows it to function as either a queue or a stack.
 * @author Zitan Wang, Mingguang Wang
 * @param <T> The type to store in the deque.
 */

public class Deque<T> implements IDeque<T> {
	
	public Deque()
	{
		First = -1;
		Last = -1;
		DequeArr = new ArrayList<T>();
	}
	
	public Deque(Iterable<T> i)
	{
		First = -1;
		Last = -1;
		DequeArr = new ArrayList<T>();
		for(T t: i)
		{
			if(First == -1)//Change First id there is not elements in Deque
				First = 0;
			DequeArr.add(t);
			Last ++;
		}
	}
	/**
	 * Adds {@code t} to the front of the deque.
	 * @param t The element to add.
	 * @return Returns true if the element was added and false otherwise.
	 */
	public boolean AddFront(T t)
	{
		int count = Count();
		if(First == -1)
			First = 0;
		DequeArr.add(First, t);
		Last ++;
		return Count() == count + 1;
	}
	
	/**
	 * Adds every element of {@code c} to the front of the deque.
	 * @param c The elements to add.
	 * @return Returns true if at least one element was added and false otherwise.
	 * @throws NullPointerException Thrown if {@code c} is null.
	 */
	public boolean AddAllFront(Iterable<? extends T> c)
	{
		if(c == null)
			throw new NullPointerException();
		int count = Count();
		int index = 0;
		for(T t: c)
		{
			if(First == -1)//Change First id there is not elements in Deque
				First = 0;
			
				DequeArr.add(index ++, t);//Add elements in c one by one to the front
			Last ++;
		}
		return Count() > count;
	}
	
	/**
	 * Obtains the front of the deque without removing it.
	 * @throws NoSuchElementException Thrown if the deque is empty.
	 */
	public T Front()
	{
		if(IsEmpty())
			throw new NoSuchElementException();
		return DequeArr.get(First);
	}
	
	/**
	 * Obtains and removes the front of the deque.
	 * @throws NoSuchElementException Thrown if the deque is empty.
	 */
	public T PollFront()
	{
		if(IsEmpty())
			throw new NoSuchElementException();
		Last --;
		return DequeArr.remove(First);
	}
	
	/**
	 * Adds {@code t} to the back of the deque.
	 * @param t The element to add.
	 * @return Returns true if the element was added and false otherwise.
	 */
	public boolean AddBack(T t)
	{
		int count = Count();
		if(First == -1)//When Deque is empty
			First = 0;
		DequeArr.add(t);
		Last ++;
		return Count() == count + 1;
	}
	
	/**
	 * Adds every element of {@code c} to the back of the deque.
	 * @param c The elements to add.
	 * @return Returns true if at least one element was added and false otherwise.
	 * @throws NullPointerException Thrown if {@code c} is null.
	 */
	public boolean AddAllBack(Iterable<? extends T> c)
	{
		if(c == null)
			throw new NullPointerException();
		int count = Count();
		for(T t: c)
		{
			if(First == -1)//When Deque is empty
				First = 0;
			DequeArr.add(t);
			Last ++;
		}
		return Count() > count;
	}
	
	/**
	 * Obtains the back of the deque without removing it.
	 * @throws NoSuchElementException Thrown if the deque is empty.
	 */
	public T Back()
	{
		if(IsEmpty())
			throw new NoSuchElementException();
		return DequeArr.get(Last);
	}
	
	/**
	 * Obtains and removes the back of the deque.
	 * @throws NoSuchElementException Thrown if the deque is empty.
	 */
	public T PollBack()
	{
		if(IsEmpty())
			throw new NoSuchElementException();
		return DequeArr.remove(Last);
	}
	
	/**
	 * Clears the deque.
	 */
	public void Clear()
	{
		First = -1;
		Last = -1;
		DequeArr = new ArrayList<T>();
	}
	
	/**
	 * The number of items in the deque.
	 */
	public int Count()
	{
		return DequeArr.size();
			
	}
	
	/**
	 * Determines if the deque is empty.
	 * @return Returns true if the deque is empty and false otherwise.
	 */
	public boolean IsEmpty()
	{
		return Count() == 0;
	}
	
	/**
	 * Pushes {@code t} onto the top of the stack.
	 * @param t The element to push onto the stack.
	 * @return Returns true if the element was pushed onto the stack and false otherwise.
	 */
	@Override
	public boolean Push(T t) {
		return this.AddBack(t);
	}

	/**
	 * Pushes all of the elements of {@code c} onto the top of the stack.
	 * The last element of {@code c} is the first to pop off the stack.
	 * The first element of {@code c} is the last element to pop off the stack.
	 * @param c The elements to add to the stack.
	 * @return Returns true if at least one item was added to the stack and false otherwise.
	 * @throws NullPointerException Thrown if {@code c} is null.
	 */
	@Override
	public boolean PushAll(Iterable<? extends T> c) {
		if(c == null)
			throw new NullPointerException();
		return this.AddAllBack(c);
	}
	
	/**
	 * Pops the top element off the stack.
	 * @throws NoSuchElementException Thrown if the stack is empty.
	 */
	@Override
	public T Pop() {
		if(IsEmpty())
			throw new NoSuchElementException();
		return this.PollBack();
	}

	/**
	 * Obtains the top element of the stack without removing it.
	 * @throws NoSuchElementException Thrown if the stack is empty.
	 */
	@Override
	public T Peek() {
		if(IsEmpty())
			throw new NoSuchElementException();
		return this.Back();
	}

	@Override
	public Iterator<T> iterator() {
		return DequeArr.iterator();
	}

	/**
	 * Adds {@code t} to the queue.
	 * @param t The item to add to the queue.
	 * @return Returns true if this queue was changed as a result of this call and false otherwise.
	 */
	@Override
	public boolean Enqueue(T t) {
		return this.AddBack(t);
	}

	/**
	 * Adds every element of {@code c} to the queue.
	 * @param c The elements to add to the queue.
	 * @return Returns true if this queue was changed as a result of this call and false otherwise.
	 * @throws NullPointerException Thrown if {@code c} is null.
	 */
	@Override
	public boolean EnqueueAll(Iterable<? extends T> c) {
		if(c == null)
			throw new NullPointerException();
		return this.AddAllBack(c);
	}

	/**
	 * Gets and removes the front item from the queue.
	 * @return Returns the front item of the queue.
	 * @throws NoSuchElementException Thrown if the queue is empty.
	 */
	@Override
	public T Dequeue() {
		if(IsEmpty())
			throw new NoSuchElementException();
		return this.PollFront();
	}
	
	protected int First;//The index of the first element in Deque (-1 if empty)
	protected int Last;// The index of the last element in Deque (-1 if empty)
	protected ArrayList<T> DequeArr;//The back up ArrayList
	
}
