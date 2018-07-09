package com.hubpd.uar.domain;

import javax.persistence.*;

/**
 * 微信文章详情
 *
 * @author cpc
 * @create 2018-06-21 9:45
 **/
@Entity(name = "t_cb_wx_content")
public class CbWxContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;                //微信公众号名称
    @Column(name = "wx_name")
    private String wxName;              //微信公众号账号
    @Column(name = "nickname_id")
    private Integer nicknameId;          //平台内公众号ID
    @Column
    private String posttime;            //文章发布时间
    @Column
    private String title;               //文章标题
    @Column
    private String summary;             //
    @Column
    private String content;             //文章正文（html格式正文）
    @Column(name = "content_pure_word")
    private String contentPureWord;             //文章正文（纯文本）
    @Column
    private String url;                 //微信文章地址
    @Column(name = "monitor_time")
    private String monitorTime;        //实时跟踪时间
    @Column
    private Integer readnum;            //文章阅读数
    @Column
    private Integer likenum;            //文章点赞数
    @Column
    private Integer top;                //文章位置
    @Column
    private Integer ispush;             //是否同步
    @Column
    private String picurl;              //封面地址
    @Column
    private String sourceurl;           //原文地址
    @Column
    private String author;              //文章作者
    @Column
    private String videourl;            //视频地址 ( 说明 : 非定制不提供此URL地址 )
    @Column
    private String imgsurl;             //图片地址 ( 说明 : 非定制不提供此URL地址)

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWxName() {
        return wxName;
    }

    public void setWxName(String wxName) {
        this.wxName = wxName;
    }

    public Integer getNicknameId() {
        return nicknameId;
    }

    public void setNicknameId(Integer nicknameId) {
        this.nicknameId = nicknameId;
    }

    public String getPosttime() {
        return posttime;
    }

    public void setPosttime(String posttime) {
        this.posttime = posttime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMonitorTime() {
        return monitorTime;
    }

    public void setMonitorTime(String monitorTime) {
        this.monitorTime = monitorTime;
    }

    public Integer getReadnum() {
        return readnum;
    }

    public void setReadnum(Integer readnum) {
        this.readnum = readnum;
    }

    public Integer getLikenum() {
        return likenum;
    }

    public void setLikenum(Integer likenum) {
        this.likenum = likenum;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Integer getIspush() {
        return ispush;
    }

    public void setIspush(Integer ispush) {
        this.ispush = ispush;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getSourceurl() {
        return sourceurl;
    }

    public void setSourceurl(String sourceurl) {
        this.sourceurl = sourceurl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    public String getImgsurl() {
        return imgsurl;
    }

    public void setImgsurl(String imgsurl) {
        this.imgsurl = imgsurl;
    }

    public String getContentPureWord() {
        return contentPureWord;
    }

    public void setContentPureWord(String contentPureWord) {
        this.contentPureWord = contentPureWord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CbWxContent that = (CbWxContent) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (wxName != null ? !wxName.equals(that.wxName) : that.wxName != null) return false;
        if (nicknameId != null ? !nicknameId.equals(that.nicknameId) : that.nicknameId != null) return false;
        if (posttime != null ? !posttime.equals(that.posttime) : that.posttime != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (summary != null ? !summary.equals(that.summary) : that.summary != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (contentPureWord != null ? !contentPureWord.equals(that.contentPureWord) : that.contentPureWord != null)
            return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (monitorTime != null ? !monitorTime.equals(that.monitorTime) : that.monitorTime != null) return false;
        if (readnum != null ? !readnum.equals(that.readnum) : that.readnum != null) return false;
        if (likenum != null ? !likenum.equals(that.likenum) : that.likenum != null) return false;
        if (top != null ? !top.equals(that.top) : that.top != null) return false;
        if (ispush != null ? !ispush.equals(that.ispush) : that.ispush != null) return false;
        if (picurl != null ? !picurl.equals(that.picurl) : that.picurl != null) return false;
        if (sourceurl != null ? !sourceurl.equals(that.sourceurl) : that.sourceurl != null) return false;
        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        if (videourl != null ? !videourl.equals(that.videourl) : that.videourl != null) return false;
        return imgsurl != null ? imgsurl.equals(that.imgsurl) : that.imgsurl == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (wxName != null ? wxName.hashCode() : 0);
        result = 31 * result + (nicknameId != null ? nicknameId.hashCode() : 0);
        result = 31 * result + (posttime != null ? posttime.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (contentPureWord != null ? contentPureWord.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (monitorTime != null ? monitorTime.hashCode() : 0);
        result = 31 * result + (readnum != null ? readnum.hashCode() : 0);
        result = 31 * result + (likenum != null ? likenum.hashCode() : 0);
        result = 31 * result + (top != null ? top.hashCode() : 0);
        result = 31 * result + (ispush != null ? ispush.hashCode() : 0);
        result = 31 * result + (picurl != null ? picurl.hashCode() : 0);
        result = 31 * result + (sourceurl != null ? sourceurl.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (videourl != null ? videourl.hashCode() : 0);
        result = 31 * result + (imgsurl != null ? imgsurl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CbWxContent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", wxName='" + wxName + '\'' +
                ", nicknameId=" + nicknameId +
                ", posttime='" + posttime + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", contentPureWord='" + contentPureWord + '\'' +
                ", url='" + url + '\'' +
                ", monitorTime='" + monitorTime + '\'' +
                ", readnum=" + readnum +
                ", likenum=" + likenum +
                ", top=" + top +
                ", ispush=" + ispush +
                ", picurl='" + picurl + '\'' +
                ", sourceurl='" + sourceurl + '\'' +
                ", author='" + author + '\'' +
                ", videourl='" + videourl + '\'' +
                ", imgsurl='" + imgsurl + '\'' +
                '}';
    }
}
