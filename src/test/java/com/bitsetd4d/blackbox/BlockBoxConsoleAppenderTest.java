package com.bitsetd4d.blackbox;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class BlockBoxConsoleAppenderTest {

    private PrintStream realSystemOut;
    private ByteArrayOutputStream outContent;
    private PrintStream fakeSystemOut;

    @Before
    public void setUp() throws Exception {
        realSystemOut = System.out;
        outContent = new ByteArrayOutputStream();
        fakeSystemOut = new PrintStream(outContent);
        System.setOut(fakeSystemOut);
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(realSystemOut);
    }

    @Test
    public void simpleOutput() throws Exception {
        // Given
        Logger logger = LoggerFactory.getLogger(BlockBoxConsoleAppenderTest.class);
        // When
        logger.info("Hello");
        // Then
        systemOutContents().contains("Hello");
    }

    private String systemOutContents() {
        String output = outContent.toString();
        realSystemOut.println("Output was <" + output + ">");
        return output;
    }

    @Test
    public void outputSuppressedUntilRequested() throws Exception {
        // Given
        Logger logger = LoggerFactory.getLogger(BlockBoxConsoleAppenderTest.class);
        BlackBoxRegistry.getBlackBoxBroadcast().holdLogging();
        // When
        logger.info("Foobar");
        // Then
        assertThat(systemOutContents(), not(containsString("Foobar")));
        assertThat(BlackBoxRegistry.getBlackBoxBroadcast().getStats().getTotalCount(), equalTo(1));

        // When
        BlackBoxRegistry.getBlackBoxBroadcast().outputHeldLogging();
        // Then
        assertThat(systemOutContents(), containsString("Foobar"));
        assertThat(BlackBoxRegistry.getBlackBoxBroadcast().getStats().getTotalCount(), equalTo(0));
    }

    @Test
    public void outputSuppressedAndDiscarded() throws Exception {
        // Given
        Logger logger = LoggerFactory.getLogger(BlockBoxConsoleAppenderTest.class);
        BlackBoxRegistry.getBlackBoxBroadcast().holdLogging();
        // When
        logger.info("Monkey");
        // Then
        assertThat(systemOutContents(), not(containsString("Monkey")));
        assertThat(BlackBoxRegistry.getBlackBoxBroadcast().getStats().getTotalCount(), equalTo(1));

        // When
        BlackBoxRegistry.getBlackBoxBroadcast().forgetHeldLogging();
        // Then
        assertThat(systemOutContents(), not(containsString("Monkey")));
        assertThat(BlackBoxRegistry.getBlackBoxBroadcast().getStats().getTotalCount(), equalTo(0));
    }

    @Test
    public void statsCollectedForHeldLogging() throws Exception {
        // Given
        Logger logger = LoggerFactory.getLogger(BlockBoxConsoleAppenderTest.class);
        BlackBoxRegistry.getBlackBoxBroadcast().forgetHeldLogging();
        BlackBoxRegistry.getBlackBoxBroadcast().holdLogging();
        BlackBoxStats stats = BlackBoxRegistry.getBlackBoxBroadcast().getStats();
        assertThat(stats.getTraceCount(), equalTo(0));
        assertThat(stats.getDebugCount(), equalTo(0));
        assertThat(stats.getInfoCount(),  equalTo(0));
        assertThat(stats.getWarnCount(),  equalTo(0));
        assertThat(stats.getErrorCount(), equalTo(0));
        assertThat(stats.getTotalCount(), equalTo(0));

        // When
        repeat(1, () -> logger.trace("Balloon"));
        repeat(2, () -> logger.debug("Cow"));
        repeat(3, () -> logger.info("Monkey"));
        repeat(4, () -> logger.warn("Orange"));
        repeat(5, () -> logger.error("Cider"));

        // Then
        stats = BlackBoxRegistry.getBlackBoxBroadcast().getStats();
        assertThat(stats.getTraceCount(), equalTo(1));
        assertThat(stats.getDebugCount(), equalTo(2));
        assertThat(stats.getInfoCount(),  equalTo(3));
        assertThat(stats.getWarnCount(),  equalTo(4));
        assertThat(stats.getErrorCount(), equalTo(5));
        assertThat(stats.getTotalCount(), equalTo(1 + 2 + 3 + 4 + 5));
    }

    private void repeat(int n, Runnable r) {
        for (int i = 0; i < n; i++) {
            r.run();
        }
    }
}