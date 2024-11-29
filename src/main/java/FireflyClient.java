import com.example.firefly.FireflyProto.*;
import com.example.firefly.FireflyServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.List;
import java.util.stream.Collectors;

public class FireflyClient {
    private final FireflyServiceGrpc.FireflyServiceBlockingStub stub;

    public FireflyClient(String host, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        stub = FireflyServiceGrpc.newBlockingStub(channel);
    }

    public List<Double> getPhases() {
        return stub.getPhases(Empty.newBuilder().build()).getPhasesList();
    }

    public List<Double> getNeighborPhases(List<Firefly> neighbors) {
        return neighbors.stream()
                .map(Firefly::getPhase)
                .collect(Collectors.toList());
    }

    public void updatePhase(double phase) {
        stub.updatePhase(PhaseRequest.newBuilder().setPhase(phase).build());
    }
}