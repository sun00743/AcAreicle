/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.notification.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.link.UriHandler;
import mika.com.android.ac.network.api.info.frodo.Notification;
import mika.com.android.ac.ui.SimpleAdapter;
import mika.com.android.ac.ui.TimeTextView;
import mika.com.android.ac.util.RecyclerViewUtils;
import mika.com.android.ac.util.ViewUtils;

public class NotificationAdapter extends SimpleAdapter<Notification,
        NotificationAdapter.ViewHolder> {

    private final ColorStateList mTextColorPrimary;
    private final ColorStateList mTextColorSecondary;

    public NotificationAdapter(List<Notification> notificationList, Context context) {
        super(notificationList);

        mTextColorPrimary = ViewUtils.getColorStateListFromAttrRes(android.R.attr.textColorPrimary,
                context);
        mTextColorSecondary = ViewUtils.getColorStateListFromAttrRes(
                android.R.attr.textColorSecondary, context);

        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ViewUtils.inflate(mika.com.android.ac.R.layout.notification_item, parent));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Context context = RecyclerViewUtils.getContext(holder);
        final Notification notification = getItem(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markNotificationAsRead(notification);
                UriHandler.open(notification.targetUri, context);
            }
        });
        holder.textText.setText(notification.text);
        holder.textText.setTextColor(notification.read ? mTextColorSecondary : mTextColorPrimary);
        holder.timeText.setDoubanTime(notification.time);
    }

    private void markNotificationAsRead(Notification notification) {
        notification.read = true;
        notifyItemChangedById(notification.id);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(mika.com.android.ac.R.id.text)
        public TextView textText;
        @BindView(mika.com.android.ac.R.id.time)
        public TimeTextView timeText;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
