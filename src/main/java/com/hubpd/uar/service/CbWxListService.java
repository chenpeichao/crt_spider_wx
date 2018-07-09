package com.hubpd.uar.service;

import com.hubpd.uar.domain.CbWxList;

import java.util.List;

/**
 * 微信公众号列表
 *
 * @author cpc
 * @create 2018-06-21 10:06
 **/
public interface CbWxListService {
    /**
     * 查询微信公众号里面的指定分组id或状态的所有数据
     * @param groupId       分组id        为null时，表示全部
     * @param status        0：有效；1：失效，为null时，表示全部
     * @return
     */
    public List<CbWxList> findAll(String groupId, Integer status);

    /**
     *  根据微信公账号查询微信信息
     * @param nicknameId    微信公众号名称
     * @return
     */
    public List<CbWxList> findOneByNicknameId(String nicknameId);

    /**
     * 更新微信公众号信息
     * @param cbWxList
     */
    public void update(CbWxList cbWxList);
}
