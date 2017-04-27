package org.live.module.play.domain;

/**
 * 直播间相关信息
 * Created by KAM on 2017/4/26.
 */

public class LiveRoomInfo {
    private String liveRoomId; //直播间id

    private String liveRoomNum ;     //直播间号

    private String liveRoomName;   //直播间名

    private String liveRoomUrl;    //拉流地址

    private String headImgUrl;     //主播头像地址

    private boolean shutupFlag;    //禁言标记

    private String onlineCount;    //在线人数

    public String getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(String onlineCount) {
        this.onlineCount = onlineCount;
    }

    public String getLiveRoomId() {
        return liveRoomId;
    }

    public void setLiveRoomId(String liveRoomId) {
        this.liveRoomId = liveRoomId;
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

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public boolean isShutupFlag() {
        return shutupFlag;
    }

    public void setShutupFlag(boolean shutupFlag) {
        this.shutupFlag = shutupFlag;
    }


    public String getLiveRoomNum() {
        return liveRoomNum;
    }

    public void setLiveRoomNum(String liveRoomNum) {
        this.liveRoomNum = liveRoomNum;
    }
}
