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
import android.widget.ImageView;
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
 *  展示我的收藏中的直播间
 * Created by Mr.wang on 2017/4/5.
 */
public class SingleCategoryActivity extends Activity {

    public static final String TAG = "HOME" ;

    private MaterialIconView backToCategoryBtn ;   //返回到直播分类的btn

    private TextView titleTextView ; //title名称的textview

    private SwipeRefreshLayout refreshLayout ;  //下拉刷新

    private RecyclerView liveRoomRecycleView ;

    private LiveRoomGridAdapter adapter ;

    private String categoryName ;   //分类名称

    private String categoryId ;     //分类id

    private String userId ;     //用户id

    private LiveRoomPresenter presenter ;

    private Handler handler ;

    private ImageView notFoundResultView ;  //未找到直播间时显示的图片

    private String entryTypeFlag ;  //进入的页面入口的判断

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_category) ;
        Intent intent = getIntent() ;

        entryTypeFlag = intent.getStringExtra(HomeConstants.ENTER_SINGLE_CATEGORY_KEY) ;
        if(HomeConstants.ATTENTION_LIVEROOM.equals(entryTypeFlag)) {  //从我的收藏中进入这个页面的
            userId = intent.getStringExtra(HomeConstants.ATTENTION_LIVEROOM_USER_ID) ;
        } else  {   //从主播分类中进入的
            categoryId = intent.getStringExtra(HomeConstants.CATEGORY_ID_KEY) ;
            categoryName = intent.getStringExtra(HomeConstants.CATEGORY_NAME_KEY) ;
        }

        initial() ;
        newInstanceHandler() ;
        presenter = new LiveRoomPresenter(getBaseContext(), handler) ;

        //预先加载数据
        if(HomeConstants.ATTENTION_LIVEROOM.equals(entryTypeFlag)) {
            presenter.loadAttentionLiveRoomByUserId(userId) ;
        } else {
            presenter.loadLiveRoomDataByCategoryId(categoryId) ;
        }

    }

    /**
     *  初始化
     */
    private void initial() {

        notFoundResultView = (ImageView) findViewById(R.id.iv_singleCategory_notFound);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.sl_singleCategory_refresh);
        refreshLayout.setColorSchemeResources( R.color.themeColor1) ;    //设置颜色

        backToCategoryBtn = (MaterialIconView) findViewById(R.id.btn_return_singleCategory) ;


        titleTextView = (TextView) findViewById(R.id.tv_title_singleCategory) ;

        liveRoomRecycleView = (RecyclerView) findViewById(R.id.rv_singleCategory_liveRoomHold);
        //设置布局
        liveRoomRecycleView.setLayoutManager(new GridLayoutManager(getBaseContext(), 2)) ;
        //设置tiem之间的边距
        liveRoomRecycleView.addItemDecoration(new LiveRoomItemDecoration()) ;
        //设置适配器
        adapter = new LiveRoomGridAdapter(getBaseContext()) ;
        liveRoomRecycleView.setAdapter(adapter) ;

        if(HomeConstants.ATTENTION_LIVEROOM.equals(entryTypeFlag)) {
            titleTextView.setText("我的收藏") ;
        } else {
            titleTextView.setText(categoryName) ;
        }



        backToCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish() ;  //关闭当前的activity
            }
        }) ;

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(HomeConstants.ATTENTION_LIVEROOM.equals(entryTypeFlag)) {
                    presenter.loadAttentionLiveRoomByUserId(userId) ;
                } else {
                    presenter.loadLiveRoomDataByCategoryId(categoryId) ;
                }
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
