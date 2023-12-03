package lab12;

import java.util.Comparator;
import java.util.EnumSet;

public class HeapTree<T> extends CompleteBinaryTree<T> {

    /**
     * The constructor of HeapTree
     * @param cmp The comparator used to sort the heap tree
     */
    public HeapTree(Comparator<T> cmp)
    {
        super();
        if(cmp == null)
            throw new NullPointerException();
        this.Cmp = cmp;
    }

    /**
     * The constructor of HeapTree
     * @param itr The iterable containing elements to be added
     * @param cmp The comparator used to sort the heap tree
     */
    public HeapTree(Iterable<? extends T> itr, Comparator<T> cmp)
    {
        super();
        if (cmp == null)
            throw new NullPointerException();
        this.Cmp = cmp;
        for (T t : itr)
            Add(t);
    }

    /**
     * Maintains some additional structural property of the binary tree.
     * This version of this maintenance function is used for when a node has been <i>added</i> to the tree.
     * @param t The node that was added or an ancestor of it as the property is propogated up the tree.
     * @return Returns a set of enum flags specifying which directions the property must be propogated to be true everywhere. If this value is empty, then there is nothing left to do.
     */
    @Override
    protected EnumSet<PropogationDirection> MaintainPropertyAdd(CompleteBinaryTreeNode<T> t) {
        if(t.IsRoot() || t == null)//if there is nothing left, stop
        {
            return EnumSet.noneOf(PropogationDirection.class);
        }

        if (Cmp.compare(t.Data, t.Parent.Data) < 0)//if the parent is larger, swap this with its parent
        {
            SwapNodeContents(t, t.Parent);
            return EnumSet.of(PropogationDirection.PARENT);//move upward
        }
        return EnumSet.noneOf(PropogationDirection.class);//stop
    }

    /**
     * Maintains some additional structural property of the binary tree.
     * This version of this maintenance function is used for when a node has been <i>removed</i> from the tree.
     * @param t The node that was removed (and hence is no longer part of the tree) or some other node reached via propogation through the tree.
     * @return Returns a set of enum flags specifying which directions the property must be propogated to be true everywhere. If this value is empty, then there is nothing left to do.
     */
    @Override
    protected EnumSet<PropogationDirection> MaintainPropertyRemove(CompleteBinaryTreeNode<T> t){
        if(!t.IsPartOfTree())
            return EnumSet.noneOf(PropogationDirection.class);

        if (!t.IsRoot() && Cmp.compare(t.Data, t.Parent.Data) < 0)//if the parent is larger, swap this with its parent
        {
            SwapNodeContents(t, t.Parent);
            return EnumSet.of(PropogationDirection.PARENT);//move upward
        }

        if(t.IsLeaf())
            return EnumSet.noneOf(PropogationDirection.class);

        CompleteBinaryTreeNode<T> l = t.Left;
        CompleteBinaryTreeNode<T> r = t.Right;

        if(l != null)//if this has left child
        {
            if(r == null || (Cmp.compare(l.Data, r.Data) < 0))
            {
                if(Cmp.compare(l.Data, t.Data) < 0)//if l is smaller than both t and r(if exist), then swap t and l
                {
                    SwapNodeContents(t, l);
                    return EnumSet.of(PropogationDirection.LEFT);//move to the left child
                }

                return EnumSet.noneOf(PropogationDirection.class);//if l >= t, the heap tree is heapified
            }

            else if(r != null && Cmp.compare(r.Data, t.Data) < 0)//if r is smaller than both t and l, then swap t and r
            {
                SwapNodeContents(t, r);
                return EnumSet.of(PropogationDirection.RIGHT);//move to the right child
            }

            return EnumSet.noneOf(PropogationDirection.class);//if r >= t, the heap tree is heapified
        }

        return EnumSet.noneOf(PropogationDirection.class);//the heap tree is heapified
    }


    //The comparator of this heap tree
    protected Comparator<T> Cmp;
}