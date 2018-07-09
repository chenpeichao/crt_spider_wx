package com.hubpd.uar.common.utils;

import cn.gsdata.index.ApiSdk;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import iims.crt.data.*;
import iims.crt.gsdata.*;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 清博数据接口调用
 *
 * @author cpc
 * @create 2018-06-24 14:53
 **/
public class GsDataApi {

    private static Logger logger = Logger.getLogger(DataApi.class.getName());
    private ApiSdk apiSdk = null;
    public static String gsdata_appID = "mzgvw1O7nYnglPvhjHST";
    public static String gsdata_appKey = "0h3mrJcord72zHOeJY4TnTFqO";
    public static String CustomAPI_wx_url_monitor = "http://open.gsdata.cn/api/custom/customapi/wx_url_content_monitor";
    public static String CustomAPI_wx_content = "http://open.gsdata.cn/api/wx/wxapi/wx_content";
    public static String CustomAPI_add_weixin2group = "http://open.gsdata.cn/api/wx/wxapi/add_wx_to_groupMonitor";
    public static String CustomAPI_addname_weixin2group = "http://open.gsdata.cn/api/wx/wxapi/nickname_one";
    public static String CustomAPI_del_weixin2group = "http://open.gsdata.cn/api/wx/wxapi/del_wx_from_groupMonitor";
    public static String CustomAPI_add_weixin2group_byurl = "http://open.gsdata.cn/api/wx/wxapi/add_wx_to_group_by_url";
    public static String CustomAPI_add_groupandmonitor = "http://open.gsdata.cn/api/custom/customapi/add_wx_to_group_monitor";
    public static String API_PDMI_KEYWORD = "http://open.gsdata.cn/api/pdmi/pdmi_keyword";
    public static String API_WX_NUMS_MONITOR = "http://open.gsdata.cn/api/wx/wxapi/wx_nums_monitor";
    public static String CUSTOMAPI_CONTENT_KEYWORD_SEARCH = "http://open.gsdata.cn/api/wx/opensearchapi/content_keyword_search";
    public static String CUSTOMAPI_CONTENT = "http://open.gsdata.cn/api/wx/wxapi/wx_content2";
    public static int gsdata_groupID = 27657;
    public static int MaxRows_Request = 10;
    public static int totalArticles = 0;
    public static int totalArticlesBeGet = 0;
    public static int totalUpdatedArticles = 0;
    public static int newArticlesBeSaved = 0;
    private static GsDataApi apiService = null;

    public static synchronized GsDataApi getInstance() {
        if (apiService == null) {
            synchronized (GsDataApi.class) {
                if (apiService == null) {
                    apiService = new GsDataApi();
                }
            }
        }

        return apiService;
    }

    private GsDataApi() {
        logger.info("initializing the GSDATA API.");
        this.apiSdk = ApiSdk.getApiSdk(gsdata_appID, gsdata_appKey);
    }

    public ArrayList<OriginNews> parseArticleList(String strJson) {
        ArrayList list = new ArrayList();

        try {
            JSONObject e = new JSONObject(strJson);
            int returnCode = e.getInt("returnCode");
            String returnMsg = e.getString("returnMsg");
            logger.info("ReturnCode=" + returnCode + "\t" + returnMsg);
            JSONObject dataObject = e.getJSONObject("returnData");
            totalArticles = dataObject.getInt("total");
            JSONArray articleListArray = dataObject.getJSONArray("rows");

            for (int i = 0; i < articleListArray.length(); ++i) {
                JSONObject ao = (JSONObject) articleListArray.get(i);
                OriginNews oriArticle = new OriginNews();
                Website media = new Website();
                media.setWebName(ao.getString("name"));
                media.setReservedTag(ao.getString("wx_name"));
                ScanPanel scanPanel = new ScanPanel();
                MediaChannel channel = new MediaChannel();
                channel.setMedia(media);
                channel.setScanPanel(scanPanel);
                oriArticle.setChannel(channel);
                oriArticle.setSubject(ao.getString("title"));
                oriArticle.setUrl(ao.getString("url"));
                oriArticle.setPostTime(ao.getString("posttime"));
                FeedbackStat feedbackStat = new FeedbackStat();
                feedbackStat.setReadCount(ao.getInt("readnum"));
                feedbackStat.setAgreeCount(ao.getInt("likenum"));
                oriArticle.setFeedbackStat(feedbackStat);
                oriArticle.setContent(ao.getString("content"));
                logger.info(oriArticle.getFeedbackStat().getAgreeCount() + "/" + oriArticle.getFeedbackStat().getReadCount() + "\t" + oriArticle.getChannel().getMedia().getReservedTag() + "\t" + oriArticle.getChannel().getMedia().getWebName() + "\t" + oriArticle.getSubject());
                list.add(oriArticle);
            }
        } catch (Exception var15) {
            logger.error("return json:" + strJson + "\n 获取代码出错:", var15);
        }

        return list;
    }

