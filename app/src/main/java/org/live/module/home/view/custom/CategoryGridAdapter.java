package org.live.module.home.view.custom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.live.R;
import org.live.common.constants.LiveConstants;
import org.live.module.home.domain.LiveCategoryVo;

import java.util.LinkedList;
import java.util.List;

/**
 *  直播分类的recycleview适配器
 * Created by Mr.wang on 2017/4/5.
 */

public class CategoryGridAdapter extends RecyclerView.Adapter<CategoryGridAdapter.LiveCategoryViewHolder> {

    private Context mContext ;

    private LayoutInflater mInflater ;

    public List<LiveCategoryVo> categoryList ;

    private OnItemClickListener listener ;


    public CategoryGridAdapter(Context context) {
        this.mContext = context ;
        categoryList = new LinkedList<LiveCategoryVo>() ;
        mInflater = LayoutInflater.from(context) ;
    }


    @Override
    public LiveCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.item_category, parent, false) ;
        LiveCategoryViewHolder viewHolder = new LiveCategoryViewHolder(itemView) ;
        return viewHolder ;
    }

    @Override
    public void onBindViewHolder(LiveCategoryViewHolder holder, int position) {
        LiveCategoryVo vo = categoryList.get(position) ;
        holder.getCategoryNameView().setText(vo.getCategoryName()) ;
        Glide.with(mContext).load(LiveConstants.REMOTE_SERVER_HTTP_IP + vo.getCoverUrl()).into(holder.getCoverImgView()) ;
    }

    @Override
    public int getItemCount() {
        return  categoryList.size() ;
    }

    /**
     *  直播分类中item的Viewholder
     */
    public  class LiveCategoryViewHolder extends RecyclerView.ViewHolder {

        /**
         * 分类封面
         */
        private ImageView coverImgView;

        /**
         * 分类名称
         */
        private TextView categoryNameView;

        public LiveCategoryViewHolder(View itemView) {
            super(itemView);
            coverImgView = (ImageView) itemView.findViewById(R.id.img_category_cover);
            categoryNameView = (TextView) itemView.findViewById(R.id.tv_category_name);
        }

        public ImageView getCoverImgView() {
            return coverImgView;
        }

        public void setCoverImgView(ImageView coverImgView) {
            this.coverImgView = coverImgView;
        }

        public TextView getCategoryNameView() {
            return categoryNameView;
        }

        public void setCategoryNameView(TextView categoryNameView) {
            this.categoryNameView = categoryNameView;
        }
    }

    /**
     * recycleview中view的点击事件
     */
    public interface OnItemClickListener {

        void onItemClick(View view, LiveCategoryVo vo) ;

    }

}

