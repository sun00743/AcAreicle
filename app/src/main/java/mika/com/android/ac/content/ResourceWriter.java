/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.content;

import android.content.Context;

import com.android.volley.Response;

import mika.com.android.ac.network.Request;
import mika.com.android.ac.network.Volley;

public abstract class ResourceWriter<W extends ResourceWriter, T>
        implements Response.Listener<T>, Response.ErrorListener {

    private ResourceWriterManager<W> mManager;

    private Request<T> mRequest;

    public ResourceWriter(ResourceWriterManager<W> manager) {
        mManager = manager;
    }

    public void onStart() {
        mRequest = onCreateRequest();
        mRequest.setListener(this);
        mRequest.setErrorListener(this);
        Volley.getInstance().addToRequestQueue(mRequest);
    }

    protected abstract Request<T> onCreateRequest();

    public void onDestroy() {
        mRequest.cancel();
        mRequest.setListener(null);
        mRequest.setErrorListener(null);
    }

    protected Context getContext() {
        return mManager.getContext();
    }

    protected void stopSelf() {
        //noinspection unchecked
        mManager.stop((W) this);
    }
}
