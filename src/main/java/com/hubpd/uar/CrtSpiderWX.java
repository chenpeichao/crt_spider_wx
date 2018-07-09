package com.hubpd.uar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 微信抓取清博公众号以及文章信息
 *
 * @author cpc
 * @create 2018-07-09 19:50
 **/
@EnableAutoConfiguration
@EnableScheduling
@SpringBootApplication
@PropertySource(value = {"classpath:config/constant/constant.properties"},encoding="utf-8")
public class CrtSpiderWX {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(CrtSpiderWX.class);
        springApplication.run(args);
    }

    //定时任务中的线程池
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        //设置线程池大小
        taskScheduler.setPoolSize(5);
        //线程名字前缀
        taskScheduler.setThreadNamePrefix("CrtSpiderWX-Thread-Pool");
        return taskScheduler;
    }
}
