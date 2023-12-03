package lab18;

import java.util.*;

import lab13.PriorityQueue;
import lab14.HashTable;
import lab15.Dictionary;
import lab17.IGraph;
import lab17.IGraph.Edge;
import lab17.IGraph.NoSuchEdgeException;
import lab17.IGraph.NoSuchVertexException;
import lab17.LGraph;
import lab7.LINQ;
import lab6.LinkedList;
import lab9.Queue;
import lab9.Stack;

/**
 * A collection of graph algorithms.
 * @author Dawn Nye
 */
public final class Algorithms
{
    /**
     * No one will ever make this.
     */
    private Algorithms()
    {return;}

    /**
     * Computes a BFS of {@code G} starting from the first unvisited vertex(s).
     * The BFS will continue selecting new start vertices until each vertex has been visited exactly once.
     * The resultant forest will be a directed graph regardless of if {@code G} is directed or undirected.
     * The edge directions indicate which direction the BFS proceeded over the edge away from the starting vertex.
     * <br><br>
     * If a vertex is outside of a previously constructed tree but can reach the tree later, then it will belong to a disjoint tree.
     * For example, in the graph A -> B -> C, if we start at B and then start at A, we will get the forest A and B -> C.
     * @param <V> The type of data in the vertices.
     * @param <E> The type of data in the edges.
     * @param G The graph to perform the BFS in.
     * @return Returns a BFS forest as described above.
     * @throws NullPointerException Thrown if {@code G} is null.
     * @throws IllegalArgumentException Thrown if {@code start} is negative.
     * @throws NoSuchVertexException Thrown if {@code start} is not a vertex ID in the graph.
     */
    public static <V,E> IGraph<V,E> BFS(IGraph<V,E> G)
    {
        if(G == null)
            throw new NullPointerException();

        Dictionary<Integer,Integer> IDmatch = new Dictionary<>();
        //the dictionary to match the ID of the same vertex in {@code G} and in {@code BFSforest}
        //Where the key is the {@code ID} from {@code G}, and the value is the {@code ID} from {@code BFSforest}

        IGraph<V,E> BFSforest = new LGraph<>(true);
        //the graph going to be constructed containing the BFS result

        HashSet<Integer> visited = new HashSet<>();
        //the hashSet including all ID of vertices in {@code G} that are visited
        for(Integer ID: G.VertexIDs())
            IDmatch.Add(ID, BFSforest.AddVertex(G.GetVertex(ID)));
        //add all vertices to {@code BFSforest} and match old and new {@code ID}

        for(Integer startID: G.VertexIDs())//visit all vertices in G
        {
            if(visited.contains(startID))//if already visited, skip
                continue;

            Queue<Integer> Q = new Queue<>();//the Queue to store {@code ID} of vertices
            visited.add(startID);//mark this vertex as visited
            Q.Enqueue(startID);//add this vertex in to {@code Q}

            while(!Q.IsEmpty())
            {
                int currentID = Q.Dequeue();//dequeue the first vertex in {@code Q}

                for(Edge<V,E> e: G.OutboundEdges(currentID))//for all neighbors of this vertex
                {
                    int neighborID = e.Destination.ID;
                    if(!visited.contains(neighborID))//if not visited before,mark it as visited
                    {
                        BFSforest.AddEdge(IDmatch.Get(currentID), IDmatch.Get(neighborID), e.Data);
                        visited.add(neighborID);
                        Q.Enqueue(neighborID);
                    }
                }
            }
        }
        return BFSforest;
    }

    /**
     * Computes a BFS of {@code G} starting from the first unvisited vertex(s).
     * The BFS will continue selecting new start vertices until each vertex has been visited exactly once.
     * The resultant forest will be a directed graph regardless of if {@code G} is directed or undirected.
     * The edge directions indicate which direction the BFS proceeded over the edge away from the starting vertex.
     * <br><br>
     * If a vertex is outside of a previously constructed tree but can reach the tree later, then it will belong to a disjoint tree.
     * For example, in the graph A -> B -> C, if we start at B and then start at A, we will get the forest A and B -> C.
     * @param <V> The type of data in the vertices.
     * @param <E> The type of data in the edges.
     * @param G The graph to perform the BFS in.
     * @param f The function which allows us to visit vertices as we explore the graph. This value can be null, in which case it is ignored.
     * @return Returns a BFS forest as described above.
     * @throws NullPointerException Thrown if {@code G} is null.
     * @throws IllegalArgumentException Thrown if {@code start} is negative.
     * @throws NoSuchVertexException Thrown if {@code start} is not a vertex ID in the graph.
     */
    public static <V,E> IGraph<V,E> BFS(IGraph<V,E> G, BFSVisitor<V,E> f)
    {
        //This function is almost identical with the last one, except colling {@code f} when visiting each vertex

        if(G == null)
            throw new NullPointerException();

        Dictionary<Integer,Integer> IDmatch = new Dictionary<>();
        //the dictionary to match the ID of the same vertex in {@code G} and in {@code BFSforest}
        //Where the key is the {@code ID} from {@code G}, and the value is the {@code ID} from {@code BFSforest}

        IGraph<V,E> BFSforest = new LGraph<>(true);
        //the graph going to be constructed containing the BFS result

        HashSet<Integer> visited = new HashSet<>();
        //the hashSet including all ID of vertices in {@code G} that are visited

        for(Integer ID: G.VertexIDs())
            IDmatch.Add(ID, BFSforest.AddVertex(G.GetVertex(ID)));
        //add all vertices to {@code BFSforest} and match old and new {@code ID}

        for(Integer startID: G.VertexIDs())//visit all vertices in G
        {
            if(visited.contains(startID))//mark is vertex as visited
                continue;

            Queue<Integer> Q = new Queue<>();//the Queue to store {@code ID} of vertices
            visited.add(startID);//mark this vertex as visited
            Q.Enqueue(startID);//add this vertex in to {@code Q}

            while(!Q.IsEmpty())
            {
                int currentID = Q.Dequeue();//dequeue the first vertex in {@code Q}
                if(f != null)
                    f.Visit(G, currentID, BFSforest, IDmatch.Get(currentID));
                for(Edge<V,E> e: G.OutboundEdges(currentID))//for all neighbors of this vertex
                {
                    int neighborID = e.Destination.ID;
                    if(!visited.contains(neighborID))//if not visited before,mark it as visited
                    {
                        BFSforest.AddEdge(IDmatch.Get(currentID), IDmatch.Get(neighborID), e.Data);
                        visited.add(neighborID);
                        Q.Enqueue(neighborID);
                    }
                }
            }
        }
        return BFSforest;
    }

