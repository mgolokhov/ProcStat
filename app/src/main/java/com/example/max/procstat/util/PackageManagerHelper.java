package com.example.max.procstat.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import timber.log.Timber;


public class PackageManagerHelper {

    public static int getPackageUid(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        int uid = -1;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            uid = packageInfo.applicationInfo.uid;
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e);
        }
        return uid;
    }
}
