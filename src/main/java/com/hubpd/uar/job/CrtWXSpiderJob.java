package com.hubpd.uar.job;

import com.hubpd.uar.common.config.SpiderConfig;
import com.hubpd.uar.domain.CbWxList;
import com.hubpd.uar.service.CbWxContentService;
import com.hubpd.uar.service.CbWxListService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 微信文章抓取job
 *
 * @author cpc
 * @create 2018-08-07 11:19
 **/
@Component
public class CrtWXSpiderJob {
    private static final Logger logger = LoggerFactory.getLogger(CrtWXSpiderJob.class);
    private static ExecutorService workerExecutorService = Executors.newFixedThreadPool(SpiderConfig.THREAD_CORE_NUM);

    @Autowired
    private CbWxListService cbWxListService;
    @Autowired
    private CbWxContentService cbWxContentService;

    private BlockingQueue<CbWxList> cbContentBlockingQueue;

    @PostConstruct
    void init() {
        cbContentBlockingQueue = new LinkedBlockingDeque<CbWxList>();
        for (int i = 0; i != SpiderConfig.THREAD_CORE_NUM; ++i) {
            CrtWXSpiderWorker crtWXSpiderWorker = new CrtWXSpiderWorker(cbContentBlockingQueue, cbWxListService, cbWxContentService);
            workerExecutorService.execute(crtWXSpiderWorker);
        }
    }

    //    @Scheduled(fixedRate = 1000*5)
    @Scheduled(cron = "0 0 * * * ?")
    public void addTask() {
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

        for(CbWxList cbWxList : gsDataCbWxListList) {
            cbContentBlockingQueue.add(cbWxList);
        }

        logger.info("GsdataPushOriginNewsKeywordsSpider end");
    }
}
