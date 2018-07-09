package com.hubpd.uar.repository;

import com.hubpd.uar.domain.CbWxList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 微信公众号列表
 *
 * @author cpc
 * @create 2018-06-21 10:04
 **/
public interface CbWxListRepository extends JpaRepository<CbWxList, Integer> {
    /**
     * 查询微信公众号里面的指定分组id或状态的所有数据
     * @param groupId       分组id        为null时，表示全部
     * @param status        0：有效；1：失效，为null时，表示全部
     * @return
     */
    @Query(" FROM com.hubpd.uar.domain.CbWxList cbWxList WHERE cbWxList.groupId = :groupId and cbWxList.status = :status ")
    public List<CbWxList> findAll(@Param("groupId") String groupId, @Param("status") Integer status);

    /**
     * 查询微信公众号里面的指定分组id或状态的所有数据
     * @param groupId       分组id
     * @return
     */
    @Query(" FROM com.hubpd.uar.domain.CbWxList cbWxList WHERE cbWxList.groupId = :groupId ")
    public List<CbWxList> findAllByGroupId(@Param("groupId") String groupId);
    /**
     * 查询微信公众号里面的指定分组id或状态的所有数据
     * @param status        0：有效；1：失效，为null时，表示全部
     * @return
     */
    @Query(" FROM com.hubpd.uar.domain.CbWxList cbWxList WHERE cbWxList.status = :status ")
    public List<CbWxList> findAllByStatus(@Param("status") Integer status);

    /**
     *  根据微信公账号查询微信信息
     * @param nicknameId    微信公众号名称
     * @return
     */
    @Query(" FROM com.hubpd.uar.domain.CbWxList cbWxList WHERE cbWxList.nicknameId = :nicknameId ")
    public List<CbWxList> findOneByNicknameId(@Param("nicknameId") String nicknameId);
}
