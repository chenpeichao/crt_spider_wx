package com.hubpd.uar.service.impl;

import com.hubpd.uar.common.config.SpiderConfig;
import com.hubpd.uar.common.utils.Md5Utils;
import com.hubpd.uar.domain.CbWxContent;
import com.hubpd.uar.domain.CbWxList;
import com.hubpd.uar.service.CbWxContentService;
import com.hubpd.uar.service.CbWxListService;
import com.hubpd.uar.service.GsdataService;
import iims.crt.gsdata.DataApi;
import iims.crt.gsdata.GroupMonitorAddResult;
import iims.crt.gsdata.ResNickNameOneResult;
import iims.crt.gsdata.WxUrlMonitorResult;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 清博接口数据获取实现类
 *
 * @author cpc
 * @create 2018-06-20 17:34
 **/
@Service
@Transactional
public class GsdataServiceImpl implements GsdataService {
    private Logger logger = Logger.getLogger(GsdataServiceImpl.class);

    @Autowired
    private CbWxListService cbWxListService;
    @Autowired
    private CbWxContentService cbWxContentService;

    public void execute() {
        logger.info("GsdataPushOriginNewsKeywordsSpider start");
        List<CbWxList> gsDataCbWxListList = new ArrayList<CbWxList>();
        //1、首先判断是否设置了查询指定微信公众号的信息
        if (SpiderConfig.SPIDER_CATCH_IS_APPOINT == 1) {
            //1.1、抓取指定微信公众号的信息
            if(StringUtils.isBlank(SpiderConfig.SPIDER_CATCH_APPOINT_WX_NICKNAME_ID)) {
                logger.error("当查询指定微信号的文章数据时，必须设置微信id！！");
                return;
            }
            //1.1.1、查询指定微信号id公众号信息
            gsDataCbWxListList = cbWxListService.findOneByNicknameId(SpiderConfig.SPIDER_CATCH_APPOINT_WX_NICKNAME_ID);
        } else {
            //1.2、抓取全部的公众号
            //1.2.1、判断是否查询指定公众号分组的公众号
            if(StringUtils.isNotBlank(SpiderConfig.SPIDER_WX_GROUP_ID)) {
                //1.2.1.1、查询指定组的公众号--0:生效状态
                gsDataCbWxListList = cbWxListService.findAll(SpiderConfig.SPIDER_WX_GROUP_ID, 0);
            } else {
                //1.2.1.2、查询全部公众号--0:生效状态
                gsDataCbWxListList = cbWxListService.findAll(null, 0);
            }
        }

        //抓取公众号相关文章信息入库-并入内容库
        spiderGSDataAndInsertUar(gsDataCbWxListList);
        logger.info("GsdataPushOriginNewsKeywordsSpider end");
    }

