package com.hubpd.uar.job;

import com.hubpd.uar.domain.CbWxContent;
import com.hubpd.uar.domain.CbWxList;
import com.hubpd.uar.service.CbWxContentService;
import com.hubpd.uar.service.CbWxListService;
import iims.crt.gsdata.DataApi;
import iims.crt.gsdata.GroupMonitorAddResult;
import iims.crt.gsdata.ResNickNameOneResult;
import iims.crt.gsdata.WxUrlMonitorResult;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 每天定点定时回溯前一天的公众号全量数据
 *
 * @author cpc
 * @create 2018-08-23 20:44
 **/
@Component
public class CrtWXSpiderOneDayBeforeJob {
    private static final Logger logger = LoggerFactory.getLogger(CrtWXSpiderOneDayBeforeJob.class);

    @Autowired
    private CbWxListService cbWxListService;
    @Autowired
    private CbWxContentService cbWxContentService;

    /**
     * 每天10点30分，定时回溯当天整天数据
     */
//    @Scheduled(fixedRate = 1000*60*50)
    @Scheduled(cron = "0 30 22 * * ?")
    public void getOneDayBeforeArticleInfo() {
        List<CbWxList> gsDataCbWxListList = cbWxListService.findAll(null, 0);
        for (CbWxList cbWxList : gsDataCbWxListList) {
            String gsdataNickNameId = null;   //定义清博识别的微信公众号标识
            Date backDay = new Date();
            //用于日期流转变化的日期标识
            List<CbWxContent> cbWxContentList = new ArrayList<CbWxContent>();
            //1、获取查询的微信id在清博库中的nickname_id
            gsdataNickNameId = getGsdataNickNameId(cbWxList);
            if (StringUtils.isBlank(gsdataNickNameId)) {
                logger.error("清博接口对于微信号【" + cbWxList.getNicknameId() + ":" + cbWxList.getNickname() + "】的清博nicknameId获取失败");
                continue;
            }
            int pageNo = 1;
            while (true) {
                List<WxUrlMonitorResult> results = new ArrayList<WxUrlMonitorResult>();
                // 获取公众号文章数据
                try {
                    results = DataApi.getInstance().getResponseData(pageNo,
                            gsdataNickNameId,
                            backDay,
                            backDay);
                } catch (Exception e) {
                    logger.error("公众号【" + cbWxList.getNickname() + "---" + cbWxList.getNicknameId() + "】" + new SimpleDateFormat("yyyy-MM-dd").format(backDay) + "10:30的数据回溯任务抓取异常！！", e);
                }

                // 获取结果
                if (results != null && results.size() > 0) {
                    for (WxUrlMonitorResult wxUrlMonitorResult : results) {
                        CbWxContent newEntity = new CbWxContent();
                        newEntity.setName(wxUrlMonitorResult.getName());        //公众号名称
                        newEntity.setWxName(wxUrlMonitorResult.getWxName());    //公众号账号
                        newEntity.setNicknameId(wxUrlMonitorResult.getNickNameId());    //公众号清博库中nickname_id
                        newEntity.setPosttime(wxUrlMonitorResult.getPostTime());    //yyyy-MM-dd HH:mm:ss
                        newEntity.setTitle(wxUrlMonitorResult.getTitle());
                        newEntity.setContentPureWord(wxUrlMonitorResult.getContent());
                        newEntity.setUrl(wxUrlMonitorResult.getUrl());
                        newEntity.setMonitorTime(wxUrlMonitorResult.getMonitorTime());
                        newEntity.setReadnum(wxUrlMonitorResult.getReadNum());
                        newEntity.setLikenum(wxUrlMonitorResult.getLikeNum());
                        newEntity.setTop(wxUrlMonitorResult.getTop());
                        newEntity.setIspush(wxUrlMonitorResult.getIsPush());
                        newEntity.setPicurl(wxUrlMonitorResult.getPicUrl());
                        newEntity.setSourceurl(wxUrlMonitorResult.getSourceUrl());
                        newEntity.setAuthor(wxUrlMonitorResult.getAuthor());
                        newEntity.setSummary(wxUrlMonitorResult.getDesc());
                        newEntity.setVideourl(wxUrlMonitorResult.getVideoUrl());
                        newEntity.setImgsurl(wxUrlMonitorResult.getImgsUrl());

                        try {
                            //从数据库中查询文章信息是否存在
                            CbWxContent result = cbWxContentService.findOneByUrl(newEntity.getUrl());
                            if (result != null) {
                                continue;
                            } else {
                                //用于根据文章url获取文章的正文html格式
                                List<String> urlList = new ArrayList<String>();
                                urlList.add(wxUrlMonitorResult.getUrl());
                                String str = DataApi.getInstance().getWeixinContentByUrls(urlList);
                                JSONObject jsonObject = new JSONObject(str);
                                JSONArray articleListArray = jsonObject.getJSONArray("returnData");
                                if (articleListArray.length() > 0) {
                                    results = new ArrayList();

                                    for (int i = 0; i < articleListArray.length(); ++i) {
                                        JSONObject returnData = (JSONObject) articleListArray.get(i);
                                        if (returnData.has("content")) {
                                            newEntity.setContent(returnData.getString("content"));
                                        }
                                    }
                                }
                                cbWxContentList.add(newEntity);
                            }
                        } catch (JSONException e) {
                            logger.error("文章html格式获取失败！【" + wxUrlMonitorResult.getUrl() + "】：", e);
                        }

                    }
                    // 如果没有内容了，直接跳出
                    if (results.size() < DataApi.MaxRows_Request) {
                        break;
                    }
                } else {
                    break;
                }
                pageNo++;
            }

            //每个公众号的文章批量保存
            if (cbWxContentList.size() > 0) {
                cbWxContentService.save(cbWxContentList);
                logger.info("公众号【" + cbWxList.getNickname() + "(" + cbWxList.getNicknameId() + ")】在【" + com.hubpd.uar.common.utils.DateUtils.parseDate2StringByPattern(backDay, "yyyy-MM-dd") + "22:30回溯抓取了【" + cbWxContentList.size() + "】篇文章");
            } else {
                logger.info("公众号【" + cbWxList.getNickname() + "(" + cbWxList.getNicknameId() + ")】在【" + com.hubpd.uar.common.utils.DateUtils.parseDate2StringByPattern(backDay, "yyyy-MM-dd") + "22:30回溯未抓取到新文章！");
            }
        }
    }

