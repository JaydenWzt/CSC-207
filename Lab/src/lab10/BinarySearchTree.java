package lab10;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Defines the bare bones basics of a tree.
 * An implementation of this interface may be a Cartesian tree, a segment tree, an AABB tree, or any other related data structure.
 * Whether the tree is self-balancing depends on the data structure it represents and the implementation.
 * The order of iteration for a tree depends on the implementation, but an in-order traversal is often the correct choice.
 * @author Zitan Wang, Mingguang Wang
 * @param <T> The type to store in the tree.
 */
public class BinarySearchTree<T> implements ITree<T> {

	
	/**
	 * The default constructor
	 * @param cmp The comparator to compare values in nodes
	 */
	public BinarySearchTree(Comparator<T> cmp)
	{
		if(cmp == null)
			throw new NullPointerException();
		this.Cmp = cmp;
		Root = null;
		index_node = -1;
	}
	
	/**
	 * The constructor
	 * @param i The Iterable with elements to be added to the tree in order
	 * @param cmp The comparator to compare values in nodes
	 */
	public BinarySearchTree(Iterable<? extends T> i, Comparator<T> cmp)
	{
		if(cmp == null || i == null)
			throw new NullPointerException();
		this.Cmp = cmp;
		for(T t: i)
			Add(t);
		index_node = -1;
	}
	
	/**
	 * The Iterator function
	 */
	@Override
	public Iterator<T> iterator() 
	{
		ArrayList<T> lst = new ArrayList<T>();
		iteratorHelper(lst, Root);
		return lst.iterator();
	}
	
	/**
	 * Helper function for iterator to do recursions
	 * @param lst The list to store values of the tree
	 * @param current The current node
	 */
	private void iteratorHelper(ArrayList<T> lst, Node current)
	{
		if(current != null)
		{
			iteratorHelper(lst, current.Left);
			lst.add(current.Value);
			iteratorHelper(lst, current.Right);
		}
	}

	/**
	 * Adds {@code t} to the tree.
	 * @param t The item to add.
	 * @return Returns true if the item was added and false otherwise.
	 */
	@Override
	public boolean Add(T t) 
	{
		int count = Count();
		if(Root == null)
			Root = new Node(t);
		else
			AddHelper(Root, t);
		return Count() == count + 1;
	}
	
	/**
	 * The helper function of Add to do recursions
	 * @param current The current node
	 * @param t The value to be added to the tree
	 * @return
	 */
	private Node AddHelper(Node current, T t)
	{
		if(current == null)
			return new Node(t);
		else if(Cmp.compare(current.Value, t) < 0)
			current.Right = AddHelper(current.Right, t);
		else if(Cmp.compare(current.Value, t) > 0)
			current.Left = AddHelper(current.Left, t);
		else 
			return current;
		return current;
	}

	/**
	 * Removes {@code t} from the tree.
	 * @param t The item to remove.
	 * @return Returns true if the item was removed and false otherwise.
	 */
	@Override
	public boolean Remove(T t) {
		int count = Count();
		Root = RemoveHelper(Root, t);
		return count - 1 == Count();
	}
	
	
	/**
	 * The helper function for Remove to do recursions
	 * @param current The current node
	 * @param t The element to be removed
	 * @return
	 */
	private Node RemoveHelper(Node current, T t)
	{
		if(current == null)
			return null;
		else if(Cmp.compare(current.Value, t) < 0)//if t is larger than current, go to the right subtree
			current.Right = RemoveHelper(current.Right, t);
		else if(Cmp.compare(current.Value, t) > 0)
			current.Left = RemoveHelper(current.Left, t);//if t is smaller than current, go to the left subtree
		else //If current is the element to be removed
		{
			if(current.NoNext())//if current is a leaf
				return null;
			else if (!current.HasLeft())//if current has only one child
				return current.Right;
			else if (!current.HasRight())//if current has only one child
				return current.Left;
			else//if current has to children
			{
				current.Value = minValue(current.Right);
	            current.Right = RemoveHelper(current.Right, current.Value);
			}
		}
		return current;
	}
	
