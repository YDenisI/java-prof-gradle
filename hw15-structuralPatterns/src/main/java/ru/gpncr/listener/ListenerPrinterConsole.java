package ru.gpncr.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gpncr.model.Message;

public class ListenerPrinterConsole implements Listener {
    private static final Logger logger = LoggerFactory.getLogger(ListenerPrinterConsole.class);

    @Override
    public void onUpdated(Message msg) {
        logger.info("oldMsg:{}", msg);
    }
}
