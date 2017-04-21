package org.live.module.home.view.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import net.steamcrafted.materialiconlib.MaterialIconView;

import org.live.R;
import org.live.common.util.SimpleResponseModel;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.domain.LiveRoomVo;
import org.live.module.home.presenter.LiveRoomPresenter;
import org.live.module.home.view.custom.LiveRoomGridAdapter;
import org.live.module.home.view.custom.LiveRoomItemDecoration;

import java.util.List;

/**
 *  展示某直播分类下的直播间
 * Created by Mr.wang on 2017/4/5.
 */
public class SingleCategoryActivity extends Activity {

    public static final String TAG = "HOME" ;

    private MaterialIconView backToCategoryBtn ;   //返回到直播分类的btn

    private TextView categoryNameTextView ; //直播分类名称的textview

    private SwipeRefreshLayout refreshLayout ;  //下拉刷新

    private RecyclerView liveRoomRecycleView ;

    private LiveRoomGridAdapter adapter ;

    private String categoryName ;   //分类名称

    private String categoryId ;     //分类id

    private LiveRoomPresenter presenter ;

    private Handler handler ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_category) ;
        Intent intent = getIntent() ;
        categoryId = intent.getStringExtra(HomeConstants.CATEGORY_ID_KEY) ;
        categoryName = intent.getStringExtra(HomeConstants.CATEGORY_NAME_KEY) ;
        initial() ;
        newInstanceHandler() ;
        presenter = new LiveRoomPresenter(getBaseContext(), handler) ;
        presenter.loadLiveRoomDataByCategoryId(categoryId) ;    //预先加载数据
    }

    /**
     *  初始化
     */
    private void initial() {

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.sl_singleCategory_refresh);
        refreshLayout.setColorSchemeResources( R.color.themeColor1) ;    //设置颜色

        backToCategoryBtn = (MaterialIconView) findViewById(R.id.btn_return_singleCategory) ;
        categoryNameTextView = (TextView) findViewById(R.id.tv_categoryName_singleCategory) ;

        liveRoomRecycleView = (RecyclerView) findViewById(R.id.rv_singleCategory_liveRoomHold);
        //设置布局
        liveRoomRecycleView.setLayoutManager(new GridLayoutManager(getBaseContext(), 2)) ;
        //设置tiem之间的边距
        liveRoomRecycleView.addItemDecoration(new LiveRoomItemDecoration()) ;
        //设置适配器
        adapter = new LiveRoomGridAdapter(getBaseContext()) ;   //
        liveRoomRecycleView.setAdapter(adapter) ;

        categoryNameTextView.setText(categoryName) ;    //设置分类名称

        backToCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish() ;  //关闭当前的activity
            }
        }) ;

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadLiveRoomDataByCategoryId(categoryId) ;
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

                            adapter.notifyDataSetChanged() ;
                        } else {
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