	/**
	 * Find the smallest node in a binary tree, helper function for RemoveHelper
	 * @param current The current node
	 * @return The value of the smallest node
	 */
	private T minValue(Node current)
	{
		while(current.HasLeft())
			current = current.Left;
		return current.Value;
	}

	/**
	 * Determines if the tree contains {@code t}.
	 * @param t The item to search for.
	 * @return Returns true if the tree contains {@code t} and false otherwise.
	 */
	@Override
	public boolean Contains(T t) {
		return ContainsHelper(Root, t);
	}
	
	/**
	 * The helper function of Contains that do the recursion
	 * @param current The node at current position
	 * @param t The value we are looking for
	 * @return Whether the tree contains this value or not
	 */
	private boolean ContainsHelper(Node current, T t)
	{
		if(current == null)// if t not in the tree
			return false;
		else if(Cmp.compare(current.Value, t) < 0)//if t is larger than current, go to the right subtree
			return ContainsHelper(current.Right, t);
		else if(Cmp.compare(current.Value, t) > 0)//if t is smaller than current, go to the left subtree
			return ContainsHelper(current.Left, t);
		else//if found the element
			return true;
	}

	/**
	 * Obtains the data at the root of the tree.
	 * @throws NoSuchElementException Thrown if the tree is empty.
	 */
	@Override
	public T Root() {
		if(Root == null)
			throw new NoSuchElementException();
		return Root.Value;
	}

	/**
	 * Clears the tree of all elements.
	 */
	@Override
	public void Clear() {
		Root = null;
	}

	/**
	 * Determines the number of items in the tree.
	 */
	@Override
	public int Count() {
		return CountHelper(Root);
	}
	
	private int CountHelper (Node current)
	{
		if(current == null)
			return 0;
		else
			return 1 + CountHelper(current.Left) + CountHelper(current.Right);
	}
	
	/**
	 * Determines if the tree is empty.
	 * @return Returns true if the tree is empty and false otherwise.
	 */
	@Override
	public boolean IsEmpty() {
		return Count() == 0;
	}

	/**
	 * Performs a pre-order traversal of the tree.
	 * This visits the elements of the tree in topological order and is equivalent to a depth-first search.
	 * @param f The function to call when visiting each node.
	 * @throws NullPointerException Thrown if {@code f} is null.
	 */
	@Override
	public void PreOrderTraversal(TraversalFunction<T> f) {
		if(f == null)
			throw new NullPointerException();
		PreOrderTraversalHelper(Root, f, 0);
		index_node = -1;
	}
	
	/**
	 * The helper function for PreOrderTraversal
	 * @param current The current node
	 * @param f The traversal function to be performed on each element in the specified order
	 * @param depth The depth of current
	 */
	private void PreOrderTraversalHelper(Node current, TraversalFunction<T> f, int depth)
	{
		if(current != null)
		{
			index_node ++;
			f.Visit(current.Value, index_node, depth);
			PreOrderTraversalHelper(current.Left, f, depth + 1);
			PreOrderTraversalHelper(current.Right, f, depth + 1);
		}
	}

	/**
	 * Performs a post-order traversal of the tree.
	 * This visits the elements of the tree in a reversed topological order and is equivalent to a depth-first search.
	 * @param f The function to call when visiting each node.
	 * @throws NullPointerException Thrown if {@code f} is null.
	 */
	@Override
	public void PostOrderTraversal(TraversalFunction<T> f) {
		if(f == null)
			throw new NullPointerException();
		PostOrderTraversalHelper(Root, f, 0);
		index_node = -1;
	}
	
	/**
	 * The helper function for PostOrderTraversal
	 * @param current The current node
	 * @param f The traversal function to be performed on each element in the specified order
	 * @param depth The depth of current
	 */
	private void PostOrderTraversalHelper(Node current, TraversalFunction<T> f, int depth)
	{
		if(current != null)
		{
			PostOrderTraversalHelper(current.Left, f, depth + 1);
			PostOrderTraversalHelper(current.Right, f, depth + 1);
			index_node ++;
			f.Visit(current.Value, index_node, depth);
		}
	}

