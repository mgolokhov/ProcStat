package com.example.max.procstat.data;

import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.max.procstat.util.NetworkStatsHelper;
import com.example.max.procstat.util.PackageManagerHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;
import timber.log.Timber;

@Singleton
public class ProcessesDataRepository implements ProcessesDataSource {
    private final Context context;
    private final PackageManager packageManager;
    private final NetworkStatsHelper networkStatsHelper;

    @Inject
    ProcessesDataRepository(Context context, PackageManager packageManager, NetworkStatsHelper networkStatsHelper) {
        this.context = context;
        this.packageManager = packageManager;
        this.networkStatsHelper = networkStatsHelper;
    }

    @Override
    public Maybe<List<ProcessInfo>> getProcesses() {
        return Maybe.fromCallable(this::getProcessesImpl);
    }

    @Override
    public Maybe<ProcessInfo> getProcess(String packageName) {
        return Maybe.fromCallable(() -> getProcessImpl(packageName));
    }

    @Override
    public Maybe<ProcessNetworkStat> getProcessNetworkStat(String packageName) {
        return Maybe.fromCallable(() -> getProcessNetworkStatImpl(packageName));
    }

    private List<ProcessInfo> getProcessesImpl() {
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
        List<ProcessInfo> packageList = new ArrayList<>(packageInfoList.size());
        for (PackageInfo packageInfo : packageInfoList) {
            ProcessInfo packageItem = getProcessImpl(packageInfo);
            packageList.add(packageItem);
        }
        return packageList;
    }

    private ProcessInfo getProcessImpl(String packageName) {
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            return getProcessImpl(packageInfo);
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e);
            return null;
        }
    }

    private ProcessInfo getProcessImpl(PackageInfo packageInfo) {
        String iconUriPath = "android.resource://" + context.getPackageName() + "/mipmap/ic_launcher";
        String appName = "N/A";
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(packageInfo.packageName, PackageManager.GET_META_DATA);
            if (ai.icon != 0) {
                iconUriPath = "android.resource://" + packageInfo.packageName + "/" + ai.icon;
            }
            CharSequence name = packageManager.getApplicationLabel(ai);
            if (name != null) {
                appName = name.toString();
            }
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e);
            // use default icon & app name
        }
        return new ProcessInfo(appName, packageInfo.packageName, iconUriPath);
    }

    private ProcessNetworkStat getProcessNetworkStatImpl(String packageName) {
        int uid = PackageManagerHelper.getPackageUid(context, packageName);
        long mobileWifiRx = networkStatsHelper.getPackageRxBytes(uid);
        long mobileWifiTx = networkStatsHelper.getPackageTxBytes(uid);
        return new ProcessNetworkStat(mobileWifiRx, mobileWifiTx);
    }
}
