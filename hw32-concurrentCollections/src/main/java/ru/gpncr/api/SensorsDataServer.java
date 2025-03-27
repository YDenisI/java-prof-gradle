package ru.gpncr.api;

import ru.gpncr.api.model.SensorData;

public interface SensorsDataServer {
    void onReceive(SensorData sensorData);
}
