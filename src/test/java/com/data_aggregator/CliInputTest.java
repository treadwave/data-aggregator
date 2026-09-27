package com.data_aggregator;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_aggregator.cli.Automatic;
import com.data_aggregator.cli.Interactive;

public class CliInputTest {
    private java.io.InputStream originalIn;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        originalIn = System.in;
        originalOut = System.out;
    }

    @AfterEach
    public void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    public void automaticRejectsInvalidUserParametersBeforeNetworkCalls() throws Exception {
        assertIllegalArgument(() -> new Automatic().run(new String[] {}));
        assertIllegalArgument(() -> new Automatic().run(new String[] {"--apis=marketstack", "--format=xml"}));
        assertIllegalArgument(() -> new Automatic().run(new String[] {"--apis=marketstack", "--threads=0"}));
        assertIllegalArgument(() -> new Automatic().run(new String[] {"--apis=marketstack", "--interval=abc"}));
        assertIllegalArgument(() -> new Automatic().run(new String[] {"--apis=missing", "--polling=true", "--threads=1", "--interval=1"}));
    }

    @Test
    public void interactiveRejectsUnknownApiChoice() throws Exception {
        String output = runInteractive("99\n");

        assertTrue(output.contains("Выберите API"));
        assertTrue(output.contains("Неверный выбор API"));
    }

    @Test
    public void interactiveRejectsWrongFormatFileModeAndWorkMode() throws Exception {
        assertTrue(runInteractive("1\n9\n").contains("Неверный формат"));
        assertTrue(runInteractive("1\n1\n9\n").contains("Неверный режим файла"));
        assertTrue(runInteractive("1\n1\n1\n9\n").contains("Неверный режим работы"));
    }

    @Test
    public void interactiveRejectsInvalidPollingNumbers() throws Exception {
        try {
            runInteractive("1\n1\n1\n2\n0\n");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("больше 0"));
            return;
        }

        throw new AssertionError("Invalid polling number must be rejected");
    }

    private String runInteractive(String input) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));

        new Interactive().run();

        return output.toString(StandardCharsets.UTF_8);
    }

    private void assertIllegalArgument(ThrowingRunnable runnable) throws Exception {
        try {
            runnable.run();
        } catch (IllegalArgumentException e) {
            return;
        }

        throw new AssertionError("IllegalArgumentException expected");
    }

    private interface ThrowingRunnable {
        void run() throws Exception;
    }
}
