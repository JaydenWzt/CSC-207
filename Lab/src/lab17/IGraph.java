package lab17;

/**
 * The bare bones that all graphs have in common.
 * @author Dawn Nye
 * @param <V> The data type stored in a vertex. Each of these values must be distinct.
 * @param <E> The data type stored in an edge.
 */
public interface IGraph<V,E>
{
    /**
     * Adds a new vertex to the graph.
     * @param vertex The vertex data to add.
     * @return Returns the ID of the vertex added or -1 if no vertex was added.
     */
    public int AddVertex(V vertex);

    /**
     * Sets the data of the vertex with ID {@code vertex} in the graph to {@code data}.
     * @param vertex The vertex ID of the vertex whose data we wish to change.
     * @param data The data to put into the vertex.
     * @return Returns the old data stored in the vertex with ID {@code vertex}.
     * @throws IllegalArgumentException Thrown if {@code vertex} is negative.
     * @throws NoSuchVertexException Thrown if no vertex with the ID {@code vertex} exists in the graph.
     */
    public V SetVertex(int vertex, V data);

    /**
     * Obtains the vertex data specified by {@code vertex}.
     * @param vertex The vertex data to obtain.
     * @return Gets the data in the vertex matching the vertex ID {@code vertex} stored in this graph.
     * @throws IllegalArgumentException Thrown if {@code vertex} is negative.
     * @throws NoSuchVertexException Thrown if no vertex with the ID {@code vertex} exists in the graph.
     */
    public V GetVertex(int vertex);

    /**
     * Remove the vertex with ID {@code vertex}.
     * This also removes any edges for which the vertex is an endpoint.
     * @param vertex The ID of the vertex to remove.
     * @return Returns true if a vertex was removed and false otherwise.
     */
    public boolean RemoveVertex(int vertex);

    /**
     * Determines if a vertex with ID {@code vertex} is in the graph.
     * @param vertex The vertex ID to check for.
     * @return Returns true if a vertex with the ID {@code vertex} belongs to the graph and false otherwise.
     */
    public boolean ContainsVertex(int vertex);

    /**
     * Adds the edge ({@code src},{@code dst}) to the graph with data {@code data}.
     * @param src The source vertex ID of the edge. In an undirected graph, this and {@code dst} may be interchanged.
     * @param dst The destination vertex of the edge. In an undirected graph, this and {@code src} may be interchanged.
     * @param data The data to put in the edge.
     * @return Returns true if the edge was added and false otherwise.
     */
    public boolean AddEdge(int src, int dst, E data);

    /**
     * Adds the edge ({@code src},{@code dst}) to the graph with data {@code data}.
     * If the edge already belongs to the graph, it replaces the edge's data with {@code data} and returns the old data.
     * @param src The source vertex ID of the edge. In an undirected graph, this and {@code dst} may be interchanged.
     * @param dst The destination vertex of the edge. In an undirected graph, this and {@code src} may be interchanged.
     * @param data The data to put in the edge.
     * @return Returns {@code data} if the edge did not already exist in the graph. If the edge did exist in the graph prior to this call, then the old data stored in that edge is returned.
     * @throws IllegalArgumentException Thrown if {@code src} or {@code dst} is negative.
     * @throws NoSuchVertexException Thrown if the graph is missing a vertex with the ID {@code src} or {@code dst}.
     */
    public E PutEdge(int src, int dst, E data);

    /**
     * Obtains the edge data in edge ({@code src},{@code dst}).
     * @param src The source vertex ID of the edge. In an undirected graph, this and {@code dst} may be interchanged.
     * @param dst The destination vertex of the edge. In an undirected graph, this and {@code src} may be interchanged.
     * @param data The data to put in the edge.
     * @return Returns the old edge data belonging to the edge ({@code src},{@code dst}).
     * @throws IllegalArgumentException Thrown if {@code src} or {@code dst} is negative.
     * @throws NoSuchVertexException Thrown if the graph is missing a vertex with the ID {@code src} or {@code dst}.
     * @throws NoSuchEdgeException Thrown if the graph does not contain the specified edge.
     */
    public E SetEdge(int src, int dst, E data);

    /**
     * Obtains the edge data in edge ({@code src},{@code dst}).
     * @param src The source vertex ID of the edge. In an undirected graph, this and {@code dst} may be interchanged.
     * @param dst The destination vertex of the edge. In an undirected graph, this and {@code src} may be interchanged.
     * @return Returns the edge data belonging to the edge ({@code src},{@code dst}) stored in this graph.
     * @throws IllegalArgumentException Thrown if {@code src} or {@code dst} is negative.
     * @throws NoSuchVertexException Thrown if the graph is missing a vertex with the ID {@code src} or {@code dst}.
     * @throws NoSuchEdgeException Thrown if the graph does not contain the specified edge.
     */
    public E GetEdge(int src, int dst);

