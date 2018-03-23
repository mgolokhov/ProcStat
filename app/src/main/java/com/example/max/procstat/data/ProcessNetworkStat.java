package com.example.max.procstat.data;

public class ProcessNetworkStat {
    private final long mobileWifiRx;
    private final long mobileWifiTx;

    public ProcessNetworkStat(long mobileWifiRx, long mobileWifiTx) {
        this.mobileWifiRx = mobileWifiRx;
        this.mobileWifiTx = mobileWifiTx;
    }

    public long getMobileWifiRx() {
        return mobileWifiRx;
    }

    public long getMobileWifiTx() {
        return mobileWifiTx;
    }
}
