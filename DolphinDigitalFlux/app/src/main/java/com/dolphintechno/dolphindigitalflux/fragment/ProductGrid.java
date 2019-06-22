package com.dolphintechno.dolphindigitalflux.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dolphintechno.dolphindigitalflux.R;
import com.dolphintechno.dolphindigitalflux.activity.ShopingProductDetail;
import com.dolphintechno.dolphindigitalflux.adapter.AdapterGridShopProductCard;
import com.dolphintechno.dolphindigitalflux.helper.BundleKeys;
import com.dolphintechno.dolphindigitalflux.helper.IntentKeys;
import com.dolphintechno.dolphindigitalflux.helper.URLs;
import com.dolphintechno.dolphindigitalflux.model.ShopProduct;
import com.dolphintechno.dolphindigitalflux.singleton.MyVolleySingleton;
import com.dolphintechno.dolphindigitalflux.utils.Tools;
import com.dolphintechno.dolphindigitalflux.widget.SpacingItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProductGrid extends Fragment {

    Context context;
    List<ShopProduct> shopProductList;
    ShopProduct shopProduct;

    AdapterGridShopProductCard mAdapter;
    RecyclerView recyclerView;
    View root;

    public ProductGrid() {
    }

    public static ProductGrid newInstance() {
        ProductGrid fragment = new ProductGrid();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_product_grid, container, false);

        context = getContext();
        shopProductList = new ArrayList<>();

        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(getActivity(), 8), true));
        recyclerView.setHasFixedSize(true);

//        List<ShopProduct> items = DataGenerator.getShoppingProduct(getActivity());
//        Collections.shuffle(items);


        fetchProoducts();

        //set data and list adapter



        return root;
    }


    void fetchProoducts(){

//        Toast.makeText(context, "in Product grid", Toast.LENGTH_LONG).show();

        final String strCategoryId = getArguments().getString(BundleKeys.keyShopSubCategoryCategoryId);
        String strCategoryName = getArguments().getString(BundleKeys.keyShopSubCategoryCategoryName);
        final String strSubCategoryId = getArguments().getString(BundleKeys.keyShopSubCategorySubCategoryId);

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.url_shop_sub_category,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject myJson = new JSONObject(response);

                            TypedArray drw_arr = context.getResources().obtainTypedArray(R.array.shop_product_image);

                            if (myJson.getBoolean(URLs.keyProductFetch)){
                                JSONArray myJsonArray = myJson.getJSONArray(URLs.keyShopSubCategoryDataArray);

                                for (int i = 0; i < myJsonArray.length(); i++){

                                    JSONObject myJsonData = myJsonArray.getJSONObject(i);

                                    shopProduct = new ShopProduct();

                                    shopProduct.image = drw_arr.getResourceId(i, -1);
                                    shopProduct.productId = myJsonData.getString(URLs.keyProductId);
                                    shopProduct.productName = myJsonData.getString(URLs.keyProductName);
                                    shopProduct.productMrp = myJsonData.getString(URLs.keyProductMrp);
                                    shopProduct.imageNameOne = myJsonData.getString(URLs.keyProductImageOne);
                                    shopProduct.imageNameTwo = myJsonData.getString(URLs.keyProductImageTwo);
                                    shopProduct.imageNameThree = myJsonData.getString(URLs.keyProductImageThree);
                                    shopProduct.imageDrw = context.getResources().getDrawable(shopProduct.image);
                                    shopProductList.add(shopProduct);
                                }

                                Collections.shuffle(shopProductList);

                                mAdapter = new AdapterGridShopProductCard(getActivity(), shopProductList);
                                recyclerView.setAdapter(mAdapter);


                                // on item list clicked
                                mAdapter.setOnItemClickListener(new AdapterGridShopProductCard.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, ShopProduct obj, int position) {
                                        Snackbar.make(root, "Item " + obj.productName + " clicked", Snackbar.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getContext(), ShopingProductDetail.class);
                                        intent.putExtra(IntentKeys.keyProductKeyForShopingProductDetail, obj.productId);
                                        intent.putExtra(IntentKeys.keyProductImgOne, obj.imageNameOne);
                                        intent.putExtra(IntentKeys.keyProductImgTwo, obj.imageNameTwo);
                                        intent.putExtra(IntentKeys.keyProductImgThree, obj.imageNameThree);
                                        startActivity(intent);

//                                        startActivity(new Intent(getContext(), ShopingProductDetail.class));
                                    }
                                });

                                mAdapter.setOnMoreButtonClickListener(new AdapterGridShopProductCard.OnMoreButtonClickListener() {
                                    @Override
                                    public void onItemClick(View view, ShopProduct obj, MenuItem item) {
                                        Snackbar.make(root, obj.productName + " (" + item.getTitle() + ") clicked", Snackbar.LENGTH_SHORT).show();
                                    }
                                });

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, String.valueOf(error), Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(URLs.keyProductCategoryId_send, strCategoryId);
                params.put(URLs.keyProductSubcategoryId_send, strSubCategoryId);
                return params;
            }
        };

        MyVolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

}