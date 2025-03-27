package ru.gpncr.services.processors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gpncr.api.SensorDataProcessor;
import ru.gpncr.api.model.SensorData;
import ru.gpncr.lib.SensorDataBufferedWriter;

@SuppressWarnings({"java:S1068"})
// Этот класс нужно реализовать
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final List<SensorData> dataBuffer;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.dataBuffer = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public synchronized void process(SensorData data) {
        synchronized (dataBuffer) {
            dataBuffer.add(data);
            if (dataBuffer.size() >= bufferSize) {
                flush();
            }
        }
    }

    public synchronized void flush() {
        List<SensorData> toFlush;
        synchronized (dataBuffer) {
            if (dataBuffer.isEmpty()) {
                return;
            }
            toFlush = new ArrayList<>(dataBuffer);
            toFlush.sort(Comparator.comparing(SensorData::getMeasurementTime));
            dataBuffer.clear();
        }
        writer.writeBufferedData(toFlush);
    }

    @Override
    public synchronized void onProcessingEnd() {

        flush();
    }
}
