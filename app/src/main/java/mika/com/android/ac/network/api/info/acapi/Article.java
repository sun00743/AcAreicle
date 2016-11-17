/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
package mika.com.android.ac.network.api.info.acapi;

import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 {
 "code":200,
 "data":{
    "article":{
        "content":"
        <p\n  style=\"margin-top: 0.0px;margin-bottom: 0.0px;padding-bottom: 20.0px;font-size: 16.0px;line-height: 27.2px;text-indent: 2.0em;font-style: normal;font-weight: normal;white-space: normal;background-color: rgb(252,251,240);\">
        据俄罗斯卫星新闻9月9日报道 俄罗斯国防部汽车装甲坦克总局局长亚历山大·舍甫琴科在接受“卫星”新闻通讯社采访时表示，新型T-14“阿玛塔”主战坦克的无人驾驶型将于2018年制成，已经在进行相关的研制工作。</p>\n
        <p align=\"center\" style=\"margin-top: 0.0px;margin-bottom: 0.0px;padding-bottom: 20.0px;font-size: 16.0px;line-height: 27.2px;text-indent: 2.0em;font-style: normal;font-weight: normal;white-space: normal;background-color: rgb(252,251,240);\">\n
        <img src=\"http://i.guancha.cn/news/2016/09/10/20160910152616331.jpg\" style=\"margin-right: 0.0px;margin-left: 0.0px;\" /></p>\n
         <p align=\"center\" style=\"margin-top: 0.0px;margin-bottom: 0.0px;padding-bottom: 20.0px;font-size: 16.0px;line-height: 27.2px;text-indent: 2.0em;font-style: normal;font-weight: normal;white-space: normal;background-color: rgb(252,251,240);\">\n
         <span\n    style=\"margin-right: 0.0px;margin-left: 0.0px;color: rgb(102,102,102);\">2016年5月舍甫琴科陪同普京视察俄自造“爱国者”多功能皮卡 拉断了门把手这位就是舍甫琴科</span></p>\n
        <p\n    style=\"margin-top: 0.0px;margin-bottom: 0.0px;padding-bottom: 20.0px;font-size: 16.0px;line-height: 27.2px;text-indent: 2.0em;font-style: normal;font-weight: normal;white-space: normal;background-color: rgb(252,251,240);\">舍甫琴科表示：“研制工作开展两年后将制成阿玛塔的无人驾驶型。该型坦克已经具备前景，<strong\n    style=\"margin-right: 0.0px;margin-left: 0.0px;\">只需解决若干小问题</strong>——<strong\n  style=\"margin-right: 0.0px;margin-left: 0.0px;\">研制根据客观状况解决任务的数字化装置</strong>。另外，阿玛塔所有微电子设备的进口替代问题已经解决。” 据他所说，无人驾驶型阿玛塔坦克的特点在于为无人驾驶自动化装甲车形成“钢筋混凝土般基础的开放式数字架构”。</p>\n<p align=\"center\" style=\"margin-top: 0.0px;margin-bottom: 0.0px;padding-bottom: 20.0px;font-size: 16.0px;line-height: 27.2px;text-indent: 2.0em;font-style: normal;font-weight: normal;white-space: normal;background-color: rgb(252,251,240);\">\n  <img src=\"http://i.guancha.cn/news/2016/09/10/20160910152905400.jpg\" style=\"margin-right: 0.0px;margin-left: 0.0px;\" /></p>\n<p style=\"margin-top: 0.0px;margin-bottom: 0.0px;padding-bottom: 20.0px;font-size: 16.0px;line-height: 27.2px;text-indent: 2.0em;font-style: normal;font-weight: normal;white-space: normal;background-color: rgb(252,251,240);\">此前俄罗斯国防部副部长鲍里索夫曾经在“军队-2016”论坛上表示，俄国防部与乌拉尔机车车辆制造厂签署合约，供应超过100辆T-14“阿尔玛塔”坦克进行试用，首批坦克已经投入作战测试。</p>\n<p style=\"margin-top: 0.0px;margin-bottom: 0.0px;padding-bottom: 20.0px;font-size: 16.0px;line-height: 27.2px;text-indent: 2.0em;font-style: normal;font-weight: normal;white-space: normal;background-color: rgb(252,251,240);\">他说：“已经购买了首批坦克，毕竟乌拉尔机车车辆制造厂不是免费制造。双方签署了100辆以上的试用批量合约，这些坦克已经投入作战测试。”</p>\n<p style=\"margin-top: 0.0px;margin-bottom: 0.0px;padding-bottom: 20.0px;font-size: 16.0px;line-height: 27.2px;text-indent: 2.0em;font-style: normal;font-weight: normal;white-space: normal;background-color: rgb(252,251,240);\">关于阿玛塔坦克现在的进度，舍普琴科表示，首批坦克正在进行野外操作，所有不符合军方要求的缺陷将通告设计局和工厂进行弥补。国家检测将在2017年完成，并随后投入使用。”</p>\n<p style=\"margin-top: 0.0px;margin-bottom: 0.0px;padding-bottom: 20.0px;font-size: 16.0px;line-height: 27.2px;text-indent: 2.0em;font-style: normal;font-weight: normal;white-space: normal;background-color: rgb(252,251,240);\">阿玛塔坦克将不仅仅作为俄罗斯未来的主战坦克，乌拉尔车辆制造厂曾经提议研制基于“阿玛塔”履带通用平台上的“道尔”或“山毛榉”防空系统。类似苏联以重型坦克通用底盘研制一系列装甲战斗车辆的思维，以“阿玛塔”重型履带平台研制的通用化系列化战斗车辆，如重型步兵战车、工程车、重型装甲运兵车、坦克支持战车、侦察与控制车等。</p>\n<p style=\"margin-top: 0.0px;margin-bottom: 0.0px;padding-bottom: 20.0px;font-size: 16.0px;line-height: 27.2px;text-indent: 2.0em;font-style: normal;font-weight: normal;white-space: normal;background-color: rgb(252,251,240);\">美军早有无人驾驶战斗车辆的一系列研究，主要试图以遥控自动化战斗车辆代替执勤人员，减轻在治安战中导致的人员伤亡问题。而至今仍处于测试阶段，离真正装备部队还有相当长的距离。</p>\n<p style=\"margin-top: 0.0px;margin-bottom: 0.0px;padding-bottom: 20.0px;font-size: 16.0px;line-height: 27.2px;text-indent: 2.0em;font-style: normal;font-weight: normal;white-space: normal;background-color: rgb(252,251,240);\">（观察者网综合俄罗斯卫星新闻等报道）</p>\n<p>\n  <br /></p>"
    },
    "channelId":110,
    "contentId":3094325,
    "cover":"http://cdn.aixifan.com/dotnet/20120923/style/image/cover-day.png",
    "description":"俄国防部：无人驾驶“阿玛塔”主战坦克将于2018年制成",
    "display":0,
    "isArticle":1,
    "isComment":true,
    "isRecommend":0,
    "owner":{
        "avatar":"http://cdn.aixifan.com/dotnet/artemis/u/cms/www/201604/17165455ymkvlvm0.jpg",
        "id":499773,
        " name":"シ浅吟夏未央へ"
    },
    "releaseDate":1473498933000,
    "status":2,
    "title":"俄国防部：无人驾驶“阿玛塔”主战坦克将于2018年制成",
    "topLevel":0,
    "updatedAt":1473502082000,
    "viewOnly":0,
    "visit":{
        "comments":10,
        "danmakuSize":0,
        "goldBanana":0,
        "score":0,
        "stows":13,
        "ups":0,
        "views
        ":1505
    }
 },
 "message":"OK"
 }

 *
 * 
 */