    /**
     * Computes a BFS of {@code G} starting from the vertex with ID {@code start}.
     * The BFS will not visit any vertices unreachable from {@code start}.
     * The resultant tree will be a directed graph regardless of if {@code G} is directed or undirected.
     * The edge directions indicate which direction the BFS proceeded over the edge away from the starting vertex.
     * Unvisited vertices will not be part of the returned graph.
     * @param <V> The type of data in the vertices.
     * @param <E> The type of data in the edges.
     * @param G The graph to perform the BFS in.
     * @param start The vertex to start the BFS on.
     * @return Returns a BFS tree as described above.
     * @throws NullPointerException Thrown if {@code G} is null.
     * @throws IllegalArgumentException Thrown if {@code start} is negative.
     * @throws NoSuchVertexException Thrown if {@code start} is not a vertex ID in the graph.
     */
    public static <V,E> IGraph<V,E> BFS(IGraph<V,E> G, int start)
    {
        if(G == null)
            throw new NullPointerException();
        if(start < 0)
            throw new IllegalArgumentException();
        if(!G.ContainsVertex(start))
            throw new NoSuchVertexException(start);

        Dictionary<Integer,Integer> IDmatch = new Dictionary<>();
        //the dictionary to match the ID of the same vertex in {@code G} and in {@code BFSforest}
        //Where the key is the {@code ID} from {@code G}, and the value is the {@code ID} from {@code BFSforest}

        IGraph<V,E> BFSforest = new LGraph<>(true);
        //the graph going to be constructed containing the BFS result

        HashSet<Integer> visited = new HashSet<>();
        //the hashSet including all ID of vertices in {@code G} that are visited
        //matching are added while looping to avoid adding vertices not reachable from {@code start}

        Queue<Integer> Q = new Queue<>();
        visited.add(start);//this time, start from {@code start}, any vertex not reachable from {@code start} will not in {@code BFSforest}
        Q.Enqueue(start);

        //the loop works almost the same as the first BFS, except adding pairs to {@code IDmatch} while looping
        while(!Q.IsEmpty())
        {
            int currentID = Q.Dequeue();
            if(!IDmatch.Contains(currentID))
                IDmatch.Add(currentID, BFSforest.AddVertex(G.GetVertex(currentID)));
            for(Edge<V,E> e: G.OutboundEdges(currentID))
            {
                int neighborID = e.Destination.ID;
                if(!IDmatch.Contains(neighborID))
                    IDmatch.Add(neighborID, BFSforest.AddVertex(G.GetVertex(neighborID)));
                if(!visited.contains(neighborID))
                {
                    BFSforest.AddEdge(IDmatch.Get(currentID), IDmatch.Get(neighborID), e.Data);
                    visited.add(neighborID);
                    Q.Enqueue(neighborID);
                }
            }
        }
        return BFSforest;
    }