    /**
     * 根据entity的信息获取清博的nickname
     *
     * @param cbWxList 单个公众号实体信息
     * @return 返回gsdataNameId
     */
    private String getGsdataNickNameId(CbWxList cbWxList) {
        String gsdataNickNameId = null;
        try {
            //1.1、首先从数据库中获取清博中存储的公众号id
            if (StringUtils.isBlank(cbWxList.getGsdataNicknameId())) {
                //不存在
                //1.2、判断数据库中存储的公众号信息以及url信息是否存在
                if (StringUtils.isNotBlank(cbWxList.getNicknameId()) && StringUtils.isNotBlank(cbWxList.getNewsUrl())) {
                    //信息正常
                    //1.3、将此公众号添加到清博监控中
                    GroupMonitorAddResult groupMonitorAddResult = DataApi.getInstance().addWeixin2Group(cbWxList.getNewsUrl());
                    if (groupMonitorAddResult == null || groupMonitorAddResult.getWxNickname() == null) {
                        // 1.4.1、添加到清博组失败，则直接放回清博nickname_id为null
                        logger.error("添加到清博组监控失败，公众号为【" + cbWxList.getNicknameId() + "】:" + groupMonitorAddResult.getErrmsg());
                        gsdataNickNameId = null;
                    } else {
                        // 1.4.2、添加清博监控成功
                        // 1.4.2.1、添加组成功的话，再从清博接口中获取一次清博库中公众号信息
                        ResNickNameOneResult resNickNameOneResult = DataApi.getInstance().getNickNameOne(cbWxList.getNicknameId());
                        // 设置对象
                        //1.4.2.2、清博中公众号的信息获取成功,保存清博库中公众号的nickname_id信息到cbWxList对象
                        cbWxList.setGsdataNicknameId(String.valueOf(resNickNameOneResult.getId()));
                        // 保存公众号信息到数据库
                        cbWxListService.update(cbWxList);
                        gsdataNickNameId = String.valueOf(resNickNameOneResult.getId());
                    }
                } else {
                    //信息不正常
                    logger.error("数据库中存储待抓取公众号的信息错误id为【" + cbWxList.getId() + "】");
                    gsdataNickNameId = null;
                }
            } else {
                //存在，直接返回
                gsdataNickNameId = cbWxList.getGsdataNicknameId();
            }
        } catch (Exception ex) {
            gsdataNickNameId = null;
            logger.error("微信id为【" + cbWxList.getNicknameId() + "】的获取清博库nicknameId异常", ex);
        } finally {
            return gsdataNickNameId;
        }
    }
}
