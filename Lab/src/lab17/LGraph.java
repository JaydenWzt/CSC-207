
package lab17;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class LGraph<V, E> implements IGraph<V, E> {

    //the backing data structure
    //the index of each pair in the arraylist is the same as the ID of the correspondent vertex's {@code ID}
    ArrayList<Pair<V, E>> lst;
    //indicating whether this graph is directed or not
    boolean Directed;

    /**
     * The constructor
     * @param directed Indicates whether this graph is directed or not
     */
    public LGraph(boolean directed) {
        Directed = directed;
        lst = new ArrayList<>();
    }

    /**
     * Adds a new vertex to the graph.
     *
     * @param vertex The vertex data to add.
     * @return Returns the ID of the vertex added or -1 if no vertex was added.
     */
    @Override
    public int AddVertex(V vertex) {
        //if {@code lst} contains a null index, add the new vertex there
        for (int i = 0; i < lst.size(); i++) {
            if (lst.get(i) == null) {
                Vertex<V> v = new Vertex<>(i, vertex);
                lst.add(new Pair<>(v, new LinkedList<>()));
                return i;
            }
        }
        //if no available index, add the new vertex at the end
        int id = this.lst.size();
        Vertex<V> v = new Vertex<>(id, vertex);
        lst.add(new Pair<>(v, new LinkedList<>()));
        return id;

    }

    /**
     * Sets the data of the vertex with ID {@code vertex} in the graph to {@code data}.
     *
     * @param vertex The vertex ID of the vertex whose data we wish to change.
     * @param data   The data to put into the vertex.
     * @return Returns the old data stored in the vertex with ID {@code vertex}.
     * @throws IllegalArgumentException Thrown if {@code vertex} is negative.
     * @throws NoSuchVertexException    Thrown if no vertex with the ID {@code vertex} exists in the graph.
     */
    @Override
    public V SetVertex(int vertex, V data) {
        if (vertex < 0)
            throw new IllegalArgumentException();
        if (vertex >= lst.size())
            throw new NoSuchVertexException(vertex);

        //set the {@code Data} of the edge to {@code data}
        V prev = lst.get(vertex).V.Data;
        lst.get(vertex).V.Data = data;
        return prev;
    }

    /**
     * Obtains the vertex data specified by {@code vertex}.
     *
     * @param vertex The vertex data to obtain.
     * @return Gets the data in the vertex matching the vertex ID {@code vertex} stored in this graph.
     * @throws IllegalArgumentException Thrown if {@code vertex} is negative.
     * @throws NoSuchVertexException    Thrown if no vertex with the ID {@code vertex} exists in the graph.
     */
    @Override
    public V GetVertex(int vertex) {
        if (vertex < 0)
            throw new IllegalArgumentException();
        if (vertex >= lst.size())
            throw new NoSuchVertexException(vertex);

        return lst.get(vertex).V.Data;
    }

    /**
     * Remove the vertex with ID {@code vertex}.
     * This also removes any edges for which the vertex is an endpoint.
     *
     * @param vertex The ID of the vertex to remove.
     * @return Returns true if a vertex was removed and false otherwise.
     */
    @Override
    public boolean RemoveVertex(int vertex) {
        //go through the entire graph to remove all edges ends with {@code vertex}
        for (Pair<V, E> pair : this.lst) {
            for (Edge<V, E> e1 : pair.Edges) {
                if (e1.Destination.ID == vertex) {
                    if (!pair.Edges.remove(e1))
                        return false;
                }
            }
        }

        //remove the pair at the {@code vertex} position in {@code lst}
        lst.set(vertex, null);
        return true;
    }

    /**
     * Determines if a vertex with ID {@code vertex} is in the graph.
     *
     * @param vertex The vertex ID to check for.
     * @return Returns true if a vertex with the ID {@code vertex} belongs to the graph and false otherwise.
     */
    @Override
    public boolean ContainsVertex(int vertex) {
        if (vertex >= lst.size() || vertex < 0)
            return false;
        return lst.get(vertex) != null;
    }

    /**
     * Adds the edge ({@code src},{@code dst}) to the graph with data {@code data}.
     *
     * @param src  The source vertex ID of the edge. In an undirected graph, this and {@code dst} may be interchanged.
     * @param dst  The destination vertex of the edge. In an undirected graph, this and {@code src} may be interchanged.
     * @param data The data to put in the edge.
     * @return Returns true if the edge was added and false otherwise.
     */
    @Override
    public boolean AddEdge(int src, int dst, E data) {
        //if either vertex {@code src} or {@code dst} doesn't exist, return false
        if (src < 0 || dst < 0 || src >= lst.size() || dst >= lst.size() || lst.get(src) == null || lst.get(dst) == null)
            return false;


        Edge<V, E> e = new Edge<>(lst.get(src).V, lst.get(dst).V, data, Directed);
        return !ContainsEdge(src, dst) && lst.get(src).Edges.add(e);//if such edge already exists, return false; otherwise,add the new edge
    }

    /**
     * Adds the edge ({@code src},{@code dst}) to the graph with data {@code data}.
     * If the edge already belongs to the graph, it replaces the edge's data with {@code data} and returns the old data.
     *
     * @param src  The source vertex ID of the edge. In an undirected graph, this and {@code dst} may be interchanged.
     * @param dst  The destination vertex of the edge. In an undirected graph, this and {@code src} may be interchanged.
     * @param data The data to put in the edge.
     * @return Returns {@code data} if the edge did not already exist in the graph. If the edge did exist in the graph prior to this call, then the old data stored in that edge is returned.
     * @throws IllegalArgumentException Thrown if {@code src} or {@code dst} is negative.
     * @throws NoSuchVertexException    Thrown if the graph is missing a vertex with the ID {@code src} or {@code dst}.
     */
    @Override
    public E PutEdge(int src, int dst, E data) {
        if (src < 0 || dst < 0)
            throw new IllegalArgumentException();
        if (src >= lst.size() || lst.get(src) == null)
            throw new NoSuchVertexException(src);
        if (dst >= lst.size() || lst.get(dst) == null)
            throw new NoSuchVertexException(dst);

        //find the edge starts with ID {@code src} and ends with ID {@code dst}
        for (Edge<V, E> e1 : lst.get(src).Edges) {
            if (e1.Destination.ID == dst) {
                E prev = e1.Data;
                e1.Data = data;//replace the weight of the edge with {@code data}
                return prev;
            }
        }

        //if not directed
        //also find the edge starts with ID {@code dst} and ends with ID {@code src}
        if (!Directed) {
            for (Edge<V, E> e2 : lst.get(dst).Edges) {
                if (e2.Destination.ID == src) {
                    E prev = e2.Data;
                    e2.Data = data;//replace the weight of the edge with {@code data}
                    return prev;
                }
            }
        }
        //if the edge does not exist, add a new edge
        AddEdge(src, dst, data);
        return data;
    }

    /**
     * Obtains the edge data in edge ({@code src},{@code dst}).
     *
     * @param src  The source vertex ID of the edge. In an undirected graph, this and {@code dst} may be interchanged.
     * @param dst  The destination vertex of the edge. In an undirected graph, this and {@code src} may be interchanged.
     * @param data The data to put in the edge.
     * @return Returns the old edge data belonging to the edge ({@code src},{@code dst}).
     * @throws IllegalArgumentException Thrown if {@code src} or {@code dst} is negative.
     * @throws NoSuchVertexException    Thrown if the graph is missing a vertex with the ID {@code src} or {@code dst}.
     * @throws NoSuchEdgeException      Thrown if the graph does not contain the specified edge.
     */
    @Override
    public E SetEdge(int src, int dst, E data) {
        if (src < 0 || dst < 0)
            throw new IllegalArgumentException();
        if (src >= lst.size() || lst.get(src) == null)
            throw new NoSuchVertexException(src);
        if (dst >= lst.size() || lst.get(dst) == null)
            throw new NoSuchVertexException(dst);

        //find the edge starts with ID {@code src} and ends with ID {@code dst}
        for (Edge<V, E> e1 : lst.get(src).Edges) {
            if (e1.Destination.ID == dst) {
                E prev = e1.Data;
                e1.Data = data;//replace the weight of the edge with {@code data}
                return prev;
            }
        }

        //if not directed
        //also find the edge starts with ID {@code dst} and ends with ID {@code src}
        if (!Directed) {
            for (Edge<V, E> e2 : lst.get(dst).Edges) {
                if (e2.Destination.ID == src) {
                    E prev = e2.Data;
                    e2.Data = data;//replace the weight of the edge with {@code data}
                    return prev;
                }
            }
        }
        //if no such edge exists
        throw new NoSuchEdgeException(src, dst);
    }

    /**
     * Obtains the edge data in edge ({@code src},{@code dst}).
     *
     * @param src The source vertex ID of the edge. In an undirected graph, this and {@code dst} may be interchanged.
     * @param dst The destination vertex of the edge. In an undirected graph, this and {@code src} may be interchanged.
     * @return Returns the edge data belonging to the edge ({@code src},{@code dst}) stored in this graph.
     * @throws IllegalArgumentException Thrown if {@code src} or {@code dst} is negative.
     * @throws NoSuchVertexException    Thrown if the graph is missing a vertex with the ID {@code src} or {@code dst}.
     * @throws NoSuchEdgeException      Thrown if the graph does not contain the specified edge.
     */
    @Override
    public E GetEdge(int src, int dst) {
        if (src < 0 || dst < 0)
            throw new IllegalArgumentException();
        if (src >= lst.size() || lst.get(src) == null)
            throw new NoSuchVertexException(src);
        if (dst >= lst.size() || lst.get(dst) == null)
            throw new NoSuchVertexException(dst);

        //go through edges start with ID {@code src} to get the edge ends with ID {@code dst}
        for (Edge<V, E> e1 : lst.get(src).Edges)
            if (e1.Destination.ID == dst)
                return e1.Data;

        //if not directed
        //also go through edges start with ID {@code dst} to get the edge ends with ID {@code src}
        if (!Directed)
            for (Edge<V, E> e2 : lst.get(dst).Edges)
                if (e2.Destination.ID == src)
                    return e2.Data;

        throw new NoSuchEdgeException(src, dst);

    }

    /**
     * Removes the edge ({@code src},{@code dst}).
     *
     * @param src The source vertex ID of the edge. In an undirected graph, this and {@code dst} may be interchanged.
     * @param dst The destination vertex of the edge. In an undirected graph, this and {@code src} may be interchanged.
     * @return Returns true if the edge was removed and false otherwise.
     */
    @Override
    public boolean RemoveEdge(int src, int dst) {
        //if either ID doesn't exist, return false
        if (src < 0 || dst < 0 || src >= lst.size() || dst >= lst.size() || lst.get(src) == null || lst.get(dst) == null)
            return false;

        //go through edges start with ID {@code src} to remove the edge ends with ID {@code dst}
        for (Edge<V, E> e1 : lst.get(src).Edges)
            if (e1.Destination.ID == dst)
                return lst.get(src).Edges.remove(e1);

        //if not directed
        //also go through edges start with ID {@code dst} to remove the edge ends with ID {@code src}
        if (!Directed)
            for (Edge<V, E> e2 : lst.get(dst).Edges)
                if (e2.Destination.ID == src)
                    return lst.get(dst).Edges.remove(e2);

        return false;
    }

    /**
     * Determines if the edge ({@code src},{@code dst}) is present in the graph.
     *
     * @param src The source vertex ID of the edge. In an undirected graph, this and {@code dst} may be interchanged.
     * @param dst The destination vertex of the edge. In an undirected graph, this and {@code src} may be interchanged.
     * @return Returns true if the edge belongs to the graph and false otherwise.
     */
    @Override
    public boolean ContainsEdge(int src, int dst) {
        //if either ID doesn't exist, return false
        if (src < 0 || dst < 0 || src >= lst.size() || dst >= lst.size() || lst.get(src) == null || lst.get(dst) == null)
            return false;

        //check whether the edges start from ID {@code src} and ends with {@code dst}
        for (Edge<V, E> e1 : lst.get(src).Edges)
            if (e1.Destination.ID == dst)//if exists, return true
                return true;

        //check whether the edges start from ID {@code src} and ends with {@code dst}
        if (!Directed)
            for (Edge<V, E> e2 : lst.get(dst).Edges)
                if (e2.Destination.ID == src)
                    return true;

        return false;
    }

    /**
     * Returns an iterable list of all neighbors of the vertex with ID {@code vertex}.
     * The neighbors will be given as their IDs.
     *
     * @param vertex The ID of the vertex whose neighbors we want to obtain.
     * @return Returns an iterable list of neighbors of the vertex with ID {@code vertex}.
     * @throws IllegalArgumentException Thrown if {@code vertex} is negative.
     * @throws NoSuchVertexException    Thrown if no vertex with the ID {@code vertex} exists in the graph.
     */
    @Override
    public Iterable<Integer> Neighbors(int vertex) {
        if (vertex < 0)
            throw new IllegalArgumentException();
        if (vertex >= lst.size())
            throw new NoSuchVertexException(vertex);

        //get the IDs of neighbors that with edge start from ID {@code vertex}
        ArrayList<Integer> neighbors = new ArrayList<>();
        for (Edge<V, E> e1 : lst.get(vertex).Edges)
            neighbors.add(e1.Destination.ID);

        //get the IDs of neighbors that with edge ends at ID {@code vertex}
        for (Pair<V, E> pair : this.lst)
            for (Edge<V, E> e2 : pair.Edges)
                if (e2.Destination.ID == vertex && !neighbors.contains(e2.Source.ID))
                    neighbors.add(e2.Source.ID);

        return new Iterable<Integer>() {
            public Iterator<Integer> iterator() {
                return new Iterator<Integer>() {
                    public boolean hasNext() {
                        return neighbors.size() != 0;
                    }

                    public Integer next() {
                        if (!hasNext())
                            throw new NoSuchElementException();
                        return neighbors.remove(0);
                    }
                };
            }
        };
    }

    /**
     * Returns the iterable list of all edges leaving the vertex with ID {@code vertex}.
     *
     * @param vertex The ID of the vertex whose outbound edges we want to obtain.
     * @return Returns an iterable list of edges leaving the vertex with ID {@code vertex}.
     * @throws IllegalArgumentException Thrown if {@code vertex} is negative.
     * @throws NoSuchVertexException    Thrown if no vertex with the ID {@code vertex} exists in the graph.
     */
    @Override
    public Iterable<Edge<V, E>> OutboundEdges(int vertex) {
        if (vertex < 0)
            throw new IllegalArgumentException();
        if (vertex >= lst.size())
            throw new NoSuchVertexException(vertex);

        //add all edges start at ID {@code vertex}
        ArrayList<Edge<V, E>> edges = new ArrayList<>(lst.get(vertex).Edges);

        //if not directed, go over the entire graph to get edges ends with ID {@code vertex}
        if (!Directed)
            for (Pair<V, E> pair : this.lst)
                for (Edge<V, E> e : pair.Edges)
                    if (e.Destination.ID == vertex)
                        edges.add(e);

        return new Iterable<Edge<V, E>>() {
            public Iterator<Edge<V, E>> iterator() {
                return new Iterator<Edge<V, E>>() {
                    public boolean hasNext() {
                        return edges.size() != 0;
                    }

                    public Edge<V, E> next() {
                        if (!hasNext())
                            throw new NoSuchElementException();
                        return edges.remove(0);
                    }
                };
            }
        };
    }

    /**
     * Returns the iterable list of all edges entering the vertex with ID {@code vertex}.
     *
     * @param vertex The ID of the vertex whose inbound edges we want.
     * @return Returns an iterable list of inbound edges entering the vertex with ID {@code vertex}.
     * @throws IllegalArgumentException Thrown if {@code vertex} is negative.
     * @throws NoSuchVertexException    Thrown if no vertex with the ID {@code vertex} exists in the graph.
     */
    @Override
    public Iterable<Edge<V, E>> InboundEdges(int vertex) {
        if (vertex < 0)
            throw new IllegalArgumentException();
        if (vertex >= lst.size())
            throw new NoSuchVertexException(vertex);

        ArrayList<Edge<V, E>> edges = new ArrayList<>();

        //if not directed, all edges starts from ID {@code vertex} would count
        if (!Directed)
            edges.addAll(lst.get(vertex).Edges);

        //go over the graph to find edges ends with ID {@code vertex}
        for (Pair<V, E> pair : this.lst)
            for (Edge<V, E> e : pair.Edges)
                if (e.Destination.ID == vertex)
                    edges.add(e);

        return new Iterable<>() {
            public Iterator<Edge<V, E>> iterator() {
                return new Iterator<>() {
                    public boolean hasNext() {
                        return edges.size() != 0;
                    }

                    public Edge<V, E> next() {
                        if (!hasNext())
                            throw new NoSuchElementException();
                        return edges.remove(0);
                    }
                };
            }
        };
    }

    /**
     * Returns an iterable list of all currently utilized vertex IDs in the graph.
     */
    @Override
    public Iterable<Integer> VertexIDs() {
        ArrayList<Integer> IDs = new ArrayList<>();

        for (Pair<V, E> pair : this.lst)//add all IDs in
            IDs.add(pair.V.ID);

        return new Iterable<Integer>() {
            public Iterator<Integer> iterator() {
                return new Iterator<Integer>() {
                    public boolean hasNext() {
                        return IDs.size() != 0;
                    }

                    public Integer next() {
                        if (!hasNext())
                            throw new NoSuchElementException();
                        return IDs.remove(0);
                    }
                };
            }
        };
    }

    /**
     * Returns an iterable list of all vertices in the graph.
     */
    @Override
    public Iterable<V> Vertices() {
        ArrayList<V> vertices = new ArrayList<>();

        for (Pair<V, E> pair : this.lst)//add all vertices in
            vertices.add(pair.V.Data);

        return new Iterable<V>() {
            public Iterator<V> iterator() {
                return new Iterator<V>() {
                    public boolean hasNext() {
                        return vertices.size() != 0;
                    }

                    public V next() {
                        if (!hasNext())
                            throw new NoSuchElementException();
                        return vertices.remove(0);
                    }
                };
            }
        };
    }

    /**
     * Returns an iterable list of all edges in the graph.
     */
    @Override
    public Iterable<Edge<V, E>> Edges() {
        ArrayList<Edge<V, E>> ver = new ArrayList<>();

        for (Pair<V, E> pair : this.lst)//add all edges in
            ver.addAll(pair.Edges);

        return new Iterable<>() {
            public Iterator<Edge<V, E>> iterator() {
                return new Iterator<>() {
                    public boolean hasNext() {
                        return ver.size() != 0;
                    }

                    public Edge<V, E> next() {
                        if (!hasNext())
                            throw new NoSuchElementException();
                        return ver.remove(0);
                    }
                };
            }
        };
    }

    /**
     * Removes all vertices and edges from the graph.
     */
    @Override
    public void Clear() {
        lst = new ArrayList<>();
    }

    /**
     * Determines the number of vertices in this graph.
     */
    @Override
    public int VertexCount() {
        int count = 0;
        for (Pair<V, E> vePair : lst) {
            if (vePair != null)
                count++;
        }
        return count;
    }

    /**
     * Determines the number of edges in this graph.
     */
    @Override
    public int EdgeCount() {
        int count = 0;
        for (Pair<V, E> pair : this.lst)
            count += pair.Edges.size();

        return count;
    }

    /**
     * Determines the out-degree of the vertex with ID {@code vertex}.
     *
     * @param vertex The ID of the vertex to count the outgoing edges of.
     * @throws IllegalArgumentException Thrown if {@code src} or {@code dst} is negative.
     * @throws NoSuchVertexException    Thrown if no vertex with the ID {@code vertex} exists in the graph.
     */
    @Override
    public int OutDegree(int vertex) {
        if (vertex < 0)
            throw new IllegalArgumentException();
        if (vertex >= lst.size())
            throw new NoSuchVertexException(vertex);

        int count = 0;

        /*
        //all edges starting from ID {@code vertex} would count
        count += lst.get(vertex).Edges.size();

        //if not directed, all edges ends with ID {@code vertex} would also count
        if (!Directed)
            for (Pair<V, E> pair : this.lst)
                for (Edge<V, E> e : pair.Edges)
                    if (e.Destination.ID == vertex)
                        count++;
         */
        for(Edge<V, E> e: OutboundEdges(vertex))
            count ++;
        return count;
    }

    /**
     * Determines the in-degree of the vertex with ID {@code vertex}.
     *
     * @param vertex The ID of the vertex to count the incoming edges of.
     * @throws IllegalArgumentException Thrown if {@code src} or {@code dst} is negative.
     * @throws NoSuchVertexException    Thrown if no vertex with the ID {@code vertex} exists in the graph.
     */
    @Override
    public int InDegree(int vertex) {
        if (vertex < 0)
            throw new IllegalArgumentException();
        if (vertex >= lst.size())
            throw new NoSuchVertexException(vertex);

        int count = 0;

        if (!Directed)//if not directed, all edges starts from ID {@code vertex} would count
            count += lst.get(vertex).Edges.size();

        //go through the entire graph to find edges ends with ID {@code vertex}
        for (Pair<V, E> pair : this.lst)
            for (Edge<V, E> e : pair.Edges)
                if (e.Destination.ID == vertex)
                    count++;

        return count;
    }

    /**
     * Determines if this is a directed graph.
     *
     * @return Returns true if the graph is directed and false otherwise.
     */
    @Override
    public boolean IsDirected() {
        return Directed;
    }

    /**
     * The inner class represents a pair of a vertex and its correspodent edges
     * @param <V> The type of each vertex
     * @param <E> The type of weight for each edge
     */
    public static class Pair<V, E> {
        //The vertex
        public Vertex<V> V;
        //The linked list containing all the connected edges
        public LinkedList<Edge<V, E>> Edges;
        public Pair(Vertex<V> v, LinkedList<Edge<V, E>> e) {
            V = v;
            Edges = e;
        }

    }
}