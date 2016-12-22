/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.network.api;

import android.text.TextUtils;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import mika.com.android.ac.AcWenApplication;
import mika.com.android.ac.network.api.info.acapi.ArticleListResult;
import mika.com.android.ac.network.api.info.acapi.CommentResult;
import mika.com.android.ac.network.api.info.acapi.Mention;
import mika.com.android.ac.network.api.info.apiv2.Broadcast;
import mika.com.android.ac.network.api.info.apiv2.Comment;
import mika.com.android.ac.network.api.info.apiv2.CommentList;
import mika.com.android.ac.network.api.info.apiv2.User;
import mika.com.android.ac.network.api.info.apiv2.UserInfo;
import mika.com.android.ac.network.api.info.apiv2.UserList;
import mika.com.android.ac.network.api.info.frodo.DiaryList;
import mika.com.android.ac.network.api.info.frodo.Notification;
import mika.com.android.ac.network.api.info.frodo.NotificationList;
import mika.com.android.ac.network.api.info.frodo.ReviewList;
import mika.com.android.ac.network.api.info.frodo.UserItemList;
import mika.com.android.ac.util.Connectivity;
import mika.com.android.ac.util.StringUtils;

/**
 * The {@code context} argument is only used for
 * {@code Volley.getInstance(context).getAuthenticator()}.
 */
public class ApiRequests {

    private ApiRequests() {
    }


    public static String getAcerUid() {
        if (AcWenApplication.getInstance().getAcer() == null) {
            return null;
        } else {
            return String.valueOf(AcWenApplication.getInstance().getAcer().userId);
        }
    }

    public static String getAcerAccessToken() {
        if (AcWenApplication.getInstance().getAcer() == null) {
            return null;
        } else {
            return AcWenApplication.getInstance().getAcer().access_token;
        }
    }

    public static ApiRequest<UserInfo> newUserInfoRequest(String userIdOrUid) {

        if (TextUtils.isEmpty(userIdOrUid)) {
            userIdOrUid = ApiContract.Request.ApiV2.UserInfo.UID_CURRENT;
        }

        return new LifeStreamRequest<>(ApiRequest.Method.GET,
                StringUtils.formatUs(ApiContract.Request.ApiV2.UserInfo.URL_FORMAT, userIdOrUid),
                new TypeToken<UserInfo>() {
                });
    }

    public static ApiRequest<UserInfo> newFollowshipRequest(String userIdOrUid, boolean follow) {
        return new LifeStreamRequest<>(follow ? ApiRequest.Method.POST : ApiRequest.Method.DELETE,
                StringUtils.formatUs(ApiContract.Request.ApiV2.Followship.URL_FORMAT, userIdOrUid),
                new TypeToken<UserInfo>() {
                });
    }

    public static ApiRequest<UserList> newFollowingListRequest(String userIdOrUid, Integer start,
                                                               Integer count, String tag) {

        ApiRequest<UserList> request = new LifeStreamRequest<>(ApiRequest.Method.GET,
                StringUtils.formatUs(ApiContract.Request.ApiV2.FollowingList.URL_FORMAT,
                        userIdOrUid), new TypeToken<UserList>() {
        });

        if (start != null) {
            request.addParam(ApiContract.Request.ApiV2.FollowingList.START, String.valueOf(start));
        }
        if (count != null) {
            request.addParam(ApiContract.Request.ApiV2.FollowingList.COUNT, String.valueOf(count));
        }
        if (!TextUtils.isEmpty(tag)) {
            request.addParam(ApiContract.Request.ApiV2.FollowingList.TAG, tag);
        }

        return request;
    }

    public static ApiRequest<UserList> newFollowingListRequest(String userIdOrUid, Integer start,
                                                               Integer count) {
        return newFollowingListRequest(userIdOrUid, start, count, null);
    }

    public static ApiRequest<UserList> newFollowerListRequest(String userIdOrUid, Integer start,
                                                              Integer count) {

        ApiRequest<UserList> request = new LifeStreamRequest<>(ApiRequest.Method.GET,
                StringUtils.formatUs(ApiContract.Request.ApiV2.FollowerList.URL_FORMAT,
                        userIdOrUid), new TypeToken<UserList>() {
        });

        if (start != null) {
            request.addParam(ApiContract.Request.ApiV2.FollowerList.START, String.valueOf(start));
        }
        if (count != null) {
            request.addParam(ApiContract.Request.ApiV2.FollowerList.COUNT, String.valueOf(count));
        }

        return request;
    }

