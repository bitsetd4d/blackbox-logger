package com.bitsetd4d.blackbox;

public interface BlackBoxStats {

    int getTraceCount();
    int getDebugCount();
    int getInfoCount();
    int getWarnCount();
    int getErrorCount();

    int getTotalCount();

    BlackBoxStats add(BlackBoxStats otherStats);

}
