package org.live.module.home.view.impl;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ImageView;
import android.widget.Toast;


import org.live.R;
import org.live.common.util.ResponseModel;
import org.live.common.util.SimpleResponseModel;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.domain.LiveRoomVo;
import org.live.module.home.presenter.LiveRoomPresenter;
import org.live.module.home.view.custom.LiveRoomGridAdapter;
import org.live.module.home.view.custom.LiveRoomItemDecoration;
import org.live.module.home.view.custom.OnItemClickListener;

import java.util.List;

/**
 *  主页fragment
 *
 * Created by Mr.wang on 2017/3/14.
 */

public class HomeFragment extends Fragment{

    public static final String TAG = "HOME" ;

    private View currentFragmentView = null ;   //当前的fragment视图

    private RecyclerView liveRoomRecycleView ;

    private GridLayoutManager layoutManager = null ;    //grid布局管理器，为RecyclerView设置布局

    private LiveRoomGridAdapter adapter = null ;    //直播数据的适配器

    private SwipeRefreshLayout refreshLayout = null ;  //下拉刷新

    private View toSearchPageBtn ;  //跳转到搜索页面的按钮

    private LiveRoomPresenter presenter ;

    private Handler handler ;

    private ImageView notFoundResultView ;  //未找到直播间的view


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "HomeFragment执行了onCreateView") ;
        if(currentFragmentView != null) return currentFragmentView ;
        currentFragmentView = inflater.inflate(R.layout.fragment_home, null) ;
        initial();
        newInstanceHandler() ;  //实例化handler
        presenter = new LiveRoomPresenter(getContext(), handler) ;
        presenter.loadLiveRoomData() ;
        return currentFragmentView ;
    }

    /**
     *  初始化
     */
    public void initial() {
        toSearchPageBtn = currentFragmentView.findViewById(R.id.btn_home_search) ;
        notFoundResultView = (ImageView) currentFragmentView.findViewById(R.id.iv_home_notFound);
        refreshLayout = (SwipeRefreshLayout) currentFragmentView.findViewById(R.id.sl_home_refresh);
        refreshLayout.setColorSchemeResources( R.color.themeColor1) ;    //设置颜色

        liveRoomRecycleView = (RecyclerView) currentFragmentView.findViewById(R.id.rv_home_liveRoomHold) ;

        layoutManager = new GridLayoutManager(getContext(), 2) ;    //一行显示2列

        liveRoomRecycleView.setLayoutManager(layoutManager) ;

        liveRoomRecycleView.addItemDecoration(new LiveRoomItemDecoration()) ;

        adapter = new LiveRoomGridAdapter(getContext()) ;

        adapter.setListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), adapter.liveRoomList.get(position).getLiveRoomName(), Toast.LENGTH_SHORT).show() ;
            }
        });

        liveRoomRecycleView.setAdapter(adapter) ;
        liveRoomRecycleView.setHasFixedSize(true) ; //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        //下拉刷新数据
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadLiveRoomData() ;
            }
        });

        toSearchPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchLiveRoomActivity.class) ;
                startActivity(intent) ;
            }
        });

    }


    public void newInstanceHandler() {
        this.handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == HomeConstants.LOAD_LIVE_ROOM_SUCCESS_FLAG) { //数据加载成功
                    SimpleResponseModel<List<LiveRoomVo>> dataModel = (SimpleResponseModel<List<LiveRoomVo>>) msg.obj;
                    if(dataModel.getStatus() == 1) {
                        adapter.liveRoomList.clear() ;    //先清空之间的直播间数据
                        List<LiveRoomVo> liveRoomVos = dataModel.getData();
                        if(liveRoomVos == null || liveRoomVos.size() == 0) {    //没有直播间信息
                            notFoundResultView.setVisibility(ImageView.VISIBLE) ;
                            adapter.notifyDataSetChanged() ;
                        } else {
                            notFoundResultView.setVisibility(ImageView.GONE) ;
                            adapter.liveRoomList.addAll(liveRoomVos) ;
                            adapter.notifyDataSetChanged() ;
                        }

                    } else {
                        Toast.makeText(getContext(), "服务器繁忙！", Toast.LENGTH_LONG).show() ;
                    }
                } else {
                    Toast.makeText(getContext(), "连接服务器失败！", Toast.LENGTH_LONG).show() ;
                }
                refreshLayout.setRefreshing(false) ;    //隐藏刷新控件
            }
        } ;
    }


}