    public static ApiRequest<NotificationList> newNotificationListRequest(Integer start,
                                                                          Integer count) {

        ApiRequest<NotificationList> request = new FrodoRequest<NotificationList>(
                ApiRequest.Method.GET, ApiContract.Request.Frodo.NotificationList.URL,
                new TypeToken<NotificationList>() {
                }) {
            @Override
            protected Response<NotificationList> parseNetworkResponse(NetworkResponse response) {
                Response<NotificationList> superResponse = super.parseNetworkResponse(response);
                if (superResponse.isSuccess()) {
                    // Fix for Frodo API.
                    for (Notification notification : superResponse.result.notifications) {
                        notification.fix();
                    }
                }
                return superResponse;
            }
        };

        if (start != null) {
            request.addParam(ApiContract.Request.Frodo.NotificationList.START,
                    String.valueOf(start));
        }
        if (count != null) {
            request.addParam(ApiContract.Request.Frodo.NotificationList.COUNT,
                    String.valueOf(count));
        }

        return request;
    }

    public static ApiRequest<List<Broadcast>> newBroadcastListRequest(String userIdOrUid,
                                                                      String topic, Long untilId,
                                                                      Integer count) {

        String url;
        if (TextUtils.isEmpty(userIdOrUid) && TextUtils.isEmpty(topic)) {
            url = ApiContract.Request.ApiV2.BroadcastList.Urls.HOME;
        } else if (TextUtils.isEmpty(topic)) {
            url = StringUtils.formatUs(ApiContract.Request.ApiV2.BroadcastList.Urls.USER_FORMAT,
                    userIdOrUid);
        } else {
            url = ApiContract.Request.ApiV2.BroadcastList.Urls.TOPIC;
        }

        ApiRequest<List<Broadcast>> request = new LifeStreamRequest<>(ApiRequest.Method.GET, url,
                new TypeToken<List<Broadcast>>() {
                });

        if (untilId != null) {
            request.addParam(ApiContract.Request.ApiV2.BroadcastList.UNTIL_ID,
                    String.valueOf(untilId));
        }
        if (count != null) {
            request.addParam(ApiContract.Request.ApiV2.BroadcastList.COUNT, String.valueOf(count));
        }
        if (topic != null) {
            request.addParam(ApiContract.Request.ApiV2.BroadcastList.Q, topic);
        }

        return request;
    }

    /**
     * 召唤或者私信 Count push Request
     *
     */
    public static ApiRequest<Mention> newPushRequest() {
        ApiRequest request = new ApiRequest<>(ApiRequest.Method.GET, ApiContract.Request.AcApi.PUSH_REQUEST_URL, new TypeToken<Mention>() {
        });
        if (getAcerUid() == null || getAcerAccessToken() == null) {
            return null;
        } else {
            request.addParam(Connectivity.UID, getAcerUid());
            request.addParam(Connectivity.ACCESS_TOKEN, getAcerAccessToken());
            return request;
        }
    }

    /**
     * 文章列表Request
     *
     */
    public static ApiRequest<ArticleListResult> newArticleListResultRequest(int channelid, int sort, int pageNo) {

        ApiRequest request = new ApiRequest(ApiRequest.Method.GET, ApiContract.Request.AcApi.ARTICLE_LIST, new TypeToken<ArticleListResult>() {
        });
        request.addParam(Connectivity.SORT, String.valueOf(sort));
        request.addParam(Connectivity.CHANNEL_ID, String.valueOf(channelid));
        request.addParam(Connectivity.PAGE_SIZE, ApiContract.Request.Params.ARTICLE_LIST_PAGE_SIZE);
        request.addParam(Connectivity.PAGE_NO, String.valueOf(pageNo));

        return request;
    }

