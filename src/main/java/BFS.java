import java.util.List;
import java.util.Queue;
import java.util.Set;


public class BFS implements Runnable {

    private final Queue<Integer> queue;
    private final Set<Integer> visited;
    private final int startNode;
    private final Graph graph;
    private boolean verbose;

    public BFS(Queue<Integer> queue, Set<Integer> visited, int startNode, Graph graph, boolean verbose) {
        this.queue = queue;
        this.visited = visited;
        this.startNode = startNode;
        this.graph = graph;
        this.verbose = verbose;
    }

    public Set getVisited(){
        return this.visited;
    }

    private void performBFS() {
        Integer node = this.startNode;
        visitNode(node);
        while (visited.size() != graph.size) {
            while (!queue.isEmpty()) {
                Integer currentNode = queue.poll();
                if (currentNode != null) {
                    List<Integer> neighbors = graph.getNeighbors(currentNode);
                    if (this.verbose) {
                        System.out.println("Current node " + currentNode);
                        System.out.println("neighbors " + neighbors);
                    }
                    neighbors.forEach(this::visitNode);
                }
            }
//            if we have nodes isolated from everyone
            while (queue.isEmpty() && visited.size() != graph.size) {
                node = node + 1;
                visitNode(node);
            }
        }
    }

    private void visitNode(Integer node) {
        synchronized (graph.nodes.get(node)) {
            if (!visited.contains(node)) {
                visited.add(node);
                queue.add(node);
            }
        }
    }

    @Override
    public void run() {
        this.performBFS();
    }
}
