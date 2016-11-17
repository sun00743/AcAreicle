/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * In order to retain instance, this fragment should always be attached to the host activity
 * directly instead of being attached to any child fragment manager.
 */
public class RetainedFragment extends Fragment {

    private List<Runnable> mPendingRunnables = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setUserVisibleHint(false);
    }

    @Override
    public void onResume() {
        super.onResume();

        Iterator<Runnable> iterator = mPendingRunnables.iterator();
        while (iterator.hasNext()) {
            Runnable runnable = iterator.next();
            iterator.remove();
            runnable.run();
        }
    }

    protected void postOnResumed(Runnable runnable) {
        if (isResumed()) {
            runnable.run();
        } else {
            mPendingRunnables.add(runnable);
        }
    }
}
