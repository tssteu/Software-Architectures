import java.util.ArrayList;
import java.util.List;

public class Torus {
    private final int size;
    private final Firefly[][] grid;
    private List<Double> naturalFrequencies = new ArrayList<>();

    public Torus(int size, FireflyClient client) {
        this.size = size;
        this.grid = new Firefly[size][size];

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                double naturalFrequency = getNaturalFrequency();
                naturalFrequencies.add(naturalFrequency);
                grid[i][j] = new Firefly(naturalFrequency, i, j, client);
            }
        }

        System.out.printf("Standardabweichung: %.3f%n", calculateStandardDeviation());
        System.out.printf("CupplingStrength / (2 * PI): %.3f%n", Firefly.getCouplingStrength() / (2 * Math.PI));

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                Firefly current = grid[i][j];
                current.addNeighbor(grid[(i - 1 + size) % size][j]);
                current.addNeighbor(grid[(i + 1) % size][j]);
                current.addNeighbor(grid[i][(j - 1 + size) % size]);
                current.addNeighbor(grid[i][(j + 1) % size]);
            }
        }
    }

    public Firefly[][] getGrid() { return grid; }

    private double getNaturalFrequency() { return 0.05 + Math.random() * 0.1; }

    public double calculateStandardDeviation() {
        double mean = naturalFrequencies.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = naturalFrequencies.stream().mapToDouble(f -> Math.pow(f - mean, 2)).average().orElse(0.0);
        return Math.sqrt(variance);
    }
}