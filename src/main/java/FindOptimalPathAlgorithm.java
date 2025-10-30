import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public abstract class FindOptimalPathAlgorithm{

    protected final Graph<customVertex, DefaultWeightedEdge> graph;
    protected final JGraphXAdapter<customVertex, DefaultWeightedEdge> graphAdapter;

    protected customVertex targetVertex;

    FindOptimalPathAlgorithm(Graph<customVertex, DefaultWeightedEdge> graph_, JGraphXAdapter<customVertex, DefaultWeightedEdge> graphAdapter_)
    {
        this.graph = graph_;
        this.graphAdapter = graphAdapter_;
    }

    public abstract pathFindingDiagnostics findOptimalPath(int startEdgeId, int goalEdgeId) throws RuntimeException;

    public abstract List<customVertex> getPath();
}


class pathFindingDiagnostics {

    public String AlgorithmName;

    public int traversedVertices;

    public double totalCostsForOptimalPath;

    public long totalTimePassed;

    @Override
    public String toString() {
        return String.format("Algorithm: %s     Total Traversed Vertices: %d       Total Path Costs: %f        Elapsed Time: %d Milliseconds",this.AlgorithmName, this.traversedVertices, this.totalCostsForOptimalPath, this.totalTimePassed);
    }
}
