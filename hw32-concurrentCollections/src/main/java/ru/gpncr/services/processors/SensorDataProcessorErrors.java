package ru.gpncr.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gpncr.api.SensorDataProcessor;
import ru.gpncr.api.model.SensorData;

public class SensorDataProcessorErrors implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorErrors.class);

    @Override
    public void process(SensorData data) {
        if (data.getValue() == null || !data.getValue().isNaN()) {
            return;
        }
        log.error("Обработка ошибочных данных: {}", data);
    }
}
