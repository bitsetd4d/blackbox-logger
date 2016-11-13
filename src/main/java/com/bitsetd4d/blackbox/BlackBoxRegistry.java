package com.bitsetd4d.blackbox;

import com.bitsetd4d.blackbox.internal.BlackBoxStatsImpl;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class BlackBoxRegistry {

    private static Set<BlackBox> weakHashSet = Collections.newSetFromMap(new WeakHashMap<>());
    private static BlackBox proxy = new BlackBoxProxy();

    static void register(BlackBox appender) {
        weakHashSet.add(appender);
    }

    static int getCount() {
        return weakHashSet.size();
    }

    public static BlackBox getBlackBoxBroadcast() {
        return proxy;
    }

    private static class BlackBoxProxy implements BlackBox {
        @Override
        public void holdLogging() {
            weakHashSet.forEach(blackBox -> blackBox.holdLogging());
        }

        @Override
        public void outputHeldLogging() {
            weakHashSet.forEach(blackBox -> blackBox.outputHeldLogging());
        }

        @Override
        public void forgetHeldLogging() {
            weakHashSet.forEach(blackBox -> blackBox.forgetHeldLogging());
        }

        @Override
        public BlackBoxStats getStats() {
            return weakHashSet
                    .stream()
                    .map(b -> b.getStats())
                    .filter(stats -> stats != null)
                    .reduce(new BlackBoxStatsImpl(0,0,0,0,0), (a, b) -> a.add(b));
        }
    }
}