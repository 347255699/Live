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
import org.live.module.home.domain.LiveRoomVo;

import java.util.LinkedList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 直播间的适配器
 * Created by Mr.wang on 2017/4/5.
 */
public class LiveRoomGridAdapter extends RecyclerView.Adapter<LiveRoomGridAdapter.LiveRoomViewHolder> {


    private Context mContext ;

    private LayoutInflater mInflater ;

    //图片圆角
    private RoundedCornersTransformation roundedCornersTransformation ;

    //图片朦胧处理
    private BlurTransformation blurTransformation ;

    //图片灰度处理
    private GrayscaleTransformation grayscaleTransformation ;

    /**
     * 数据集合
     */
    public List<LiveRoomVo> liveRoomList ;

    private OnItemClickListener listener ;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public LiveRoomGridAdapter(Context context) {
        this.mContext = context ;
        liveRoomList = new LinkedList<LiveRoomVo>() ;
        mInflater = LayoutInflater.from(context) ;

        roundedCornersTransformation =  new RoundedCornersTransformation(mContext, 30, 5) ;
        blurTransformation = new BlurTransformation(context, 15) ;
        grayscaleTransformation = new GrayscaleTransformation(context) ;
    }


    @Override
    public LiveRoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View itemView = mInflater.inflate(R.layout.item_live_room, parent, false) ;

        final LiveRoomViewHolder viewHolder = new LiveRoomViewHolder(itemView) ;
        //itemView设置点击事件，并回调自定义的点击事件
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onItemClick(itemView, viewHolder.getAdapterPosition()) ;
                }
            }
        });
        return viewHolder ;
    }

    @Override
    public void onBindViewHolder(LiveRoomViewHolder holder, int position) {
        LiveRoomVo vo = liveRoomList.get(position) ;

        if(vo.isLiveFlag()) {
            Glide.with(mContext).load(LiveConstants.REMOTE_SERVER_HTTP_IP+ vo.getLiveRoomCoverUrl())
                    .bitmapTransform(roundedCornersTransformation).into(holder.getLiveCoverImageView()) ;
        } else {
            ImageView coverImageView = holder.getLiveCoverImageView();
            coverImageView.setImageAlpha(75);
            Glide.with(mContext).load(LiveConstants.REMOTE_SERVER_HTTP_IP+ vo.getLiveRoomCoverUrl())
                    .bitmapTransform(roundedCornersTransformation).into(coverImageView) ;
        }

        holder.getLiveRoomNameTextView().setText(vo.getLiveRoomName()) ;
        holder.getAnchorNameTextView().setText(vo.getAnchorName()) ;
        holder.getOnlineCountTextView().setText(vo.getOnlineCount()+"") ;
    }

    @Override
    public int getItemCount() {
        return liveRoomList.size() ;
    }



    /**
     * 直播间的viewHolder
     */
    public class LiveRoomViewHolder extends RecyclerView.ViewHolder {
        /**
         *  直播间封面
         */
        private ImageView liveCoverImageView ;

        /**
         *  直播间名称 TextView
         */
        private TextView liveRoomNameTextView ;

        /**
         * 主播名称 TextView
         */
        private TextView anchorNameTextView ;

        /**
         * 在线人数 TextView
         */
        private TextView onlineCountTextView ;

        public LiveRoomViewHolder(View itemView) {
            super(itemView) ;
            liveCoverImageView = (ImageView) itemView.findViewById(R.id.iv_live_liveCover) ;
            liveRoomNameTextView = (TextView) itemView.findViewById(R.id.tv_live_liveName) ;
            liveRoomNameTextView.setSingleLine() ;  //单行
            anchorNameTextView = (TextView) itemView.findViewById(R.id.tv_live_anchorName);
            anchorNameTextView.setSingleLine() ;   //单行
            onlineCountTextView = (TextView) itemView.findViewById(R.id.tv_live_liveOnlineCount);
        }

        public ImageView getLiveCoverImageView() {
            return liveCoverImageView;
        }

        public TextView getLiveRoomNameTextView() {
            return liveRoomNameTextView;
        }

        public TextView getAnchorNameTextView() {
            return anchorNameTextView;
        }

        public TextView getOnlineCountTextView() {
            return onlineCountTextView;
        }
    }

}
