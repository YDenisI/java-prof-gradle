package ru.gpncr.processor;

import java.time.LocalTime;
import ru.gpncr.model.Message;

@SuppressWarnings("java:S112")
public class ProcessorThrowExceptionOnEvenSecond implements Processor {
    @Override
    public Message process(Message message) {
        if (LocalTime.now().getSecond() % 2 == 0) {
            throw new RuntimeException(
                    "Exception on even second: " + LocalTime.now().getSecond());
        }
        return message;
    }
}