public class Article {
    private static String TAG = "Article";
//    private static Pattern emoticonReg = Pattern.compile("");
    private static Pattern imageReg = Pattern.compile("<img.+?src=[\"|'](.+?)[\"|']");
    static Pattern pageReg = Pattern.compile("\\[NextPage\\]([^\\[\\]]+)\\[/NextPage\\]");
    public int channelId;
    public int comments;
    public ArrayList<SubContent> contents;
    public String description;
    public int id;
    public ArrayList<String> imgUrls;
    public boolean isRecommend;
    public long postTime;
    public User poster;
    public int stows;
    public String title;
    public int views;

    private static void findImageUrls(Article paramArticle, SubContent paramSubContent)
    {
        Matcher matcher = imageReg.matcher(paramSubContent.content);
        while (matcher.find()) {
            paramArticle.imgUrls.add(matcher.group(1));
        }
    }

    public static Article newArticle(JSONObject paramJSONObject)
            throws Article.InvalidArticleError
    {
        Article localArticle = new Article();
        localArticle.imgUrls = new ArrayList();
        localArticle.title = paramJSONObject.getString("title");
        localArticle.postTime = paramJSONObject.getLongValue("releaseDate");
        localArticle.id = paramJSONObject.getIntValue("contentId");
        localArticle.description = paramJSONObject.getString("description");
        localArticle.poster = parseUser(paramJSONObject);
        JSONObject localJSONObject = paramJSONObject.getJSONObject("visit");
        localArticle.views = localJSONObject.getIntValue("views");
        localArticle.comments = localJSONObject.getIntValue("comments");
        localArticle.stows = localJSONObject.getIntValue("stows");
        localArticle.contents = new ArrayList();
        parseContentArray(paramJSONObject, localArticle);
        localArticle.channelId = paramJSONObject.getIntValue("channelId");
        return localArticle;
    }

