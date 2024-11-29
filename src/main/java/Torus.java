import java.util.ArrayList;
import java.util.List;

public class Torus {
    private final int size;
    private final Firefly[][] grid;

    private List<Double> naturalFrequencies = new ArrayList<>();

    public Torus(int size, double baseFrequency, FireflyClient client) {
        this.size = size;
        this.grid = new Firefly[size][size];

        // Glühwürmchen erstellen
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double value = lorentzRandom();
                naturalFrequencies.add(value);
                grid[i][j] = new Firefly(value, i, j, client);
            }
        }

        System.out.printf("Standardabweichung: %.3f%n", calculateStandardDeviation());
        System.out.printf("CupplingStrength / (2 * PI): %.3f%n", Firefly.getCupplingStrength() / (2 * Math.PI));

        // Nachbarn verknüpfen
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Firefly current = grid[i][j];
                current.addNeighbor(grid[(i - 1 + size) % size][j]); // Oben
                current.addNeighbor(grid[(i + 1) % size][j]); // Unten
                current.addNeighbor(grid[i][(j - 1 + size) % size]); // Links
                current.addNeighbor(grid[i][(j + 1) % size]); // Rechts
            }
        }
    }

    public Firefly[][] getGrid() {
        return grid;
    }

    public double calculateSynchronization() {
        double realSum = 0;
        double imagSum = 0;
        int totalFireflies = size * size;

        for (Firefly[] row : grid) {
            for (Firefly firefly : row) {
                double phase = firefly.getPhase() * 2 * Math.PI;
                realSum += Math.cos(phase);
                imagSum += Math.sin(phase);
            }
        }

        double averageReal = realSum / totalFireflies;
        double averageImag = imagSum / totalFireflies;

        return Math.sqrt(averageReal * averageReal + averageImag * averageImag);
    }

    private double lorentzRandom() {
        double value;
        value = 0.05 + Math.random() * 0.1;
        return value;
    }

    public double calculateStandardDeviation() {
        double mean = naturalFrequencies.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = naturalFrequencies.stream().mapToDouble(f -> Math.pow(f - mean, 2)).average().orElse(0.0);
        return Math.sqrt(variance);
    }
}