    public String getResponseData(int pageNo) {
        String ret_json = null;

        try {
            HashMap e = new HashMap();
            SimpleDateFormat formats = new SimpleDateFormat("yyyy-MM-dd");
            String yesterday = formats.format(Long.valueOf((new Date()).getTime() - 86400000L));
            e.put("beginDate", yesterday);
            e.put("group_id", Integer.valueOf(gsdata_groupID));
            e.put("page", Integer.valueOf(pageNo));
            ret_json = this.apiSdk.callInterFace(CustomAPI_wx_url_monitor, e);
            logger.info(ret_json);
        } catch (Exception var6) {
            logger.error("获取代码出错:", var6);
        }

        return ret_json;
    }

    public String getResponseData(int page, String nicknameIds) {
        String ret_json = null;

        try {
            HashMap e = new HashMap();
            SimpleDateFormat formats = new SimpleDateFormat("yyyy-MM-dd");
            String yesterday = formats.format(Long.valueOf((new Date()).getTime() - 86400000L));
            e.put("beginDate", yesterday);
            e.put("nickname_ids", nicknameIds);
            e.put("page", Integer.valueOf(page));
            ret_json = this.apiSdk.callInterFace(CustomAPI_wx_url_monitor, e);
            logger.info(ret_json);
        } catch (Exception var7) {
            logger.error("获取代码出错:", var7);
        }

        return ret_json;
    }

    public String getResponseData(int page, String nicknameIds, String beginDate, String endDate) {
        String ret_json = null;

        try {
            HashMap e = new HashMap();
            e.put("beginDate", beginDate);
            e.put("endDate", endDate);
            e.put("nickname_ids", nicknameIds);
            e.put("page", Integer.valueOf(page));
            ret_json = this.apiSdk.callInterFace(CustomAPI_wx_url_monitor, e);
            logger.info(ret_json);
        } catch (Exception var7) {
            logger.error("获取代码出错:", var7);
        }

        return ret_json;
    }

