package lab3;

public class DynamicArray {

	public DynamicArray()
	{
		this(DefaultCapacity);
	}
	
	public DynamicArray(int capacity)
	{
		Array = new Object[capacity];
		Count = 0;
		Capacity = capacity;
	}
	
    /**
     * Creates a new array of twice the length of {@code arr}.
     * It then copies the elements of {@code arr} into the front of the new array in the same order that they appear in {@code arr}.
     * For example, an input {1,2,3} returns a new array containing {1,2,3,-,-,-}, where - denotes an null value.
     * @param arr The array to double.
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
    
    public static Object[] ContractArray(Object[] arr)
    {
        int length = arr.length;//the original length
        Object new_list[] = new Object[length/2];//create an array with twice the length
        for(int i=0; i<length/2; i++)//putting all items from the previous array to the new array
        {
            new_list[i] = arr[i];
        }

        return new_list;
    }
	
	public void Add(Object data)
	{
		if(Count == Capacity)
		{
			Array = ExpandArray(Array);
			Capacity *= 2;
		}
		
		Array[Count] = data;
		Count++;
		return;
	}
	
	public boolean Add(Object data, int index)
	{
		if(index < 0 || index > Count)
		{
			return false;
		}
		
		else
		{
			if(Count == Capacity)
			{
				Array = ExpandArray(Array);
				Capacity *= 2;
			}
			Object temp1 = Array[index];
			Object temp2;
			Array[index] = data;
			for(int i = index + 1; i < Count; i++)
			{
				temp2 = Array[i];
				Array[i] = temp1;
				temp1 = temp2;
				temp2 = Array[i+1];
			}
			Count++;
			return true;
		}
	}
	
	public void AddAll(DynamicArray arr2)
	{
		for(int i = 0; i< arr2.Count; i++)
		{
			this.Add(arr2.Get(i));
		}
		return;
	}
	
	public Object Remove(int index)
	{
		if(index < 0 || index >= Count)
			return null;
		else
		{	Object temp = Array[index];
			for(int i = index; i < Count - 1; i++)
			{
				Array[i] = Array[i+1];
			}
			Count--;
			if(Capacity > 10 && Count < (Capacity/2))
			{
				Array = ContractArray(Array);
			}
			return temp;
		}
	}
	
	public boolean Remove(Object data)
	{
		return false;
	}
	
	public Object Get(int index)
	{
		return null;
	}
	
	public Object Set(Object data, int index)
	{
		return null;
	}
	
	public int IndexOf(Object data)
	{
		return -1;
	}
	
	public boolean Contains(Object data)
	{
		return false;
	}
	
	public void Clear()
	{
		return;
	}
	
	public int Count()
	{
		return Count;
	}
	
	public int Capacity()
	{
		return Capacity;
	}
	
	protected Object[] Array;
	protected int Count;
	protected int Capacity;
	
	protected static final int DefaultCapacity = 10;
}
