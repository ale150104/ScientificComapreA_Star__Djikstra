import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.AStarAdmissibleHeuristic;
import org.jgrapht.alg.shortestpath.AStarShortestPath;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.generate.WattsStrogatzGraphGenerator;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.util.SupplierUtil;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class wattsStrogatzGraph {

    public void run() {
        // --- 1. Graph erzeugen ---
        Supplier<customVertex> vertexSupplier = new Supplier<>() {
            private int id = 0;

            @Override
            public customVertex get() {
                return new customVertex(id++, Integer.MAX_VALUE);
            }
        };

        Graph<customVertex, DefaultWeightedEdge> graph =
                new SimpleWeightedGraph<>(vertexSupplier, SupplierUtil.DEFAULT_WEIGHTED_EDGE_SUPPLIER);


        // 100 Knoten, jeder mit 4 Nachbarn, Umverdrahtungswahrscheinlichkeit 0.1
        WattsStrogatzGraphGenerator<customVertex, DefaultWeightedEdge> generator =
                new WattsStrogatzGraphGenerator<customVertex, DefaultWeightedEdge>(1000, 90, 0.75);


        generator.generateGraph(graph);

        Random random = new Random();

        for (DefaultWeightedEdge edge : graph.edgeSet()) {
            int weight = random.nextInt(1, 100); // z.B. Gewicht zwischen 1 und 10
            graph.setEdgeWeight(edge, weight);
        }


        // --- 2. Visualisierung mit JGraphX ---
        JGraphXAdapter<customVertex, DefaultWeightedEdge> graphAdapter = new JGraphXAdapter<>(graph);


//        graph.edgeSet().forEach((edge) -> {
//            System.out.println(graph.getEdgeWeight(edge));
//        });

//        mxCircleLayout layout = new mxCircleLayout(graphAdapter);
//        layout.execute(graphAdapter.getDefaultParent());
//
//        JFrame frame = new JFrame("Watts–Strogatz Graph");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(new mxGraphComponent(graphAdapter));
//        frame.pack();
//        frame.setVisible(true);


//        A_Star_Algorithm a_star_Algorithm = new A_Star_Algorithm(graph, graphAdapter);
//        pathFindingDiagnostics result = a_star_Algorithm.findOptimalPath(1, 98);
//        System.out.println(result);
//
//        System.out.println("PATH:");
//        customVertex targetvertex = graph.vertexSet().stream().filter((vertex -> vertex.id == 98)).findFirst().get();
//
//        List<customVertex> path = a_star_Algorithm.getPath(targetvertex);



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



}
