/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.user.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.network.api.info.apiv2.User;
import mika.com.android.ac.ui.SimpleAdapter;
import mika.com.android.ac.util.ImageUtils;
import mika.com.android.ac.util.RecyclerViewUtils;
import mika.com.android.ac.util.ViewUtils;

public abstract class BaseUserAdapter extends SimpleAdapter<User, BaseUserAdapter.ViewHolder> {

    public BaseUserAdapter() {
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        //noinspection deprecation
        return getItem(position).id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ViewUtils.inflate(getLayoutResource(), parent));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Context context = RecyclerViewUtils.getContext(holder);
        final User user = getItem(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                context.startActivity(ProfileActivity.makeIntent(user, context));
            }
        });
        ImageUtils.loadAvatar(holder.avatarImage, user.avatar);
        holder.nameText.setText(user.name);
        //noinspection deprecation
        holder.descriptionText.setText(user.uid);
    }

    abstract protected int getLayoutResource();

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(mika.com.android.ac.R.id.avatar)
        public ImageView avatarImage;
        @BindView(mika.com.android.ac.R.id.name)
        public TextView nameText;
        @BindView(mika.com.android.ac.R.id.description)
        public TextView descriptionText;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
