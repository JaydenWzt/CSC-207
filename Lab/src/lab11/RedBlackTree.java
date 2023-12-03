package lab11;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

/**
 * A balanced binary tree implemented via a red-black scheme.
 * Duplicates are not permitted in the tree, but null is allowed if the {@code Comparator} used to compare {@code T} types permits it.
 * Note that the {@code Comparator} is assumed to return 0 only if items are equal, not merely equivalent.
 * @author Dawn Nye
 * @param <T> The type of object stored in the tree.
 */
public class RedBlackTree<T> extends AbstractBalancedBinarySearchTree<T,RedBlackTreeNode<T>>
{

	
	/**
	 * Creates an empty red-black tree.
	 * @param cmp The means by which we compare elements of the tree.
	 * @throws NullPointerException Thrown if {@code cmp} is null.
	 */
	public RedBlackTree(Comparator<T> cmp)
	{
		super(cmp);
		return;
	}
	
	/**
	 * Creates a red-black tree initially populated with {@code seed}.
	 * @param seed The initial values to palce in the tree. They will be added to the tree one by one in the order they appear.
	 * @param cmp The means by which we compare elements of the tree.
	 * @throws NullPointerException Thrown if {@code cmp} or {@code seed} is null.
	 */
	public RedBlackTree(Iterable<? extends T> seed, Comparator<T> cmp)
	{
		super(seed,cmp);
		return;
	}
	
	
	
	
	protected void BalanceAdd(RedBlackTreeNode<T> n) {

		if (n == null) {
			throw new NullPointerException();
		}

		//Case 1:n be the red root node
		if ((n.IsRoot())) {
			return;
		}

		RedBlackTreeNode<T> P = n.Parent;

		//Case 2: n's parent is black (so in the later case, p is red
		if (P.IsBlack()) {
			return;
		}

		//Case 3: P is a red root node
		if (P.IsRoot()) {
			P.MakeBlack();
			return;
		}

		RedBlackTreeNode<T> C = n;
		RedBlackTreeNode<T> U = n.GetUncle();
		RedBlackTreeNode<T> G = n.GetGrandparent();
		//Case 4: Uncle is red
		if (U != null && U.IsRed()) {
			U.MakeBlack();
			P.MakeBlack();
			G.MakeRed();
			BalanceAdd(G);
			return;
		}

		else if (P.IsLeftChild()) {
			if (C.IsRightChild()) { //possible error
				if (P.LeftRotation()) {
					this.Root = C;
				}
				RedBlackTreeNode<T> temp = P;
				P = C;
				C = temp;
			}
			//6.1
			if (C.IsLeftChild()) {
				if (G.RightRotation()) {
					this.Root = G.Parent;
				}
				P.MakeBlack();
				G.MakeRed();
				return;
			}
		} else if(P.IsRightChild()){
			if (C.IsLeftChild()) {
				if (C.Parent.RightRotation()) {
					this.Root = C;
				}
				RedBlackTreeNode<T> temp = P;
				P = C;
				C = temp;
			}
			if (C.IsRightChild()) {
				if (G.LeftRotation()) {
					this.Root = G.Parent;
				}
				P.MakeBlack();
				G.MakeRed();
				return;
			}


		}


		/*
		RedBlackTreeNode<T> C = n;

		//If current is the root, do nothing(case 1)
		if(C.IsRoot())
		{
			C.MakeBlack();
			return;
		}

		RedBlackTreeNode<T> P = n.Parent;
		//If parent is black, do nothing(case 2)
		if(P.IsBlack())
			return;

		RedBlackTreeNode<T> G = P.Parent;
		//parent is black from now on
		//If parent is the root (case 3)
		if(P.IsRoot())
		{
			P.MakeBlack();
			return;
		}

		RedBlackTreeNode<T> U = C.GetUncle();
		//if uncle is red (case 4)
		if(U != null && U.IsRed())
		{
			P.MakeBlack();
			U.MakeBlack();
			G.MakeRed();

			//recursive call on grandparent
			BalanceAdd(G);
			return;
		}

		//P is the left child
		else if(P.IsLeftChild())
		{
			//C is inner node (case 5.1)
			if(C.IsRightChild())
			{
				if(P.LeftRotation())
				{
					this.Root = C;
				}
				RedBlackTreeNode<T> temp = P;
				P = C;
				C = temp;

			}

			//C is outer node (case 6.1)
			if(C.IsLeftChild())
			{
				if(G.RightRotation())
				{
					this.Root = G.Parent;
				}
				P.MakeBlack();
				G.MakeRed();
				return;
			}
		}

		//P is the right child
		else if(P.IsRightChild())
		{
			//C is inner node (case 5.2)
			if(C.IsLeftChild())
			{
				if(P.RightRotation())
				{
					this.Root = C;
				}
				RedBlackTreeNode<T> temp = P;
				P = C;
				C = temp;
			}

			//C is outer node (case 6.2)
			if(C.IsRightChild())
			{
				if(G.LeftRotation())
				{
					this.Root = G.Parent;
				}
				P.MakeBlack();
				G.MakeRed();
				return;
			}
		}

		 */
	}
			
			
		
		
		

		
	
	
	protected void BalanceRemove(RedBlackTreeNode<T> n, boolean was_left_child)
	{
		if(n==null) {
			throw new NullPointerException();
		}
		
		//Case 1:n be the red  node
		if((n.IsRed())) {
			return;
		}
		
		//Case 2: n be the root
		if (n.IsRoot()) {
			return;
		}
		
		RedBlackTreeNode<T> P =n.Parent;
		RedBlackTreeNode<T> S =n.GetSibling(was_left_child);
		
		//Case 3: n'sibling is red
		if(S!=null && S.IsRed()) {
			S.MakeBlack();
			P.MakeRed();
			
			if(!was_left_child) {
			if(n.Parent.RightRotation()) {
				this.Root=S;
			}
			S=n.GetSibling(was_left_child);
			}
			else {
				if(n.Parent.LeftRotation()) {
					this.Root=S;
				}
				S=n.GetSibling(was_left_child);
			}
		}
		
		//Case 4: O and I are black
		if(S != null &&
				(n.GetInnerNibling()==null||n.GetInnerNibling().IsBlack())&&
				(n.GetOuterNibling() == null || n.GetOuterNibling().IsBlack())) {
			S.MakeRed();
			if (P.IsRed()) {
				P.MakeBlack();
			}
			else {
				BalanceRemove(P, was_left_child);
			}
		}
		
		else if (S!=null) {
			if(was_left_child &&(n.GetOuterNibling(was_left_child)==null ||n.GetOuterNibling(was_left_child).IsBlack())) {
				S.MakeRed();
				n.GetInnerNibling().MakeBlack();
				if(S.RightRotation()) {
					Root = S.Parent;
				}
				S=n.Parent.Right;
				}
			
			else if(!was_left_child &&(n.GetOuterNibling(was_left_child)==null ||n.GetOuterNibling(was_left_child).IsBlack())) {
				S.MakeRed();
				n.GetInnerNibling().MakeBlack();
				if(S.LeftRotation()) {
					Root = S.Parent;
				}
				S=n.Parent.Left;

			}
			
			if(P.IsBlack()) {
				S.MakeBlack();
				}
			else {
				S.MakeRed();
			}
			
			P.MakeBlack();
			
			if(was_left_child) {
				n.GetOuterNibling(was_left_child).MakeBlack();
				if(P.LeftRotation()) {
					Root=P.Parent;
			}
		}
			else {
				n.GetOuterNibling(was_left_child).MakeBlack();
				if(P.RightRotation()) {
					Root=P.Parent;
			}
			}
		}
		
	}
	
