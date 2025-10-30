public class customVertex{
    public int id;

    public int pathCost;

    public double heuristicCosts;

    public customVertex parentVertex;

    public customVertex(int id_, int pathCost_)
    {
        this.id = id_;
        this.pathCost = pathCost_;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != this.getClass())
        {
            return false;
        }
        return ((customVertex) obj).id == this.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}