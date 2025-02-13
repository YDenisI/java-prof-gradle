package ru.gpncr;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import ru.gpncr.model.Message;
import ru.gpncr.model.ObjectForMessage;
import ru.gpncr.processor.ProcessorThrowExceptionOnEvenSecond;

class ProcessorThrowExceptionOnEvenSecondTest {

    private ProcessorThrowExceptionOnEvenSecond processor;

    @BeforeEach
    void setUp() {
        processor = new ProcessorThrowExceptionOnEvenSecond();
    }

    @Test
    void testProcessThrowsExceptionOnEvenSecond() {
        LocalTime fixedTime = LocalTime.of(12, 0, 2);
        try (MockedStatic<LocalTime> mockedLocalTime = Mockito.mockStatic(LocalTime.class)) {
            mockedLocalTime.when(LocalTime::now).thenReturn(fixedTime);

            Exception exception = assertThrows(RuntimeException.class, () -> {
                processor.process(new Message.Builder(1L)
                        .field1("field1")
                        .field2("field2")
                        .field3("field3")
                        .field6("field6")
                        .field10("field10")
                        .field11("field11")
                        .field12("field12")
                        .field13(new ObjectForMessage())
                        .build());
            });

            assertEquals("Exception on even second: 2", exception.getMessage());
        }
    }

    @Test
    void testProcessReturnsMessageOnOddSecond() {
        LocalTime fixedTime = LocalTime.of(12, 0, 3);
        try (MockedStatic<LocalTime> mockedLocalTime = Mockito.mockStatic(LocalTime.class)) {
            mockedLocalTime.when(LocalTime::now).thenReturn(fixedTime);

            Message message = new Message.Builder(1L)
                    .field1("field1")
                    .field2("field2")
                    .field3("field3")
                    .field6("field6")
                    .field10("field10")
                    .field11("field11")
                    .field12("field12")
                    .field13(new ObjectForMessage())
                    .build();
            Message processedMessage = processor.process(message);

            assertSame(message, processedMessage);
        }
    }
}
