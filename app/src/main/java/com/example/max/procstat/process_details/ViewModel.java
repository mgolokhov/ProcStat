package com.example.max.procstat.process_details;

public class ViewModel {
    private final String iconPath;
    private final String appName;
    private final long rx;
    private final long tx;

    public ViewModel(String iconPath, String appName, long rx, long tx) {
        this.iconPath = iconPath;
        this.appName = appName;
        this.rx = rx;
        this.tx = tx;
    }

    public String getIconPath() {
        return iconPath;
    }

    public String getAppName() {
        return appName;
    }

    public long getRx() {
        return rx;
    }

    public long getTx() {
        return tx;
    }
}