    public List<WxUrlMonitorResult> getResponseData(int page, String nicknameIds, Date beginDate, Date endDate) {
        ArrayList results = null;

        try {
            HashMap e = new HashMap();
            if (beginDate == null) {
                beginDate = new Date();
            }

            e.put("beginDate", DateFormatUtils.format(beginDate, "yyyy-MM-dd"));
            if (endDate == null) {
                endDate = new Date();
            }

            e.put("endDate", DateFormatUtils.format(endDate, "yyyy-MM-dd"));
            e.put("nickname_ids", nicknameIds);
            e.put("page", Integer.valueOf(page));
            String ret_json = this.apiSdk.callInterFace(CustomAPI_wx_url_monitor, e);
            logger.info(MessageFormat.format("params is {0}, \n response data is {1}", new Object[]{(new Gson()).toJson(e), ret_json}));
            JSONObject jo = new JSONObject(ret_json);
            JSONObject dataObject = jo.getJSONObject("returnData");
            JSONArray articleListArray = dataObject.getJSONArray("rows");
            if (articleListArray.length() > 0) {
                results = new ArrayList();
                WxUrlMonitorResult record = null;

                for (int i = 0; i < articleListArray.length(); ++i) {
                    JSONObject returnData = (JSONObject) articleListArray.get(i);
                    record = new WxUrlMonitorResult();
                    if (returnData.has("name")) {
                        record.setName(returnData.getString("name"));
                    }

                    if (returnData.has("wx_name")) {
                        record.setWxName(returnData.getString("wx_name"));
                    }

                    if (returnData.has("nickname_id")) {
                        record.setNickNameId(returnData.getInt("nickname_id"));
                    }

                    if (returnData.has("posttime")) {
                        record.setPostTime(returnData.getString("posttime"));
                    }

                    if (returnData.has("title")) {
                        record.setTitle(returnData.getString("title"));
                    }

                    if (returnData.has("content")) {
                        record.setContent(returnData.getString("content"));
                    }

                    if (returnData.has("url")) {
                        record.setUrl(returnData.getString("url"));
                    }

                    if (returnData.has("add_time")) {
                        record.setAddTime(returnData.getString("add_time"));
                    }

                    if (returnData.has("monitor_time")) {
                        record.setMonitorTime(returnData.getString("monitor_time"));
                    }

                    if (returnData.has("readnum")) {
                        record.setReadNum(returnData.getInt("readnum"));
                    }

                    if (returnData.has("likenum")) {
                        record.setLikeNum(returnData.getInt("likenum"));
                    }

                    if (returnData.has("top")) {
                        record.setLikeNum(returnData.getInt("top"));
                    }

                    if (returnData.has("ispush")) {
                        record.setIsPush(returnData.getInt("ispush"));
                    }

                    if (returnData.has("picurl")) {
                        record.setPicUrl(returnData.getString("picurl"));
                    }

                    if (returnData.has("sourceurl")) {
                        record.setSourceUrl(returnData.getString("sourceurl"));
                    }

                    if (returnData.has("author")) {
                        record.setAuthor(returnData.getString("author"));
                    }

                    if (returnData.has("desc")) {
                        record.setDesc(returnData.getString("desc"));
                    }

                    if (returnData.has("videourl")) {
                        record.setVideoUrl(returnData.getString("videourl"));
                    }

                    if (returnData.has("imgsurl")) {
                        record.setImgsUrl(returnData.getString("imgsurl"));
                    }

                    results.add(record);
                }
            }
        } catch (Exception var14) {
            logger.error("获取代码出错:", var14);
        }

        return results;
    }

    public String getWeixinContent(String gsWeixinUrl) {
        String content = null;

        try {
            HashMap e = new HashMap();
            e.put("url", gsWeixinUrl);
            String ret_json = this.apiSdk.callInterFace(CustomAPI_wx_content, e);
            if (ret_json == null) {
                return null;
            }

            logger.debug(ret_json);
            JSONObject jo = new JSONObject(ret_json);
            JSONArray returnData = jo.getJSONArray("returnData");
            if (returnData == null || returnData.length() == 0) {
                return null;
            }

            JSONObject ao = (JSONObject) returnData.get(0);
            content = ao.getString("content");
            logger.debug(gsWeixinUrl + "\t" + content);
        } catch (Exception var8) {
            logger.error("获取代码出错:", var8);
        }

        return content;
    }

