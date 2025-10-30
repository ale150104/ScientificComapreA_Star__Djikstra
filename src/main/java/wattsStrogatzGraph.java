import com.mxgraph.layout.mxCircleLayout;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.AStarAdmissibleHeuristic;
import org.jgrapht.alg.shortestpath.AStarShortestPath;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.generate.WattsStrogatzGraphGenerator;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.util.SupplierUtil;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class wattsStrogatzGraph {

    private Graph<customVertex, DefaultWeightedEdge> graph;

    private JGraphXAdapter<customVertex, DefaultWeightedEdge> graphAdapter;

    public void run() {
        // --- 1. Graph erzeugen ---
        Supplier<customVertex> vertexSupplier = new Supplier<>() {
            private int id = 0;

            @Override
            public customVertex get() {
                return new customVertex(id++, Integer.MAX_VALUE);
            }
        };

        this.graph = new SimpleWeightedGraph<>(vertexSupplier, SupplierUtil.DEFAULT_WEIGHTED_EDGE_SUPPLIER);


        WattsStrogatzGraphGenerator<customVertex, DefaultWeightedEdge> generator =
                new WattsStrogatzGraphGenerator<customVertex, DefaultWeightedEdge>(400, 52, 0.45);


        generator.generateGraph(graph);

        int startEdgeId = 1;
        int targetEdgeId = 98;


        // --- 2. Visualisierung mit JGraphX ---
        this.graphAdapter = new JGraphXAdapter<>(graph);


        mxCircleLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        this.adaptGraphForHeuristicToBeAdmissinble();

//        JFrame frame = new JFrame("Watts–Strogatz Graph");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(new mxGraphComponent(graphAdapter));
//        frame.pack();
//        frame.setVisible(true);


        A_Star_Algorithm a_star_Algorithm = new A_Star_Algorithm(graph, graphAdapter);
        pathFindingDiagnostics result = a_star_Algorithm.findOptimalPath(startEdgeId, targetEdgeId);

        List<customVertex> path = a_star_Algorithm.getPath();

        System.out.println("----- Results: -----");
        System.out.println("Path:");
        for(customVertex vertex: path)
        {
            System.out.println(vertex.id);
        }
        System.out.println(result);


        Djikstra_Algorithm djikstra_Algorithm = new Djikstra_Algorithm(graph, graphAdapter);
        pathFindingDiagnostics djikstraResult = djikstra_Algorithm.findOptimalPath(startEdgeId, targetEdgeId);

        List<customVertex> djikstraPath = djikstra_Algorithm.getPath();

        System.out.println("----- Results: -----");
        System.out.println("Path:");
        for(customVertex vertex: djikstraPath)
        {
            System.out.println(vertex.id);
        }
        System.out.println(djikstraResult);


        AStarAdmissibleHeuristic<customVertex> heuristic = (p1, p2) ->
        {
            Point vertexCell = graphAdapter.getVertexToCellMap().get(p1).getGeometry().getPoint();
            Point targetVertexCell = graphAdapter.getVertexToCellMap().get(p2).getGeometry().getPoint();

            return (int) Math.floor(targetVertexCell.distance(vertexCell));
        };

        AStarShortestPath<customVertex, DefaultWeightedEdge> aStar =
                new AStarShortestPath<>(graph, heuristic);


        // Kürzesten Pfad berechnen
        customVertex startV = graph.vertexSet().stream().filter((vertex) -> vertex.id == 1).findFirst().get();
        customVertex targetV = graph.vertexSet().stream().filter((vertex) -> vertex.id == 98).findFirst().get();
        GraphPath<customVertex, DefaultWeightedEdge> pathh = aStar.getPath(startV, targetV);

        System.out.println(pathh.getWeight());


        for(customVertex x : pathh.getVertexList())
        {
            System.out.println(x.id);
        }


    }

    // To call after Graph and Graphadapter,Layout had been initialized

    private void adaptGraphForHeuristicToBeAdmissinble()
    {
        for(DefaultWeightedEdge edge : this.graph.edgeSet())
        {
            customVertex sourceVertex = graph.getEdgeSource(edge);
            customVertex targetVertex = graph.getEdgeTarget(edge);
            Point sourcePoint = graphAdapter.getVertexToCellMap().get(sourceVertex).getGeometry().getPoint();
            Point targetPoint = graphAdapter.getVertexToCellMap().get(targetVertex).getGeometry().getPoint();

            double airlineDistance = sourcePoint.distance(targetPoint);

            double currentEdgeWeight = graph.getEdgeWeight(edge);
            if(currentEdgeWeight < airlineDistance)
            {
                System.err.println("Current Edgeweight violates Admissible Heuristic");
                Random randomGenerator = new Random();
                int newEdgeWeight = (int) Math.ceil(randomGenerator.nextDouble( airlineDistance, airlineDistance + 10));
                // System.out.println(String.format("Airlinedistance: %f   ->  Current Weight: %f   ->   New Weight: %f", airlineDistance, currentEdgeWeight, newEdgeWeight));
                graph.setEdgeWeight(edge, newEdgeWeight);
            }


        }
    }



}
