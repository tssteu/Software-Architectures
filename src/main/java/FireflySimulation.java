public class FireflySimulation {
    public static void main(String[] args) {
        int gridSize = 20;
        double baseFrequency = 0.05;

        Torus torus = new Torus(gridSize, baseFrequency);

        for (Firefly[] row : torus.getGrid()) {
            for (Firefly firefly : row) {
                new Thread(firefly).start();
            }
        }

        FireflyGUI gui = new FireflyGUI(torus);
        gui.setVisible(true);

        new Thread(() -> {
            while (true) {
                double synchronization = torus.calculateSynchronization();
                System.out.printf("Synchronisation: R = %.3f%n", synchronization);
                try {
                    Thread.sleep(1000); // Ausgabe alle 1000 ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();

        while (true) {
            gui.repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}