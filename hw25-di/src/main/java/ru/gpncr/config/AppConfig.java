package ru.gpncr.config;

import ru.gpncr.appcontainer.api.AppComponent;
import ru.gpncr.appcontainer.api.AppComponentsContainerConfig;
import ru.gpncr.services.*;

@SuppressWarnings({"java:S106"})
@AppComponentsContainerConfig(order = 1)
public class AppConfig {
    @AppComponent(order = 0, name = "equationPreparer")
    public EquationPreparer equationPreparer() {
        return new EquationPreparerImpl();
    }

    @AppComponent(order = 1, name = "playerService")
    public PlayerService playerService(IOService ioService) {
        return new PlayerServiceImpl(ioService);
    }

    @AppComponent(order = 2, name = "gameProcessor")
    public GameProcessor gameProcessor(
            IOService ioService, PlayerService playerService, EquationPreparer equationPreparer) {
        return new GameProcessorImpl(ioService, equationPreparer, playerService);
    }

    @AppComponent(order = 0, name = "ioService")
    public IOService ioService() {
        return new IOServiceStreams(System.out, System.in);
    }
}