	/**
	 * Performs an in-order traversal of the (binary) tree.
	 * This visits the elements of the tree in ascending order.
	 * @param f The function to call when visiting each node.
	 * @throws NullPointerException Thrown if {@code f} is null.
	 * @throws UnsupportedOperationException Thrown if this tree is not a binary tree and has no alternative meaning assigned to an in-order traversal.
	 */
	@Override
	//gaile
	public void InOrderTraversal(TraversalFunction<T> f) {
		if(f == null)
			throw new NullPointerException();
		index_node = -1;
		InOrderTraversalHelper(Root, f, 0);
	}
	
	/**
	 * The helper function for InOrderTraversal
	 * @param current The current node
	 * @param f The traversal function to be performed on each element in the specified order
	 * @param depth The depth of current
	 */
	private void InOrderTraversalHelper(Node current, TraversalFunction<T> f, int depth)
	{
		if(current != null)
		{
			InOrderTraversalHelper(current.Left, f, depth + 1);
			index_node ++;
			f.Visit(current.Value, index_node, depth);
			InOrderTraversalHelper(current.Right, f, depth + 1);
		}
	}

	/**
	 * Performs a level-order traversal of the tree.
	 * This is equivalent to a breadth-first search.
	 * @param f The function to call when visiting each node.
	 * @throws NullPointerException Thrown if {@code f} is null.
	 */
	@Override
	public void LevelOrderTraversal(TraversalFunction<T> f) {
		if(f == null)
			throw new NullPointerException();
		Queue<Node> queue = new LinkedList<Node>(); // A queue to store nodes in order
        queue.add(Root);
        index_node = -1;
        while (!queue.isEmpty()) 
        {
        	Node temp_node = queue.poll();
        	if(temp_node != null)
        	{
        		T temp = temp_node.Value;
                index_node ++;
                f.Visit(temp, index_node, GetDepth(temp, Root, 0));//first process the parent
     
                if (temp_node.Left != null)//get the children to the queue
                {
                    queue.add(temp_node.Left);
                }
     
                if (temp_node.Right != null)//get the children to the queue
                {
                    queue.add(temp_node.Right);
                }
        	}
        }
	}
	
	/**
	 * A helper function to get the depth of a node
	 * @param t The element to be found
	 * @param current The current node
	 * @param depth The depth of current
	 * @return The depth
	 */
	private int GetDepth(T t, Node current, int depth)
	{
		if(current == null)
			return -1;
		else if(Cmp.compare(current.Value, t) < 0)
			return GetDepth(t, current.Right, depth + 1);
		else if(Cmp.compare(current.Value, t) > 0)
			return GetDepth(t, current.Left, depth + 1);
		else 
			return depth;
	}
	
	/**
	 * This is a inner class representing a node in a tree
	 * It contains a value, the node's left node and right node
	 * @author Zitan Wang, Mingguang Wang
	 *
	 */
	protected class Node
	{
		/**
		 * Default constructor
		 */
		public Node()
		{
			Value = null;
			Left = null;
			Right = null;
		}
		
		/**
		 * The constructor
		 * @param value The value of this node
		 */
		public Node(T value)
		{
			Left = null;
			Right = null;
			this.Value = value;
		}
		
		/**
		 * See if the node has a left node
		 * @return True if yes, false if no
		 */
		public boolean HasLeft()
		{
			return this.Left != null;
		}
		
		/**
		 * See if the node has a right node
		 * @return True if yes, false if no
		 */
		public boolean HasRight()
		{
			return this.Right != null;
		}
		
		/**
		 * See if the node has no children
		 * @return True if yes, false if no
		 */
		public boolean NoNext()
		{
			return !this.HasLeft() && !this.HasRight();
		}
		
		protected T Value;
		protected Node Left;
		protected Node Right;
		
	}
	
	protected Node Root;
	protected Comparator<T> Cmp;
	protected int index_node; //The index to be used in the visit function when doing traversal

}
