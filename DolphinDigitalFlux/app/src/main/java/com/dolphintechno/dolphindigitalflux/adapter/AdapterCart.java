package com.dolphintechno.dolphindigitalflux.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.dolphintechno.dolphindigitalflux.R;
import com.dolphintechno.dolphindigitalflux.activity.Cart;
import com.dolphintechno.dolphindigitalflux.helper.URLs;
import com.dolphintechno.dolphindigitalflux.model.CartProduct;
import com.dolphintechno.dolphindigitalflux.singleton.MyVolleySingleton;
import com.dolphintechno.dolphindigitalflux.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdapterCart extends RecyclerView.Adapter<AdapterCart.CartItemViewHolder>{

    private List<CartProduct> cartProductList;
    Context context;

    public AdapterCart(List<CartProduct> cartProductList, Context context) {
        this.cartProductList = cartProductList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cart, viewGroup, false);

        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartItemViewHolder cartItemViewHolder, int i) {

        final CartProduct cartProduct = cartProductList.get(i);
        cartItemViewHolder.tv_cart_item_name.setText(cartProduct.getProductName());
        cartItemViewHolder.tv_cart_item_seller_name.setText(cartProduct.getProductSellerName());
        cartItemViewHolder.tv_cart_item_product_mrp.setText(cartProduct.getProductMrp());
        cartItemViewHolder.tv_cart_item_quantity.setText(cartProduct.getProductQuantity());
        cartItemViewHolder.tv_cart_item_product_id.setText(cartProduct.getProductId());

        final String stringImageUrl = URLs.url_product_iamge + cartProduct.getImgNameDB();

        ImageRequest imageRequest = new ImageRequest(stringImageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                cartItemViewHolder.img_v_cart_item_image.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_INSIDE, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        MyVolleySingleton.getInstance(context).addToRequestQueue(imageRequest);

//        cartItemViewHolder.lyt_parent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mOnItemClickListener != null) {
//                    mOnItemClickListener.onItemClick(view, items.get(position), position);
//                }
//            }
//        });
//
//        view.more.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (onMoreButtonClickListener == null) return;
//                onMoreButtonClick(view, p);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return cartProductList.size();
    }

    public class CartItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView img_v_cart_item_image;
        public TextView tv_cart_item_name, tv_cart_item_seller_name, tv_cart_item_product_mrp, tv_cart_item_quantity, tv_cart_item_product_id;
        public ImageButton img_btn_cart_item_increse_quantity, img_btn_cart_item_decrese_quantity;
        public Button btn_item_cart_update;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);

            img_v_cart_item_image = (ImageView) itemView.findViewById(R.id.img_v_cart_item_image);
            tv_cart_item_name = (TextView) itemView.findViewById(R.id.tv_cart_item_name);
            tv_cart_item_seller_name = (TextView) itemView.findViewById(R.id.tv_cart_item_seller_name);
            tv_cart_item_product_mrp = (TextView) itemView.findViewById(R.id.tv_cart_item_product_mrp);
            tv_cart_item_quantity = (TextView) itemView.findViewById(R.id.tv_cart_item_quantity);
            tv_cart_item_product_id = (TextView) itemView.findViewById(R.id.tv_cart_item_product_id);
            img_btn_cart_item_increse_quantity = (ImageButton) itemView.findViewById(R.id.img_btn_cart_item_increse_quantity);
            img_btn_cart_item_decrese_quantity = (ImageButton) itemView.findViewById(R.id.img_btn_cart_item_decrese_quantity);
            btn_item_cart_update = (Button) itemView.findViewById(R.id.btn_item_cart_update);

            img_btn_cart_item_increse_quantity.setOnClickListener(this);
            img_btn_cart_item_decrese_quantity.setOnClickListener(this);
            btn_item_cart_update.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v == img_btn_cart_item_increse_quantity){
                int qty1 = Integer.parseInt(tv_cart_item_quantity.getText().toString());

                qty1 = qty1 + 1;

                tv_cart_item_quantity.setText(String.valueOf(qty1));

                btn_item_cart_update.setVisibility(View.VISIBLE);

            }else if (v == img_btn_cart_item_decrese_quantity){
                int qty1 = Integer.parseInt(tv_cart_item_quantity.getText().toString());

                qty1 = qty1 - 1;

                tv_cart_item_quantity.setText(String.valueOf(qty1));

                btn_item_cart_update.setVisibility(View.VISIBLE);

            }else if (v == btn_item_cart_update){

                final String strProductId = tv_cart_item_product_id.getText().toString();
                final String strQuantity = tv_cart_item_quantity.getText().toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.url_update_cart_item_quantity,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean isCartItemUpdated = jsonObject.getBoolean(URLs.keyIsCartItemUpdated);
                                    if (isCartItemUpdated){
                                        btn_item_cart_update.setVisibility(View.GONE);
                                        Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(context, Cart.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                    }else {
                                        Toast.makeText(context, "something wrong", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(URLs.url_update_cart_item_quantity, String.valueOf(error));
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put(URLs.keyCartItemProductID, strProductId);
                        params.put(URLs.keyCartItemQuantity, strQuantity);
                        params.put(URLs.keyCartItemCode, URLs.cartItemCodeValue);
                        return params;
                    }
                };

                MyVolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
            }
        }



    }

}
