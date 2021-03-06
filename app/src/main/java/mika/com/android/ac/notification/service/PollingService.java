package mika.com.android.ac.notification.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import mika.com.android.ac.R;
import mika.com.android.ac.network.Volley;
import mika.com.android.ac.network.api.ApiRequest;
import mika.com.android.ac.network.api.ApiRequests;
import mika.com.android.ac.network.api.info.acapi.Mention;

public class PollingService extends Service
//        implements
//        Response.Listener<Mention>,
//        Response.ErrorListener
{

    private static final String ACTION = "ac.mika.message.poll";
    //    private MainActivity.PushBinder mBinder ;
    private PushBinder mBinder;
    private MentionResponseListener mentionResponseListener;
    private OnNewMentionListener mMentionListener;

    public PollingService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mentionResponseListener = new MentionResponseListener();
        initNotificationManager();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ApiRequest<Mention> request = ApiRequests.newPushRequest();
        if (request == null) {
            mentionResponseListener.onErrorResponse(new VolleyError(getResources().getString(R.string.request_acer_error)));

            return super.onStartCommand(intent, flags, startId);
        }
        request.setListener(mentionResponseListener).setErrorListener(mentionResponseListener);
        Volley.getInstance().addToRequestQueue(request);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        mBinder = new PushBinder();
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private void initNotificationManager() {
    }

    private void showNotification() {
    }

    public void setNewMentionListener(OnNewMentionListener l) {
        mMentionListener = l;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * mention response
     */
    private class MentionResponseListener implements Response.Listener<Mention>, Response.ErrorListener {
        @Override
        public void onResponse(Mention response) {
            if (mMentionListener == null) {
                return;
            }
            mMentionListener.onNewMention(response.mention);
        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }
    }

    /**
     * mention listener
     */
    public interface OnNewMentionListener {
        void onNewMention(int count);
    }

    /**
     * my binder
     * return this service
     */
    public class PushBinder extends Binder {
        public PollingService getPollingService() {
            return PollingService.this;
        }
    }

}
