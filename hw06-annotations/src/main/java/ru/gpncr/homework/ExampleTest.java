package ru.gpncr.homework;

@SuppressWarnings({"java:S106", "java:S1104", "java:S1144", "java:S112", "java:S1141"})
public class ExampleTest {

    @Before
    public void setUp() {
        System.out.println("Setting up...");
    }

    @Before
    public void setUp1() {
        System.out.println("Setting up1...");
    }

    @Test
    public void testOne() {
        System.out.println("Running test one...");
        throw new RuntimeException();
    }

    @Test
    public void testTwo() {
        System.out.println("Running test two...");
    }

    @After
    public void tearDown() {
        System.out.println("Tearing down...");
    }
}