    public ResNickNameOneResult getNickNameOne(String wxName) {
        ResNickNameOneResult resNickNameOneResult = null;

        try {
            HashMap e = new HashMap();
            e.put("wx_name", wxName);
            String ret_json = this.apiSdk.callInterFace(CustomAPI_addname_weixin2group, e);
            if (ret_json == null) {
                return null;
            }

            logger.debug(ret_json);
            JSONObject jo = new JSONObject(ret_json);
            if (jo.has("returnData")) {
                JSONObject returnData = jo.getJSONObject("returnData");
                if (returnData == null || returnData.length() == 0) {
                    return null;
                }

                resNickNameOneResult = new ResNickNameOneResult();
                if (returnData.has("id")) {
                    resNickNameOneResult.setId(returnData.getInt("id"));
                }

                if (returnData.has("wx_nickname")) {
                    resNickNameOneResult.setWxNickname(returnData.getString("wx_nickname"));
                }

                if (returnData.has("wx_openid")) {
                    resNickNameOneResult.setWxOpenid(returnData.getString("wx_openid"));
                }

                if (returnData.has("wx_biz")) {
                    resNickNameOneResult.setWxBiz(returnData.getString("wx_biz"));
                }

                if (returnData.has("wx_type")) {
                    resNickNameOneResult.setWxType(returnData.getInt("wx_type"));
                }

                if (returnData.has("wx_name")) {
                    resNickNameOneResult.setWxName(returnData.getString("wx_name"));
                }

                if (returnData.has("wx_qrcode")) {
                    resNickNameOneResult.setWxQrcode(returnData.getString("wx_qrcode"));
                }

                if (returnData.has("wx_note")) {
                    resNickNameOneResult.setWxNote(returnData.getString("wx_note"));
                }

                if (returnData.has("wx_vip")) {
                    resNickNameOneResult.setWxVip(returnData.getString("wx_vip"));
                }

                if (returnData.has("wx_vip_note")) {
                    resNickNameOneResult.setWxVipNote(returnData.getString("wx_vip_note"));
                }

                if (returnData.has("wx_country")) {
                    resNickNameOneResult.setWxCountry(returnData.getString("wx_country"));
                }

                if (returnData.has("wx_country")) {
                    resNickNameOneResult.setWxProvince(returnData.getString("wx_province"));
                }

                if (returnData.has("wx_city")) {
                    resNickNameOneResult.setWxCity(returnData.getString("wx_city"));
                }

                if (returnData.has("wx_title")) {
                    resNickNameOneResult.setWxTitle(returnData.getString("wx_title"));
                }

                if (returnData.has("wx_url")) {
                    resNickNameOneResult.setWxUrl(returnData.getString("wx_url"));
                }

                if (returnData.has("wx_url_posttime")) {
                    resNickNameOneResult.setWxUrlPosttime(returnData.getString("wx_url_posttime"));
                }

                if (returnData.has("uid")) {
                    resNickNameOneResult.setUid(returnData.getInt("uid"));
                }

                if (returnData.has("time_start")) {
                    resNickNameOneResult.setTimeSart(returnData.getString("time_start"));
                }

                if (returnData.has("time_end")) {
                    resNickNameOneResult.setTimeEnd(returnData.getString("time_end"));
                }

                if (returnData.has("time_stop")) {
                    resNickNameOneResult.setTimeStop(returnData.getString("time_stop"));
                }

                if (returnData.has("add_time")) {
                    resNickNameOneResult.setAddTime(returnData.getString("add_time"));
                }

                if (returnData.has("status")) {
                    resNickNameOneResult.setStatus(returnData.getInt("status"));
                }

                if (returnData.has("isenable")) {
                    resNickNameOneResult.setIsEnable(returnData.getInt("isenable"));
                }

                if (returnData.has("update_status")) {
                    resNickNameOneResult.setUpdateStatus(returnData.getInt("update_status"));
                }

                logger.debug("\t" + resNickNameOneResult);
            } else {
                logger.error(MessageFormat.format("wexin code {0} get request error,return data is \n ", new Object[]{wxName}) + ret_json);
            }
        } catch (Exception var7) {
            logger.error("get data error:", var7);
        }

        return resNickNameOneResult;
    }

