package org.live.module.play.domain;

/**
 * 直播间相关信息
 * Created by KAM on 2017/4/26.
 */

public class LiveRoomInfo {


    private String liveRoomId; //直播间id
    private String anchorId; // 主播id
    private String liveRoomNum; // 直播间号
    private String onlineCount; // 在线人数
    private String headImgUrl; // 用户头像
    private String liveRoomName; // 直播间名
    private String liveRoomUrl; // 直播间地址
    private boolean shutupFlag; // 禁言标志

    public LiveRoomInfo(String liveRoomId, String anchorId, String liveRoomNum, String onlineCount, String headImgUrl, String liveRoomName, String liveRoomUrl, boolean shutupFlag) {
        this.liveRoomId = liveRoomId;
        this.anchorId = anchorId;
        this.liveRoomNum = liveRoomNum;
        this.onlineCount = onlineCount;
        this.headImgUrl = headImgUrl;
        this.liveRoomName = liveRoomName;
        this.liveRoomUrl = liveRoomUrl;
        this.shutupFlag = shutupFlag;
    }

    public String getLiveRoomId() {
        return liveRoomId;
    }

    public void setLiveRoomId(String liveRoomId) {
        this.liveRoomId = liveRoomId;
    }

    public String getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(String anchorId) {
        this.anchorId = anchorId;
    }

    public String getLiveRoomNum() {
        return liveRoomNum;
    }

    public void setLiveRoomNum(String liveRoomNum) {
        this.liveRoomNum = liveRoomNum;
    }

    public String getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(String onlineCount) {
        this.onlineCount = onlineCount;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getLiveRoomName() {
        return liveRoomName;
    }

    public void setLiveRoomName(String liveRoomName) {
        this.liveRoomName = liveRoomName;
    }

    public String getLiveRoomUrl() {
        return liveRoomUrl;
    }

    public void setLiveRoomUrl(String liveRoomUrl) {
        this.liveRoomUrl = liveRoomUrl;
    }

    public boolean isShutupFlag() {
        return shutupFlag;
    }

    public void setShutupFlag(boolean shutupFlag) {
        this.shutupFlag = shutupFlag;
    }


}
