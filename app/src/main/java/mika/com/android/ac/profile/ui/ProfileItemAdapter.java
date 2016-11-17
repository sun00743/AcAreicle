/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.profile.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import mika.com.android.ac.link.UriHandler;
import mika.com.android.ac.network.api.info.frodo.Item;
import mika.com.android.ac.ui.RatioFrameLayout;
import mika.com.android.ac.ui.SimpleAdapter;
import mika.com.android.ac.util.DrawableUtils;
import mika.com.android.ac.util.ImageUtils;
import mika.com.android.ac.util.RecyclerViewUtils;
import mika.com.android.ac.util.ViewCompat;
import mika.com.android.ac.util.ViewUtils;

public class ProfileItemAdapter extends SimpleAdapter<Item, ProfileItemAdapter.ViewHolder> {

    public ProfileItemAdapter() {
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(ViewUtils.inflate(mika.com.android.ac.R.layout.profile_item_item, parent));
        ViewCompat.setBackground(holder.scrimView, DrawableUtils.makeScrimDrawable());
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Item item = getItem(position);
        float ratio = 1;
        switch (item.getType()) {
            case BOOK:
            case EVENT:
            case MOVIE:
                ratio = 2f / 3;
                break;
        }
        holder.itemLayout.setRatio(ratio);
        final Context context = RecyclerViewUtils.getContext(holder);
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
                UriHandler.open(item.url, context);
            }
        });
        ImageUtils.loadImage(holder.coverImage, item.cover.getLarge());
        holder.titleText.setText(item.title);
        // FIXME: This won't work properly if items are changed.
        ViewUtils.setVisibleOrGone(holder.dividerSpace, position != getItemCount() - 1);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(mika.com.android.ac.R.id.item)
        public RatioFrameLayout itemLayout;
        @BindView(mika.com.android.ac.R.id.cover)
        public ImageView coverImage;
        @BindView(mika.com.android.ac.R.id.scrim)
        public View scrimView;
        @BindView(mika.com.android.ac.R.id.title)
        public TextView titleText;
        @BindView(mika.com.android.ac.R.id.divider)
        public Space dividerSpace;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
