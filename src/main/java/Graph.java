import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Graph {
    int size;
    int[][] adjacencyMatrix;
    List<Integer> nodes;

    public Graph(int size) {
        this.size = size;
        this.adjacencyMatrix = new int[size][size];
//        get indexes for each of the nodes from 0 to size - 1
        this.nodes = IntStream.rangeClosed(0, size - 1).boxed().collect(Collectors.toList());
    }

    public List<Integer> getNeighbors(int node) {
        List<Integer> neighbors = new ArrayList<>();
        for (int i = 0; i < this.size; i++)
            if (this.adjacencyMatrix[node][i] == 1)
                neighbors.add(i);

        return neighbors;
    }

    @Override
    public String toString() {
//        print adjacency matrix
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                result.append(this.adjacencyMatrix[i][j]);
                result.append(", ");
            }
            result.append("\n");
        }
        return result.toString();
    }
}