    /**
     * 评论列表Request
     *
     */
    public static ApiRequest<CommentResult> newCommentListResultRequest(int articleId, int pageNO) {
        ApiRequest request = new ApiRequest(ApiRequest.Method.GET, ApiContract.Request.AcApi.COMMENT_LIST, new TypeToken<CommentResult>() {
        });
        request.addParam(Connectivity.VERSION_COMMENT_LIST, ApiContract.Request.Params.VERSION_COMMENT_LIST);
        request.addParam(Connectivity.CONTENT_ID, String.valueOf(articleId));
        request.addParam(Connectivity.PAGE_SIZE, ApiContract.Request.Params.COMMENT_LIST_PAGE_SIZE);
        request.addParam(Connectivity.PAGE_NO, String.valueOf(pageNO));

        request.addHeaders(Connectivity.UA_MAP);
        request.addHeader(Connectivity.UID, getAcerUid());
        request.addHeader(Connectivity.MARKET, ApiContract.Request.Headers.MARKET);
        request.addHeader(Connectivity.HOST, ApiContract.Request.Headers.HOST);
        request.addHeader(Connectivity.REQUEST_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA).format(System.currentTimeMillis()));

//        request.addHeader("uuid", "ca83390e-927e-4fa4-85b8-328fff5fba2b");
//        request.addHeader("udid", "61558168-3cab-37db-8b37-fd03279ebceb");
        return request;
    }

    public static ApiRequest<Broadcast> newBroadcastRequest(long broadcastId) {
        return new LifeStreamRequest<>(ApiRequest.Method.GET,
                StringUtils.formatUs(ApiContract.Request.ApiV2.Broadcast.URL_FORMAT, broadcastId),
                new TypeToken<Broadcast>() {
                });
    }

    public static ApiRequest<CommentList> newBroadcastCommentListRequest(long broadcastId,
                                                                         Integer start,
                                                                         Integer count) {

        ApiRequest<CommentList> request = new LifeStreamRequest<>(ApiRequest.Method.GET,
                StringUtils.formatUs(ApiContract.Request.ApiV2.BroadcastCommentList.URL_FORMAT,
                        broadcastId), new TypeToken<CommentList>() {
        });

        if (start != null) {
            request.addParam(ApiContract.Request.ApiV2.BroadcastCommentList.START,
                    String.valueOf(start));
        }
        if (count != null) {
            request.addParam(ApiContract.Request.ApiV2.BroadcastCommentList.COUNT,
                    String.valueOf(count));
        }

        return request;
    }

    public static ApiRequest<Broadcast> newLikeBroadcastRequest(long broadcastId, boolean like) {
        return new LifeStreamRequest<>(like ? ApiRequest.Method.POST : ApiRequest.Method.DELETE,
                StringUtils.formatUs(ApiContract.Request.ApiV2.LikeBroadcast.URL_FORMAT,
                        broadcastId), new TypeToken<Broadcast>() {
        });
    }

    public static ApiRequest<Broadcast> newRebroadcastBroadcastRequest(long broadcastId,
                                                                       boolean rebroadcast) {
        return new LifeStreamRequest<>(rebroadcast ? ApiRequest.Method.POST
                : ApiRequest.Method.DELETE,
                StringUtils.formatUs(ApiContract.Request.ApiV2.RebroadcastBroadcast.URL_FORMAT,
                        broadcastId), new TypeToken<Broadcast>() {
        });
    }

    public static ApiRequest<List<User>> newBroadcastLikerListRequest(long broadcastId,
                                                                      Integer start,
                                                                      Integer count) {

        ApiRequest<List<User>> request = new LifeStreamRequest<>(ApiRequest.Method.GET,
                StringUtils.formatUs(ApiContract.Request.ApiV2.BroadcastLikerList.URL_FORMAT,
                        broadcastId), new TypeToken<List<User>>() {
        });

        if (start != null) {
            request.addParam(ApiContract.Request.ApiV2.BroadcastLikerList.START,
                    String.valueOf(start));
        }
        if (count != null) {
            request.addParam(ApiContract.Request.ApiV2.BroadcastLikerList.COUNT,
                    String.valueOf(count));
        }

        return request;
    }

    public static ApiRequest<List<User>> newBroadcastRebroadcasterListRequest(long broadcastId,
                                                                              Integer start,
                                                                              Integer count) {

        ApiRequest<List<User>> request = new LifeStreamRequest<>(ApiRequest.Method.GET,
                StringUtils.formatUs(
                        ApiContract.Request.ApiV2.BroadcastRebroadcasterList.URL_FORMAT,
                        broadcastId), new TypeToken<List<User>>() {
        });

        if (start != null) {
            request.addParam(ApiContract.Request.ApiV2.BroadcastRebroadcasterList.START,
                    String.valueOf(start));
        }
        if (count != null) {
            request.addParam(ApiContract.Request.ApiV2.BroadcastRebroadcasterList.COUNT,
                    String.valueOf(count));
        }

        return request;
    }

    public static ApiRequest<Boolean> newDeleteBroadcastCommentRequest(long broadcastId,
                                                                       long commentId) {
        return new LifeStreamRequest<>(ApiRequest.Method.DELETE, StringUtils.formatUs(
                ApiContract.Request.ApiV2.DeleteBroadcastComment.URL_FORMAT, broadcastId,
                commentId), new TypeToken<Boolean>() {
        });
    }

    public static ApiRequest<Comment> newSendBroadcastCommentRequest(long broadcastId,
                                                                     String comment) {
        ApiRequest<Comment> request = new LifeStreamRequest<>(ApiRequest.Method.POST,
                StringUtils.formatUs(ApiContract.Request.ApiV2.SendBroadcastComment.URL_FORMAT,
                        broadcastId), new TypeToken<Comment>() {
        });

        request.addParam(ApiContract.Request.ApiV2.SendBroadcastComment.TEXT, comment);

        return request;
    }

    public static ApiRequest<Broadcast> newDeleteBroadcastRequest(long broadcastId) {
        return new LifeStreamRequest<>(ApiRequest.Method.DELETE,
                StringUtils.formatUs(ApiContract.Request.ApiV2.DeleteBroadcast.URL_FORMAT,
                        broadcastId), new TypeToken<Broadcast>() {
        });
    }

    public static ApiRequest<DiaryList> newDiaryListRequest(String userIdOrUid, Integer start,
                                                            Integer count) {

        ApiRequest<DiaryList> request = new UserIdOrUidFrodoRequest<>(ApiRequest.Method.GET,
                StringUtils.formatUs(ApiContract.Request.Frodo.UserDiaryList.URL_FORMAT,
                        userIdOrUid), new TypeToken<DiaryList>() {
        })
                .withUserIdOrUid(userIdOrUid);

        if (start != null) {
            request.addParam(ApiContract.Request.Frodo.UserDiaryList.START, String.valueOf(start));
        }
        if (count != null) {
            request.addParam(ApiContract.Request.Frodo.UserDiaryList.COUNT, String.valueOf(count));
        }

        return request;
    }

    public static ApiRequest<UserItemList> newUserItemListRequest(String userIdOrUid) {
        return new UserIdOrUidFrodoRequest<>(ApiRequest.Method.GET, StringUtils.formatUs(
                ApiContract.Request.Frodo.UserItemList.URL_FORMAT, userIdOrUid),
                new TypeToken<UserItemList>() {
                })
                .withUserIdOrUid(userIdOrUid);
    }

    public static ApiRequest<ReviewList> newUserReviewListRequest(String userIdOrUid, Integer start,
                                                                  Integer count) {

        ApiRequest<ReviewList> request = new UserIdOrUidFrodoRequest<>(ApiRequest.Method.GET,
                StringUtils.formatUs(ApiContract.Request.Frodo.UserReviewList.URL_FORMAT,
                        userIdOrUid), new TypeToken<ReviewList>() {
        })
                .withUserIdOrUid(userIdOrUid);

        if (start != null) {
            request.addParam(ApiContract.Request.Frodo.UserReviewList.START, String.valueOf(start));
        }
        if (count != null) {
            request.addParam(ApiContract.Request.Frodo.UserReviewList.COUNT, String.valueOf(count));
        }

        return request;
    }

    public static ApiRequest<ReviewList> newItemReviewListRequest(long itemId, Integer start,
                                                                  Integer count) {

        ApiRequest<ReviewList> request = new FrodoRequest<>(ApiRequest.Method.GET,
                StringUtils.formatUs(ApiContract.Request.Frodo.ItemReviewList.URL_FORMAT, itemId),
                new TypeToken<ReviewList>() {
                });

        if (start != null) {
            request.addParam(ApiContract.Request.Frodo.ItemReviewList.START, String.valueOf(start));
        }
        if (count != null) {
            request.addParam(ApiContract.Request.Frodo.ItemReviewList.COUNT, String.valueOf(count));
        }

        return request;
    }

}
