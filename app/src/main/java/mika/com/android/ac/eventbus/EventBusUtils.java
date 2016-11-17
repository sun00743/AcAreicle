/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.eventbus;

import android.os.Handler;

import de.greenrobot.event.EventBus;

public class EventBusUtils {

    private static final EventBus sEventBus = EventBus.getDefault();

    private EventBusUtils() {}

    public static void register(Object subscriber) {
        sEventBus.register(subscriber);
    }

    public static void unregister(Object subscriber) {
        sEventBus.unregister(subscriber);
    }

    public static void postSync(Object event) {
        sEventBus.post(event);
    }

    public static void postAsync(final Object event) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                sEventBus.post(event);
            }
        });
    }
}
