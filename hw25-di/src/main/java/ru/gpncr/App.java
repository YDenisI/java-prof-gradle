package ru.gpncr;

/*
В классе AppComponentsContainerImpl реализовать обработку, полученной в конструкторе конфигурации,
основываясь на разметке аннотациями из пакета appcontainer. Так же необходимо реализовать методы getAppComponent.
В итоге должно получиться работающее приложение. Менять можно только класс AppComponentsContainerImpl.
Можно добавлять свои исключения.

Раскоментируйте тест:
@Disabled //надо удалить
Тест и демо должны проходить для всех реализованных вариантов
Не называйте свой проект ДЗ "homework-template", это имя заготовки)

PS Приложение представляет собой тренажер таблицы умножения
*/

import ru.gpncr.appcontainer.AppComponentsContainerImpl;
import ru.gpncr.appcontainer.api.AppComponentsContainer;
import ru.gpncr.config.AppConfig;
import ru.gpncr.services.GameProcessor;

@SuppressWarnings({"java:S125", "java:S1130"})
public class App {

    public static void main(String[] args) throws Exception {
        // Опциональные варианты
        // AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig1.class, AppConfig2.class);

        // Тут можно использовать библиотеку Reflections (см. зависимости)
        // AppComponentsContainer container = new AppComponentsContainerImpl("ru.otus.config");

        // Обязательный вариант
        AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig.class);

        // Приложение должно работать в каждом из указанных ниже вариантов
        // GameProcessor gameProcessor = container.getAppComponent(GameProcessor.class);
        // GameProcessor gameProcessor = container.getAppComponent(GameProcessorImpl.class);
        GameProcessor gameProcessor = container.getAppComponent("gameProcessor");

        gameProcessor.startGame();
    }
}
