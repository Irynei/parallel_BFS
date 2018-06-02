import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        int size = 1000;
        int threadsCount = 4;
        System.out.println("Input size: " + size);
        System.out.println("Threads count: " + threadsCount + "\n");

        Graph graph = generateRandomGraph(size);

        long startTime = System.currentTimeMillis();
        Set seqRes = sequentialBFS(graph, 0, false);
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Sequential result size: " + seqRes);
        System.out.println("Sequential time: " + totalTime + "ms\n");

        long start = System.currentTimeMillis();
        Set parRes = parallelBFS(graph, threadsCount, false);
        long end = System.currentTimeMillis();
        long total = end - start;
        System.out.println("Parallel result: " + parRes);
        System.out.println("Parallel time: " + total + "ms");
    }

    private static Set parallelBFS(Graph graph, int numThreads, boolean verbose) {
        ExecutorService taskExecutor = Executors.newFixedThreadPool(numThreads);
        Queue<Integer> queue = new LinkedBlockingQueue<>();
        Set<Integer> visitedSet = Collections.synchronizedSet(new LinkedHashSet<>(graph.size));
        for (int i = 0; i < numThreads; i++) {
            Runnable worker = new BFS(queue, visitedSet, i % numThreads, graph, verbose);
            taskExecutor.execute(worker);
        }
        taskExecutor.shutdown();
        try {
            taskExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.exit(1);
        }
        return visitedSet;
    }

    private static Set sequentialBFS(Graph graph, int startNode, boolean verbose) {
        Queue<Integer> queue = new LinkedBlockingQueue<>();
        LinkedHashSet<Integer> visitedSet = new LinkedHashSet<>(graph.size);
        BFS bfs = new BFS(queue, visitedSet, startNode, graph, verbose);
        bfs.run();
        return bfs.getVisited();
    }

    private static Graph generateRandomGraph(int size) {
//        randomly generate symmetric adjacency matrix with 0 on diagonal
        Graph graph = new Graph(size);
        for (int i = 0; i < size; i++) {
            int numberOfConnections = ThreadLocalRandom.current().nextInt(0, size);
            while (numberOfConnections > 0) {
                int j = ThreadLocalRandom.current().nextInt(0, size);
                if(i != j && graph.adjacencyMatrix[i][j] != 1) {
                    graph.adjacencyMatrix[i][j] = 1;
                    graph.adjacencyMatrix[j][i] = 1;
                }
                numberOfConnections--;
            }
        }
        return graph;
    }
}
