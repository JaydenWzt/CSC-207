package lab3;

//@author Zitan Wang, Mingguang Wang
public class DynamicArray {

	/**
	 * The default Constructor
	 */
	public DynamicArray()
	{
		this(DefaultCapacity);
	}

	/**
	 * Constructor
	 * It then copies the elements of {@code arr} into the front of the new array in the same order that they appear in {@code arr}.
	 * @param capacity The number of items the array can store.
	 */
	public DynamicArray(int capacity)
	{
		Array = new Object[capacity];
		Count = 0;
		Capacity = capacity;
	}
	
    /**
     * Creates a new array of twice the length of {@code arr}.
     * It then copies the elements of {@code arr} into the front of the new array in the same order that they appear in {@code arr}.
     * @param arr The array to Object
     * @return Returns a new array twice the length of {@code arr} such that all of the elements of arr are copied into the new array.
     */
    public static Object[] ExpandArray(Object[] arr)
    {
        int length = arr.length;//the original length
        Object new_list[] = new Object[length*2];//create an array with twice the length
        for(int i=0; i<length; i++)//putting all items from the previous array to the new array
        {
            new_list[i] = arr[i];
        }

        return new_list;
    }

	/**
	 * Creates a new array of half the length of {@code arr}.
	 * It then copies the elements of {@code arr} into the front of the new array in the same order that they appear in {@code arr}.
	 * @param arr The array to Object.
	 * @return Returns a new array half the length of {@code arr} such that all of the elements of arr are copied into the new array.
	 */
    public static Object[] ContractArray(Object[] arr)
    {
        int length = arr.length;//the original length
        Object new_list[] = new Object[length/2];//create an array with half the length
        for(int i=0; i<length/2; i++)//putting all items from the previous array to the new array
		{
			new_list[i] = arr[i];
		}

        return new_list;
    }

	/**
	 * It takes an Object and adds it to the end of the array
	 * @param data A Object to be added in
	 */
	public void Add(Object data)
	{
		//Check if there is still enough sapce
		if(Count == Capacity)
		{
			Array = ExpandArray(Array);
			Capacity *= 2;
		}

		//Add data to the end of the array
		Array[Count] = data;
		Count++;
		return;
	}

	/**
	 *  If the int value is between 0 and Count() (both inclusive),
	 *  it shifts all values at or after that position right one and then adds the Object to that position.
	 *  It returns true if the object was added and false otherwise.
	 * @param data A Object to be added in。
	 * @param index The index data will be add to
	 * @return boolean Indicating whether data is added successfully
	 */
	public boolean Add(Object data, int index)
	{
		//false if index not reachable
		if(index < 0 || index > Count)
		{
			return false;
		}

		else
		{
			//Check if there is still enough space
			if(Count >= Capacity-1)
			{
				Array = ExpandArray(Array);
				Capacity *= 2;
			}
			//Adding the new Object in and declare varaibles to store Objects temporarily
			Object temp1 = Array[index];
			Object temp2;
			Array[index] = data;
			//Shifting all values at or after that position right one
			for(int i = index + 1; i <= Count; i++)
			{
				temp2 = Array[i];
				Array[i] = temp1;
				temp1 = temp2;
				temp2 = Array[i+1];
			}
			Count++; //Increments the number of item in the array
			return true;
		}
	}

	/**
	 *  It takes a DynamicArray and adds every element in it
	 *  to the end of the array in the order they appear in the parameter’s array.
	 * @param arr2 Another DynamicArray
	 */
	public void AddAll(DynamicArray arr2)
	{
		//Adding all Objects to Array one by one
		for(int i = 0; i< arr2.Count; i++)
		{
			this.Add(arr2.Get(i));
		}
		return;
	}

	/**
	 *  It takes an int index, removes the element in the array at that index,
	 *  shifts all items after that index left one, and returns the removed element.
	 *  If the index is less than 0 or at least Count(), it returns null and does nothing.
	 * @param index The int reprenting the index of a Object to be removed
	 * @return boolean Whehter that Object at that position was removed successfully
	 */
	public Object Remove(int index)
	{
		//Check whether index is reachable
		if(index < 0 || index >= Count)
			return null;
		else
		{	Object temp = Array[index]; //Keep the Object at index
			//Move all Objects after index left one.
			for(int i = index; i < Count - 1; i++)
			{
				Array[i] = Array[i+1];
			}
			Array[Count - 1] = null;
			Count--;
			//Contract the array when there is to many space
			if(Capacity > 10 && Count < (Capacity/2))
			{
				Array = ContractArray(Array);
				Capacity/=2;
			}
			return temp;
		}
	}

	/**
	 *  It takes an Object, removes the first occurrence of that object in the array (if any),
	 *  and returns a boolean.
	 *  If an object was removed, it returns true. If not, then it returns false.
	 * @param data The Object to be removed
	 * @return boolean Whether that Object was removed successfully
	 */
	public boolean Remove(Object data)
	{
		//Scanning Array to find data
		for(int i = 0; i < Count; i++)
		{
			if(Array[i].equals(data))
			{
				this.Remove(i);//remove data from Array
				return true;
			}
		}
		return false;
	}

	/**
	 *  It takes an int index that returns the element at that index or null if the index is out of bounds
	 * @param index The int reprenting the index of a Object to be getten.
	 * @return Object The Object at position index.
	 */
	public Object Get(int index)
	{
		if(index<Capacity && index>=0)
			return Array[index];
		return null;
	}

	/**
	 *  It takes an Object and an int index (in that order).
	 *  It sets the value at the given index to the given Object and returns the old value at that index.
	 *  If the index was out of bounds, it instead does nothing and returns null.
	 * @param data The Object to be put in.
	 * @param index The int representing the index data to be put to.
	 * @return Object The Object that was at Array[index] previously.
	 */
	public Object Set(Object data, int index)
	{
		//Check whether index is reachable
		if(index>=Count || index<0)
		{
			return null;
		}
		Object temp = Array[index];
		Array[index] = data;
		return temp;
	}

	/**
	 *  It takes an Object and returns either the int index
	 *  of its first occurrence in the array or -1 if it is not in the array
	 * @param data The Object to be found.
	 * @return int The index of data in Array
	 */
	public int IndexOf(Object data)
	{
		//scanning Array to find data
		for(int i = 0; i < Count; i++)
		{
			if(Array[i].equals(data))
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 *  It takes an Object and returns a boolean true
	 *  if that object is present in the array and false otherwise
	 * @param data The Object to be found.
	 * @return boolean Whether Array contains this Object or not
	 */
	public boolean Contains(Object data)
	{
		return (this.IndexOf(data) != -1);
	}

	/**
	 *  It empties the array
	 */
	public void Clear()
	{
		//reset the array
		Array = new Object[Capacity];
		Count = 0;
		return;
	}

	/**
	 *  It returns the int number of items in the array
	 * @return int The number of items
	 */
	public int Count()
	{
		return Count;
	}

	/**
	 *  It returns the int number of capacity in the array
	 * @return int The capacity
	 */
	public int Capacity()
	{
		return Capacity;
	}
	
	protected Object[] Array;
	protected int Count;
	protected int Capacity;
	
	protected static final int DefaultCapacity = 10;
}