    /**
     * Removes the edge ({@code src},{@code dst}).
     * @param src The source vertex ID of the edge. In an undirected graph, this and {@code dst} may be interchanged.
     * @param dst The destination vertex of the edge. In an undirected graph, this and {@code src} may be interchanged.
     * @return Returns true if the edge was removed and false otherwise.
     */
    public boolean RemoveEdge(int src, int dst);

    /**
     * Determines if the edge ({@code src},{@code dst}) is present in the graph.
     * @param src The source vertex ID of the edge. In an undirected graph, this and {@code dst} may be interchanged.
     * @param dst The destination vertex of the edge. In an undirected graph, this and {@code src} may be interchanged.
     * @return Returns true if the edge belongs to the graph and false otherwise.
     */
    public boolean ContainsEdge(int src, int dst);

    /**
     * Returns an iterable list of all neighbors of the vertex with ID {@code vertex}.
     * The neighbors will be given as their IDs.
     * @param vertex The ID of the vertex whose neighbors we want to obtain.
     * @return Returns an iterable list of neighbors of the vertex with ID {@code vertex}.
     * @throws IllegalArgumentException Thrown if {@code vertex} is negative.
     * @throws NoSuchVertexException Thrown if no vertex with the ID {@code vertex} exists in the graph.
     */
    public Iterable<Integer> Neighbors(int vertex);

    /**
     * Returns the iterable list of all edges leaving the vertex with ID {@code vertex}.
     * @param vertex The ID of the vertex whose outbound edges we want to obtain.
     * @return Returns an iterable list of edges leaving the vertex with ID {@code vertex}.
     * @throws IllegalArgumentException Thrown if {@code vertex} is negative.
     * @throws NoSuchVertexException Thrown if no vertex with the ID {@code vertex} exists in the graph.
     */
    public Iterable<Edge<V,E>> OutboundEdges(int vertex);

    /**
     * Returns the iterable list of all edges entering the vertex with ID {@code vertex}.
     * @param vertex The ID of the vertex whose inbound edges we want.
     * @return Returns an iterable list of inbound edges entering the vertex with ID {@code vertex}.
     * @throws IllegalArgumentException Thrown if {@code vertex} is negative.
     * @throws NoSuchVertexException Thrown if no vertex with the ID {@code vertex} exists in the graph.
     */
    public Iterable<Edge<V,E>> InboundEdges(int vertex);

    /**
     * Returns an iterable list of all currently utilized vertex IDs in the graph.
     */
    public Iterable<Integer> VertexIDs();

    /**
     * Returns an iterable list of all edges in the graph.
     */
    public Iterable<V> Vertices();

    /**
     * Returns an iterable list of all edges in the graph.
     */
    public Iterable<Edge<V,E>> Edges();

    /**
     * Removes all vertices and edges from the graph.
     */
    public void Clear();

    /**
     * Determines the number of vertices in this graph.
     */
    public int VertexCount();

    /**
     * Determines the number of edges in this graph.
     */
    public int EdgeCount();

    /**
     * Determines the out-degree of the vertex with ID {@code vertex}.
     * @param vertex The ID of the vertex to count the outgoing edges of.
     * @throws IllegalArgumentException Thrown if {@code src} or {@code dst} is negative.
     * @throws NoSuchVertexException Thrown if no vertex with the ID {@code vertex} exists in the graph.
     */
    public int OutDegree(int vertex);

    /**
     * Determines the in-degree of the vertex with ID {@code vertex}.
     * @param vertex The ID of the vertex to count the incoming edges of.
     * @throws IllegalArgumentException Thrown if {@code src} or {@code dst} is negative.
     * @throws NoSuchVertexException Thrown if no vertex with the ID {@code vertex} exists in the graph.
     */
    public int InDegree(int vertex);

    /**
     * Determines if this is a directed graph.
     * @return Returns true if the graph is directed and false otherwise.
     */
    public boolean IsDirected();

    /**
     * Determines if this is an undirected graph.
     * @return Returns true if the graph is undirected and false otherwise.
     */
    public default boolean IsUndirected()
    {return !IsDirected();}

    /**
     * The bare bones of a vertex of a graph.
     * @author Dawn Nye
     * @param <V> The data type to store in the vertex.
     */
    public static class Vertex<V>
    {
        /**
         * Creates an empty vertex with the provided id.
         * @param id The vertex ID.
         * @throws IllegalArgumentException Thrown if {@code id} is negative.
         */
        public Vertex(int id)
        {
            if(id < 0)
                throw new IllegalArgumentException();

            ID = id;
            Data = null;

            return;
        }

