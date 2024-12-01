import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Firefly implements Runnable {
    private final FireflyClient client;
    private double phase;
    private double naturalFrequency;
    private final int x, y;
    private final List<Firefly> neighbors;
    private boolean running;
    private static double couplingStrength = 0.5;

    public Firefly(double naturalFrequency, int x, int y, FireflyClient client) {
        this.phase = Math.random() * 2 * Math.PI;
        this.naturalFrequency = naturalFrequency;
        this.x = x;
        this.y = y;
        this.neighbors = new CopyOnWriteArrayList<>();
        this.running = true;
        this.client = client;
        sendPhaseToServer();
    }

    public void addNeighbor(Firefly neighbor) { neighbors.add(neighbor); }
    
    @Override
    public void run() {
        while(running) {
            client.updatePhase(phase, x, y);
            
            List<Double> neighborPhases = client.getPhases(x, y);
            double sum = 0;

            for (double neighborPhase : neighborPhases) {
                sum += Math.sin(neighborPhase - phase);
            }
            
            if (!neighborPhases.isEmpty()) {
                phase += naturalFrequency + (Firefly.getCouplingStrength() / neighborPhases.size()) * sum;
            }

            phase = phase % (2 * Math.PI);
            client.updatePhase(phase, x, y);

            try {
                Thread.sleep(50);
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public boolean isOn() { return phase < Math.PI; }

    public double getPhase() { return phase; }

    public static double getCouplingStrength() { return couplingStrength; }

    public FireflyClient getClient() { return client; }

    public void sendPhaseToServer() { client.updatePhase(phase, x, y); }
}