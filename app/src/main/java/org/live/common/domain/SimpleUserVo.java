package org.live.common.domain;



/**
 * 简易的userVo，用于移动端弹出框
 * Created by wang on 2017/4/24.
 */
public class SimpleUserVo {

    /**
     * 用户id
     */
    private String userId ;

    /**
     * 账号
     */
    private String account ;

    /**
     * 昵称
     */
    private String nickname ;

    /**
     * 头像url地址
     */
    private String headImgUrl ;

    public SimpleUserVo() {
    }

    public SimpleUserVo(String userId, String account, String nickname, String headImgUrl) {
        this.userId = userId;
        this.account = account;
        this.nickname = nickname;
        this.headImgUrl = headImgUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }
}

