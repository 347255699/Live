package org.live.module.home.domain;

/**
 * 用户查看主播信息
 *
 * Created by wang on 2017/4/23.
 */
public class AppAnchorInfo {

    /**
     * 主播在用户表中的id
     */
    private String anchorUserId ;

    /**
     * 主播头像
     */
    private String headImgUrl ;

    /**
     * 主播账号
     */
    private String account ;

    /**
     * 主播昵称
     */
    private String nickname ;

    /**
     * 主播简介
     */
    private String description ;

    /**
     * 该主播的关注数
     */
    private int attentionCount ;

    /**
     * 用户查看主播时，用户是否关注了该主播
     */
    private boolean attentionFlag ;

    public String getAnchorUserId() {
        return anchorUserId;
    }

    public void setAnchorUserId(String anchorUserId) {
        this.anchorUserId = anchorUserId;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAttentionCount() {
        return attentionCount;
    }

    public void setAttentionCount(int attentionCount) {
        this.attentionCount = attentionCount;
    }

    public boolean isAttentionFlag() {
        return attentionFlag;
    }

    public void setAttentionFlag(boolean attentionFlag) {
        this.attentionFlag = attentionFlag;
    }
}
