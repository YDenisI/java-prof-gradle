package ru.gpncr.protobuf;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gpncr.protobuf.generated.NumberGeneratorGrpc;
import ru.gpncr.protobuf.generated.Numbers.GenerateRequest;
import ru.gpncr.protobuf.generated.Numbers.GenerateResponse;

@SuppressWarnings({"java:S106", "java:S3457", "java:S2629", "java:S6881"})
public class NumbersClient {
    private static final Logger logger = LoggerFactory.getLogger(NumbersClient.class);
    private int currentValue = 0;
    private int lastReceivedValue = 0;
    private boolean isNewValueReceived = false;

    public static void main(String[] args) {
        NumbersClient client = new NumbersClient();
        client.start();
    }

    public void start() {
        logger.info("Numbers Client is starting...");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8190)
                .usePlaintext()
                .build();

        NumberGeneratorGrpc.NumberGeneratorStub stub = NumberGeneratorGrpc.newStub(channel);

        GenerateRequest request =
                GenerateRequest.newBuilder().setFirstValue(0).setLastValue(30).build();

        stub.generateNumbers(request, new StreamObserver<GenerateResponse>() {
            @Override
            public void onNext(GenerateResponse response) {
                synchronized (NumbersClient.this) {
                    lastReceivedValue = response.getValue();
                    isNewValueReceived = true;
                    logger.info("new value: {}", lastReceivedValue);
                }
            }

            @Override
            public void onError(Throwable t) {
                logger.error("Error occurred: {}", t.getMessage());
            }

            @Override
            public void onCompleted() {
                logger.info("Stream completed.");
            }
        });

        new Thread(() -> {
                    while (currentValue < 50) {
                        try {
                            Thread.sleep(1000); // Задержка в 1 секунду
                            synchronized (this) {
                                if (isNewValueReceived) {
                                    currentValue += lastReceivedValue + 1;
                                    isNewValueReceived = false;
                                } else {
                                    currentValue++;
                                }
                            }
                            logger.info("currentValue: {}", currentValue);

                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                })
                .start();

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            channel.shutdown();
        }
    }
}