    /**
     * 查询指定微信公众号集合的文章信息
     * @param gsDataCbWxListList        微信公众号集合
     */
    private void spiderGSDataAndInsertUar(List<CbWxList> gsDataCbWxListList) {
        if(gsDataCbWxListList == null || gsDataCbWxListList.size() == 0) {
            logger.error("未查找到有效的公众号信息！！");
            return;
        }
        String gsdataNickNameId = null;   //定义清博识别的微信公众号标识

        Date startDay = new Date();
        Date endDay = startDay;
        //2、查询对于日期周期是否进行判断--进行设置了，对于开始时间和结束时间进行设置
        if(SpiderConfig.SPIDER_DATA_IS_SET == 1) {
            try {
                startDay = DateUtils.parseDate(SpiderConfig.SPIDER_START_DAY, new String[]{"yyyy-MM-dd"});
                endDay = DateUtils.parseDate(SpiderConfig.SPIDER_END_DAY, new String[]{"yyyy-MM-dd"});
            } catch (ParseException e) {
                logger.error("日期格式错误请重新查看！！", e);
                //当日期格式有问题时，日期设置默认设置当天时间
                startDay = new Date();
                endDay = startDay;
            }
        }
        //用于日期流转变化的日期标识
        Date date = startDay;
        while (date.compareTo(endDay) <= 0) {
            // 循环处理待查询公众号集合
            for (CbWxList cbWxList : gsDataCbWxListList) {
                List<CbWxContent> cbWxContentList = new ArrayList<CbWxContent>();
                //1、获取查询的微信id在清博库中的nickname_id
                gsdataNickNameId = getGsdataNickNameId(cbWxList);
                if (StringUtils.isBlank(gsdataNickNameId)) {
                    logger.error("清博接口对于微信号【"+cbWxList.getNicknameId()+":"+cbWxList.getNickname()+"】的清博nicknameId获取失败");
                    continue;
                }
                int pageNo = 1;
                while (true) {
                    List<WxUrlMonitorResult> results = new ArrayList<WxUrlMonitorResult>();
                    // 获取数据
                    try {
                        results = DataApi.getInstance().getResponseData(pageNo,
                                gsdataNickNameId,
                                date,
                                date);
                    } catch (Exception e) {
                        logger.error("公众号【" + cbWxList.getNickname() + "---" + cbWxList.getNicknameId() + "】"+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)+"数据抓取异常！！",e);
                    }

                    // 获取结果
                    if (results != null && results.size() > 0) {
                        for (WxUrlMonitorResult wxUrlMonitorResult : results) {
                            CbWxContent newEntity = new CbWxContent();
                            newEntity.setName(wxUrlMonitorResult.getName());        //公众号名称
                            newEntity.setWxName(wxUrlMonitorResult.getWxName());    //公众号账号
                            newEntity.setNicknameId(wxUrlMonitorResult.getNickNameId());    //公众号清博库中nickname_id
                            newEntity.setPosttime(wxUrlMonitorResult.getPostTime());
//                            try {
//                                //清博中返回时间转换成内容库中识别的时间格式
//                                newEntity.setPosttime(com.hubpd.uar.common.utils.DateUtils.parseDateStrByPattern(wxUrlMonitorResult.getPostTime(), "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss"));
//                            } catch (ParseException e) {
//                                logger.error("微信文章日期【"+wxUrlMonitorResult.getPostTime()+"】格式【yyyy-MM-dd HH:mm:ss】转换【yyyyMMddHHmmss】格式错误！！", e);
//                            }
                            newEntity.setTitle(wxUrlMonitorResult.getTitle());
//                            newEntity.setContent(wxUrlMonitorResult.getContent());
                            newEntity.setContentPureWord(wxUrlMonitorResult.getContent());
                            newEntity.setUrl(wxUrlMonitorResult.getUrl());
//                            newEntity.setAddTime(wxUrlMonitorResult.getAddTime());
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

                            //用于根据文章url获取文章的正文html格式
                            List<String> urlList = new ArrayList<String>();
                            urlList.add(wxUrlMonitorResult.getUrl());
                            String str = DataApi.getInstance().getWeixinContentByUrls(urlList);

                            try {
                                JSONObject jsonObject = new JSONObject(str);
                                JSONArray articleListArray = jsonObject.getJSONArray("returnData");
                                if(articleListArray.length() > 0) {
                                    results = new ArrayList();

                                    for (int i = 0; i < articleListArray.length(); ++i) {
                                        JSONObject returnData = (JSONObject) articleListArray.get(i);
                                        if (returnData.has("content")) {
                                            newEntity.setContent(returnData.getString("content"));
                                        }
                                    }
                                }

                                //从数据库中查询文章信息是否存在
                                CbWxContent result = cbWxContentService.findOneByUrl(newEntity.getUrl());
                                if (result != null) {
                                    continue;
                                } else {
                                    cbWxContentList.add(newEntity);
                                }
                            } catch (JSONException e) {
                                logger.error("文章html格式获取失败！【"+wxUrlMonitorResult.getUrl()+"】：", e);
                                e.printStackTrace();
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
                Date currentHourDate = new Date();
                if(cbWxContentList.size() > 1) {
                    cbWxContentService.save(cbWxContentList);
                    logger.info("公众号【"+cbWxList.getNickname()+"("+cbWxList.getNicknameId()+")】在【"+ com.hubpd.uar.common.utils.DateUtils.parseDate2StringByPattern(DateUtils.addHours(currentHourDate, -1), "yyyy年MM月dd日")+"】抓取了【" +cbWxContentList.size()+"】篇文章");
                } else {
                    logger.info("公众号【"+cbWxList.getNickname()+"("+cbWxList.getNicknameId()+")】在【"+ com.hubpd.uar.common.utils.DateUtils.parseDate2StringByPattern(DateUtils.addHours(currentHourDate, -1), "yyyy年MM月dd日")+"】未抓取到文章");
                }
            }

            // 添加1天
            date = DateUtils.addDays(date, 1);
        }
    }

    /**
     * 根据entity的信息获取清博的nickname
     * @param cbWxList 单个公众号实体信息
     * @return 返回gsdataNameId
     */
    private String getGsdataNickNameId(CbWxList cbWxList) {
        String gsdataNickNameId = null;
        try {
            if (StringUtils.isBlank(cbWxList.getGsdataNicknameId()) && StringUtils.isNotBlank(cbWxList.getNewsUrl())) {

                // 1.1、获取查询的nickname_id
                ResNickNameOneResult resNickNameOneResult = DataApi.getInstance().getNickNameOne(cbWxList.getNicknameId());

                if (resNickNameOneResult == null || resNickNameOneResult.getId() < 0) {
                    // 1.2、未获取到nickname_id
                    // 1.2.1将公众号添加到清博中人民日报的监控组中
                    GroupMonitorAddResult groupMonitorAddResult = DataApi.getInstance().addWeixin2Group(cbWxList.getNewsUrl());
                    if (groupMonitorAddResult == null || groupMonitorAddResult.getWxNickname() == null) {
                        // 1.2.1.1、添加到清博组失败，则直接放回清博nickname_id为null
                        return gsdataNickNameId;
                    }

                    // 1.2.2、添加组成功的话，再从清博接口中获取一次清博库中公众号信息
                    resNickNameOneResult = DataApi.getInstance().getNickNameOne(cbWxList.getNicknameId());

                    if (resNickNameOneResult == null || resNickNameOneResult.getId() < 0) {
                        //1.2.2.1、清博中公众号的信息获取失败，则直接返回清博nickname_id为null
                        return gsdataNickNameId;

                    }
                    // 设置对象
                    //1.2.2.2、清博中公众号的信息获取成功,保存清博库中公众号的nickname_id信息到cbWxList对象
                    cbWxList.setGsdataNicknameId(String.valueOf(resNickNameOneResult.getId()));

                } else {
                    // 1.3、获取清博接口中的公众号nickname_id成功，保存清博库中公众号的nickname_id信息到cbWxList对象
                    cbWxList.setGsdataNicknameId(String.valueOf(resNickNameOneResult.getId()));
                }

                // 保存数据到数据库
                cbWxListService.update(cbWxList);
            }
        } catch (Exception ex) {
            logger.error("微信id为【"+cbWxList.getNicknameId()+"】的获取清博库nicknameId异常", ex);
        }
        //对清博中的nickname_id信息进行返回
        return cbWxList.getGsdataNicknameId();
    }
}
