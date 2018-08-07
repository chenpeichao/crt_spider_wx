package com.hubpd.uar.common.task;

import com.hubpd.uar.common.utils.DateUtils;
import com.hubpd.uar.service.GsdataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定时任务
 *
 * @author cpc
 * @create 2018-06-20 17:16
 **/
@Component
public class TaskExecuteStatusScheduler {
    @Autowired
    private GsdataService gsdataService;

    //每整点执行一次-----暂时废弃，使用多线程抓取任务
//    @Scheduled(cron="0 0 * * * ?")
//    @Scheduled(fixedRate=1000*60*50)
    public void updateTaskFinishStatus() {
        gsdataService.execute();
    }
}
