public class FireflySimulation {
    public static void main(String[] args) {
        int gridSize = 10;

        FireflyClient client = new FireflyClient("localhost", 8080);
        Torus torus = new Torus(gridSize, client);

        Firefly[][] grid = torus.getGrid();
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                new Thread(grid[i][j]).start();
            }
        }

        FireflyGUI gui = new FireflyGUI(torus);
        gui.setVisible(true);

        while(true) {
            gui.repaint();
            try {
                Thread.sleep(100);
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
