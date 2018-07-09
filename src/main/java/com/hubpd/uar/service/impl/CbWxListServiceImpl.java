package com.hubpd.uar.service.impl;

import com.hubpd.uar.domain.CbWxList;
import com.hubpd.uar.repository.CbWxListRepository;
import com.hubpd.uar.service.CbWxListService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 微信公众号列表
 *
 * @author cpc
 * @create 2018-06-21 10:06
 **/
@Service
public class CbWxListServiceImpl implements CbWxListService {
    private Logger logger = Logger.getLogger(CbWxListServiceImpl.class);

    /**
     * 微信公众号列表
     */
    @Autowired
    private CbWxListRepository cbWxListRepository;

    /**
     * 查询微信公众号里面的指定分组id或状态的所有数据
     * @param groupId       分组id        为null时，表示全部
     * @param status        0：有效；1：失效，为null时，表示全部
     * @return
     */
    public List<CbWxList> findAll(String groupId, Integer status) {
        if(groupId == null && status == null) {
            return this.cbWxListRepository.findAll();
        } else if(groupId == null && status != null) {
            return this.cbWxListRepository.findAllByStatus(status);
        } else if(groupId != null && status == null) {
            return this.cbWxListRepository.findAllByGroupId(groupId);
        } else {
            return this.cbWxListRepository.findAll(groupId, status);
        }
    }

    /**
     *  根据微信公账号查询微信信息
     * @param nicknameId    微信公众号名称
     * @return
     */
    public List<CbWxList> findOneByNicknameId(String nicknameId) {
        return this.cbWxListRepository.findOneByNicknameId(nicknameId);
    }

    /**
     * 更新微信公众号信息
     * @param cbWxList
     */
    public void update(CbWxList cbWxList) {
        this.cbWxListRepository.saveAndFlush(cbWxList);
    }
}
