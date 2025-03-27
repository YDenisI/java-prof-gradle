package ru.gpncr.lib;

import java.util.List;
import ru.gpncr.api.model.SensorData;

public interface SensorDataBufferedWriter {
    void writeBufferedData(List<SensorData> bufferedData);
}
