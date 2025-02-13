package ru.gpncr;

import java.util.List;
import ru.gpncr.handler.ComplexProcessor;
import ru.gpncr.listener.ListenerPrinterConsole;
import ru.gpncr.model.Message;
import ru.gpncr.processor.LoggerProcessor;
import ru.gpncr.processor.ProcessorConcatFields;
import ru.gpncr.processor.ProcessorUpperField10;

@SuppressWarnings("java:S106")
public class Demo {
    public static void main(String[] args) {
        var processors = List.of(new ProcessorConcatFields(), new LoggerProcessor(new ProcessorUpperField10()));

        var complexProcessor = new ComplexProcessor(processors, ex -> {});
        var listenerPrinter = new ListenerPrinterConsole();
        complexProcessor.addListener(listenerPrinter);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        complexProcessor.removeListener(listenerPrinter);
    }
}
