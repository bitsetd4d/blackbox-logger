package com.bitsetd4d.blackbox.internal;

import com.bitsetd4d.blackbox.BlackBoxStats;

public class BlackBoxStatsImpl implements BlackBoxStats {

    private final int trace;
    private final int debug;
    private final int info;
    private final int warn;
    private final int error;

    public BlackBoxStatsImpl(int trace, int debug, int info, int warn, int error) {
        this.trace = trace;
        this.debug = debug;
        this.info = info;
        this.warn = warn;
        this.error = error;
    }

    @Override
    public BlackBoxStats add(BlackBoxStats other) {
        return new BlackBoxStatsImpl(
                this.trace + other.getTraceCount(),
                this.debug + other.getDebugCount(),
                this.info  + other.getInfoCount(),
                this.warn  + other.getWarnCount(),
                this.error + other.getErrorCount());
    }

    @Override
    public int getTraceCount() {
        return trace;
    }

    @Override
    public int getDebugCount() {
        return debug;
    }

    @Override
    public int getInfoCount() {
        return info;
    }

    @Override
    public int getWarnCount() {
        return warn;
    }

    @Override
    public int getErrorCount() {
        return error;
    }

    @Override
    public int getTotalCount() {
        return trace + debug + info + warn + error;
    }

    @Override
    public String toString() {
        return "BlackBoxStatsImpl{" +
                "trace=" + trace +
                ", debug=" + debug +
                ", info=" + info +
                ", warn=" + warn +
                ", error=" + error +
                '}';
    }
}