    public boolean addWeixin2Group(String weixinNames, String originUrl) {
        String ret_json = null;

        try {
            ApiSdk e = ApiSdk.getApiSdk(gsdata_appID, gsdata_appKey);
            HashMap returnCode = new HashMap();
            returnCode.put("url", originUrl);
            ret_json = e.callInterFace(CustomAPI_add_groupandmonitor, returnCode);
        } catch (Exception var7) {
            logger.error("获取代码出错:", var7);
        }

        if (ret_json != null) {
            try {
                JSONObject e1 = new JSONObject(ret_json);
                int returnCode1 = e1.getInt("returnCode");
                logger.info("\n\nAdd to gsdata WeixinHao. ReturnCode=" + returnCode1 + "\t" + ret_json);
                if (returnCode1 == 1001) {
                    return true;
                }
            } catch (Exception var6) {
                logger.error("获取代码出错:", var6);
            }
        }

        return false;
    }

    public GroupMonitorAddResult addWeixin2Group(String originUrl) {
        GroupMonitorAddResult result = null;

        try {
            ApiSdk e = ApiSdk.getApiSdk(gsdata_appID, gsdata_appKey);
            HashMap requestParamsMap = new HashMap();
            requestParamsMap.put("url", originUrl);
            String ret_json = e.callInterFace(CustomAPI_add_groupandmonitor, requestParamsMap);
            JSONObject jsonObject = new JSONObject(ret_json);
            JSONObject returnData = jsonObject.getJSONObject("returnData");
            logger.info("\n\nAdd to gsdata WeixinHao.response:" + ret_json);
            result = new GroupMonitorAddResult();
            if (returnData.has("errmsg")) {
                result.setErrmsg(returnData.getString("errmsg"));
            }

            if (returnData.has("errcode")) {
                result.setErrcode(returnData.getInt("errcode"));
            }

            if (returnData.has("wx_nickname")) {
                result.setWxNickname(returnData.getString("wx_nickname"));
            }

            if (returnData.has("wx_name")) {
                result.setWxName(returnData.getString("wx_name"));
            }

            if (returnData.has("biz")) {
                result.setBiz(returnData.getString("biz"));
            }
        } catch (Exception var8) {
            logger.error("获取代码出错:", var8);
        }

        return result;
    }

