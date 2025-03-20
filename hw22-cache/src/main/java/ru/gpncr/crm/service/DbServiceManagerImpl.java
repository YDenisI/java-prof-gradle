package ru.gpncr.crm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gpncr.cachehw.MyCache;
import ru.gpncr.core.repository.DataTemplate;
import ru.gpncr.core.sessionmanager.TransactionRunner;
import ru.gpncr.crm.model.Manager;
import ru.gpncr.crm.model.ManagerKey;

public class DbServiceManagerImpl implements DBServiceManager {
    private static final Logger log = LoggerFactory.getLogger(DbServiceManagerImpl.class);

    private final DataTemplate<Manager> managerDataTemplate;
    private final TransactionRunner transactionRunner;
    private final MyCache<ManagerKey, Manager> managerCache;

    public DbServiceManagerImpl(TransactionRunner transactionRunner, DataTemplate<Manager> managerDataTemplate) {
        this.transactionRunner = transactionRunner;
        this.managerDataTemplate = managerDataTemplate;
        this.managerCache = new MyCache<>();
    }

    @Override
    public Manager saveManager(Manager manager) {
        return transactionRunner.doInTransaction(connection -> {
            if (manager.getNo() == null) {
                var managerNo = managerDataTemplate.insert(connection, manager);
                var createdManager = new Manager(managerNo, manager.getLabel(), manager.getParam1());
                log.info("created manager: {}", createdManager);
                managerCache.put(
                        new ManagerKey("key:" + createdManager.getNo(), createdManager.getNo()), createdManager);
                return createdManager;
            }
            managerDataTemplate.update(connection, manager);
            log.info("updated manager: {}", manager);
            managerCache.put(new ManagerKey("key:" + manager.getNo(), manager.getNo()), manager);
            return manager;
        });
    }

    @Override
    public Optional<Manager> getManager(long no) {
        if (managerCache.containsKey(no)) {
            log.info("Client retrieved from cache: {}", managerCache.get(new ManagerKey("key:" + no, no)));
            return Optional.of(managerCache.get(new ManagerKey("key:" + no, no)));
        }
        return transactionRunner.doInTransaction(connection -> {
            var managerOptional = managerDataTemplate.findById(connection, no);
            log.info("manager: {}", managerOptional);
            return managerOptional;
        });
    }

    @Override
    public List<Manager> findAll() {
        List<Manager> managersFromCache = new ArrayList<>(managerCache.values());

        if (!managersFromCache.isEmpty()) {
            return managersFromCache;
        }
        return transactionRunner.doInTransaction(connection -> {
            var managerList = managerDataTemplate.findAll(connection);
            for (Manager manager : managerList) {
                managerCache.put(new ManagerKey("key:" + manager.getNo(), manager.getNo()), manager);
            }
            log.info("managerList:{}", managerList);
            return managerList;
        });
    }
}
