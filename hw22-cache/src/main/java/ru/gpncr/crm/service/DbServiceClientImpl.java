package ru.gpncr.crm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gpncr.cachehw.MyCache;
import ru.gpncr.core.repository.DataTemplate;
import ru.gpncr.core.sessionmanager.TransactionRunner;
import ru.gpncr.crm.model.Client;
import ru.gpncr.crm.model.ClientKey;

@SuppressWarnings({"java:S2789"})
public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;
    private final MyCache<ClientKey, Client> clientCache;

    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
        this.clientCache = new MyCache<>();
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                log.info("created client: {}", createdClient);
                clientCache.put(new ClientKey("key:" + createdClient.getId(), createdClient.getId()), createdClient);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            log.info("updated client: {}", client);
            clientCache.put(new ClientKey("key:" + client.getId(), client.getId()), client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        if (clientCache.containsKey(id)) {
            log.info("Client retrieved from cache: {}", clientCache.get(new ClientKey("key:" + id, id)));
            return Optional.of(clientCache.get(new ClientKey("key:" + id, id)));
        }
        return transactionRunner.doInTransaction(connection -> {
            var clientOptional = dataTemplate.findById(connection, id);
            log.info("client 111: {}", clientOptional);
            if (clientOptional != null) {
                clientOptional.ifPresent(
                        client -> clientCache.put(new ClientKey("key:" + client.getId(), client.getId()), client));
            }
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        List<Client> clientsFromCache = new ArrayList<>(clientCache.values());

        if (!clientsFromCache.isEmpty()) {
            return clientsFromCache;
        }
        return transactionRunner.doInTransaction(connection -> {
            var clientList = dataTemplate.findAll(connection);
            for (Client client : clientList) {
                clientCache.put(new ClientKey("key:" + client.getId(), client.getId()), client);
            }
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }
}