    public boolean delWeixin2Group(String weixinNames) {
        String ret_json = null;

        try {
            ApiSdk e = ApiSdk.getApiSdk(gsdata_appID, gsdata_appKey);
            HashMap returnCode = new HashMap();
            returnCode.put("wx_name", weixinNames);
            ret_json = e.callInterFace(CustomAPI_addname_weixin2group, returnCode);
            JsonParser json = new JsonParser();
            String id = json.parse(ret_json).getAsJsonObject().getAsJsonObject("returnData").get("id").getAsString();
            returnCode = new HashMap();
            returnCode.put("wxJson", "[ {\"nickname_id\":\"" + id + "\"}]");
            ret_json = e.callInterFace(CustomAPI_del_weixin2group, returnCode);
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        if (ret_json != null) {
            try {
                JSONObject e1 = new JSONObject(ret_json);
                int returnCode1 = e1.getInt("returnCode");
                logger.info("\n\nAdd to gsdata WeixinHao. ReturnCode=" + returnCode1 + "\t" + ret_json);
                if (returnCode1 == 1001) {
                    return true;
                }
            } catch (Exception var7) {
                logger.error("获取代码出错:", var7);
            }
        }

        return false;
    }

    public static String getContentKeywordSearch(String keyword, int start) {
        try {
            ApiSdk e = ApiSdk.getApiSdk(gsdata_appID, gsdata_appKey);
            HashMap requestParamsMap = new HashMap();
            requestParamsMap.put("keyword", keyword);
            requestParamsMap.put("start", Integer.valueOf(start));
            requestParamsMap.put("startdate", DateFormatUtils.format(org.apache.commons.lang.time.DateUtils.addDays(new Date(), -2), "yyyy-MM-dd"));
            requestParamsMap.put("enddate", DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
            requestParamsMap.put("sortname", "readnum");
            requestParamsMap.put("sort", "desc");
            String jsonResponeData = e.callInterFace(CUSTOMAPI_CONTENT_KEYWORD_SEARCH, requestParamsMap);
            logger.debug(CUSTOMAPI_CONTENT_KEYWORD_SEARCH + " params:" + requestParamsMap + " response:" + jsonResponeData);
            return jsonResponeData;
        } catch (Exception var5) {
            logger.error(CUSTOMAPI_CONTENT_KEYWORD_SEARCH + "获取代码出错:", var5);
            return null;
        }
    }

    public static String getWeixinContentByUrls(List<String> contentUrlList) {
        String result = null;
        HashMap requestParamsMap = new HashMap();

        try {
            ApiSdk ex = ApiSdk.getApiSdk(gsdata_appID, gsdata_appKey);
            requestParamsMap.put("url", StringUtils.join(contentUrlList, ","));
            result = ex.callInterFace(CUSTOMAPI_CONTENT, requestParamsMap);
            logger.info("url:" + requestParamsMap + " api:" + CUSTOMAPI_CONTENT + " response json:" + result);
        } catch (Exception var4) {
            logger.error(CUSTOMAPI_CONTENT + "获取代码出错:", var4);
        }

        return result;
    }

    public static void main(String[] args) {
        GsDataApi spider = new GsDataApi();
        logger.info("iWeixin Starts at :" + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
        ArrayList articleList = new ArrayList();
        int pageNo = 2;

        while (true) {
            String articlePage = spider.getResponseData(pageNo);
            ArrayList list = null;
            if (articlePage != null) {
                logger.info("articlePage=" + articlePage);
                list = spider.parseArticleList(articlePage);
            }

            if (list != null) {
                articleList.addAll(list);
            }

            if (list == null || list.size() < MaxRows_Request) {
                logger.info("Null or less than 10 items, " + pageNo + " should be the last page. break.");
                totalArticlesBeGet = articleList.size();
                if (articleList.size() == 0) {
                    logger.fatal("NO ARTICLES BE FOUND FROM THE API. Return.");
                    return;
                } else {
                    return;
                }
            }

            ++pageNo;

            try {
                Thread.sleep(5000L);
            } catch (Exception var7) {
                ;
            }
        }
    }

    public PdmiKeyWordAddResult addPdmiKeyWord(String key, String keyword, int expiryNum) {
        String jsonResponeData = "";
        HashMap requestParamsMap = new HashMap();

        try {
            requestParamsMap.put("keyword", keyword);
            requestParamsMap.put("key", key);
            requestParamsMap.put("expiry_num", Integer.valueOf(expiryNum));
            requestParamsMap.put("opt_type", Integer.valueOf(1));
            jsonResponeData = this.searchRealKeyWord(requestParamsMap);
            if (StringUtils.isNotBlank(jsonResponeData)) {
                JSONObject e = new JSONObject(jsonResponeData);
                JSONObject returnData = e.getJSONObject("returnData");
                PdmiKeyWordAddResult result = new PdmiKeyWordAddResult();
                if (returnData.has("key")) {
                    result.setKey(returnData.getString("key"));
                }

                if (returnData.has("keyword")) {
                    result.setKeyWord(returnData.getString("keyword"));
                }

                if (returnData.has("starttime")) {
                    result.setStartTime(returnData.getLong("starttime"));
                }

                if (returnData.has("endtime")) {
                    result.setEndTime(returnData.getLong("endtime"));
                }

                if (returnData.has("create_time")) {
                    result.setCreateTime(returnData.getLong("create_time"));
                }

                logger.debug(API_PDMI_KEYWORD + " params:" + requestParamsMap + "\n response:" + jsonResponeData);
                return result;
            }

            logger.info(API_PDMI_KEYWORD + " params:" + requestParamsMap + "\n response is null:" + jsonResponeData);
        } catch (Exception var9) {
            logger.error(API_PDMI_KEYWORD + "  参数为:" + (new Gson()).toJson(requestParamsMap) + "\n response:" + jsonResponeData + "\n获取代码出错:", var9);
        }

        return null;
    }

    public PdmiKeyWordGetResult getPdmiKeyWordList(String key, int pageIndex, int pageSize) {
        try {
            HashMap e = new HashMap();
            if (StringUtils.isNotBlank(key)) {
                e.put("key", key);
            }

            e.put("opt_type", Integer.valueOf(2));
            e.put("page_index", Integer.valueOf(pageIndex));
            if (pageSize <= 0 || pageSize > 50) {
                pageSize = 50;
            }

            e.put("page_size", Integer.valueOf(pageSize));
            String jsonResponeData = this.searchRealKeyWord(e);
            if (StringUtils.isNotBlank(jsonResponeData)) {
                JSONObject jsonObject = new JSONObject(jsonResponeData);
                JSONArray returnData = jsonObject.getJSONArray("returnData");
                if (returnData.length() > 0) {
                    PdmiKeyWordGetResult result = new PdmiKeyWordGetResult();
                    ArrayList records = new ArrayList();
                    PdmiKeyWordAddResult record = null;

                    for (int i = 0; i < returnData.length(); ++i) {
                        JSONObject item = (JSONObject) returnData.get(i);
                        record = new PdmiKeyWordAddResult();
                        if (item.has("key")) {
                            record.setKey(item.getString("key"));
                        }

                        if (item.has("keyword")) {
                            record.setKeyWord(item.getString("keyword"));
                        }

                        if (item.has("starttime")) {
                            record.setStartTime(item.getLong("starttime"));
                        }

                        if (item.has("endtime")) {
                            record.setEndTime(item.getLong("endtime"));
                        }

                        if (item.has("create_time")) {
                            record.setCreateTime(item.getLong("create_time"));
                        }

                        records.add(record);
                    }

                    result.setRecords(records);
                    return result;
                }
            }

            logger.debug(API_PDMI_KEYWORD + " params:" + e + " response:" + jsonResponeData);
        } catch (Exception var13) {
            logger.error(API_PDMI_KEYWORD + "获取代码出错:", var13);
        }

        return null;
    }

    public PdmiKeyWordGetResult getPdmiKeyWordList(String key, int pageIndex, int pageSize, String searchDay) {
        try {
            HashMap e = new HashMap();
            if (StringUtils.isNotBlank(key)) {
                e.put("key", key);
            }

            e.put("opt_type", Integer.valueOf(2));
            e.put("page_index", Integer.valueOf(pageIndex));
            if (pageSize <= 0 || pageSize > 50) {
                pageSize = 50;
            }

            e.put("page_size", Integer.valueOf(pageSize));
            long datetime = 0L;
            if (StringUtils.isBlank(searchDay)) {
                datetime = org.apache.commons.lang.time.DateUtils.parseDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd") + " 00:00:00", new String[]{"yyyy-MM-dd HH:mm:ss"}).getTime();
            } else {
                datetime = org.apache.commons.lang.time.DateUtils.parseDate(searchDay + " 00:00:00", new String[]{"yyyy-MM-dd HH:mm:ss"}).getTime();
            }

            e.put("datetime", String.valueOf(datetime));
            String jsonResponeData = this.searchRealKeyWord(e);
            if (StringUtils.isNotBlank(jsonResponeData)) {
                JSONObject jsonObject = new JSONObject(jsonResponeData);
                JSONArray returnData = jsonObject.getJSONArray("returnData");
                if (returnData.length() > 0) {
                    PdmiKeyWordGetResult result = new PdmiKeyWordGetResult();
                    ArrayList records = new ArrayList();
                    PdmiKeyWordAddResult record = null;

                    for (int i = 0; i < returnData.length(); ++i) {
                        JSONObject item = (JSONObject) returnData.get(i);
                        record = new PdmiKeyWordAddResult();
                        if (item.has("key")) {
                            record.setKey(item.getString("key"));
                        }

                        if (item.has("keyword")) {
                            record.setKeyWord(item.getString("keyword"));
                        }

                        if (item.has("starttime")) {
                            record.setStartTime(item.getLong("starttime"));
                        }

                        if (item.has("endtime")) {
                            record.setEndTime(item.getLong("endtime"));
                        }

                        if (item.has("create_time")) {
                            record.setCreateTime(item.getLong("create_time"));
                        }

                        records.add(record);
                    }

                    result.setRecords(records);
                    return result;
                }
            }

            logger.debug(API_PDMI_KEYWORD + " params:" + e + " response:" + jsonResponeData);
        } catch (Exception var16) {
            logger.error(API_PDMI_KEYWORD + "获取代码出错:", var16);
        }

        return null;
    }

    public String deletePdmiKeyWord(String key) {
        try {
            HashMap e = new HashMap();
            e.put("key", key);
            e.put("opt_type", Integer.valueOf(3));
            String jsonResponeData = this.searchRealKeyWord(e);
            if (StringUtils.isNotBlank(jsonResponeData)) {
                JSONObject jsonObject = new JSONObject(jsonResponeData);
                JSONObject returnData = jsonObject.getJSONObject("returnData");
                if (returnData.has("status")) {
                    return returnData.getString("status");
                }
            }

            logger.debug(API_PDMI_KEYWORD + " params:" + e + " response:" + jsonResponeData);
        } catch (Exception var6) {
            logger.error(API_PDMI_KEYWORD + "获取代码出错:", var6);
        }

        return null;
    }

    private String searchRealKeyWord(Map<String, Object> requestParamsMap) {
        String jsonResponeData = null;

        try {
            ApiSdk e = ApiSdk.getApiSdk(gsdata_appID, gsdata_appKey);
            jsonResponeData = e.callInterFace(API_PDMI_KEYWORD, requestParamsMap);
            logger.debug(API_PDMI_KEYWORD + " params:" + requestParamsMap + " response:" + jsonResponeData);
        } catch (Exception var4) {
            logger.error(API_PDMI_KEYWORD + "获取代码出错:", var4);
        }

        return jsonResponeData;
    }

    public String getInfoByWxname(String wxname) {
        ApiSdk dataApi = ApiSdk.getApiSdk(gsdata_appID, gsdata_appKey);
        HashMap requestParamsMap = new HashMap();
        requestParamsMap.put("wx_name", wxname);
        String ret_json = dataApi.callInterFace(CustomAPI_addname_weixin2group, requestParamsMap);
        return ret_json;
    }

    public WxNumsMonitorResult getWxNumsMonitorData(String url) {
        WxNumsMonitorResult result = null;
        String jsonResponeData = null;

        try {
            ApiSdk e = ApiSdk.getApiSdk(gsdata_appID, gsdata_appKey);
            HashMap requestParamsMap = new HashMap();
            requestParamsMap.put("url", url);
            jsonResponeData = e.callInterFace(API_WX_NUMS_MONITOR, requestParamsMap);
            if (StringUtils.isNotBlank(jsonResponeData)) {
                JSONObject jsonObject = new JSONObject(jsonResponeData);
                JSONObject returnData = jsonObject.getJSONObject("returnData");
                if (returnData.length() > 0) {
                    result = new WxNumsMonitorResult();
                    if (returnData.has("status")) {
                        result.setStatus(returnData.getInt("status"));
                    }

                    if (returnData.has("likenum")) {
                        result.setLikeNum(returnData.getInt("likenum"));
                    }

                    if (returnData.has("real_read_num")) {
                        result.setRealReadNum(returnData.getInt("real_read_num"));
                    }

                    if (returnData.has("readnum")) {
                        result.setReadNum(returnData.getInt("readnum"));
                    }

                    return result;
                }
            }

            logger.debug(API_WX_NUMS_MONITOR + " params:" + requestParamsMap + " response:" + jsonResponeData);
        } catch (Exception var8) {
            logger.error(API_WX_NUMS_MONITOR + "response data:" + jsonResponeData + "获取代码出错:", var8);
            result = null;
        }

        return result;
    }

}
