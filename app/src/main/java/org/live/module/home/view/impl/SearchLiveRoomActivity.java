package org.live.module.home.view.impl;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import org.live.R;
import org.live.common.util.SimpleResponseModel;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.domain.LiveRoomVo;
import org.live.module.home.presenter.LiveRoomPresenter;
import org.live.module.home.view.custom.LiveRoomGridAdapter;
import org.live.module.home.view.custom.LiveRoomItemDecoration;
import org.live.module.home.view.custom.OnItemClickListener;
import org.live.module.play.view.impl.PlayActivity;

import java.util.List;


/**
 * 搜索直播间的Activity
 * Created by wang on 2017/4/22.
 */
public class SearchLiveRoomActivity extends AppCompatActivity {

    private View returnBtn ;    //返回按钮

    private View searchBtn ;    //搜索按钮

    private EditText searchInputEdit ;  //搜索框

    private ImageView notFoundResultView ;  //未找到直播间时显示的图片

    private SwipeRefreshLayout refreshLayout ;  //下拉刷新

    private RecyclerView liveRoomRecycleView ;

    private LiveRoomGridAdapter adapter ;

    private Handler handler ;

    private LiveRoomPresenter presenter ;

    /**
     * 暂存点击的直播间的下标
     */
    private int clickPositionFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_room) ;
        initial() ;
        newInstanceHandler() ;
        presenter = new LiveRoomPresenter(getBaseContext(), handler) ;
    }

    /**
     * 初始化ui
     */
    protected void initial() {

        returnBtn = findViewById(R.id.btn_search_return) ;

        searchBtn = findViewById(R.id.btn_search_search) ;
        searchInputEdit = (EditText) findViewById(R.id.et_search_input) ;

        notFoundResultView = (ImageView) findViewById(R.id.iv_search_notFound);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.sl_search_refresh) ;
        refreshLayout.setColorSchemeResources( R.color.themeColor1) ;    //设置颜色

        liveRoomRecycleView = (RecyclerView) findViewById(R.id.rv_search_liveRoomHold);
        //设置布局
        liveRoomRecycleView.setLayoutManager(new GridLayoutManager(getBaseContext(), 2)) ;
        //设置tiem之间的边距
        liveRoomRecycleView.addItemDecoration(new LiveRoomItemDecoration()) ;
        //设置适配器
        adapter = new LiveRoomGridAdapter(getBaseContext()) ;
        liveRoomRecycleView.setAdapter(adapter) ;
        //点击直播间
        adapter.setListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(getContext(), adapter.liveRoomList.get(position).getLiveRoomName(), Toast.LENGTH_SHORT).show() ;
                String liveRoomId = adapter.liveRoomList.get(position).getLiveRoomId();    //直播间id
                String userId = HomeActivity.mobileUserVo.getUserId();  //用户id
                clickPositionFlag = position;  //更改暂存的下标
                presenter.loadLiveRoomLimitations(userId, liveRoomId);
            }
        });

        returnBtn.setClickable(true) ;
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish() ;
            }
        });

        searchBtn.setClickable(true) ;
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String input = searchInputEdit.getText().toString().trim() ;
               if("".equals(input)) {
                   Toast.makeText(getBaseContext(), "请输入搜索条件进行搜索！", Toast.LENGTH_SHORT).show() ;
                    return ;
               }
                presenter.loadSearchLiveRoomData(input) ;
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false) ;
            }
        });
    }


    public void newInstanceHandler() {
        this.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == HomeConstants.LOAD_LIVE_ROOM_SUCCESS_FLAG) { //直播间数据加载成功
                    SimpleResponseModel<List<LiveRoomVo>> dataModel = (SimpleResponseModel<List<LiveRoomVo>>) msg.obj;
                    if (dataModel.getStatus() == 1) {
                        adapter.liveRoomList.clear();    //先清空之间的直播间数据
                        List<LiveRoomVo> liveRoomVos = dataModel.getData();
                        if (liveRoomVos == null || liveRoomVos.size() == 0) {    //没有直播间信息
                            notFoundResultView.setVisibility(ImageView.VISIBLE);
                            adapter.notifyDataSetChanged();
                        } else {
                            notFoundResultView.setVisibility(ImageView.GONE);
                            adapter.liveRoomList.addAll(liveRoomVos);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "服务器繁忙！", Toast.LENGTH_LONG).show();
                    }

                } else if (msg.what == HomeConstants.LOAD_LIMITATION_SUCCESS_FLAG) {     //用户在直播间的限制加载成功
                    SimpleResponseModel<List<Integer>> dataModel = (SimpleResponseModel<List<Integer>>) msg.obj;
                    if (dataModel.getStatus() == 1) {
                        List<Integer> limitations = dataModel.getData();
                        if (limitations.contains(HomeConstants.LIMIT_TYPE_KICKOUT)) {
                            Toast.makeText(getBaseContext(), "您被直播踢出直播间，暂时不能进入该直播间！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //进入直播间
                        Intent intent = new Intent(getBaseContext(), PlayActivity.class);
                        if (limitations.contains(HomeConstants.LIMIT_TYPE_SHUTUP)) {
                            intent.putExtra(HomeConstants.LIMIT_TYPE_KEY_FLAG, HomeConstants.LIMIT_TYPE_SHUTUP);
                        }
                        LiveRoomVo liveRoomVo = adapter.liveRoomList.get(clickPositionFlag);   //得到被点击的直播间
                        intent.putExtra(HomeConstants.ANCHOR_ID_KEY, liveRoomVo.getAnchorId()); // 主播id
                        intent.putExtra(HomeConstants.LIVE_ROOM_ID_KEY, liveRoomVo.getLiveRoomId());   //房间id
                        intent.putExtra(HomeConstants.LIVE_ROOM_NAME_KEY, liveRoomVo.getLiveRoomName());   //房间名
                        intent.putExtra(HomeConstants.LIVE_ROOM_URL_KEY, liveRoomVo.getLiveRoomUrl()); //直播推流地址
                        intent.putExtra(HomeConstants.HEAD_IMG_URL_KEY, liveRoomVo.getHeadImgUrl());   //头像url
                        intent.putExtra(HomeConstants.LIVE_ROOM_ONLINE_COUNT_KEY, liveRoomVo.getOnlineCount());    //在线人数
                        intent.putExtra(HomeConstants.LIVE_ROOM_NUM_KEY, liveRoomVo.getRoomNum());     //直播间号
                        startActivity(intent);

                    } else {
                        Toast.makeText(getBaseContext(), "服务器繁忙！", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getBaseContext(), "连接服务器失败！", Toast.LENGTH_LONG).show();
                }
                refreshLayout.setRefreshing(false);    //隐藏刷新控件
            }
        };
    }



}
