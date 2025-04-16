package ru.gpncr.protobuf.service;

import io.grpc.stub.StreamObserver;
import java.util.Timer;
import java.util.TimerTask;
import ru.gpncr.protobuf.generated.NumberGeneratorGrpc;
import ru.gpncr.protobuf.generated.Numbers.GenerateRequest;
import ru.gpncr.protobuf.generated.Numbers.GenerateResponse;

public class NumberGeneratorServiceImpl extends NumberGeneratorGrpc.NumberGeneratorImplBase {

    @Override
    public void generateNumbers(GenerateRequest request, StreamObserver<GenerateResponse> responseObserver) {
        int firstValue = request.getFirstValue();
        int lastValue = request.getLastValue();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    int currentValue = firstValue;

                    @Override
                    public void run() {
                        if (currentValue <= lastValue) {
                            GenerateResponse response = GenerateResponse.newBuilder()
                                    .setValue(currentValue)
                                    .build();
                            responseObserver.onNext(response);
                            currentValue++;
                        } else {
                            responseObserver.onCompleted();
                            timer.cancel();
                        }
                    }
                },
                0,
                2000);
    }
}
