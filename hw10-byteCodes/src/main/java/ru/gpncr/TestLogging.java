package ru.gpncr;

public class TestLogging implements TestLoggingInterface {
    @Override
    @Log
    public void calculation(int param) {
        // здесь может быть логика расчета
    }

    @Override
    public void calculation(int param1, int param2) {
        // здесь может быть логика расчета
    }

    @Override
    @Log
    public void calculation(int param1, int param2, String param3) {
        // здесь может быть логика расчета
    }
}
