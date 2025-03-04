package ru.gpncr;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.io.PrintStream;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.gpncr.appcontainer.AppComponentsContainerImpl;
import ru.gpncr.appcontainer.api.AppComponent;
import ru.gpncr.appcontainer.api.AppComponentsContainerConfig;
import ru.gpncr.config.AppConfig;
import ru.gpncr.services.*;

@SuppressWarnings({"java:S6204", "java:S1130", "java:S5786"})
class AppTest {

    @DisplayName("Из контекста тремя способами должен корректно доставаться компонент с проставленными полями")
    @ParameterizedTest(name = "Достаем по: {0}")
    @CsvSource(
            value = {
                "GameProcessor, ru.gpncr.services.GameProcessor",
                "GameProcessorImpl, ru.gpncr.services.GameProcessor",
                "gameProcessor, ru.gpncr.services.GameProcessor",
                "IOService, ru.gpncr.services.IOService",
                "IOServiceStreams, ru.gpncr.services.IOService",
                "ioService, ru.gpncr.services.IOService",
                "PlayerService, ru.gpncr.services.PlayerService",
                "PlayerServiceImpl, ru.gpncr.services.PlayerService",
                "playerService, ru.gpncr.services.PlayerService",
                "EquationPreparer, ru.gpncr.services.EquationPreparer",
                "EquationPreparerImpl, ru.gpncr.services.EquationPreparer",
                "equationPreparer, ru.gpncr.services.EquationPreparer"
            })
    public void shouldExtractFromContextCorrectComponentWithNotNullFields(String classNameOrBeanId, Class<?> rootClass)
            throws Exception {
        var ctx = new AppComponentsContainerImpl(AppConfig.class);

        assertThat(classNameOrBeanId).isNotEmpty();
        Object component;
        if (classNameOrBeanId.charAt(0) == classNameOrBeanId.toUpperCase().charAt(0)) {
            Class<?> gameProcessorClass = Class.forName("ru.gpncr.services." + classNameOrBeanId);
            assertThat(rootClass).isAssignableFrom(gameProcessorClass);

            component = ctx.getAppComponent(gameProcessorClass);
        } else {
            component = ctx.getAppComponent(classNameOrBeanId);
        }
        assertThat(component).isNotNull();
        assertThat(rootClass).isAssignableFrom(component.getClass());

        var fields = Arrays.stream(component.getClass().getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()))
                .peek(f -> f.setAccessible(true))
                .collect(Collectors.toList());

        for (var field : fields) {
            var fieldValue = field.get(component);
            assertThat(fieldValue)
                    .isNotNull()
                    .isInstanceOfAny(
                            IOService.class,
                            PlayerService.class,
                            EquationPreparer.class,
                            PrintStream.class,
                            Scanner.class);
        }
    }

    @DisplayName("В контексте не должно быть компонентов с одинаковым именем")
    @Test
    public void shouldNotAllowTwoComponentsWithSameName() throws Exception {
        assertThatCode(() -> new AppComponentsContainerImpl(ConfigWithTwoComponentsWithSameName.class))
                .isInstanceOf(Exception.class);
    }

    @DisplayName(
            "При попытке достать из контекста отсутствующий или дублирующийся компонент, должно выкидываться исключение")
    @Test
    public void shouldThrowExceptionWhenContainerContainsMoreThanOneOrNoneExpectedComponents() throws Exception {
        var ctx = new AppComponentsContainerImpl(ConfigWithTwoSameComponents.class);

        assertThatCode(() -> ctx.getAppComponent(EquationPreparer.class)).isInstanceOf(Exception.class);
        assertThatCode(() -> ctx.getAppComponent(PlayerService.class)).isInstanceOf(Exception.class);
    }

    @AppComponentsContainerConfig(order = 1)
    public static class ConfigWithTwoComponentsWithSameName {
        public ConfigWithTwoComponentsWithSameName() {}

        @AppComponent(order = 1, name = "equationPreparer")
        public EquationPreparer equationPreparer1() {
            return new EquationPreparerImpl();
        }

        @AppComponent(order = 1, name = "equationPreparer")
        public IOService ioService() {
            return new IOServiceStreams(System.out, System.in);
        }
    }

    @AppComponentsContainerConfig(order = 1)
    public static class ConfigWithTwoSameComponents {

        @AppComponent(order = 1, name = "equationPreparer1")
        public EquationPreparer equationPreparer1() {
            return new EquationPreparerImpl();
        }

        @AppComponent(order = 1, name = "equationPreparer2")
        public EquationPreparer equationPreparer2() {
            return new EquationPreparerImpl();
        }
    }
}
