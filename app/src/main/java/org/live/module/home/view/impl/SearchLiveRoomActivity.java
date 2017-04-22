package org.live.module.home.view.impl;

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
        this.handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == HomeConstants.LOAD_LIVE_ROOM_SUCCESS_FLAG) { //数据加载成功
                    SimpleResponseModel<List<LiveRoomVo>> dataModel = (SimpleResponseModel<List<LiveRoomVo>>) msg.obj;
                    if(dataModel.getStatus() == 1) {
                        adapter.liveRoomList.clear() ;    //先清空之间的直播间数据
                        List<LiveRoomVo> liveRoomVos = dataModel.getData();
                        if(liveRoomVos == null || liveRoomVos.size() == 0) {    //没有直播间信息
                            notFoundResultView.setVisibility(ImageView.VISIBLE);    //未找到结果的图片显示出来
                            adapter.notifyDataSetChanged() ;
                        } else {
                            notFoundResultView.setVisibility(ImageView.GONE);    //隐藏未找到结果的图片
                            adapter.liveRoomList.addAll(liveRoomVos) ;
                            adapter.notifyDataSetChanged() ;
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "服务器繁忙！", Toast.LENGTH_LONG).show() ;
                    }
                } else {
                    Toast.makeText(getBaseContext(), "连接服务器失败！", Toast.LENGTH_LONG).show() ;
                }
                refreshLayout.setRefreshing(false) ;    //隐藏刷新控件
            }
        } ;
    }



}
