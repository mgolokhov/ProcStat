package com.example.max.procstat.util;

import android.annotation.TargetApi;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.RemoteException;
import android.telephony.TelephonyManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
@TargetApi(Build.VERSION_CODES.M)
public class NetworkStatsHelper {

    private final Context context;
    private final NetworkStatsManager networkStatsManager;

    @Inject
    public NetworkStatsHelper(Context context, NetworkStatsManager networkStatsManager) {
        this.context = context;
        this.networkStatsManager = networkStatsManager;
    }


    public long getPackageRxBytesMobile(int packageUid) {
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(
                    ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    0,
                    System.currentTimeMillis(),
                    packageUid);
            return getRxBytesFromBuckets(networkStats);
        } catch (Exception e){
            Timber.e(e);
        } finally {
            if (networkStats != null) networkStats.close();
        }
        return -1;
    }

    public long getPackageTxBytesMobile(int packageUid) {
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(
                    ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    0,
                    System.currentTimeMillis(),
                    packageUid);
            return getTxBytesFromBuckets(networkStats);
        } catch (Exception e){
            Timber.e(e);
        } finally {
            if (networkStats != null) networkStats.close();
        }
        return -1;
    }

    public long getPackageRxBytesWifi(int packageUid) {
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(
                    ConnectivityManager.TYPE_WIFI,
                    "",
                    0,
                    System.currentTimeMillis(),
                    packageUid);
            return getRxBytesFromBuckets(networkStats);
        } catch (Exception e){
            Timber.e(e);
        } finally {
            if (networkStats != null) networkStats.close();
        }
        return -1;
    }

    public long getPackageTxBytesWifi(int packageUid) {
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(
                    ConnectivityManager.TYPE_WIFI,
                    "",
                    0,
                    System.currentTimeMillis(),
                    packageUid);
            return getTxBytesFromBuckets(networkStats);
        } catch (Exception e){
            Timber.e(e);
        } finally {
            if (networkStats != null) networkStats.close();
        }
        return -1;
    }

    public long getPackageTxBytes(int packageUid){
        return getPackageTxBytesMobile(packageUid) + getPackageTxBytesWifi(packageUid);
    }

    public long getPackageRxBytes(int packageUid){
        return getPackageRxBytesMobile(packageUid) + getPackageRxBytesWifi(packageUid);
    }

    private long getRxBytesFromBuckets(NetworkStats networkStats){
        long result = 0;
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while(networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            result += bucket.getRxBytes();
        }
        return result;
    }

    private long getTxBytesFromBuckets(NetworkStats networkStats){
        long result = 0;
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        while(networkStats.hasNextBucket()) {
            networkStats.getNextBucket(bucket);
            result += bucket.getTxBytes();
        }
        return result;
    }

    private String getSubscriberId(Context context, int networkType) {
        if (ConnectivityManager.TYPE_MOBILE == networkType) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSubscriberId();
        }
        return "";
    }
}