        /**
         * Creates a new vertex with the given data.
         * @param id The vertex ID.
         * @param data The data to store in this vertex.
         * @throws IllegalArgumentException Thrown if {@code id} is negative.
         */
        public Vertex(int id, V data)
        {
            if(id < 0)
                throw new IllegalArgumentException();

            ID = id;
            Data = data;

            return;
        }

        @Override public boolean equals(Object obj)
        {
            if(obj instanceof Vertex)
                return ID == ((Vertex)obj).ID; // Vertices are equal iff their IDs are equal by default

            return false;
        }

        @Override public int hashCode()
        {return ID;} // Vertices are equal iff their IDs are equal by default, and we pick a hashcode function to match

        @Override public String toString()
        {return ID + ": " + Data;}

        /**
         * The vertex ID.
         */
        public int ID;

        /**
         * The data associated with this vertex.
         */
        public V Data;
    }

    /**
     * The bare bones of an edge of a graph.
     * @author Dawn Nye
     * @param <V> The data type stored in a vertex.
     * @param <E> The data type to store in an edge.
     */
    public static class Edge<V,E>
    {
        /**
         * Creates a new edge between {@code src} and {@code dst} with the data {@code data}.
         * @param src The source vertex for the edge.
         * @param dst The destination vertex for the edge.
         * @param data The data stored in the edge.
         * @param dir This value indicates if this is a directed edge.
         */
        public Edge(Vertex<V> src, Vertex<V> dst, E data, boolean dir)
        {
            Source = src;
            Destination = dst;
            Data = data;
            Directed = dir;

            return;
        }

        /**
         * Creates a dummy edge that will pass an equality test or a hashcode check but contains no data.
         * @param src The source vertex ID.
         * @param dst The destination vertex ID.
         * @param dir This value indicates if this is a directed edge.
         */
        public Edge(int src, int dst, boolean dir)
        {
            Source = new Vertex<V>(src);
            Destination = new Vertex<V>(dst);
            Data = null;
            Directed = dir;

            return;
        }

        @Override public boolean equals(Object obj)
        {
            if(obj instanceof Edge)
            {
                Edge e = (Edge)obj;

                if(Directed) // Directed edges are equal if they have the same source and destination
                    return Source.equals(e.Source) && Destination.equals(e.Destination);
                else // Undirected edges are equal if their endpoints match
                    return Source.equals(e.Source) && Destination.equals(e.Destination) || Source.equals(e.Destination) && Destination.equals(e.Source);
            }

            return false;
        }

        @Override public int hashCode()
        {return Destination.hashCode();} // Edges are hashed via their destination only, since edges should be sorted by their source first

        @Override public String toString()
        {return "(" + Source.ID + "," + Destination.ID + ")";}

        /**
         * The source vertex of this edge.
         */
        public final Vertex<V> Source;

        /**
         * The destination vertex of this edge.
         */
        public final Vertex<V> Destination;

        /**
         * If true, this is a directed edge.
         * If false, this is an undirected edge.
         */
        public boolean Directed;

        /**
         * The data stored in this edge.
         */
        public E Data;
    }

    /**
     * An exception thrown when a vertex doesn't exist.
     * @author Dawn Nye
     */
    public static class NoSuchVertexException extends RuntimeException
    {
        /**
         * Creates a new NoSuchVertexException.
         * @param id The ID of the missing vertex.
         */
        public NoSuchVertexException(int id)
        {
            super();
            ID = id;

            return;
        }

        /**
         * Creates a new NoSuchVertexException.
         * @param id The ID of the missing vertex.
         * @param msg The message in the exception.
         */
        public NoSuchVertexException(int id, String msg)
        {
            super(msg);
            ID = id;

            return;
        }

        /**
         * The ID of the missing vertex.
         */
        public final int ID;
    }

    /**
     * An exception thrown when an edge doesn't exist.
     * @author Dawn Nye
     */
    public static class NoSuchEdgeException extends RuntimeException
    {
        /**
         * Creates a new NoSuchEdgeException.
         * @param src The source vertex ID of the edge.
         * @param dst The destination vertex ID of the edge.
         */
        public NoSuchEdgeException(int src, int dst)
        {
            super();

            Source = src;
            Destination = dst;

            return;
        }

        /**
         * Creates a new NoSuchEdgeException.
         * @param src The source vertex ID of the edge.
         * @param dst The destination vertex ID of the edge.
         * @param msg The message in the exception.
         */
        public NoSuchEdgeException(int src, int dst, String msg)
        {
            super(msg);

            Source = src;
            Destination = dst;

            return;
        }

        /**
         * The ID of the source vertex.
         */
        public final int Source;

        /**
         * The ID of the destination vertex.
         */
        public final int Destination;
    }
}