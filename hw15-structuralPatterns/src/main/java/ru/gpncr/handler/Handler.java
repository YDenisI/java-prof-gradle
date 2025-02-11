package ru.gpncr.handler;

import ru.gpncr.listener.Listener;
import ru.gpncr.model.Message;

public interface Handler {
    Message handle(Message msg);

    void addListener(Listener listener);

    void removeListener(Listener listener);
}
