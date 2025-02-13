package ru.gpncr.listener;

import ru.gpncr.model.Message;

@SuppressWarnings("java:S1135")
public interface Listener {

    void onUpdated(Message msg);
}
