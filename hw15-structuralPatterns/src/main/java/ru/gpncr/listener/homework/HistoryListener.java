package ru.gpncr.listener.homework;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ru.gpncr.listener.Listener;
import ru.gpncr.model.Message;
import ru.gpncr.model.ObjectForMessage;

public class HistoryListener implements Listener, HistoryReader {
    private final List<Message> history = new ArrayList<>();

    @Override
    public Optional<Message> findMessageById(long id) {
        return history.stream().filter(message -> message.getId() == id).findFirst();
    }

    @Override
    public void onUpdated(Message message) {

        ObjectForMessage copiedField13 = new ObjectForMessage();
        copiedField13.setData(
                message.getField13().getData() != null
                        ? new ArrayList<>(message.getField13().getData())
                        : null);

        Message copiedMessage = new Message.Builder(message.getId())
                .field1(message.getField1())
                .field2(message.getField2())
                .field3(message.getField3())
                .field6(message.getField6())
                .field10(message.getField10())
                .field11(message.getField11())
                .field12(message.getField12())
                .field13(copiedField13)
                .build();

        history.add(copiedMessage);
    }

    public List<Message> getHistory() {
        return history;
    }
}
