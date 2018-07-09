package com.hubpd.uar.service.impl;

import com.hubpd.uar.domain.CbWxContent;
import com.hubpd.uar.repository.CbWxContentRepository;
import com.hubpd.uar.service.CbWxContentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 微信文章详情
 *
 * @author cpc
 * @create 2018-06-21 10:46
 **/
@Service
public class CbWxContentServiceImpl implements CbWxContentService {
    private Logger logger = Logger.getLogger(CbWxContentServiceImpl.class);

    @Autowired
    private CbWxContentRepository cbWxContentRepository;

    /**
     * 根据微信文章地址获取唯一文章信息
     * @param url       微信文章地址
     * @return
     */
    public CbWxContent findOneByUrl(String url) {
        if(StringUtils.isBlank(url)) {
            return null;
        }
        return cbWxContentRepository.findOneByUrl(url);
    }

//    /**
//     * 根据微信文章地址获取唯一文章信息
//     * @param urlMd5       微信文章地址md5值
//     * @return
//     */
//    public CbWxContent findOneByUrlMd5(String urlMd5) {
//        if(StringUtils.isBlank(urlMd5)) {
//            return null;
//        }
//        //md5冗余可能查到两个结果的内容
//        List<CbWxContent> cbWxContentList = cbWxContentRepository.findByUrlMd5(urlMd5);
//        if(cbWxContentList != null && cbWxContentList.size() > 0) {
//            return cbWxContentList.get(0);
//        }
//        return null;
//    }

    /**
     * 保存微信文章信息
     * @param cbWxContent       微信文章实体
     */
    public void saveOne(CbWxContent cbWxContent) {
        cbWxContentRepository.saveAndFlush(cbWxContent);
    }

    /**
     * 保存微信文章信息
     * @param cbWxContentList       微信文章实体集合
     */
    public void save(List<CbWxContent> cbWxContentList) {
        cbWxContentRepository.save(cbWxContentList);
    }
}
