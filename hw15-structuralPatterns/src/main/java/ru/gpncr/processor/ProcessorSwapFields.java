package ru.gpncr.processor;

import ru.gpncr.model.Message;

public class ProcessorSwapFields implements Processor {
    @Override
    public Message process(Message message) {
        String field11 = message.getField11();
        String field12 = message.getField12();

        return new Message.Builder(message.getId())
                .field1(message.getField1())
                .field2(message.getField2())
                .field3(message.getField3())
                .field6(message.getField6())
                .field10(message.getField10())
                .field11(field12)
                .field12(field11)
                .field13(message.getField13())
                .build();
    }
}
