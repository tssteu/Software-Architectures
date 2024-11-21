import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Firefly implements Runnable {
    private double phase; // Phase (theta)
    private double naturalFrequency; // Natürliche Frequenz
    private final int x, y; // Position im Torus
    private final List<Firefly> neighbors; // Nachbarn
    private boolean running;

    public Firefly(double naturalFrequency, int x, int y) {
        this.phase = Math.random(); // Startphase zufällig
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
            // Phase aktualisieren
            phase += naturalFrequency;
            if (phase >= 1.0) phase -= 1.0; // Phase zurücksetzen

            // Kuramoto-Modell: Einfluss der Nachbarn
            double influence = 0;
            for (Firefly neighbor : neighbors) {
                influence += Math.sin(2 * Math.PI * (neighbor.phase - this.phase));
            }
            phase += 0.02 * influence; // Kopplungsstärke (K) * Einfluss

            // Schlafzeit für Simulation
            try {
                Thread.sleep(50); // Simuliert kontinuierlichen Prozess
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public boolean isOn() {
        // Gelb, wenn Phase < 0.5; Weiß sonst
        return phase < 0.5;
    }

    // Neue Methode, um die aktuelle Phase zu holen
    public double getPhase() {
        return phase;
    }
}