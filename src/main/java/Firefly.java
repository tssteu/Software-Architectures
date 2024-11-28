import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Firefly implements Runnable {
    private double phase; // Phase (theta)
    private double naturalFrequency; // Natürliche Frequenz
    private final int x, y; // Position im Torus
    private final List<Firefly> neighbors; // Nachbarn
    private boolean running;
    private static double cupplingStrength = 1.0;

    public Firefly(double naturalFrequency, int x, int y) {
        this.phase = Math.random() * 2 * Math.PI; // Startphase zufällig
        this.naturalFrequency = naturalFrequency;
        this.x = x;
        this.y = y;
        this.neighbors = new CopyOnWriteArrayList<>();
        this.running = true;
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
            double sum = 0;

            for (Firefly neighbor : neighbors) {
                sum += Math.sin(neighbor.phase - this.phase);
            }

            phase += naturalFrequency + ((cupplingStrength / neighbors.size()) * sum);

            phase = phase % (2 * Math.PI);

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
}