    private static void parseContentArray(JSONObject paramJSONObject, Article paramArticle)
            throws Article.InvalidArticleError
    {
        String content = paramJSONObject.getJSONObject("article").getString("content");
        Object localObject = pageReg.matcher(content);
        int i = 0;
        while ((((Matcher)localObject).find()) && (i < content.length()))
        {
            Log.i(TAG, "Find next page tag: " + ((Matcher)localObject).group());
            int j = ((Matcher)localObject).start();
            SubContent localSubContent = new SubContent();
            localSubContent.subTitle = ((Matcher)localObject).group(1).replaceAll("<span[^>]+>", "").replaceAll("</span>", "");
            localSubContent.content = content.substring(i, j);
            i = ((Matcher)localObject).end();
            validate(localSubContent);
            findImageUrls(paramArticle, localSubContent);
            paramArticle.contents.add(localSubContent);
        }
        if (paramArticle.contents.isEmpty())
        {
            localObject = new SubContent();
            ((SubContent)localObject).content = content;
            ((SubContent)localObject).subTitle = paramArticle.title;
            validate((SubContent)localObject);
            findImageUrls(paramArticle, (SubContent)localObject);
            paramArticle.contents.add((SubContent) localObject);
        }
    }

    private static User parseUser(JSONObject paramJSONObject)
            throws JSONException
    {
        JSONObject owner = paramJSONObject.getJSONObject("owner");
        User localUser = new User();
        localUser.name = owner.getString("name");
        localUser.id = owner.getIntValue("id");
        localUser.avatar = owner.getString("avatar");
        return localUser;
    }

    private static void validate(SubContent paramSubContent)
            throws Article.InvalidArticleError
    {
        if (paramSubContent.content == null) {
            throw new InvalidArticleError();
        }
        if (paramSubContent.content.matches(".*\\[[v|V]ideo\\]\\d+\\[/[v|V]ideo\\].*")) {
            throw new InvalidArticleError();
        }
    }

    public static class InvalidArticleError
            extends VolleyError
    {
        private static final long serialVersionUID = 1111L;

        public InvalidArticleError() {}

        public InvalidArticleError(Throwable paramThrowable)
        {
            super();
        }
    }

    public static class SubContent
    {
        public String content;
        public String subTitle;
    }
}
