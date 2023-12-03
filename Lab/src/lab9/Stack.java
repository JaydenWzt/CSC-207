package lab9;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A stack data structure.
 * @author Zitan Wang, Mingguang Wang
 * @param <T> The type to store in the stack.
 */
public class Stack<T> implements IStack<T>{

	/**
	 * The default constructor
	 */
	public Stack()
	{
		StackArr = new ArrayList<T>();
		TopPosition = -1;
	}
	
	/**
	 * Constructor
	 * @param i The Iterable with data to be added to the Stack
	 */
	public Stack(Iterable<T> i)
	{
		StackArr = new ArrayList<T>();
		TopPosition = -1;
		for(T t: i)
		{
			StackArr.add(t);
			TopPosition ++;
		}
	}


	/**
	 * Pushes {@code t} onto the top of the stack.
	 * @param t The element to push onto the stack.
	 * @return Returns true if the element was pushed onto the stack and false otherwise.
	 */
	@Override
	public boolean Push(T t) {
		int count = Count();
		StackArr.add(t);
		TopPosition ++;
		return Count() == count + 1;
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
		int count = Count();
		for(T t:c)
		{
			StackArr.add(t);
			TopPosition ++;
		}
		return Count() > count;
	}

	/**
	 * Pops the top element off the stack.
	 * @throws NoSuchElementException Thrown if the stack is empty.
	 */
	@Override
	public T Pop() {
		if(TopPosition == -1)
			throw new NoSuchElementException();
		
		TopPosition--;
		return StackArr.remove(TopPosition + 1);
	}

	/**
	 * Obtains the top element of the stack without removing it.
	 * @throws NoSuchElementException Thrown if the stack is empty.
	 */
	@Override
	public T Peek() {
		if(TopPosition == -1)
			throw new NoSuchElementException();
		
		return StackArr.get(TopPosition);
	}

	/**
	 * Clears the stack.
	 */
	@Override
	public void Clear() {
		StackArr = new ArrayList<T>();
		TopPosition = -1;
	}

	/**
	 * The number of items in the stack.
	 */
	@Override
	public int Count() {
		return TopPosition + 1;
	}

	/**
	 * Determines if the stack is empty.
	 * @return Returns true if the stack is empty and false otherwise.
	 */
	@Override
	public boolean IsEmpty() {
		return TopPosition == -1;
	}
	
	/**
	 */
	@Override
	public Iterator<T> iterator() {
		return StackArr.iterator();
	}
	
	protected ArrayList<T> StackArr;
	protected int TopPosition;
}
