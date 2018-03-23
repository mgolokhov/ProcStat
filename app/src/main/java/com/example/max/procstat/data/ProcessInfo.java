package com.example.max.procstat.data;

public class ProcessInfo {
    private final String name;
    private final String packageName;
    private final String iconPath;

    public ProcessInfo(String name, String packageName, String iconPath) {
        this.name = name;
        this.packageName = packageName;
        this.iconPath = iconPath;
    }

    public String getIconPath() {
        return iconPath;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

}
