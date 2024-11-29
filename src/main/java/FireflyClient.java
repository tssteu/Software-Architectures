import com.example.firefly.FireflyProto.*;
import com.example.firefly.FireflyServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.List;

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

    public void updatePhase(double phase) {
        stub.updatePhase(PhaseRequest.newBuilder().setPhase(phase).build());
    }
}