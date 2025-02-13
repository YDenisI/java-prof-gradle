package ru.gpncr.processor;

import ru.gpncr.model.Message;

@SuppressWarnings("java:S1135")
public interface Processor {

    Message process(Message message);
}
