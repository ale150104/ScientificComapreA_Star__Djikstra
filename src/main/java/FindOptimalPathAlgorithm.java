import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;

public abstract class FindOptimalPathAlgorithm{

    protected final Graph<customVertex, DefaultWeightedEdge> graph;
    protected final JGraphXAdapter<customVertex, DefaultWeightedEdge> graphAdapter;

    FindOptimalPathAlgorithm(Graph<customVertex, DefaultWeightedEdge> graph_, JGraphXAdapter<customVertex, DefaultWeightedEdge> graphAdapter_)
    {
        this.graph = graph_;
        this.graphAdapter = graphAdapter_;
    }

    public abstract pathFindingDiagnostics findOptimalPath(int startEdgeId, int goalEdgeId) throws RuntimeException;
}


class pathFindingDiagnostics {

    public String AlgorithmName;

    public int traversedVertices;

    public double totalCostsForOptimalPath;

    public long totalTimePassed;

    @Override
    public String toString() {
        return String.format("Total Traversed Vertices: %d       Total Path Costs: %f", this.traversedVertices, this.totalCostsForOptimalPath);
    }
}