    /**
     * Computes a BFS of {@code G} starting from the vertex with ID {@code start}.
     * The BFS will not visit any vertices unreachable from {@code start}.
     * The resultant tree will be a directed graph regardless of if {@code G} is directed or undirected.
     * The edge directions indicate which direction the BFS proceeded over the edge away from the starting vertex.
     * Unvisited vertices will not be part of the returned graph.
     * @param <V> The type of data in the vertices.
     * @param <E> The type of data in the edges.
     * @param G The graph to perform the BFS in.
     * @param start The vertex to start the BFS on.
     * @param f The function which allows us to visit vertices as we explore the graph. This value can be null, in which case it is ignored.
     * @return Returns a BFS tree as described above.
     * @throws NullPointerException Thrown if {@code G} is null.
     * @throws IllegalArgumentException Thrown if {@code start} is negative.
     * @throws NoSuchVertexException Thrown if {@code start} is not a vertex ID in the graph.
     */
    public static <V,E> IGraph<V,E> BFS(IGraph<V,E> G, int start, BFSVisitor<V,E> f)
    {
        //this function is like a combination of the two BFS functions directly above this one.
        //vertices not reachable from {@code start} will not be visited via {@code f] nor added to {@code BFSforest}
        //other things stay the same

        if(G == null)
            throw new NullPointerException();
        if(start < 0)
            throw new IllegalArgumentException();
        if(!G.ContainsVertex(start))
            throw new NoSuchVertexException(start);

        Dictionary<Integer,Integer> IDmatch = new Dictionary<>();
        IGraph<V,E> BFSforest = new LGraph<>(true);
        HashSet<Integer> visited = new HashSet<>();
        for(Integer ID: G.VertexIDs())
            IDmatch.Add(ID, BFSforest.AddVertex(G.GetVertex(ID)));

        Queue<Integer> Q = new Queue<>();
        visited.add(start);
        Q.Enqueue(start);

        while(!Q.IsEmpty())
        {
            int currentID = Q.Dequeue();
            if(f != null)
                f.Visit(G, currentID, BFSforest, IDmatch.Get(currentID));
            for(Edge<V,E> e: G.OutboundEdges(currentID))
            {
                int neighborID = e.Destination.ID;
                if(!visited.contains(neighborID))
                {
                    BFSforest.AddEdge(IDmatch.Get(currentID), IDmatch.Get(neighborID), e.Data);
                    visited.add(neighborID);
                    Q.Enqueue(neighborID);
                }
            }
        }
        return BFSforest;
    }

    /**
     * Computes a DFS of {@code G} starting from the first unvisited vertex(s).
     * The DFS will continue selecting new start vertices until each vertex has been visited exactly once.
     * The resultant forest will be a directed graph regardless of if {@code G} is directed or undirected.
     * The edge directions indicate which direction the DFS proceeded over the edge away from the starting vertex.
     * <br><br>
     * If a vertex is outside of a previously constructed tree but can reach the tree later, then it will belong to a disjoint tree.
     * For example, in the graph A -> B -> C, if we start at B and then start at A, we will get the forest A and B -> C.
     * @param <V> The type of data in the vertices.
     * @param <E> The type of data in the edges.
     * @param G The graph to perform the DFS in.
     * @return Returns a DFS forest as described above.
     * @throws NullPointerException Thrown if {@code G} is null.
     */
    public static <V,E> IGraph<V,E> DFS(IGraph<V,E> G)
    {
        //All the data structures used here
        //{@code IDmatch}, {@code DFSforest}, {@code visited}
        //serve the same purpose as mentioned in the BFS functions above

        if(G == null)
            throw new NullPointerException();

        Dictionary<Integer,Integer> IDmatch = new Dictionary<>();

        IGraph<V,E> DFSforest = new LGraph<>(true);
        for(Integer gID: G.VertexIDs())
            IDmatch.Add(gID, DFSforest.AddVertex(G.GetVertex(gID)));

        HashSet<Integer> visited = new HashSet<>();

        for(Integer startID: G.VertexIDs())//visit all vertices in {@code G}
        {
            if(visited.contains(startID))//if we've already visited this vertex, skip it
                continue;

            Stack<Integer> S = new Stack<>();//the stack used for DFS
            S.Push(startID);
            visited.add(startID);

            while(!S.IsEmpty())//while the stack is not empty
            {
                int currentID = S.Peek();
                boolean allVisited = true;
                for(Edge<V,E> e: G.OutboundEdges(currentID))//for the first not visited neighbor of {@code currentID}
                {
                    int neighborID = e.Destination.ID;
                    if(!visited.contains(neighborID))
                    {
                        allVisited = false;
                        DFSforest.AddEdge(IDmatch.Get(currentID), IDmatch.Get(neighborID), e.Data);
                        S.Push(neighborID);
                        visited.add(neighborID);
                        break;
                    }
                }
                if(allVisited)//if all neighbors are visited, pop out {@code currentID} from {@code S}
                    S.Pop();
            }
        }
        return DFSforest;
    }

    /**
     * Computes a DFS of {@code G} starting from the first unvisited vertex(s).
     * The DFS will continue selecting new start vertices until each vertex has been visited exactly once.
     * The resultant forest will be a directed graph regardless of if {@code G} is directed or undirected.
     * The edge directions indicate which direction the DFS proceeded over the edge away from the starting vertex.
     * <br><br>
     * If a vertex is outside of a previously constructed tree but can reach the tree later, then it will belong to a disjoint tree.
     * For example, in the graph A -> B -> C, if we start at B and then start at A, we will get the forest A and B -> C.
     * @param <V> The type of data in the vertices.
     * @param <E> The type of data in the edges.
     * @param G The graph to perform the DFS in.
     * @param f The function which allows us to visit vertices as we explore the graph. This value can be null, in which case it is ignored.
     * @return Returns a DFS forest as described above.
     * @throws NullPointerException Thrown if {@code G} is null.
     */
    public static <V,E> IGraph<V,E> DFS(IGraph<V,E> G, DFSVisitor<V,E> f)
    {
        //nearly identical with the above DFS function, but will call {@code f} as we visit vertices

        if(G == null)
            throw new NullPointerException();

        Dictionary<Integer,Integer> IDmatch = new Dictionary<>();

        IGraph<V,E> DFSforest = new LGraph<>(true);
        for(Integer gID: G.VertexIDs())
            IDmatch.Add(gID, DFSforest.AddVertex(G.GetVertex(gID)));

        HashSet<Integer> visited = new HashSet<>();

        for(Integer startID: G.VertexIDs())
        {
            if(visited.contains(startID))
                continue;

            Stack<Integer> S = new Stack<>();
            S.Push(startID);
            //visited.add(startID);
            if(f != null)
                f.Visit(G, startID, DFSforest, IDmatch.Get(startID), true);

            while(!S.IsEmpty())
            {
                int currentID = S.Peek();
                visited.add(currentID);

                boolean allVisited = true;
                for(Edge<V,E> e: G.OutboundEdges(currentID))
                {
                    int neighborID = e.Destination.ID;

                    if(!visited.contains(neighborID))
                    {
                        if(f != null)
                            f.Visit(G, neighborID, DFSforest, IDmatch.Get(neighborID), true);
                        allVisited = false;
                        DFSforest.AddEdge(IDmatch.Get(currentID), IDmatch.Get(neighborID), e.Data);
                        S.Push(neighborID);
                        //visited.add(neighborID);
                        break;
                    }
                }
                if(allVisited)
                {
                    int ID = S.Pop();
                    if(f != null)
                        f.Visit(G, ID, DFSforest, IDmatch.Get(ID), false);
                }
            }
        }
        return DFSforest;
    }

