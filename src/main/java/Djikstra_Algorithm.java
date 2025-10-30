import com.mxgraph.model.mxICell;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.List;

public class Djikstra_Algorithm extends FindOptimalPathAlgorithm{

    Djikstra_Algorithm(Graph<customVertex, DefaultWeightedEdge> graph_, JGraphXAdapter<customVertex, DefaultWeightedEdge> graphAdapter_) {
        super(graph_, graphAdapter_);
    }


    @Override
    public pathFindingDiagnostics findOptimalPath(int startEdgeId, int goalEdgeId) throws RuntimeException{


        Comparator<customVertex> vertexSortingComparator = new Comparator<customVertex>() {
            @Override
            public int compare(customVertex o1, customVertex o2) {
                double costsWithHeuristicV1 = o1.pathCost;
                double costsWithHeuristicV2 = o2.pathCost;

                // System.out.println(String.format("%f    -    %f    ->    %d", costsWithHeuristicV1, costsWithHeuristicV2, Double.compare(costsWithHeuristicV1, costsWithHeuristicV2)));
                return Double.compare(costsWithHeuristicV1, costsWithHeuristicV2);

            }
        };

        LinkedList<customVertex> queue = new LinkedList<>();

        LinkedList<customVertex> closedList = new LinkedList<>();

//        DefaultWeightedEdge edge = graph.edgesOf(5).iterator().next();

//        graph.getEdgeWeight(edge);

        Optional<customVertex> startVertex = this.graph.vertexSet().stream().filter((x) ->  x.id == startEdgeId).findFirst();

        if(!startVertex.isPresent()){
            throw new RuntimeException("Vertex not found");
        }


        //queue.add(startVertex.get());

        customVertex targetVertex = graph.vertexSet().stream().filter((x) ->  x.id == goalEdgeId).findFirst().get();
        customVertex currentVertex = startVertex.get();
        currentVertex.pathCost = 0;

        pathFindingDiagnostics diagnostics = new pathFindingDiagnostics();


        customVertex lastVisitedVertex = null;

        Instant start = Instant.now();

        while(currentVertex != null)
        {
            if(currentVertex.equals(targetVertex))
            {
                this.targetVertex = currentVertex;

                diagnostics.totalTimePassed = Duration.between(start, Instant.now()).toMillis();
                diagnostics.AlgorithmName = "Djikstra Algorithm";
                diagnostics.totalCostsForOptimalPath = currentVertex.pathCost;

                return diagnostics;
            }

            Set<DefaultWeightedEdge> edgesOfCurrentVertex =  graph.edgesOf(currentVertex);
            for(DefaultWeightedEdge successorEdge: edgesOfCurrentVertex)
            {
                //The edges for currentVertex can either be from currentVertex -> successorVertex or currentVertex <- successorVertex
                customVertex successorVertex = (graph.getEdgeTarget(successorEdge).id == currentVertex.id)? graph.getEdgeSource(successorEdge) : graph.getEdgeTarget(successorEdge);

                int successor_Current_Cost = currentVertex.pathCost + (int) graph.getEdgeWeight(successorEdge) ;
                if(queue.contains(successorVertex)) {
                    if (successorVertex.pathCost <= successor_Current_Cost) {
                        continue;
                    }
                }
                else if (closedList.contains(successorVertex)) {
                    if(successorVertex.pathCost <= successor_Current_Cost)
                    {
                        continue;
                    }

                    closedList.remove(successorVertex);
                    queue.add(successorVertex);
                }
                else
                {
                    queue.add(successorVertex);
                }
                successorVertex.pathCost = successor_Current_Cost;
                successorVertex.parentVertex = currentVertex;
            }
            closedList.add(currentVertex);
            diagnostics.traversedVertices++;
            lastVisitedVertex = currentVertex;
            queue.sort(vertexSortingComparator);

            currentVertex = queue.removeFirst();
        }

        throw new RuntimeException("Open List is Empty");
    }

    @Override
    public List<customVertex> getPath()
    {
        List<customVertex> path = new LinkedList<>();
        customVertex currentVertex = targetVertex;
        while(currentVertex != null)
        {
            path.add(currentVertex);
            currentVertex = currentVertex.parentVertex;
        }
        return path;
    }


}
