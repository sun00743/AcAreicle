/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.user.content;

import com.android.volley.VolleyError;

import java.util.List;

import mika.com.android.ac.network.api.ApiRequest;
import mika.com.android.ac.network.api.info.apiv2.User;

public abstract class RawUserListResource extends BaseUserListResource<List<User>> {

    protected abstract ApiRequest<List<User>> onCreateRequest(Integer start, Integer count);

    @Override
    protected void onDeliverLoadFinished(boolean successful, List<User> userList, VolleyError error,
                                         boolean loadMore, int count) {
        onLoadFinished(successful, userList, error, loadMore, count);
    }
}
