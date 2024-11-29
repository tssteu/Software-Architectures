import com.example.firefly.FireflyProto.*;
import com.example.firefly.FireflyServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

public class FireflyServer extends FireflyServiceGrpc.FireflyServiceImplBase {
    private final CopyOnWriteArrayList<Double> phases = new CopyOnWriteArrayList<>();

    @Override
    public void updatePhase(PhaseRequest request, io.grpc.stub.StreamObserver<Empty> responseObserver) {
        synchronized (phases) {
            phases.add(request.getPhase());
            if (phases.size() > 100) { // Begrenze die Liste
                phases.remove(0);
            }
        }
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void getPhases(Empty request, io.grpc.stub.StreamObserver<PhaseList> responseObserver) {
        System.out.println(request);
        PhaseList.Builder responseBuilder = PhaseList.newBuilder();
        synchronized (phases) {
            responseBuilder.addAllPhases(phases);
        }
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8080)
                .addService(new FireflyServer())
                .build();

        System.out.println("Server gestartet auf Port 8080");
        server.start();
        server.awaitTermination();
    }
}