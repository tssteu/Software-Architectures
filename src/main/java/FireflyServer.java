import com.example.firefly.FireflyProto.*;
import com.example.firefly.FireflyServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FireflyServer extends FireflyServiceGrpc.FireflyServiceImplBase {
    private static final Map<Integer, Double> phases = new ConcurrentHashMap<>();

    @Override
    public void updatePhase(PhaseRequest request, io.grpc.stub.StreamObserver<Empty> responseObserver) {
        double phase = request.getPhase();
        int indexOfFireflyX = request.getX();
        int indexOfFireflyY = request.getY();
        int indexKey = indexOfFireflyX * 10 + indexOfFireflyY;

        synchronized (phases) {
            phases.put(indexKey, phase);
        }
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void getPhases(GetPhase request, io.grpc.stub.StreamObserver<PhaseList> responseObserver) {
        int indexOfFireflyX = request.getX();
        int indexOfFireflyY = request.getY();

        PhaseList.Builder responseBuilder = PhaseList.newBuilder();
        synchronized (phases) {
            int upper = getUpper(indexOfFireflyX, 10) + indexOfFireflyY;
            int lower = getLower(indexOfFireflyX, 10) + indexOfFireflyY;
            int left = (indexOfFireflyX * 10) + getLeft(indexOfFireflyY, 10);
            int right = (indexOfFireflyX * 10) + getRight(indexOfFireflyY, 10);

            System.out.println("Upper: " + upper + ", Lower: " + lower + ", Left: " + left + ", Right: " + right);

            responseBuilder.addPhases(phases.get(upper));
            responseBuilder.addPhases(phases.get(lower));
            responseBuilder.addPhases(phases.get(left));
            responseBuilder.addPhases(phases.get(right));
        }
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    public int getUpper(int index, int size) {
        return (index - 1 + size) % size; // Korrektur: Nur Modulo-Berechnung für x-Koordinate
    }

    public int getLower(int index, int size) {
        return (index + 1) % size; // Korrektur: Nur Modulo-Berechnung für x-Koordinate
    }

    public int getLeft(int index, int size) {
        return (index - 1 + size) % size; // Korrektur: Nur Modulo-Berechnung für y-Koordinate
    }

    public int getRight(int index, int size) {
        return (index + 1) % size; // Korrektur: Nur Modulo-Berechnung für y-Koordinate
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8080)
                .addService(new FireflyServer())
                .build();

        System.out.println("Server gestartet auf Port 8080");
        server.start();
        printPhases();
        server.awaitTermination();
    }

    public static void printPhases() {
        System.out.println("Aktuelle Phasen: " + phases.toString());
    }
}