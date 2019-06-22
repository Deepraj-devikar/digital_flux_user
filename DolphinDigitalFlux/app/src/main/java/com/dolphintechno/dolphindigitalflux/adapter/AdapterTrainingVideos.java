package com.dolphintechno.dolphindigitalflux.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.dolphintechno.dolphindigitalflux.R;
import com.dolphintechno.dolphindigitalflux.model.VideoInfo;
import com.dolphintechno.dolphindigitalflux.singleton.MyVolleySingleton;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class AdapterTrainingVideos extends RecyclerView.Adapter<AdapterTrainingVideos.VideoViewHolder> {

    List<VideoInfo> videoInfoList;
    Context context;

    public AdapterTrainingVideos(List<VideoInfo> videoInfoList, Context context) {
        this.videoInfoList = videoInfoList;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video, viewGroup, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoViewHolder videoViewHolder, int i) {

        VideoInfo videoInfo = videoInfoList.get(i);

        videoViewHolder.tv_training_video_name.setText(videoInfo.getAuthName());
        videoViewHolder.tv_training_video_date.setText(videoInfo.getTime());
        videoViewHolder.tv_training_video_desc.setText(videoInfo.getvDesc());

        final String videoLink = videoInfo.getvLink();


//        String videoId[] = videoLink.split("embed/"); //you will get this video id "tResEeK6P5I"

        String thumbnailMq = "http://img.youtube.com/vi/"+videoInfo.getYouTubeVideoId()+"/mqdefault.jpg"; //medium quality thumbnail

        String thumbnailHq = "http://img.youtube.com/vi/"+videoInfo.getYouTubeVideoId()+"/hqdefault.jpg"; //high quality thumbnail

        ImageRequest imageRequest = new ImageRequest(thumbnailHq, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                videoViewHolder.imv_training_video_thumbnail.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, ""+error, Toast.LENGTH_LONG).show();
                    }
                });
        MyVolleySingleton.getInstance(context).addToRequestQueue(imageRequest);

        videoViewHolder.imv_training_video_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == videoViewHolder.imv_training_video_thumbnail){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoLink));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
//                    Intent intent = new Intent(String.valueOf(Uri.parse(videoLink)));
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return videoInfoList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {

        public ImageView imv_training_video_thumbnail;

        public CircularImageView civ_training_video_brands;

        public TextView tv_training_video_name, tv_training_video_date, tv_training_video_desc;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            imv_training_video_thumbnail = (ImageView) itemView.findViewById(R.id.imv_training_video_thumbnail);

            civ_training_video_brands = (CircularImageView) itemView.findViewById(R.id.civ_training_video_brands);

            tv_training_video_name = (TextView) itemView.findViewById(R.id.tv_training_video_name);
            tv_training_video_date = (TextView) itemView.findViewById(R.id.tv_training_video_date);
            tv_training_video_desc = (TextView) itemView.findViewById(R.id.tv_training_video_desc);

        }
    }
}
