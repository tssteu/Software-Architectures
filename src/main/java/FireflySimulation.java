public class FireflySimulation {
    public static void main(String[] args) {
        int gridSize = 5;

        FireflyClient client = new FireflyClient("localhost", 8080);


        // Firefly-Threads starten
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                double frequency = 0.05 + Math.random() * 0.1; // Zufällige Frequenz
                Firefly firefly = new Firefly(frequency, i, j, client);
                new Thread(firefly).start();
            }
        }
    }
}