package ru.gpncr.protobuf;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gpncr.protobuf.generated.NumberGeneratorGrpc;
import ru.gpncr.protobuf.generated.Numbers;

@SuppressWarnings({"java:S106", "java:S2142"})
public class GRPCServer extends NumberGeneratorGrpc.NumberGeneratorImplBase {
    private static final Logger logger = LoggerFactory.getLogger(GRPCServer.class);

    @Override
    public void generateNumbers(
            Numbers.GenerateRequest request, StreamObserver<Numbers.GenerateResponse> responseObserver) {
        int firstValue = request.getFirstValue();
        int lastValue = request.getLastValue();

        for (int i = firstValue; i <= lastValue; i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            Numbers.GenerateResponse response =
                    Numbers.GenerateResponse.newBuilder().setValue(i).build();
            responseObserver.onNext(response);
        }

        responseObserver.onCompleted();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server =
                ServerBuilder.forPort(8190).addService(new GRPCServer()).build().start();

        logger.info("Server started on port 8190");
        server.awaitTermination();
    }
}
