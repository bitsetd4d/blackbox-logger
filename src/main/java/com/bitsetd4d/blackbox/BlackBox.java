package com.bitsetd4d.blackbox;

public interface BlackBox {

    void holdLogging();
    void outputHeldLogging();
    void forgetHeldLogging();

    BlackBoxStats getStats();
}