    /**
     * Computes a DFS of {@code G} starting from the vertex with ID {@code start}.
     * The DFS will not visit any vertices unreachable from {@code start}.
     * The resultant tree will be a directed graph regardless of if {@code G} is directed or undirected.
     * The edge directions indicate which direction the DFS proceeded over the edge away from the starting vertex.
     * Unvisited vertices will not be part of the returned graph.
     * @param <V> The type of data in the vertices.
     * @param <E> The type of data in the edges.
     * @param G The graph to perform the DFS in.
     * @param start The vertex to start the DFS on.
     * @return Returns a DFS forest as described above.
     * @throws NullPointerException Thrown if {@code G} is null.
     * @throws IllegalArgumentException Thrown if {@code start} is negative.
     * @throws NoSuchVertexException Thrown if {@code start} is not a vertex ID in the graph.
     */
    public static <V,E> IGraph<V,E> DFS(IGraph<V,E> G, int start)
    {
        //nearly identical with the above DFS function, but will only visit vertices reachable from {@code start}
        if(G == null)
            throw new NullPointerException();
        if(start < 0)
            throw new IllegalArgumentException();
        if(!G.ContainsVertex(start))
            throw new NoSuchVertexException(start);

        Dictionary<Integer,Integer> IDmatch = new Dictionary<>();

        IGraph<V,E> DFSforest = new LGraph<>(true);

        HashSet<Integer> visited = new HashSet<>();

        Stack<Integer> S = new Stack<>();
        S.Push(start);
        visited.add(start);
        IDmatch.Add(start, DFSforest.AddVertex(G.GetVertex(start)));

        while(!S.IsEmpty())
        {
            int currentID = S.Peek();
            boolean allVisited = true;
            for(Edge<V,E> e: G.OutboundEdges(currentID))
            {
                int neighborID = e.Destination.ID;
                if(!visited.contains(neighborID))
                {
                    allVisited = false;
                    IDmatch.Add(neighborID, DFSforest.AddVertex(G.GetVertex(neighborID)));
                    DFSforest.AddEdge(IDmatch.Get(currentID), IDmatch.Get(neighborID), e.Data);
                    S.Push(neighborID);
                    visited.add(neighborID);
                    break;
                }
            }
            if(allVisited)
                S.Pop();
        }
        return DFSforest;
    }

    /**
     * Computes a DFS of {@code G} starting from the vertex with ID {@code start}.
     * The DFS will not visit any vertices unreachable from {@code start}.
     * The resultant tree will be a directed graph regardless of if {@code G} is directed or undirected.
     * The edge directions indicate which direction the DFS proceeded over the edge away from the starting vertex.
     * Unvisited vertices will not be part of the returned graph.
     * @param <V> The type of data in the vertices.
     * @param <E> The type of data in the edges.
     * @param G The graph to perform the DFS in.
     * @param start The vertex to start the DFS on.
     * @param f The function which allows us to visit vertices as we explore the graph. This value can be null, in which case it is ignored.
     * @return Returns a DFS tree as described above.
     * @throws NullPointerException Thrown if {@code G} is null.
     * @throws IllegalArgumentException Thrown if {@code start} is negative.
     * @throws NoSuchVertexException Thrown if {@code start} is not a vertex ID in the graph.
     */
    public static <V,E> IGraph<V,E> DFS(IGraph<V,E> G, int start, DFSVisitor<V,E> f)
    {
        //kind of a combination of the above two DFS functions,
        //only visits vertices reachable from {@code start} and allows us to visit vertices using {@code f} as we explore the graph

        if(G == null)
            throw new NullPointerException();
        if(start < 0)
            throw new IllegalArgumentException();
        if(!G.ContainsVertex(start))
            throw new NoSuchVertexException(start);

        if(G == null)
            throw new NullPointerException();

        Dictionary<Integer,Integer> IDmatch = new Dictionary<>();

        IGraph<V,E> DFSforest = new LGraph<>(true);

        HashSet<Integer> visited = new HashSet<>();

        Stack<Integer> S = new Stack<>();
        S.Push(start);
        visited.add(start);
        IDmatch.Add(start, DFSforest.AddVertex(G.GetVertex(start)));

        if(f != null)
            f.Visit(G, start, DFSforest, IDmatch.Get(start), true);

        while(!S.IsEmpty())
        {
            int currentID = S.Peek();
            boolean allVisited = true;
            for(Edge<V,E> e: G.OutboundEdges(currentID))
            {
                int neighborID = e.Destination.ID;
                if(!visited.contains(neighborID))
                {
                    allVisited = false;
                    IDmatch.Add(neighborID, DFSforest.AddVertex(G.GetVertex(neighborID)));
                    DFSforest.AddEdge(IDmatch.Get(currentID), IDmatch.Get(neighborID), e.Data);
                    S.Push(neighborID);
                    visited.add(neighborID);
                    break;
                }
                if(f != null)
                    f.Visit(G, neighborID, DFSforest, IDmatch.Get(neighborID), true);
            }
            if(allVisited)
                S.Pop();
        }
        return DFSforest;
    }

