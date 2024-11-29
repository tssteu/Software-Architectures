import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Firefly implements Runnable {
    private final FireflyClient client;
    private double phase; // Phase (theta)
    private double naturalFrequency; // Natürliche Frequenz
    private final int x, y; // Position im Torus
    private final List<Firefly> neighbors; // Nachbarn
    private boolean running;
    private static double cupplingStrength = 1.0;

    public Firefly(double naturalFrequency, int x, int y, FireflyClient client) {
        this.phase = Math.random() * 2 * Math.PI; // Startphase zufällig
        this.naturalFrequency = naturalFrequency;
        this.x = x;
        this.y = y;
        this.neighbors = new CopyOnWriteArrayList<>();
        this.running = true;
        this.client = client;
    }

    public void addNeighbor(Firefly neighbor) {
        neighbors.add(neighbor);
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            // Phasen der Nachbarn abrufen
            List<Double> neighborPhases = client.getPhases();
            double sum = 0;

            System.out.println(neighborPhases.size());
            // System.out.println(neighborPhases.stream().map(Object::toString).collect(Collectors.joining(" | ")));

            for (double neighborPhase : neighborPhases) {
                sum += Math.sin(neighborPhase - phase);
            }

            System.out.println(phase);

            // Synchronisation berechnen
            if (!neighborPhases.isEmpty()) {
                phase += naturalFrequency + (Firefly.getCupplingStrength() / neighborPhases.size()) * sum;
            }

            // Phase normalisieren
            phase = phase % (2 * Math.PI);

            // Eigene Phase an den Server senden
            client.updatePhase(phase);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public boolean isOn() {
        return phase < Math.PI;
    }

    public double getPhase() {
        return phase;
    }

    public static double getCupplingStrength() {
        return cupplingStrength;
    }

    public FireflyClient getClient() {
        return client;
    }
}