	protected RedBlackTreeNode<T> CreateNode(T data, RedBlackTreeNode<T> parent, RedBlackTreeNode<T> left, RedBlackTreeNode<T> right, boolean is_left_child)
	{return new RedBlackTreeNode<T>(data,parent,left,right,is_left_child,false);}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////   Local Main   ///////////////////////////////////////////
	//////////////////////////////////////   Can Chnage   ///////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args)
	{
		RedBlackTree<Integer> T = new RedBlackTree<Integer>((a,b) -> a.compareTo(b));
		Random rand = new Random();

		// Test a battery of random trees
		// We'll probably run into every Add/Remove case this way without having to think about it
		for(int i = 0;i < 10000;i++)
		{
			int len = rand.nextInt(100);
			LinkedList<Integer> l = new LinkedList<Integer>();

			// Add a bunch of random data
			for(int j = 0;j < len;j++)
			{
				int k = rand.nextInt(100);

				if(T.Add(k))
					l.add(k); // We'll save this for later

				if(!VerifyRedBlackStructure(T,(a,b) -> a.compareTo(b)))
				{
					PrintError(T,"Add Error!");
					return;
				}
			}

			// Now let's randomly remove everything from the tree
			while(!l.isEmpty())
			{
				int k = rand.nextInt(l.size());

				if(!T.Remove(l.get(k)))
				{
					PrintError(T,"Remove Failure!");
					return;
				}
				else
					l.remove(k);

				if(!VerifyRedBlackStructure(T,(a,b) -> a.compareTo(b)))
				{
					PrintError(T,"Remove Error!");
					return;
				}
			}

			if(!T.IsEmpty())
			{
				PrintError(T,"Remove Error!");
				return;
			}
		}

		return;
	}

	protected static void PrintError(RedBlackTree<Integer> T, String error)
	{
		System.err.println(error);
		System.out.println("Size of T: " + T.Count() + "\n");
		System.out.println(T + "\n");

		return;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////// Test Functions ///////////////////////////////////////////
	//////////////////////////////////////  Don't Chnage  ///////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////// But you may use them ////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Verities that the given tree has the red-black tree properties.
	 * @param t The tree to verify has the correct red-black structure.
	 * @param cmp The {@code Comparator} that {@code t} uses to sort its data.
	 * @return Returns true if the red-black structure of {@code t} is correct and false otherwise.
	 */
	public static <T> boolean VerifyRedBlackStructure(RedBlackTree<T> t, Comparator<T> cmp)
	{
		// First, we'll make sure we have the binary search tree property intact
		ArrayList<T> l = new ArrayList<T>();
		t.InOrderTraversal((data,index,depth) -> l.add(data));

		for(int i = 0;i < l.size() - 1;i++)
			if(cmp.compare(l.get(i),l.get(i + 1)) >= 0)
				return false;

		return true;
	}

	/**
	 * Verifies that the red-black structure is correct on the subtree {@code n}.
	 * @param n The root node of the subtree.
	 * @param bh The remaining black height before we get to a null child.
	 * @return Returns true if the red-black structure starting from the subtree {@code n} is correct and false otherwise.
	 */
}
