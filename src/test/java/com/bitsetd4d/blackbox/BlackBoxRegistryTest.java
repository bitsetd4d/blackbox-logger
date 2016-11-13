package com.bitsetd4d.blackbox;

import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BlackBoxRegistryTest {

    @Test
    public void appenderIsRegisteredInRegistry() throws Exception {
        Logger logger = LoggerFactory.getLogger(BlockBoxConsoleAppenderTest.class);
        assertThat(logger, notNullValue());
        assertThat(BlackBoxRegistry.getCount(), equalTo(1));
    }

    @Test
    public void registryBroadcastsToBlackBoxes() throws Exception {
        // Given
        final BlackBox blackBox = Mockito.mock(BlackBox.class);
        int count = BlackBoxRegistry.getCount();
        BlackBoxRegistry.register(blackBox);
        assertThat(BlackBoxRegistry.getCount(), equalTo(count + 1));

        // When
        BlackBoxRegistry.getBlackBoxBroadcast().holdLogging();
        BlackBoxRegistry.getBlackBoxBroadcast().forgetHeldLogging();
        BlackBoxRegistry.getBlackBoxBroadcast().outputHeldLogging();
        BlackBoxRegistry.getBlackBoxBroadcast().getStats();

        // Then
        verify(blackBox, times(1)).holdLogging();
        verify(blackBox, times(1)).forgetHeldLogging();
        verify(blackBox, times(1)).outputHeldLogging();
        verify(blackBox, times(1)).getStats();
    }

}