/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.broadcast.content;

import android.accounts.Account;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.List;

import mika.com.android.ac.account.util.AccountUtils;
import mika.com.android.ac.network.api.info.apiv2.Broadcast;
import mika.com.android.ac.settings.info.Settings;
import mika.com.android.ac.util.Callback;
import mika.com.android.ac.util.FragmentUtils;

public class HomeBroadcastListResource extends BroadcastListResource {

    private static final String FRAGMENT_TAG_DEFAULT = HomeBroadcastListResource.class.getName();

    private final Handler mHandler = new Handler();

    private Account mAccount;

    private boolean mStopped;

    private static HomeBroadcastListResource newInstance() {
        //noinspection deprecation
        HomeBroadcastListResource resource = new HomeBroadcastListResource();
        resource.setArguments(null, null);
        return resource;
    }

    public static HomeBroadcastListResource attachTo(FragmentActivity activity) {
        return attachTo(activity, FRAGMENT_TAG_DEFAULT, REQUEST_CODE_INVALID);
    }

    public static HomeBroadcastListResource attachTo(FragmentActivity activity, String tag,
                                                     int requestCode) {
        return attachTo(activity, tag, true, null, requestCode);
    }

    public static HomeBroadcastListResource attachTo(Fragment fragment) {
        return attachTo(fragment, FRAGMENT_TAG_DEFAULT, REQUEST_CODE_INVALID);
    }

    public static HomeBroadcastListResource attachTo(Fragment fragment, String tag,
                                                     int requestCode) {
        return attachTo(fragment.getActivity(), tag, false, fragment, requestCode);
    }

    private static HomeBroadcastListResource attachTo(FragmentActivity activity, String tag,
                                                      boolean targetAtActivity,
                                                      Fragment targetFragment, int requestCode) {
        HomeBroadcastListResource resource = FragmentUtils.findByTag(activity, tag);
        if (resource == null) {
            resource = newInstance();
            if (targetAtActivity) {
                resource.targetAtActivity(requestCode);
            } else {
                resource.targetAtFragment(targetFragment, requestCode);
            }
            FragmentUtils.add(resource, activity, tag);
        }
        return resource;
    }

    /**
     * @deprecated Use {@code attachTo()} instead.
     */
    @SuppressWarnings("deprecation")
    public HomeBroadcastListResource() {}

    @Override
    public void onStart() {
        super.onStart();

        mStopped = false;
    }

    @Override
    public void onStop() {
        super.onStop();

        mStopped = true;

        List<Broadcast> broadcastList = get();
        if (broadcastList != null && broadcastList.size() > 0) {
            saveToCache(broadcastList);
        }
    }

    @Override
    protected void loadOnStart() {
        loadFromCache();
    }

    private void loadFromCache() {

        if (isLoading()) {
            return;
        }

        setLoading(true);

        onStartLoad();
        HomeBroadcastListCache.get(mAccount, mHandler, new Callback<List<Broadcast>>() {
            @Override
            public void onValue(List<Broadcast> broadcastList) {
                onLoadFromCacheComplete(broadcastList);
            }
        }, getActivity());
    }

    @Override
    protected void onStartLoad() {
        super.onStartLoad();

        mAccount = AccountUtils.getActiveAccount(getContext());
    }

    private void onLoadFromCacheComplete(List<Broadcast> broadcastList) {

        setLoading(false);

        if (mStopped) {
            return;
        }

        boolean hasCache = broadcastList != null && broadcastList.size() > 0;
        if (hasCache) {
            set(broadcastList);
        }

        if (!hasCache || Settings.AUTO_REFRESH_HOME.getValue(getActivity())) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mStopped) {
                        return;
                    }
                    load(false);
                }
            });
        }
    }

    private void saveToCache(List<Broadcast> broadcastList) {
        HomeBroadcastListCache.put(mAccount, broadcastList, getActivity());
    }
}
