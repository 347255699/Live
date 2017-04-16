package org.live.module.home.view.impl;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import org.live.R;

import org.live.module.home.domain.LiveRoomVo;

import org.live.module.home.view.custom.LiveRoomGridAdapter;
import org.live.module.home.view.custom.LiveRoomItemDecoration;

/**
 *  主页fragment
 *
 * Created by Mr.wang on 2017/3/14.
 */

public class HomeFragment extends Fragment {

    public static final String TAG = "HOME" ;

    private View currentFragmentView = null ;   //当前的fragment视图

    private RecyclerView liveRoomRecycleView ;

    private GridLayoutManager layoutManager = null ;    //grid布局管理器，为RecyclerView设置布局

    private LiveRoomGridAdapter adapter = null ;    //直播数据的适配器

    private SwipeRefreshLayout refreshLayout ;  //下拉刷新


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "HomeFragment执行了onCreateView") ;
        if(currentFragmentView != null) return currentFragmentView ;


        currentFragmentView = inflater.inflate(R.layout.fragment_home, null) ;

        initial();
        return currentFragmentView ;
    }

    /**
     *  初始化
     */
    public void initial() {

        refreshLayout = (SwipeRefreshLayout) currentFragmentView.findViewById(R.id.sl_home_refresh);

        liveRoomRecycleView = (RecyclerView) currentFragmentView.findViewById(R.id.rv_home_liveRoomHold) ;

        layoutManager = new GridLayoutManager(getContext(), 2) ;    //一行显示2列

        liveRoomRecycleView.setLayoutManager(layoutManager) ;

        liveRoomRecycleView.addItemDecoration(new LiveRoomItemDecoration()) ;



        adapter = new LiveRoomGridAdapter(getContext()) ;
        liveRoomRecycleView.setAdapter(adapter) ;
        liveRoomRecycleView.setHasFixedSize(true) ; //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能

        LiveRoomVo vo = new LiveRoomVo() ;
        vo.setLiveRoomId("11") ;
        vo.setLiveRoomName("欢迎来到我的直播间直播间直播间") ;
        vo.setAnchorName("我是城院大帅逼") ;
        vo.setOnlineCount(1002) ;
        vo.setLiveRoomCoverUrl(R.drawable.pause_publish+"") ;


        LiveRoomVo vo1 = new LiveRoomVo() ;
        vo1.setLiveRoomId("22") ;
        vo1.setLiveRoomName("欢迎来到你的直播间") ;
        vo1.setAnchorName("你是城院大傻逼大傻逼大傻逼大傻逼") ;
        vo1.setOnlineCount(999) ;
        vo1.setLiveRoomCoverUrl(R.drawable.bg+"") ;

        adapter.liveRoomList.add(vo) ;
        adapter.liveRoomList.add(vo) ;
        adapter.liveRoomList.add(vo) ;
        adapter.liveRoomList.add(vo) ;

        adapter.liveRoomList.add(vo1) ;
        adapter.liveRoomList.add(vo1) ;
        adapter.liveRoomList.add(vo1) ;
        adapter.liveRoomList.add(vo) ;

        adapter.notifyDataSetChanged() ;

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false) ;
            }
        });


    }


}