    /**
     * Finds a minimum length path from {@code start} to every other vertex in a weighted graph {@code G}.
     * @param <V> The type of data in the vertices.
     * @param <E> The type of data in the edges.
     * @param G The graph to search for a path within.
     * @param cmp The means by which {@code E} types are compared.
     * @param sum The means by which {@code E} types are summed.
     * @param start The vertex ID to start from.
     * @return
     * Returns the minimum length paths from {@code start} to every other vertex in {@code G}.
     * This will be given as a set of dictionary entries.
     * The first value in the dictionary is the total weight of the path from {@code start} to the vertex in question.
     * The second value is the previous vertex on the shorted path from {@code start} to that vertex.
     * A full path can be constructed by following the previous vertices back to {@code start}.
     * The dictionary entries for start will always be {@code (null,-1)}.
     * <br><br>
     * If a vertex is unreachable from {@code start} in {@code G}, then it will not have an entry in the dictionary returned.
     * @throws NullPointerException Thrown if {@code G}, {@code cmp}, or {@code sum} is null.
     * @throws IllegalArgumentException Thrown if {@code start} or {@code end} is negative.
     * @throws NoSuchVertexException Thrown if {@code start} or {@code end} is not a vertex ID in the graph.
     */
    public static <V,E> Dictionary<Integer, LINQ.Pair<E,Integer>> Dijkstra(IGraph<V,E> G, Comparator<? super E> cmp, SumComputer<E> sum, int start)
    {
        if(sum == null || cmp == null || G == null)
            throw new NullPointerException();
        if(start < 0)
            throw new IllegalArgumentException();
        if(!G.ContainsVertex(start))
            throw new NoSuchVertexException(start);

        Dictionary<Integer, LINQ.Pair<E,Integer>> D = new Dictionary();
        D.Add(start, new LINQ.Pair<>(null, -1));

        HashSet<Integer> visited = new HashSet<>();//vertices have all neighbors explored

        java.util.PriorityQueue<Integer> Q = new java.util.PriorityQueue<Integer>((e1,e2) -> cmp.compare(D.Get(e1).Item1,D.Get(e2).Item1));
        Q.add(start);
        //Q.Enqueue(start);

        while(!Q.isEmpty())
        {
            int currentID = Q.poll();
            //Q.Dequeue();
            visited.add(currentID);

            for(Edge<V, E> e: G.OutboundEdges(currentID))//for each neighbor of current
            {
                int nextID = e.Destination.ID;
                if(visited.contains(nextID))
                    continue;
                E lastWeight = D.Get(currentID).Item1;
                E newWeight = lastWeight == null ? e.Data : sum.Sum(lastWeight, e.Data);
                if(!D.Contains(nextID) || cmp.compare(newWeight, D.Get(nextID).Item1) < 0)//if we found a shorter path to next
                {
                    D.Put(nextID, new LINQ.Pair<>(newWeight, currentID));
                    Q.remove(nextID);
                    Q.add(nextID);
                }
            }
        }
        return D;
    }

    /**
     * Finds a minimum length path from {@code start} to {@code end} in a weighted graph {@code G}.
     * @param <V> The type of data in the vertices.
     * @param <E> The type of data in the edges.
     * @param G The graph to search for a path within.
     * @param cmp The means by which {@code E} types are compared.
     * @param sum The means by which {@code E} types are summed.
     * @param start The vertex ID to start from.
     * @param end The vertex ID to end at.
     * @return Returns the minimum length path from {@code start} to {@code end} in {@code G} or null if there is no such path.
     * @throws NullPointerException Thrown if {@code G}, {@code cmp}, or {@code sum} is null.
     * @throws IllegalArgumentException Thrown if {@code start} or {@code end} is negative.
     * @throws NoSuchVertexException Thrown if {@code start} or {@code end} is not a vertex ID in the graph.
     */
    public static <V,E> Iterable<Integer> Dijkstra(IGraph<V,E> G, Comparator<? super E> cmp, SumComputer<E> sum, int start, int end)
    {
        if(sum == null || cmp == null || G == null)
            throw new NullPointerException();
        if(start < 0 || end < 0)
            throw new IllegalArgumentException();
        if(!G.ContainsVertex(start))
            throw new NoSuchVertexException(start);
        if(!G.ContainsVertex(end))
            throw new NoSuchVertexException(end);

        //this function directly uses the Dijkstra function above to find the path
        Dictionary<Integer, LINQ.Pair<E,Integer>> D = Dijkstra(G, cmp, sum, start);
        if(!D.Contains(end) || start == end)//if there is no path from start to end, return null
            return null;
        //return the path from start to end
        ArrayList<Integer> path = new ArrayList<>();
        int currentVertex = end;
        while(currentVertex != start)
        {
            path.add(0, currentVertex);
            currentVertex = D.Get(currentVertex).Item2;
        }
        path.add(0, start);
        return path;
    }

