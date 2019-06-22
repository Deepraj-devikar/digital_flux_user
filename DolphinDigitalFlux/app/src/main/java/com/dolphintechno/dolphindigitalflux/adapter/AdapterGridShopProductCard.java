package com.dolphintechno.dolphindigitalflux.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.dolphintechno.dolphindigitalflux.R;
import com.dolphintechno.dolphindigitalflux.helper.URLs;
import com.dolphintechno.dolphindigitalflux.model.ShopProduct;
import com.dolphintechno.dolphindigitalflux.singleton.MyVolleySingleton;

import java.util.ArrayList;
import java.util.List;

public class AdapterGridShopProductCard extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ShopProduct> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnMoreButtonClickListener onMoreButtonClickListener;

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setOnMoreButtonClickListener(final OnMoreButtonClickListener onMoreButtonClickListener) {
        this.onMoreButtonClickListener = onMoreButtonClickListener;
    }

    public AdapterGridShopProductCard(Context context, List<ShopProduct> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView tv_item_shop_product_card_product_name;
        public TextView tv_item_shop_product_card_product_mrp;
        public ImageButton more;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            tv_item_shop_product_card_product_name = (TextView) v.findViewById(R.id.tv_item_shop_product_card_product_name);
            tv_item_shop_product_card_product_mrp= (TextView) v.findViewById(R.id.tv_item_shop_product_card_product_mrp);
            more = (ImageButton) v.findViewById(R.id.more);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_product_card, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            final ShopProduct p = items.get(position);
            view.tv_item_shop_product_card_product_name.setText(p.productName);
            view.tv_item_shop_product_card_product_mrp.setText(p.productMrp);
            fetchImage(view.image, p.imageNameOne);
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

            view.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onMoreButtonClickListener == null) return;
                    onMoreButtonClick(view, p);
                }
            });
        }
    }

    private void fetchImage(final ImageView image, String imageNameOne){

        final String url_img = URLs.url_product_iamge+imageNameOne;

//        Toast.makeText(ctx, "Hey i am here", Toast.LENGTH_LONG).show();

        ImageRequest imageRequest = new ImageRequest(url_img, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                image.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_INSIDE, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ctx, String.valueOf(error), Toast.LENGTH_LONG).show();
                    }
                });
        MyVolleySingleton.getInstance(ctx).addToRequestQueue(imageRequest);
    }

    private void onMoreButtonClick(final View view, final ShopProduct p) {
        PopupMenu popupMenu = new PopupMenu(ctx, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMoreButtonClickListener.onItemClick(view, p, item);
                return true;
            }
        });
        popupMenu.inflate(R.menu.menu_product_more);
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, ShopProduct obj, int pos);
    }

    public interface OnMoreButtonClickListener {
        void onItemClick(View view, ShopProduct obj, MenuItem item);
    }

}
