/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.content;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mika.com.android.ac.broadcast.content.DeleteBroadcastCommentManager;
import mika.com.android.ac.broadcast.content.DeleteBroadcastManager;
import mika.com.android.ac.broadcast.content.LikeBroadcastManager;
import mika.com.android.ac.broadcast.content.RebroadcastBroadcastManager;
import mika.com.android.ac.broadcast.content.SendBroadcastCommentManager;
import mika.com.android.ac.followship.content.FollowUserManager;

public class ResourceWriterService extends Service {

    private List<ResourceWriterManager> mWriterManagers = new ArrayList<>();

    public static Intent makeIntent(Context context) {
        return new Intent(context, ResourceWriterService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        addWriterManager(FollowUserManager.getInstance());
        addWriterManager(LikeBroadcastManager.getInstance());
        addWriterManager(RebroadcastBroadcastManager.getInstance());
        addWriterManager(DeleteBroadcastManager.getInstance());
        addWriterManager(DeleteBroadcastCommentManager.getInstance());
        addWriterManager(SendBroadcastCommentManager.getInstance());
    }

    private void addWriterManager(ResourceWriterManager writerManager) {
        mWriterManagers.add(writerManager);
        writerManager.onBind(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        removeWriterManagers();
    }

    private void removeWriterManagers() {
        Iterator<ResourceWriterManager> iterator = mWriterManagers.iterator();
        while (iterator.hasNext()) {
            ResourceWriterManager writerManager = iterator.next();
            writerManager.onUnbind();
            iterator.remove();
        }
    }
}
