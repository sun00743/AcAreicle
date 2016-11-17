/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.network.api;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class LifeStreamRequest<T> extends ApiV2Request<T> {

    public LifeStreamRequest(int method, String url, Type type) {
        super(method, url, type);

        init();
    }

    public LifeStreamRequest(int method, String url, TypeToken<T> typeToken) {
        super(method, url, typeToken);

        init();
    }

    private void init() {
        addParam(ApiContract.Request.ApiV2.LifeStream.VERSION,
                String.valueOf(ApiContract.Request.ApiV2.LifeStream.Versions.TWO));
    }
}
