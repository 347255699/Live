package org.live.module.home.view.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import net.steamcrafted.materialiconlib.MaterialIconView;

import org.live.R;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.domain.LiveRoomVo;
import org.live.module.home.view.custom.LiveRoomGridAdapter;
import org.live.module.home.view.custom.LiveRoomItemDecoration;

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

    private String categoryName ;   //分类名称

    private String categoryId ;     //分类id




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_category) ;
        Intent intent = getIntent() ;
        categoryId = intent.getStringExtra(HomeConstants.CATEGORY_ID_KEY) ;
        categoryName = intent.getStringExtra(HomeConstants.CATEGORY_NAME_KEY) ;
        initial() ;



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

        categoryNameTextView.setText(categoryName) ;
        Toast.makeText(getApplicationContext(), categoryId, Toast.LENGTH_SHORT).show() ;

        backToCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish() ;  //关闭当前的activity
            }
        }) ;

        LiveRoomGridAdapter adapter = new LiveRoomGridAdapter(getBaseContext()) ;   //

        LiveRoomVo vo = new LiveRoomVo() ;
        vo.setAnchorName("我是城院大帅比") ;
        vo.setLiveRoomName("欢迎欢迎") ;
        vo.setLiveRoomId("11111") ;
        vo.setOnlineCount(111) ;
        vo.setLiveRoomCoverUrl(R.drawable.bg+"") ;
        adapter.liveRoomList.add(vo) ;
        adapter.liveRoomList.add(vo) ;
        adapter.liveRoomList.add(vo) ;
        adapter.liveRoomList.add(vo) ;
        adapter.liveRoomList.add(vo) ;

        liveRoomRecycleView.setAdapter(adapter) ;


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });






    }
}
