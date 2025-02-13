package ru.gpncr.listener.homework;

import java.util.Optional;
import ru.gpncr.model.Message;

public interface HistoryReader {

    Optional<Message> findMessageById(long id);
}