    /**
     * Finds a minimum length path from {@code start} to {@code end} in an unweighted graph {@code G}.
     * @param <V> The type of data in the vertices.
     * @param <E> The type of data in the edges.
     * @param G The graph to search for a path within.
     * @param start The vertex ID to start from.
     * @param end The vertex ID to end at.
     * @return Returns a minimum length unweighted path from {@code start} to {@code end} in {@code G} or null if there is no such path.
     * @throws NullPointerException Thrown if {@code G} is null.
     * @throws IllegalArgumentException Thrown if {@code start} or {@code end} is negative.
     * @throws NoSuchVertexException Thrown if {@code start} or {@code end} is not a vertex ID in the graph.
     * @implNote This algorithm uses a BFS to search for a minimum length path on an unweighted graph.
     */
    public static <V,E> Iterable<Integer> FindPath(IGraph<V,E> G, int start, int end)
    {
        if(G == null)
            throw new NullPointerException();
        if(start < 0)
            throw new IllegalArgumentException();
        if(!G.ContainsVertex(start))
            throw new NoSuchVertexException(start);

        IGraph<V,E> forest = new LGraph<>(true);
        HashSet<Integer> visited = new HashSet<>();

        for(Integer ID: G.VertexIDs())
            forest.AddVertex(G.GetVertex(ID));

        Queue<Integer> Q = new Queue<>();
        visited.add(start);
        Q.Enqueue(start);

        while(!Q.IsEmpty())
        {
            int currentID = Q.Dequeue();
            for(Edge<V,E> e: G.OutboundEdges(currentID))
            {
                int neighborID = e.Destination.ID;
                if(!visited.contains(neighborID))
                {
                    forest.AddEdge(currentID, neighborID, e.Data);
                    visited.add(neighborID);
                    Q.Enqueue(neighborID);
                }
            }
            if(visited.contains(end))
                break;
        }

        //the above code is a BFS that builds a forest of the shortest path tree
        //it ends building when it finds the end vertex or when it runs out of vertices to search

        //then retrieve the path from {@code start} to {@code end} from the forest
        ArrayList<Integer> path = new ArrayList<>();
        if(!Reachable(G, start, end)) {
            return null;
        }
        int current = end;
        while(current != start)
        {
            Iterator<Edge<V, E>> iter = forest.InboundEdges(current).iterator();
            if(!iter.hasNext())
                return null;
            Edge<V, E> e = iter.next();
            path.add(0, current);
            current = e.Source.ID;
        }
        path.add(0, start);
        return path;
    }

    /**
     * Computes the length of a path given by a sequence of vertex IDs in {@code G}.
     * @param <V> The type of data in the vertices.
     * @param <E> The type of data in the edges.
     * @param G The graph to compute a path length in.
     * @param path The path in {@code G} of interest. This should be given by a sequence of vertex IDs.
     * @param sum The means by which {@code E} types are summed.
     * @return Returns the length of {@code path} in {@code G}. If {@code path} specifies a path of zero or one vertices, null is returned.
     * @throws NullPointerException Thrown if {@code G}, {@code path}, or {@code sum} is null.
     * @throws IllegalArgumentException Thrown if any vertex ID in {@code path} is negative.
     * @throws NoSuchVertexException Thrown if any vertex ID in {@code path} is not a vertex ID in the graph.
     * @throws NoSuchEdgeException Thrown if any adjacent pairs of vertex IDs in {@code path} are not connected by an edge in {@code G}.
     */
    public static <V,E> E PathLength(IGraph<V,E> G, Iterable<? extends Integer> path, SumComputer<E> sum)
    {
        if(G == null || path == null || sum == null)
            throw new NullPointerException();


        Iterator<Integer> iter = (Iterator<Integer>) path.iterator();
        if(!iter.hasNext())
            return null;

        E weightSum = null;
        Integer srcID;
        Integer dstID = iter.next();

        //it retrieves the vertices in the path,
        //and then sum up the weights of the edges between them
        while(iter.hasNext())
        {
            srcID = dstID;
            dstID = iter.next();

            if(!G.ContainsEdge(srcID, dstID))
                throw new NoSuchEdgeException(srcID, dstID);
            if(srcID < 0 || dstID < 0)
                throw new IllegalArgumentException();
            if(!G.ContainsVertex(srcID))
                throw new NoSuchVertexException(srcID);
            if(!G.ContainsVertex(dstID))
                throw new NoSuchVertexException(dstID);

            if(weightSum == null)
                weightSum = G.GetEdge(srcID, dstID);
            else
                weightSum = sum.Sum(weightSum, G.GetEdge(srcID, dstID));
        }
        return weightSum;
    }

