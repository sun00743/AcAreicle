package me.zhanghai.android.douya.network.api.info.acapi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 名字没取好啊，其实应该是articleDes之类的
 * Created by Administrator on 2016/9/5.
 */
public class ArticleList implements Parcelable{

    public int commentCount;

    public boolean isTudouDomain;

    public String description;

    public String title;

    public String channelPath;

    public int  parentChannelId;

    public long latestCommentTime;

    public int  bananaCount;

    public boolean isTopLevel;
    /**
     * UP主ID
     */
    public long userId;

    public int  favoriteCount;
    /**
     * 文章ID
     */
    public int id;

    public String coverImage;

    public int channelId;

    public long viewCount;

    public String username;

    public long contributeTime;

    public String userAvatar;

    public String contributeTimeFormat;
    /**
     * 文章链接
     */
    public String link;

    public String userUrl;

    public String contributeTimeFormat2;
    /**
     * 时间差
     */
    public String diffTime;

    public static String makeTransitionName(long id) {
        return "articleDes-" + id;
    }

    public String makeTransitionName() {
        return makeTransitionName(id);
    }

    protected ArticleList(Parcel in) {
        commentCount = in.readInt();
        isTudouDomain = in.readByte() != 0;
        description = in.readString();
        title = in.readString();
        channelPath = in.readString();
        parentChannelId = in.readInt();
        latestCommentTime = in.readLong();
        bananaCount = in.readInt();
        isTopLevel = in.readByte() != 0;
        userId = in.readLong();
        favoriteCount = in.readInt();
        id = in.readInt();
        coverImage = in.readString();
        channelId = in.readInt();
        viewCount = in.readLong();
        username = in.readString();
        contributeTime = in.readLong();
        userAvatar = in.readString();
        contributeTimeFormat = in.readString();
        link = in.readString();
        userUrl = in.readString();
        contributeTimeFormat2 = in.readString();
        diffTime = in.readString();
    }

    public static final Creator<ArticleList> CREATOR = new Creator<ArticleList>() {
        @Override
        public ArticleList createFromParcel(Parcel in) {
            return new ArticleList(in);
        }

        @Override
        public ArticleList[] newArray(int size) {
            return new ArticleList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(commentCount);
        dest.writeByte((byte) (isTudouDomain ? 1 : 0));
        dest.writeString(description);
        dest.writeString(title);
        dest.writeString(channelPath);
        dest.writeInt(parentChannelId);
        dest.writeLong(latestCommentTime);
        dest.writeInt(bananaCount);
        dest.writeByte((byte) (isTopLevel ? 1 : 0));
        dest.writeLong(userId);
        dest.writeInt(favoriteCount);
        dest.writeInt(id);
        dest.writeString(coverImage);
        dest.writeInt(channelId);
        dest.writeLong(viewCount);
        dest.writeString(username);
        dest.writeLong(contributeTime);
        dest.writeString(userAvatar);
        dest.writeString(contributeTimeFormat);
        dest.writeString(link);
        dest.writeString(userUrl);
        dest.writeString(contributeTimeFormat2);
        dest.writeString(diffTime);
    }
}
/*
"commentCount":0,
        "isTudouDomain":false,
        "description":"日本出现能在坟墓显示死者的VR技术",
        "channelPath":"a",
        "parentChannelId":63,
        "title":"日本出现能在坟墓显示死者的VR技术",
        "latestCommentTime":1472990213000,
        "duration":0,
        "bananaCount":0,
        "isTopLevel":false,
        "userId":613827,
        "danmuSize":0,
        "favoriteCount":0,
        "id":3078329,
        "coverImage":"http://cdn.aixifan.com/dotnet/20120923/style/image/cover-night.png",
        "channelId":110,
        "viewCount":104,
        "username":"九折坂二人",
        "contributeTime":1472990213000,
        "userAvatar":"http://cdn.aixifan.com/dotnet/artemis/u/cms/www/201512/141907402i6eisps.jpg",
        "contributeTimeFormat":"2016-09-04 19:56:53",
        "viewCountFormat":104,
        "commentCountFormat":0,
        "link":"/a/ac3078329",
        "userUrl":"http://www.acfun.tv/u/613827.aspx",
        "contributeTimeFormat2":"9月4日(星期日)19时56分"*/
