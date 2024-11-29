public class FireflySimulation {
    public static void main(String[] args) {
        int gridSize = 5;
        double baseFrequency = 0.05;

        FireflyClient client = new FireflyClient("localhost", 8080);

        // Torus erstellen und füllen
        Torus torus = new Torus(gridSize, baseFrequency, client);

        // Firefly-Threads starten
        Firefly[][] grid = torus.getGrid();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                new Thread(grid[i][j]).start();
            }
        }

        // GUI starten
        FireflyGUI gui = new FireflyGUI(torus);
        gui.setVisible(true);

        // GUI-Aktualisierung
        while (true) {
            gui.repaint();
            try {
                Thread.sleep(100); // Refresh alle 100ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}