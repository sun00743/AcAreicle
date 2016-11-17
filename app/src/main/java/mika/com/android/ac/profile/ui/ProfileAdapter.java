/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.profile.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mika.com.android.ac.network.api.info.apiv2.Broadcast;
import mika.com.android.ac.network.api.info.apiv2.User;
import mika.com.android.ac.network.api.info.apiv2.UserInfo;
import mika.com.android.ac.network.api.info.frodo.Diary;
import mika.com.android.ac.network.api.info.frodo.Review;
import mika.com.android.ac.network.api.info.frodo.UserItems;
import mika.com.android.ac.util.ViewUtils;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private static final int ITEM_INTRODUCTION = 0;
    private static final int ITEM_BROADCASTS = 1;
    private static final int ITEM_FOLLOWSHIP = 2;
    private static final int ITEM_DIARIES = 3;
    private static final int ITEM_BOOKS = 4;
    private static final int ITEM_MOVIES = 5;
    private static final int ITEM_MUSIC = 6;
    private static final int ITEM_REVIEWS = 7;

    private static final int ITEM_COUNT = 8;

    private ProfileIntroductionLayout.Listener mListener;

    private Data mData;

    public ProfileAdapter(ProfileIntroductionLayout.Listener listener) {

        mListener = listener;

        setHasStableIds(true);
    }

    public void setData(Data data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData != null ? ITEM_COUNT : 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;
        switch (viewType) {
            case ITEM_INTRODUCTION:
                layoutRes = mika.com.android.ac.R.layout.profile_introduction_item;
                break;
            case ITEM_BROADCASTS:
                layoutRes = mika.com.android.ac.R.layout.profile_broadcasts_item;
                break;
            case ITEM_FOLLOWSHIP:
                layoutRes = mika.com.android.ac.R.layout.profile_followship_item;
                break;
            case ITEM_DIARIES:
                layoutRes = mika.com.android.ac.R.layout.profile_diaries_item;
                break;
            case ITEM_BOOKS:
                layoutRes = mika.com.android.ac.R.layout.profile_books_item;
                break;
            case ITEM_MOVIES:
                layoutRes = mika.com.android.ac.R.layout.profile_movies_item;
                break;
            case ITEM_MUSIC:
                layoutRes = mika.com.android.ac.R.layout.profile_music_item;
                break;
            case ITEM_REVIEWS:
                layoutRes = mika.com.android.ac.R.layout.profile_reviews_item;
                break;
            default:
                return null;
        }
        return new ViewHolder(ViewUtils.inflate(layoutRes, parent));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (position) {
            case ITEM_INTRODUCTION: {
                ProfileIntroductionLayout layout = ((ProfileIntroductionLayout) holder.getChild());
                layout.bind(mData.userInfo);
                layout.setListener(mListener);
                break;
            }
            case ITEM_BROADCASTS:
                ((ProfileBroadcastsLayout) holder.getChild()).bind(mData.userInfo,
                        mData.broadcastList);
                break;
            case ITEM_FOLLOWSHIP:
                ((ProfileFollowshipLayout) holder.getChild()).bind(mData.userInfo,
                        mData.followingList);
                break;
            case ITEM_DIARIES:
                ((ProfileDiariesLayout) holder.getChild()).bind(mData.userInfo, mData.diaryList);
                break;
            case ITEM_BOOKS:
                ((ProfileBooksLayout) holder.getChild()).bind(mData.userInfo, mData.userItemList);
                break;
            case ITEM_MOVIES:
                ((ProfileMoviesLayout) holder.getChild()).bind(mData.userInfo, mData.userItemList);
                break;
            case ITEM_MUSIC:
                ((ProfileMusicLayout) holder.getChild()).bind(mData.userInfo, mData.userItemList);
                break;
            case ITEM_REVIEWS:
                ((ProfileReviewsLayout) holder.getChild()).bind(mData.userInfo, mData.reviewList);
                break;
        }
    }

    public static class Data {

        public UserInfo userInfo;
        public List<Broadcast> broadcastList;
        public List<User> followingList;
        public List<Diary> diaryList;
        public List<UserItems> userItemList;
        public List<Review> reviewList;

        public Data(UserInfo userInfo, List<Broadcast> broadcastList, List<User> followingList,
                    List<Diary> diaryList, List<UserItems> userItemList, List<Review> reviewList) {
            this.userInfo = userInfo;
            this.broadcastList = broadcastList;
            this.followingList = followingList;
            this.diaryList = diaryList;
            this.userItemList = userItemList;
            this.reviewList = reviewList;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public View getChild() {
            return ((ViewGroup) itemView).getChildAt(0);
        }
    }
}