    /**
     * Computes a minimum spanning tree of {@code G} via Prim's algorithm.
     * The graph must be connected and undirected.
     * @param <V> The type of data in the vertices.
     * @param <E> The type of data in the edges.
     * @param G The graph to find a minimum spanning tree within.
     * @param cmp The means by which we compare {@code E} types.
     * @return Returns a minimum spanning tree of {@code G}.
     * @throws NullPointerException Thrown if {@code G} or {@code cmp} is null.
     * @throws IllegalArgumentException Thrown if {@code start} is negative or if {@code G} is directed.
     * @throws NoSuchVertexException Thrown if {@code start} is not a vertex ID in the graph.
     * @throws UnconnectedGraphException Thrown if {@code G} is an unconnected graph.
     */
    public static <V,E> IGraph<V,E> Prim(IGraph<V,E> G, Comparator<? super E> cmp)
    {return Prim(G,cmp,G.VertexCount() == 0 ? 0 : G.VertexIDs().iterator().next());}

    /**
     * Computes a minimum spanning tree of {@code G} via Prim's algorithm starting from {@code start}.
     * The graph must be connected and undirected.
     * @param <V> The type of data in the vertices.
     * @param <E> The type of data in the edges.
     * @param G The graph to find a minimum spanning tree within.
     * @param cmp The means by which we compare {@code E} types.
     * @param start The vertex to start growing a minimum spanning tree from.
     * @return Returns a minimum spanning tree of {@code G}.
     * @throws NullPointerException Thrown if {@code G} or {@code cmp} is null.
     * @throws IllegalArgumentException Thrown if {@code start} is negative or if {@code G} is directed.
     * @throws NoSuchVertexException Thrown if {@code start} is not a vertex ID in the graph.
     * @throws UnconnectedGraphException Thrown if {@code G} is an unconnected graph.
     */
    public static <V,E> IGraph<V,E> Prim(IGraph<V,E> G, Comparator<? super E> cmp, int start)
    {
        if(G == null || cmp == null)
            throw new NullPointerException();

        if(G.IsDirected())
            throw new IllegalArgumentException("The graph must be undirected.");

        if(start < 0)
            throw new IllegalArgumentException();

        if(!G.ContainsVertex(start))
            throw new NoSuchVertexException(start);

        // We're going to construct a tree, so an adjacency graph will be a good fit for building it
        IGraph<V,E> ret = new LGraph<V,E>(false);

        Dictionary<Integer,Integer> ID_translation = new Dictionary<Integer,Integer>();
        ID_translation.Add(start,ret.AddVertex(G.GetVertex(start)));

        // Add the initial edges out of our start vertex to the queue
        PriorityQueue<Edge<V,E>> Q = new PriorityQueue<Edge<V,E>>(G.OutboundEdges(start),(e1,e2) -> cmp.compare(e1.Data,e2.Data));

        // We loop until we run out of edges or we're done
        while(!Q.IsEmpty() && ret.VertexCount() < G.VertexCount())
        {
            Edge<V,E> e = Q.Dequeue();

            // If we've already visited a vertex, do nothing
            if(ID_translation.Contains(e.Destination.ID))
                continue;

            // We can now add a new vertex and edge
            int src;

            ID_translation.Add(e.Destination.ID,src = ret.AddVertex(G.GetVertex(e.Destination.ID)));
            ret.AddEdge(ID_translation.Get(e.Source.ID),src,G.GetEdge(e.Source.ID,e.Destination.ID));

            // Add each unvisited neighbor of the new vertex
            for(Edge<V,E> ee : G.OutboundEdges(e.Destination.ID))
                if(!ID_translation.Contains(ee.Destination.ID))
                    Q.Enqueue(ee);
        }

        // If we haven't added every vertex, we're in trouble
        if(ret.VertexCount() != G.VertexCount())
            throw new UnconnectedGraphException();

        return ret;
    }

    /**
     * Determines if {@code start} is reachable from {@code end}.
     * @param <V> The type of data in the vertices.
     * @param <E> The type of data in the edges.
     * @param G The graph to search to determine reachability in.
     * @param start The vertex ID to start from.
     * @param end The vertex ID to end at.
     * @return Returns true if there is a path from {@code start} to {@code end} in {@code G} and false otherwise.
     * @throws NullPointerException Thrown if {@code G} is null.
     * @throws IllegalArgumentException Thrown if {@code start} or {@code end} is negative.
     * @throws NoSuchVertexException Thrown if {@code start} or {@code end} is not a vertex ID in the graph.
     * @implNote This algorithm uses a BFS to search for a minimum length path on an unweighted graph.
     */
    public static <V,E> boolean Reachable(IGraph<V,E> G, int start, int end)
    {
        //this is a revised version of BFS
        if(G == null)
            throw new NullPointerException();
        if(start < 0 || end < 0)
            throw new IllegalArgumentException();
        if(!G.ContainsVertex(start))
            throw new NoSuchVertexException(start);
        if(!G.ContainsVertex(end))
            throw new NoSuchVertexException(end);

        Dictionary<Integer,Integer> IDmatch = new Dictionary<>();
        IGraph<V,E> BFSforest = new LGraph<>(true);
        HashSet<Integer> visited = new HashSet<>();


        Queue<Integer> Q = new Queue<>();
        visited.add(start);
        Q.Enqueue(start);

        while(!Q.IsEmpty())
        {
            if(visited.contains(end))//if we have found {@code end}, we can stop
                return true;
            int currentID = Q.Dequeue();
            if(!IDmatch.Contains(currentID))
                IDmatch.Add(currentID, BFSforest.AddVertex(G.GetVertex(currentID)));
            for(Edge<V,E> e: G.OutboundEdges(currentID))
            {
                int neighborID = e.Destination.ID;
                if(!IDmatch.Contains(neighborID))
                    IDmatch.Add(neighborID, BFSforest.AddVertex(G.GetVertex(neighborID)));
                if(!visited.contains(neighborID))
                {
                    BFSforest.AddEdge(IDmatch.Get(currentID), IDmatch.Get(neighborID), e.Data);
                    visited.add(neighborID);
                    Q.Enqueue(neighborID);
                }
            }
        }
        //if we could not find {@code end}, we can return false
        return false;

    }

