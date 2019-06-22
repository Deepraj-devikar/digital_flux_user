package com.dolphintechno.dolphindigitalflux.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.dolphintechno.dolphindigitalflux.activity.ShopSubCategory;
import com.dolphintechno.dolphindigitalflux.helper.IntentKeys;
import com.dolphintechno.dolphindigitalflux.helper.URLs;
import com.dolphintechno.dolphindigitalflux.model.ShopCategory;
import com.dolphintechno.dolphindigitalflux.singleton.MyVolleySingleton;

import java.util.ArrayList;
import java.util.List;

public class AdapterShop extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ShopCategory> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, ShopCategory obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterShop(Context context, List<ShopCategory> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            title = (TextView) v.findViewById(R.id.title);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_category_card, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final ShopCategory p = items.get(position);
            view.title.setText(p.title);
//            view.image.setImageDrawable(p.imageDrw);
            ImageRequest imageRequest = new ImageRequest(URLs.url_product_category_iamge + p.ctegoryImage,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            view.image.setImageBitmap(response);
                        }
                    }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ctx, "img req"+error, Toast.LENGTH_LONG).show();
                        }
                    });
            MyVolleySingleton.getInstance(ctx).addToRequestQueue(imageRequest);
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);

                        Intent intent = new Intent(ctx, ShopSubCategory.class);
                        intent.putExtra(IntentKeys.keyShopCategoryId, p.categoryId);
                        intent.putExtra(IntentKeys.keyShopCategoryName, p.title);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ctx.startActivity(intent);
                    }

                }


            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}