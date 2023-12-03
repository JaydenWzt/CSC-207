package lab17;

public class Main {

    public static void main(String[] args) {
        LGraph<Integer,String> test = new LGraph<>(true);

        for(int i = 0; i < 4; i++)
        {
            test.AddVertex(i);
        }

        for(int i = 1; i < 4; i++)
        {
            test.AddEdge(0,i,"from 0 to " + i);
        }
        //test.AddEdge(1,2,"from 1 to 2");
        test.AddEdge(1,3,"from 1 to 3");
        test.AddEdge(2,3,"from 2 to 3");

        for(int i = 1; i < 4; i++)
        {
            test.AddEdge(i,0,"from " + i + " to 0");
        }
        test.AddEdge(2,1,"from 2 to 1");
        test.AddEdge(3,1,"from 3 to 1");
        test.AddEdge(3,2,"from 3 to 2");



        for(IGraph.Edge<Integer,String> e: test.OutboundEdges(0))
            System.out.println(e.Data);
        System.out.println("");
        for(IGraph.Edge<Integer,String> e: test.OutboundEdges(1))
            System.out.println(e.Data);
        System.out.println("");
        for(IGraph.Edge<Integer,String> e: test.OutboundEdges(2))
            System.out.println(e.Data);
        System.out.println("");
        for(IGraph.Edge<Integer,String> e: test.OutboundEdges(3))
            System.out.println(e.Data);

        System.out.println("");

        for(IGraph.Edge<Integer,String> e: test.InboundEdges(0))
            System.out.println(e.Data);
        System.out.println("");
        for(IGraph.Edge<Integer,String> e: test.InboundEdges(1))
            System.out.println(e.Data);
        System.out.println("");
        for(IGraph.Edge<Integer,String> e: test.InboundEdges(2))
            System.out.println(e.Data);
        System.out.println("");
        for(IGraph.Edge<Integer,String> e: test.InboundEdges(3))
            System.out.println(e.Data);

        System.out.println("");
        System.out.println(test.InDegree(0));
        System.out.println(test.InDegree(1));
        System.out.println(test.InDegree(2));
        System.out.println(test.InDegree(3));
        System.out.println("");
        System.out.println(test.OutDegree(0));
        System.out.println(test.OutDegree(1));
        System.out.println(test.OutDegree(2));
        System.out.println(test.OutDegree(3));
        /*
        for(IGraph.Edge<Integer,String> e: test.InboundEdges(0))
            System.out.println(e.Data);
         */
    }
}
