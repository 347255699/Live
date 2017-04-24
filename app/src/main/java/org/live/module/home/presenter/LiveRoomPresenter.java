package org.live.module.home.presenter;

import android.content.Context;
import android.os.Handler;

import org.live.module.home.model.LiveRoomModel;

/**
 * Created by wang on 2017/4/21.
 */
public class LiveRoomPresenter {

    private Context context ;

    private LiveRoomModel liveRoomModel ;

    public LiveRoomPresenter(Context context, Handler handler) {
        this.liveRoomModel = new LiveRoomModel(context, handler) ;
        this.context = context ;
    }

    /**
     * 加载直播间数据
     */
    public void loadLiveRoomData(){
        liveRoomModel.loadLiveRoomData() ;
    }

    /**
     * 根据直播分类id加载数据
     * @param categoryId
     */
    public void loadLiveRoomDataByCategoryId(String categoryId) {
        liveRoomModel.loadLiveRoomDataByCategoryId(categoryId) ;
    }

    /**
     * 加载用户关注的直播间
     */
    public void loadAttentionLiveRoomByUserId(String userId) {
        liveRoomModel.loadAttentionLiveRoomByUserId(userId) ;
    }

    /**
     * 搜索直播间
     * @param searchStr 搜索条件
     */
    public void loadSearchLiveRoomData(String searchStr) {
        liveRoomModel.loadSearchLiveRoomData(searchStr) ;
    }

    /**
     * 查看直播间的限制
     * @param userId
     * @param liveRoomId
     */
    public void loadLiveRoomLimitations(String userId, String liveRoomId) {
        liveRoomModel.loadLiveRoomLimitations(userId, liveRoomId) ;
    }

    /**
     * 加载主播信息
     */
    public void loadAnchorInfoData(String userId, String liveRoomId) {
        liveRoomModel.loadAnchorInfoData(userId, liveRoomId) ;
    }

    /**
     * 查询某个直播间的所有限制用户
     * @param liveRoomId
     */
    public void loadAllLimitationByLiveRoomId(String liveRoomId) {
        liveRoomModel.loadAllLimitationByLiveRoomId(liveRoomId) ;
    }

}
