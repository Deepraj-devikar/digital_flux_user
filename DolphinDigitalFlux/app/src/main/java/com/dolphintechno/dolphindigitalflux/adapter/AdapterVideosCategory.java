package com.dolphintechno.dolphindigitalflux.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dolphintechno.dolphindigitalflux.R;
import com.dolphintechno.dolphindigitalflux.model.VideosCategory;

import java.util.List;

public class AdapterVideosCategory extends RecyclerView.Adapter<AdapterVideosCategory.VideoCategoryViewHolder> {

    List<VideosCategory> videosCategoryList;
    Context context;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterVideosCategory(List<VideosCategory> videosCategoryList, Context context) {
        this.videosCategoryList = videosCategoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_training_video_category, viewGroup, false);
        return new VideoCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoCategoryViewHolder videoCategoryViewHolder, final int i) {

        VideosCategory videosCategory = videosCategoryList.get(i);

        videoCategoryViewHolder.tv_training_vdo_category_name.setText(videosCategory.getVidedoCategoryName());

        videoCategoryViewHolder.tv_training_vdo_category_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(videoCategoryViewHolder, videosCategoryList.get(i), i);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return videosCategoryList.size();
    }

    public class VideoCategoryViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_training_video_category;
        TextView tv_training_vdo_category_name;
        public VideoCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_training_video_category = (LinearLayout) itemView.findViewById(R.id.layout_training_video_category);
            tv_training_vdo_category_name = (TextView) itemView.findViewById(R.id.tv_training_vdo_category_name);
        }
    }




    public interface OnItemClickListener {
        void onItemClick(VideoCategoryViewHolder view, VideosCategory obj, int pos);
    }
}
