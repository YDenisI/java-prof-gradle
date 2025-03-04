package ru.gpncr.crm.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.gpncr.base.AbstractHibernateTest;
import ru.gpncr.crm.model.Address;
import ru.gpncr.crm.model.Client;
import ru.gpncr.crm.model.Phone;

@SuppressWarnings({"java:S1854", "java:S5786", "java:S5838"})
@DisplayName("Демо работы с hibernate (с абстракциями) должно ")
class DbServiceClientTest extends AbstractHibernateTest {

    @Test
    @DisplayName(" корректно сохранять, изменять и загружать клиента")
    void shouldCorrectSaveClient() {
        // given
        var client = new Client("Ivan");

        client = new Client(
                null,
                "Vasya",
                new Address(null, "AnyStreet"),
                List.of(new Phone(null, "13-555-22"), new Phone(null, "14-666-333")));

        // when
        var savedClient = dbServiceClient.saveClient(client);
        System.out.println(savedClient);

        // then
        var loadedSavedClient = dbServiceClient.getClient(savedClient.getId());
        assertThat(loadedSavedClient).isPresent();
        assertThat(loadedSavedClient.get()).usingRecursiveComparison().isEqualTo(savedClient);

        // when
        var savedClientUpdated = loadedSavedClient.get().clone();
        savedClientUpdated.setName("updatedName");
        dbServiceClient.saveClient(savedClientUpdated);

        // then
        var loadedClient = dbServiceClient.getClient(savedClientUpdated.getId());
        assertThat(loadedClient).isPresent();
        assertThat(loadedClient.get()).usingRecursiveComparison().isEqualTo(savedClientUpdated);
        System.out.println(loadedClient);

        // when
        var clientList = dbServiceClient.findAll();

        // then
        assertThat(clientList.size()).isEqualTo(1);
        assertThat(clientList.get(0)).usingRecursiveComparison().isEqualTo(loadedClient.get());
    }
}
