package com.hubpd.uar.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 爬虫的配置文件
 * Created by cpc on 2018/6/6.
 */
@Component
public class SpiderConfig {
    /** 是否抓取指定微信公众号(1：是，0：否) */
    public static Integer SPIDER_CATCH_IS_APPOINT;
    /** 抓取指定的微信号 */
    public static String SPIDER_CATCH_APPOINT_WX_NICKNAME_ID;
    /** 从数据库中查询指定分组的公众号（不指定查询所有分组，spider_catch_is_appoint为0时，才有效） */
    public static String SPIDER_WX_GROUP_ID;
    /** 抓取时间是否设置周期(1：设置，下面的开始结束时间才有效；0：不设置，默认查询当天) */
    public static Integer SPIDER_DATA_IS_SET;
    /** 指定查询周期的开始时间 */
    public static String SPIDER_START_DAY;
    /** 指定查询周期的结束时间 */
    public static String SPIDER_END_DAY;
    /** 核心线程数 */
    public static Integer THREAD_CORE_NUM;
    /** 最大运行线程数 */
    public static Integer THREAD_MAX_NUM;
    /** 队列数 */
    public static Integer THREAD_QUEUE_NUM;


    @Value("${spider_catch_is_appoint}")
    public void setSpiderCatchIsAppoint(Integer spiderCatchIsAppoint) {
        this.SPIDER_CATCH_IS_APPOINT = spiderCatchIsAppoint;
    }
    @Value("${spider_catch_appoint_wx_nickname_id}")
    public void setSpiderCatchAppointWxNicknameId(String spiderCatchAppointWxNicknameId) {
        this.SPIDER_CATCH_APPOINT_WX_NICKNAME_ID = spiderCatchAppointWxNicknameId;
    }

    @Value("${spider_wx_group_id}")
    public void setSpiderWxGroupId(String spiderWxGroupId) {
        this.SPIDER_WX_GROUP_ID = spiderWxGroupId;
    }

    @Value("${spider_data_is_set}")
    public void setSpiderDataIsSet(Integer spiderDataIsSet) {
        this.SPIDER_DATA_IS_SET = spiderDataIsSet;
    }

    @Value("${spider_start_day}")
    public void setSpiderStartDay(String spiderStartDay) {
        this.SPIDER_START_DAY = spiderStartDay;
    }

    @Value("${spider_end_day}")
    public void setSpiderEndDay(String spiderEndDay) {
        this.SPIDER_END_DAY = spiderEndDay;
    }

    @Value("${thread_core_num}")
    public void setThreadCoreNum(Integer threadCoreNum) {
        this.THREAD_CORE_NUM = threadCoreNum;
    }

    @Value("${thread_max_num}")
    public void setThreadMaxNum(Integer threadMaxNum) {
        this.THREAD_MAX_NUM = threadMaxNum;
    }

    @Value("${thread_queue_num}")
    public void setThreadQueueNum(Integer threadQueueNum) {
        this.THREAD_QUEUE_NUM = threadQueueNum;
    }
}
