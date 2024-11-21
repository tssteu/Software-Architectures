public class Torus {
    private final int size;
    private final Firefly[][] grid;

    public Torus(int size, double baseFrequency) {
        this.size = size;
        this.grid = new Firefly[size][size];

        // Glühwürmchen erstellen
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new Firefly(baseFrequency + Math.random() * 0.01, i, j);
            }
        }

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

    // Berechnung des Synchronisationsgrads R
    public double calculateSynchronization() {
        double realSum = 0;
        double imagSum = 0;
        int totalFireflies = size * size;

        for (Firefly[] row : grid) {
            for (Firefly firefly : row) {
                double phase = firefly.getPhase() * 2 * Math.PI; // Phase in Radiant
                realSum += Math.cos(phase);
                imagSum += Math.sin(phase);
            }
        }

        double averageReal = realSum / totalFireflies;
        double averageImag = imagSum / totalFireflies;

        // Amplitude des Durchschnittszeigers ist R
        return Math.sqrt(averageReal * averageReal + averageImag * averageImag);
    }
}