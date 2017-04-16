package org.live.module.home.domain;

/**
 * 直播间的vo
 *
 * Created by Mr.wang on 2017/4/5.
 */
public class LiveRoomVo {

    /**
     *  直播间id
     */
    private String liveRoomId ;

    /**
     *  直播间封面
     */
    private String liveRoomCoverUrl ;

    /**
     * 直播间名称
     */
    private String liveRoomName ;

    /**
     *  主播名称
     */
    private String anchorName ;

    /**
     *  在线人数
     */
    private int onlineCount ;

    /**
     *  正在直播的标记
     */
    private boolean liveFlag ;

    public String getLiveRoomId() {
        return liveRoomId;
    }

    public void setLiveRoomId(String liveRoomId) {
        this.liveRoomId = liveRoomId;
    }

    public String getLiveRoomCoverUrl() {
        return liveRoomCoverUrl;
    }

    public void setLiveRoomCoverUrl(String liveRoomCoverUrl) {
        this.liveRoomCoverUrl = liveRoomCoverUrl;
    }

    public String getLiveRoomName() {
        return liveRoomName;
    }

    public void setLiveRoomName(String liveRoomName) {
        this.liveRoomName = liveRoomName;
    }

    public String getAnchorName() {
        return anchorName;
    }

    public void setAnchorName(String anchorName) {
        this.anchorName = anchorName;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    public boolean isLiveFlag() {
        return liveFlag;
    }

    public void setLiveFlag(boolean liveFlag) {
        this.liveFlag = liveFlag;
    }
}
