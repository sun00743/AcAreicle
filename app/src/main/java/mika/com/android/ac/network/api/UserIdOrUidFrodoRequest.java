/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.network.api;

import com.android.volley.AuthFailureError;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

// HACK: Silly Frodo API take requests with uid for invalid uri, because of a whitelist activated
// when access token is sent (actually a bug in the whitelist). As a hack we drop the access token
// when requesting with uid.
// FIXME: This will render friend-visible diaries invisible.
public class UserIdOrUidFrodoRequest<T> extends FrodoRequest<T> {

    private boolean mIsUid;

    public UserIdOrUidFrodoRequest(int method, String url, Type type) {
        super(method, url, type);
    }

    public UserIdOrUidFrodoRequest(int method, String url, TypeToken<T> typeToken) {
        super(method, url, typeToken);
    }

    public UserIdOrUidFrodoRequest<T> withUserIdOrUid(String userIdOrUid) {
        mIsUid = !userIdOrUid.matches("\\d+");
        return this;
    }

    @Override
    public void onPreparePerformRequest() throws AuthFailureError {
        if (!mIsUid) {
            super.onPreparePerformRequest();
        }
    }
}
