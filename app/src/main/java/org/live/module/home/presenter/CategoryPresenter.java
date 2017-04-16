package org.live.module.home.presenter;

import android.content.Context;
import android.os.Handler;

import org.live.module.home.model.impl.CategoryModel;

/**
 * Created by Mr.wang on 2017/4/4.
 */
public class CategoryPresenter {

    private CategoryModel categoryModel ;

    public CategoryPresenter(Context context, Handler handler) {
        categoryModel = new CategoryModel(handler) ;
    }

    /**
     *  加载直播分类的数据
     * @return
     */
    public void loadCategoryData() {
        categoryModel.loadCategoryData() ;
    }
}
