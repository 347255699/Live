package org.live.module.home.view.impl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import org.live.R;
import org.live.common.util.BackThread;
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
 * 展示某直播分类下的直播间
 * 展示我的收藏中的直播间
 * Created by Mr.wang on 2017/4/5.
 */
public class SingleCategoryActivity extends AppCompatActivity {

    public static final String TAG = "HOME";

    private TextView titleTextView; //title名称的textview

    private SwipeRefreshLayout refreshLayout;  //下拉刷新

    private RecyclerView liveRoomRecycleView;

    private LiveRoomGridAdapter adapter;

    private String categoryName;   //分类名称

    private String categoryId;     //分类id

    private String userId;     //用户id

    private LiveRoomPresenter presenter;

    private Handler handler;

    private ImageView notFoundResultView;  //未找到直播间时显示的图片

    private String entryTypeFlag;  //进入的页面入口的判断

    /**
     * 暂存点击的直播间的下标
     */
    private int clickPositionFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_category);
        Intent intent = getIntent();

        entryTypeFlag = intent.getStringExtra(HomeConstants.ENTER_SINGLE_CATEGORY_KEY);
        if (HomeConstants.ATTENTION_LIVEROOM.equals(entryTypeFlag)) {  //从我的收藏中进入这个页面的
            userId = intent.getStringExtra(HomeConstants.ATTENTION_LIVEROOM_USER_ID);
        } else {   //从主播分类中进入的
            categoryId = intent.getStringExtra(HomeConstants.CATEGORY_ID_KEY);
            categoryName = intent.getStringExtra(HomeConstants.CATEGORY_NAME_KEY);
        }

        initial();
        newInstanceHandler();
        presenter = new LiveRoomPresenter(getBaseContext(), handler);

        //预先加载数据
        if (HomeConstants.ATTENTION_LIVEROOM.equals(entryTypeFlag)) {
            presenter.loadAttentionLiveRoomByUserId(userId);
        } else {
            presenter.loadLiveRoomDataByCategoryId(categoryId);
        }

        //初始化标题栏
        initActionBar();
    }

    /**
     * 初始化
     */
    private void initial() {

        notFoundResultView = (ImageView) findViewById(R.id.iv_singleCategory_notFound);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.sl_singleCategory_refresh);
        refreshLayout.setColorSchemeResources(R.color.themeColor1);    //设置颜色

        titleTextView = (TextView) findViewById(R.id.tv_title_singleCategory);

        liveRoomRecycleView = (RecyclerView) findViewById(R.id.rv_singleCategory_liveRoomHold);
        //设置布局
        liveRoomRecycleView.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));
        //设置tiem之间的边距
        liveRoomRecycleView.addItemDecoration(new LiveRoomItemDecoration());
        //设置适配器
        adapter = new LiveRoomGridAdapter(getBaseContext());
        liveRoomRecycleView.setAdapter(adapter);
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

        if (HomeConstants.ATTENTION_LIVEROOM.equals(entryTypeFlag)) {
            titleTextView.setText("我的关注");
        } else {
            titleTextView.setText(categoryName);
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (HomeConstants.ATTENTION_LIVEROOM.equals(entryTypeFlag)) {
                    presenter.loadAttentionLiveRoomByUserId(userId);
                } else {
                    presenter.loadLiveRoomDataByCategoryId(categoryId);
                }
            }
        });
    }

    /**
     * 初始化标题栏
     */
    private void initActionBar() {
        Toolbar lToolbar = (Toolbar) findViewById(R.id.rl_singleCategory_top);
        lToolbar.setTitle("");
        setSupportActionBar(lToolbar);
        lToolbar.setNavigationIcon(getIconDrawable(MaterialDrawableBuilder.IconValue.CHEVRON_LEFT, Color.WHITE));

        lToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackThread().start(); // 模拟返回键点击
            }
        });

    }

    /**
     * 获取图标
     *
     * @param
     * @return
     */
    private Drawable getIconDrawable(MaterialDrawableBuilder.IconValue iconValue, int color) {
        Drawable drawable = MaterialDrawableBuilder.with(this)
                .setIcon(iconValue)
                .setColor(color)
                .setToActionbarSize().setSizeDp(35)
                .build();
        return drawable;
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
                            Toast.makeText(getBaseContext(), "您被踢出直播间，暂时不能进入该直播间！", Toast.LENGTH_SHORT).show();
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
