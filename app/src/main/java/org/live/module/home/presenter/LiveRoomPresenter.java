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

}
