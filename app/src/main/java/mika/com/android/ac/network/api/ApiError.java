/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.network.api;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RedirectError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ApiError extends VolleyError {

    private static final Map<Integer, Integer> ERROR_CODE_STRING_RES_MAP;
    static {

        ERROR_CODE_STRING_RES_MAP = new HashMap<>();

        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Custom.INVALID_ERROR_RESPONSE,
                mika.com.android.ac.R.string.api_error_invalid_error_response);

        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Base.UNKNOWN_V2_ERROR, mika.com.android.ac.R.string.api_error_unknown_v2_error);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Base.NEED_PERMISSION, mika.com.android.ac.R.string.api_error_need_permission);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Base.URI_NOT_FOUND, mika.com.android.ac.R.string.api_error_uri_not_found);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Base.MISSING_ARGS, mika.com.android.ac.R.string.api_error_missing_args);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Base.IMAGE_TOO_LARGE, mika.com.android.ac.R.string.api_error_image_too_large);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Base.HAS_BAN_WORD, mika.com.android.ac.R.string.api_error_has_ban_word);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Base.INPUT_TOO_SHORT, mika.com.android.ac.R.string.api_error_input_too_short);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Base.TARGET_NOT_FOUND, mika.com.android.ac.R.string.api_error_target_not_found);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Base.NEED_CAPTCHA, mika.com.android.ac.R.string.api_error_need_captcha);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Base.IMAGE_UNKNOWN, mika.com.android.ac.R.string.api_error_image_unknown);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Base.IMAGE_WRONG_FORMAT,
                mika.com.android.ac.R.string.api_error_image_wrong_format);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Base.IMAGE_WRONG_CK, mika.com.android.ac.R.string.api_error_image_wrong_ck);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Base.IMAGE_CK_EXPIRED, mika.com.android.ac.R.string.api_error_image_ck_expired);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Base.TITLE_MISSING, mika.com.android.ac.R.string.api_error_title_missing);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Base.DESC_MISSING, mika.com.android.ac.R.string.api_error_desc_missing);

        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.INVALID_REQUEST_SCHEME,
                mika.com.android.ac.R.string.api_error_token_invalid_request_scheme);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.INVALID_REQUEST_METHOD,
                mika.com.android.ac.R.string.api_error_token_invalid_request_method);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.ACCESS_TOKEN_IS_MISSING,
                mika.com.android.ac.R.string.api_error_token_access_token_is_missing);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.INVALID_ACCESS_TOKEN,
                mika.com.android.ac.R.string.api_error_token_invalid_access_token);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.INVALID_APIKEY,
                mika.com.android.ac.R.string.api_error_token_invalid_apikey);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.APIKEY_IS_BLOCKED,
                mika.com.android.ac.R.string.api_error_token_apikey_is_blocked);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.ACCESS_TOKEN_HAS_EXPIRED,
                mika.com.android.ac.R.string.api_error_token_access_token_has_expired);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.INVALID_REQUEST_URI,
                mika.com.android.ac.R.string.api_error_token_invalid_request_uri);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.INVALID_CREDENCIAL1,
                mika.com.android.ac.R.string.api_error_token_invalid_credencial1);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.INVALID_CREDENCIAL2,
                mika.com.android.ac.R.string.api_error_token_invalid_credencial2);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.NOT_TRIAL_USER,
                mika.com.android.ac.R.string.api_error_token_not_trial_user);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.RATE_LIMIT_EXCEEDED1,
                mika.com.android.ac.R.string.api_error_token_rate_limit_exceeded1);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.RATE_LIMIT_EXCEEDED2,
                mika.com.android.ac.R.string.api_error_token_rate_limit_exceeded2);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.REQUIRED_PARAMETER_IS_MISSING,
                mika.com.android.ac.R.string.api_error_token_required_parameter_is_missing);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.UNSUPPORTED_GRANT_TYPE,
                mika.com.android.ac.R.string.api_error_token_unsupported_grant_type);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.UNSUPPORTED_RESPONSE_TYPE,
                mika.com.android.ac.R.string.api_error_token_unsupported_response_type);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.CLIENT_SECRET_MISMATCH,
                mika.com.android.ac.R.string.api_error_token_client_secret_mismatch);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.REDIRECT_URI_MISMATCH,
                mika.com.android.ac.R.string.api_error_token_redirect_uri_mismatch);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.INVALID_AUTHORIZATION_CODE,
                mika.com.android.ac.R.string.api_error_token_invalid_authorization_code);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.INVALID_REFRESH_TOKEN,
                mika.com.android.ac.R.string.api_error_token_invalid_refresh_token);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.USERNAME_PASSWORD_MISMATCH,
                mika.com.android.ac.R.string.api_error_token_username_password_mismatch);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.INVALID_USER, mika.com.android.ac.R.string.api_error_token_invalid_user);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.USER_HAS_BLOCKED,
                mika.com.android.ac.R.string.api_error_token_user_has_blocked);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.ACCESS_TOKEN_HAS_EXPIRED_SINCE_PASSWORD_CHANGED,
                mika.com.android.ac.R.string.api_error_token_access_token_has_expired_since_password_changed);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.ACCESS_TOKEN_HAS_NOT_EXPIRED,
                mika.com.android.ac.R.string.api_error_token_access_token_has_not_expired);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.INVALID_REQUEST_SCOPE,
                mika.com.android.ac.R.string.api_error_token_invalid_request_scope);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.INVALID_REQUEST_SOURCE,
                mika.com.android.ac.R.string.api_error_token_invalid_request_source);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.THIRDPARTY_LOGIN_AUTH_FAILED,
                mika.com.android.ac.R.string.api_error_token_thirdparty_login_auth_failed);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Token.USER_LOCKED, mika.com.android.ac.R.string.api_error_token_user_locked);

        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Followship.ALREADY_FOLLOWED,
                mika.com.android.ac.R.string.api_error_followship_already_followed);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Followship.NOT_FOLLOWED_YET,
                mika.com.android.ac.R.string.api_error_followship_not_followed_yet);

        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Broadcast.NOT_FOUND, mika.com.android.ac.R.string.api_error_broadcast_not_found);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.Broadcast.AUTHOR_BANNED,
                mika.com.android.ac.R.string.api_error_broadcast_author_banned);

        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.LikeBroadcast.ALREADY_LIKED,
                mika.com.android.ac.R.string.api_error_like_broadcast_already_liked);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.LikeBroadcast.NOT_LIKED_YET,
                mika.com.android.ac.R.string.api_error_like_broadcast_not_liked_yet);

        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.RebroadcastBroadcast.ALREADY_REBROADCASTED,
                mika.com.android.ac.R.string.api_error_rebroadcast_broadcast_already_rebroadcasted);
        ERROR_CODE_STRING_RES_MAP.put(ApiContract.Response.Error.Codes.RebroadcastBroadcast.NOT_REBROADCASTED_YET,
                mika.com.android.ac.R.string.api_error_rebroadcast_broadcast_not_rebroadcasted_yet);
    }

    public String responseString;
    public JSONObject responseJson;
    public int code;
    public String localizedMessage;
    public String message;
    public String request;

    protected ApiError(NetworkResponse response) {
        super(response);

        if (response.headers == null || response.data == null) {
            return;
        }
        String charset = HttpHeaderParser.parseCharset(response.headers);
        // Don't throw an exception from here, just do the best we can.
        try {
            responseString = new String(response.data, charset);
            parseResponse();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    protected ApiError(VolleyError volleyError) {
        super(volleyError);
    }

    public static ApiError wrap(VolleyError error) {
        if (error.networkResponse != null) {
            return new ApiError(error.networkResponse);
        } else {
            return new ApiError(error);
        }
    }

    private void parseResponse() {
        try {
            responseJson = new JSONObject(responseString);
            code = responseJson.optInt(ApiContract.Response.Error.CODE, 0);
            message = responseJson.optString(ApiContract.Response.Error.MSG, null);
            request = responseJson.optString(ApiContract.Response.Error.REQUEST, null);
            localizedMessage = responseJson.optString(ApiContract.Response.Error.LOCALIZED_MESSAGE, null);
        } catch (JSONException e) {
            e.printStackTrace();
            code = ApiContract.Response.Error.Codes.Custom.INVALID_ERROR_RESPONSE;
        }
    }

    public int getErrorStringRes() {

        if (networkResponse == null) {
            // Return as the wrapped error.
            // We only have two constructors, so this cast is safe.
            return getErrorStringRes((VolleyError) this.getCause());
        }

        Integer StringRes = ERROR_CODE_STRING_RES_MAP.get(code);
        return StringRes != null ? StringRes : mika.com.android.ac.R.string.api_error_unknown;
    }

    public static int getErrorStringRes(VolleyError error) {
        if (error instanceof ParseError) {
            return mika.com.android.ac.R.string.api_error_parse;
        } else if (error instanceof TimeoutError) {
            return mika.com.android.ac.R.string.api_error_timeout;
        } else if (error instanceof NoConnectionError) {
            return mika.com.android.ac.R.string.api_error_no_connection;
        } else if (error instanceof AuthFailureError) {
            return mika.com.android.ac.R.string.api_error_auth_failure;
        } else if (error instanceof RedirectError) {
            return mika.com.android.ac.R.string.api_error_redirect;
        } else if (error instanceof ServerError) {
            return mika.com.android.ac.R.string.api_error_server;
        } else if (error instanceof NetworkError) {
            return mika.com.android.ac.R.string.api_error_network;
        } else if (error instanceof ApiError) {
            return ((ApiError) error).getErrorStringRes();
        } else {
            return mika.com.android.ac.R.string.api_error_unknown;
        }
    }

    public static String getErrorString(VolleyError error, Context context) {
        return context.getString(getErrorStringRes(error));
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "responseString='" + responseString + '\'' +
                ", responseJson=" + responseJson +
                ", code=" + code +
                ", localizedMessage='" + localizedMessage + '\'' +
                ", message='" + message + '\'' +
                ", request='" + request + '\'' +
                "} " + super.toString();
    }
}