    /**
     * Computes a topological sort of {@code G}.
     * The graph must be directed and acyclic.
     * @param <V> The type of data in the vertices.
     * @param <E> The type of data in the edges.
     * @param G The graph to topologically sort.
     * @return Returns a topological sort of {@code G}.
     * @throws NullPointerException Thrown if {@code G} is null.
     * @throws IllegalArgumentException Thrown if {@code G} is undirected.
     * @throws CyclicGraphException Thrown if {@code G} contains a cycle.
     */
    public static <V,E> Iterable<Integer> TopologicalSort(IGraph<V,E> G)
    {
        if(G == null)
            throw new NullPointerException();

        if(G.IsUndirected())
            throw new IllegalArgumentException();

        LinkedList<Integer> ret = new LinkedList<Integer>();

        // Seen vertices are those we have seen but not yet visited twice (once on the way out and once on the way back)
        // If there is a cycle in the graph, we will find it with a DFS using seen vertices and no fully visited vertcies
        // We will add vertices to this on the way out and remove them from it on the way back
        // This is because there can be multiple paths to a vertex
        HashTable<Integer> seen = new HashTable<Integer>();

        DFS(G,(g,g_id,s,s_id,forward) ->
        {
            if(forward)
            {
                for(Integer n : g.Neighbors(g_id))
                    if(seen.contains(n))
                        throw new CyclicGraphException();

                seen.add(g_id);
            }
            else
            {
                ret.AddFront(g_id);
                seen.remove(g_id);
            }

            return;
        });

        return ret;
    }

    /**
     * A function that allows us to visit vertices in a DFS-like search.
     * This corresponds to a search which explores a graph using a stack, allowing us to visit vertices before their neighbors and/or after their neighbors.
     * @author Dawn Nye
     * @param <V> The data type stored in vertices.
     * @param <E> The data type stored in edges.
     */
    @FunctionalInterface public interface DFSVisitor<V,E>
    {
        /**
         * Visits a vertex.
         * @param g The graph the search is being performed within.
         * @param g_me The vertex ID in {@code g} being visited.
         * @param search The search graph being built.
         * @param s_me The vertex ID in {@code search} being visited.
         * @param forward
         * If true, then the traversal calling this is proceeding forward away from the start vertex.
         * If false, then the traversal call this is proceeding backward toward the start vertex.
         */
        public void Visit(IGraph<V,E> g, int g_me, IGraph<V,E> search, int s_me, boolean forward);
    }

    /**
     * A function that allows us to visit vertices in a BFS-like search.
     * This corresponds to a search which explores a graph using a queue, allowing us to visit vertices only when we first reach them right before adding their neighbors to the queue.
     * @author Dawn Nye
     * @param <V> The data type stored in vertices.
     * @param <E> The data type stored in edges.
     */
    @FunctionalInterface public interface BFSVisitor<V,E>
    {
        /**
         * Visits a vertex.
         * @param g The graph the search is being performed within.
         * @param g_me The vertex ID in {@code g} being visited.
         * @param search The search graph being built.
         * @param s_me The vertex ID in {@code search} being visited.
         */
        public void Visit(IGraph<V,E> g, int g_me, IGraph<V,E> search, int s_me);
    }

    /**
     * Computes the sum of a generic type.
     * @author Dawn Nye
     * @param <E> The type to sum.
     */
    @FunctionalInterface public interface SumComputer<E>
    {
        /**
         * Computes the sum of {@code e1} and {@code e2}.
         * @param e1 The left-hand side of the sum.
         * @param e2 The right-hand side of the sum.
         * @return Returns the sum of {@code e1} + {@code e2}.
         * @throws NullPointerException Thrown if {@code e1} or {@code e2} is null and null values are not permitted to be summed.
         */
        public E Sum(E e1, E e2);
    }

    /**
     * An exception thrown when a graph is unconnected when it should be.
     * @author Dawn Nye
     */
    public static class UnconnectedGraphException extends RuntimeException
    {
        /**
         * Creates a new UnconnectedGraphException.
         */
        public UnconnectedGraphException()
        {
            super();
            return;
        }

        /**
         * Creates a new UnconnectedGraphException.
         * @param msg The message in the exception.
         */
        public UnconnectedGraphException(String msg)
        {
            super(msg);
            return;
        }
    }

    /**
     * An exception thrown when a graph is cyclic when it should be acyclic.
     * @author Dawn Nye
     */
    public static class CyclicGraphException extends RuntimeException
    {
        /**
         * Creates a new CyclicGraphException.
         */
        public CyclicGraphException()
        {
            super();
            return;
        }

        /**
         * Creates a new CyclicGraphException.
         * @param msg The message in the exception.
         */
        public CyclicGraphException(String msg)
        {
            super(msg);
            return;
        }
    }
}