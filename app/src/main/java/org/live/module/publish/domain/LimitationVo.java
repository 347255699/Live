package org.live.module.publish.domain;

/**
 *  用户限制的vo
 * Created by wang on 2017/4/24.
 */
public class LimitationVo {

    /**
     * 限制类型，禁言
     */
    public static final int LIMIT_TYPE_SHUTUP = 1 ;

    /**
     * 限制类型，踢出房间
     */
    public static final int LIMIT_TYPE_KICKOUT = 2 ;

    /**
     *  被限制用户的id
     */
    private String userId ;

    /**
     *  被限制用户的账号
     */
    private String account ;

    /**
     * 被限制用户的昵称
     */
    private String nickname ;

    /**
     * 直播间id
     */
    private String liveRoomId ;

    /**
     * 直播间号
     */
    private String roomNum ;

    /**
     * 直播间名
     */
    private String roomName ;

    /**
     * 限制类型 1.禁言。 2.踢出房间
     */
    private int limitType ;

    public LimitationVo(){}

    public LimitationVo(String userId, String account, String nickname, String liveRoomId, String roomNum, String roomName, int limitType) {
        this.userId = userId;
        this.account = account;
        this.nickname = nickname;
        this.liveRoomId = liveRoomId;
        this.roomNum = roomNum;
        this.roomName = roomName;
        this.limitType = limitType;
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

    public String getLiveRoomId() {
        return liveRoomId;
    }

    public void setLiveRoomId(String liveRoomId) {
        this.liveRoomId = liveRoomId;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getLimitType() {
        return limitType;
    }

    public void setLimitType(int limitType) {
        this.limitType = limitType;
    }
}
