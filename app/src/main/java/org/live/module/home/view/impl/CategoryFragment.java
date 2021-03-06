package org.live.module.home.view.impl;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import org.live.R;

import org.live.module.home.constants.HomeConstants;
import org.live.module.home.domain.CategoryDataModel;
import org.live.module.home.domain.LiveCategoryVo;
import org.live.module.home.presenter.CategoryPresenter;
import org.live.module.home.view.custom.CategoryGridAdapter;
import org.live.module.home.view.custom.CategoryItemDecoration;
import org.live.module.home.view.custom.OnItemClickListener;


/**
 * 分类直播的页面
 * <p>
 * Created by Mr.wang on 2017/3/14.
 */

public class CategoryFragment extends Fragment {

    public static final String TAG = "HOME";

    private View currentFragmentView = null;   //当前的fragment视图

    private RecyclerView categoryRecyclerView = null;  //

    private GridLayoutManager layoutManager = null;    //grid布局管理器，为RecyclerView设置布局

    private View toSearchPageBtn ;  //跳转到搜索页面的按钮

    private SwipeRefreshLayout refreshLayout = null;   //下拉刷新

    private CategoryGridAdapter adapter = null;    //分类数据的适配器

    private CategoryPresenter presenter = null;

    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "执行了categoryFragment的onCreateView方法");

        if (currentFragmentView != null) return currentFragmentView;

        currentFragmentView = inflater.inflate(R.layout.fragment_category, null);

        initial();
        return currentFragmentView;
    }

    /**
     * 初始化
     */
    public void initial() {
        toSearchPageBtn = currentFragmentView.findViewById(R.id.btn_category_search) ;
        toSearchPageBtn.setClickable(true) ;    //设置为可点击的
        refreshLayout = (SwipeRefreshLayout) currentFragmentView.findViewById(R.id.sl_category_refresh);
        refreshLayout.setColorSchemeResources( R.color.themeColor1) ;    //设置颜色

        categoryRecyclerView = (RecyclerView) currentFragmentView.findViewById(R.id.rv_category_hold);

        layoutManager = new GridLayoutManager(getContext(), 3);   //一行显示3列
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryRecyclerView.addItemDecoration(new CategoryItemDecoration()); //设置行间距

        adapter = new CategoryGridAdapter(getContext());
        categoryRecyclerView.setAdapter(adapter);  //设置适配器
        categoryRecyclerView.setHasFixedSize(true); //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能

        handler = new Handler() {   //处理消息
            @Override
            public void handleMessage(Message msg) {
                Log.d(TAG, "handle处理消息----> " + msg);
                Log.d(TAG, "message----> " + msg.obj);
                if (msg.what == HomeConstants.LOAD_CATEGORY_SUCCESS_FLAG) {  //数据加载成功
                    CategoryDataModel dataModel = (CategoryDataModel) msg.obj;
                    if (dataModel.getStatus() == 1) {
                        adapter.categoryList.clear();
                        adapter.categoryList.addAll(dataModel.getData());
                    } else {
                        Toast.makeText(getContext(), "服务器忙！", Toast.LENGTH_LONG).show();
                    }
                } else if (msg.what == HomeConstants.LOAD_CATEGORY_EXCEPTION_FLAG) {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "连接服务器失败！", Toast.LENGTH_LONG).show();
                    }
                }
                refreshLayout.setRefreshing(false) ;    //隐藏刷新
                adapter.notifyDataSetChanged();
            }
        };

        presenter = new CategoryPresenter(getContext(), handler);
        presenter.loadCategoryData();  //初始化数据

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadCategoryData() ;
            }
        });

        adapter.setListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //开启一个Activity展示该直播分类下的直播间
                LiveCategoryVo vo = adapter.categoryList.get(position);
                Intent intent = new Intent(getContext(), SingleCategoryActivity.class) ;
                //用于是这里跳转的页面，还是我的收藏中跳转的页面
                intent.putExtra(HomeConstants.ENTER_SINGLE_CATEGORY_KEY, HomeConstants.SINGLE_CATEGORY_LIVEROOM) ;
                intent.putExtra(HomeConstants.CATEGORY_ID_KEY, vo.getId());
                intent.putExtra(HomeConstants.CATEGORY_NAME_KEY, vo.getCategoryName());
                startActivity(intent);
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


}
