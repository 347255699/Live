package org.live.module.play.domain;

/**
 * 直播间相关信息
 * Created by KAM on 2017/4/26.
 */

public class LiveRoomInfo {

    private String liveRoomId; //直播间id
    private String anchorId; // 主播id
    private String liveRoomNum; // 直播间号

    public LiveRoomInfo(String liveRoomId, String anchorId, String liveRoomNum) {
        this.liveRoomId = liveRoomId;
        this.anchorId = anchorId;
        this.liveRoomNum = liveRoomNum;
    }

    public String getLiveRoomNum() {
        return liveRoomNum;
    }

    public void setLiveRoomNum(String liveRoomNum) {
        this.liveRoomNum = liveRoomNum;
    }


    public String getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(String anchorId) {
        this.anchorId = anchorId;
    }

    public String getLiveRoomId() {
        return liveRoomId;
    }

    public void setLiveRoomId(String liveRoomId) {
        this.liveRoomId = liveRoomId;
    }

}
