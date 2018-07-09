package com.hubpd.uar.domain;

import javax.persistence.*;

/**
 * 微信公众号添加列表详情信息
 *
 * @author cpc
 * @create 2018-06-21 9:31
 **/
@Entity(name = "t_cb_wx_list")
public class CbWxList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "group_id")
    private String groupId;         //公众号分组id
    @Column(name = "nickname_id")
    private String nicknameId;      //公众号id
    @Column
    private String nickname;        //公众号名称
    @Column
    private Integer status;         //0：有效；1：失效
    @Column(name = "news_url")
    private String newsUrl;
    @Column(name = "gsdata_nickname_id")
    private String gsdataNicknameId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getNicknameId() {
        return nicknameId;
    }

    public void setNicknameId(String nicknameId) {
        this.nicknameId = nicknameId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getGsdataNicknameId() {
        return gsdataNicknameId;
    }

    public void setGsdataNicknameId(String gsdataNicknameId) {
        this.gsdataNicknameId = gsdataNicknameId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CbWxList cbWxList = (CbWxList) o;

        if (id != null ? !id.equals(cbWxList.id) : cbWxList.id != null) return false;
        if (groupId != null ? !groupId.equals(cbWxList.groupId) : cbWxList.groupId != null) return false;
        if (nicknameId != null ? !nicknameId.equals(cbWxList.nicknameId) : cbWxList.nicknameId != null) return false;
        if (nickname != null ? !nickname.equals(cbWxList.nickname) : cbWxList.nickname != null) return false;
        if (status != null ? !status.equals(cbWxList.status) : cbWxList.status != null) return false;
        if (newsUrl != null ? !newsUrl.equals(cbWxList.newsUrl) : cbWxList.newsUrl != null) return false;
        return gsdataNicknameId != null ? gsdataNicknameId.equals(cbWxList.gsdataNicknameId) : cbWxList.gsdataNicknameId == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        result = 31 * result + (nicknameId != null ? nicknameId.hashCode() : 0);
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (newsUrl != null ? newsUrl.hashCode() : 0);
        result = 31 * result + (gsdataNicknameId != null ? gsdataNicknameId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CbWxList{" +
                "id=" + id +
                ", groupId='" + groupId + '\'' +
                ", nicknameId='" + nicknameId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", status=" + status +
                ", newsUrl='" + newsUrl + '\'' +
                ", gsdataNicknameId='" + gsdataNicknameId + '\'' +
                '}';
    }
}
