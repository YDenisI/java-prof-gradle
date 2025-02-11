package ru.gpncr;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gpncr.handler.ComplexProcessor;
import ru.gpncr.listener.ListenerPrinterConsole;
import ru.gpncr.listener.homework.HistoryListener;
import ru.gpncr.model.Message;
import ru.gpncr.model.ObjectForMessage;
import ru.gpncr.processor.LoggerProcessor;
import ru.gpncr.processor.ProcessorSwapFields;
import ru.gpncr.processor.ProcessorThrowExceptionOnEvenSecond;
import ru.gpncr.processor.ProcessorUpperField10;

@SuppressWarnings("java:S3740")
public class HomeWork {

    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
        var historyListener = new HistoryListener();
        var listenerPrinter = new ListenerPrinterConsole();

        var processors = List.of(
                new ProcessorSwapFields(),
                new LoggerProcessor(new ProcessorUpperField10()),
                new ProcessorThrowExceptionOnEvenSecond());

        var complexProcessor = new ComplexProcessor(processors, ex -> logger.error("Processing error", ex));
        complexProcessor.addListener(listenerPrinter);
        complexProcessor.addListener(historyListener);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .field13(new ObjectForMessage())
                .build();

        try {
            var result = complexProcessor.handle(message);
            logger.info("Result: {}", result);
        } catch (Exception e) {
            logger.error("Exception during message processing", e);
        }

        Long searchId = 1L;
        Optional foundMessage = historyListener.findMessageById(searchId);
        if (foundMessage.isPresent()) {
            logger.info("Found message with ID {}: {}", searchId, foundMessage);
        } else {
            logger.info("Message with ID {} not found in history.", searchId);
        }
        logger.info("History Size: {}", historyListener.getHistory().size());
    }
}
