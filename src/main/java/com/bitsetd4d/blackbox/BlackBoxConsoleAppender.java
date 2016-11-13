package com.bitsetd4d.blackbox;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import com.bitsetd4d.blackbox.internal.BlackBoxStatsImpl;

import java.util.ArrayList;
import java.util.List;

public class BlackBoxConsoleAppender<E> extends ConsoleAppender<E> implements BlackBox {

    private boolean hold = false;
    private List<ILoggingEvent> buffer = new ArrayList<>();

    public BlackBoxConsoleAppender() {
        BlackBoxRegistry.register(this);
    }

    @Override
    protected synchronized void append(E eventObject) {
        if (hold && eventObject instanceof ILoggingEvent) {
            buffer.add((ILoggingEvent) eventObject);
        } else {
            super.append(eventObject);
        }
    }

    @Override
    public synchronized void holdLogging() {
        hold = true;
    }

    @Override
    public synchronized void outputHeldLogging() {
        hold = false;
        buffer.forEach(e -> append((E) e));
        buffer.clear();
    }

    @Override
    public synchronized void forgetHeldLogging() {
        hold = false;
        buffer.clear();
    }

    @Override
    public BlackBoxStats getStats() {
        return new BlackBoxStatsImpl(
                count(Level.TRACE),
                count(Level.DEBUG),
                count(Level.INFO),
                count(Level.WARN),
                count(Level.ERROR));
    }

    private int count(Level level) {
        return (int) buffer.stream().filter(e -> e.getLevel() == level).count();
    }
}
