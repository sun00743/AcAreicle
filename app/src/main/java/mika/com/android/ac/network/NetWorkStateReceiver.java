/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import mika.com.android.ac.AcWenApplication;

public class NetWorkStateReceiver extends BroadcastReceiver {

    public NetWorkStateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()){
                switch (networkInfo.getType()){
                    case ConnectivityManager.TYPE_WIFI:
                        AcWenApplication.getInstance().CONNECTIVITY_TYPE = NetState.WIFI;
                        break;
                    case ConnectivityManager.TYPE_MOBILE:
                        AcWenApplication.getInstance().CONNECTIVITY_TYPE = NetState.MOBILE;
                        break;
                }
            }
        }
    }
}
