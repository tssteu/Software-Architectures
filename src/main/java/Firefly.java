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
            // Phase aktualisieren
            //phase += naturalFrequency;
            // if (phase >= 1.0) phase -= 1.0; // Phase zurücksetzen

            // Kuramoto-Modell: Einfluss der Nachbarn
            double sum = 0;
            for (Firefly neighbor : neighbors) {
                sum += Math.sin(neighbor.phase - this.phase);
            }
            // oszillators[i].phase + dt * (oszillators[i].omega + (K / N) * sum);
            phase += naturalFrequency + ((cupplingStrength / neighbors.size()) * sum); // Kopplungsstärke (K) * Einfluss
            // System.out.println(naturalFrequency + ((0.05 / neighbors.size()) * sum));

            phase = phase % (2 * Math.PI); // Phase auf [0, 2*PI] begrenzen

            // Schlafzeit für Simulation
            try {
                Thread.sleep(50); // Simuliert kontinuierlichen Prozess
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public boolean isOn() {
        // Gelb, wenn Phase < 3.1415; Weiß sonst
        return phase < Math.PI;
    }

    // Neue Methode, um die aktuelle Phase zu holen
    public double getPhase() {
        return phase;
    }

    public double getNaturalFrequency() {
        return naturalFrequency;
    }

    public static double getCupplingStrength() {
        return cupplingStrength;
    }
}