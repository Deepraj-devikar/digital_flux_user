package com.dolphintechno.dolphindigitalflux.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dolphintechno.dolphindigitalflux.R;
import com.dolphintechno.dolphindigitalflux.adapter.AdapterMainCategory;
import com.dolphintechno.dolphindigitalflux.adapter.AdapterSubCategory;
import com.dolphintechno.dolphindigitalflux.data.MySharedPreferences;
import com.dolphintechno.dolphindigitalflux.helper.SharedPrefKeys;
import com.dolphintechno.dolphindigitalflux.helper.URLs;
import com.dolphintechno.dolphindigitalflux.model.MainCategory;
import com.dolphintechno.dolphindigitalflux.model.SubCategory;
import com.dolphintechno.dolphindigitalflux.singleton.MyVolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProductSelectCategory extends AppCompatActivity implements View.OnClickListener {


    MySharedPreferences dataProccessor;

    String strProductName, strProductInfo, strProductMrp, strProductSellingPrice, strProductGST;

    List<MainCategory> mainCategoryList;
    List<MainCategory> categoryList;
    List<SubCategory> subCategoryList;
    List<MainCategory> brandList;
    MainCategory mcat;
    SubCategory scat;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    AdapterMainCategory adapterMainCategory;
    AdapterMainCategory adapterCategory;
    AdapterSubCategory adapterSubCategory;
    AdapterMainCategory adapterBrand;

    EditText et_add_product_main_category, et_add_product_category, et_add_product_sub_category, et_add_product_brand, et_add_product_admin_charge;

    TextView tv_selection_text;

    Button bt_submit_add_product;


    String mainCatId, catId, subCatId, brandId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_select_category);

        mainCategoryList = new ArrayList<>();
        categoryList = new ArrayList<>();
        subCategoryList = new ArrayList<>();
        brandList = new ArrayList<>();

        et_add_product_main_category = (EditText) findViewById(R.id.et_add_product_main_category);
        et_add_product_category = (EditText) findViewById(R.id.et_add_product_category);
        et_add_product_sub_category = (EditText) findViewById(R.id.et_add_product_sub_category);
        et_add_product_brand = (EditText) findViewById(R.id.et_add_product_brand);
        et_add_product_admin_charge = (EditText) findViewById(R.id.et_add_product_admin_charge);

        tv_selection_text = (TextView) findViewById(R.id.tv_selection_text);

        bt_submit_add_product = (Button) findViewById(R.id.bt_submit_add_product);

        Intent comingIntent = getIntent();
        strProductName = comingIntent.getStringExtra("Product Name");
        strProductInfo = comingIntent.getStringExtra("Product Info");
        strProductMrp = comingIntent.getStringExtra("Product MRP");
        strProductSellingPrice = comingIntent.getStringExtra("Product Selling Price");
        strProductGST = comingIntent.getStringExtra("Product GST");

        recyclerView = (RecyclerView) findViewById(R.id.rec_view_select_category) ;
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        fetchMainCategory();

        bt_submit_add_product.setOnClickListener(this);


    }

    private void fetchMainCategory() {

        tv_selection_text.setText("Select Main Category");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.url_add_product_data_mcat,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {


                            JSONObject jsonObject = new JSONObject(response);
                            boolean isMainCategory = jsonObject.getBoolean(URLs.isAddProductData);
                            if (isMainCategory){
                                JSONArray jsonArray = jsonObject.getJSONArray(URLs.keyAddProductDataThisList);
                                for (int i = 0; i < jsonArray.length(); i++){
                                    mcat = new MainCategory();
                                    JSONObject jsonData = jsonArray.getJSONObject(i);
                                    mcat.setCategoryId(jsonData.getString(URLs.keyAddProductDataThisId));
                                    mcat.setCategoryName(jsonData.getString(URLs.keyAddProductDataThisName));
//                                    Toast.makeText(getApplicationContext(), mcat.getCategoryName(),Toast.LENGTH_LONG).show();
                                    mainCategoryList.add(mcat);
                                }
                                adapterMainCategory = new AdapterMainCategory(mainCategoryList, getApplicationContext());
                                recyclerView.setAdapter(adapterMainCategory);

                                // on item list clicked
                                adapterMainCategory.setOnItemClickListener(new AdapterMainCategory.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterMainCategory.MainCategoryViewHolder view, MainCategory obj, int position) {
                                        Toast.makeText(getApplicationContext(), "--"+obj.getCategoryId(), Toast.LENGTH_LONG).show();
                                        mainCatId = obj.getCategoryId();
                                        et_add_product_main_category.setText(obj.getCategoryName());
                                        fetchCategory(obj.getCategoryId());
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

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("c", URLs.addProductDataCode);
                return params;
            }
        };
        MyVolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void fetchCategory(final String mainCategoryId) {

        tv_selection_text.setText("Select Category");

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URLs.url_add_product_data_cat,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            boolean isMainCategory = jsonObject.getBoolean(URLs.isAddProductData);
                            if (isMainCategory){
                                JSONArray jsonArray = jsonObject.getJSONArray(URLs.keyAddProductDataThisList);
                                for (int i = 0; i < jsonArray.length(); i++){
                                    mcat = new MainCategory();
                                    JSONObject jsonData = jsonArray.getJSONObject(i);
                                    mcat.setCategoryId(jsonData.getString(URLs.keyAddProductDataThisId));
                                    mcat.setCategoryName(jsonData.getString(URLs.keyAddProductDataThisName));
//                                    Toast.makeText(getApplicationContext(), mcat.getCategoryName(),Toast.LENGTH_LONG).show();
                                    categoryList.add(mcat);
                                }
                                adapterCategory = new AdapterMainCategory(categoryList, getApplicationContext());
                                recyclerView.setAdapter(adapterCategory);

                                // on item list clicked
                                adapterCategory.setOnItemClickListener(new AdapterMainCategory.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterMainCategory.MainCategoryViewHolder view, MainCategory obj, int position) {
                                        Toast.makeText(getApplicationContext(), "--"+obj.getCategoryId(), Toast.LENGTH_LONG).show();
                                        catId = obj.getCategoryId();
                                        et_add_product_category.setText(obj.getCategoryName());
                                        fetchSubCategory(mainCategoryId, obj.getCategoryId());

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

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("c", URLs.addProductDataCode);
                params.put(URLs.mCat, mainCategoryId);
                return params;
            }
        };
        MyVolleySingleton.getInstance(this).addToRequestQueue(stringRequest1);

    }


    private void fetchSubCategory(final String mainCategoryId, final String categoryId) {

        tv_selection_text.setText("Select Sub Category");

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, URLs.url_add_product_data_scat,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            boolean isSubCategory = jsonObject.getBoolean(URLs.isAddProductData);
                            if (isSubCategory){
                                subCategoryList = new ArrayList<>(100);
                                JSONArray jsonArray = jsonObject.getJSONArray(URLs.keyAddProductDataThisList);
                                for (int i = 0; i < jsonArray.length(); i++){
                                    scat = new SubCategory();
                                    JSONObject jsonData = jsonArray.getJSONObject(i);
                                    scat.setCategoryId(jsonData.getString(URLs.keyAddProductDataThisId));
                                    scat.setCategoryName(jsonData.getString(URLs.keyAddProductDataThisName));
                                    scat.setAdminCharge(jsonData.getString(URLs.keyAddProductDataThisAdminCharge));
//                                    Toast.makeText(getApplicationContext(), mcat.getCategoryName(),Toast.LENGTH_LONG).show();
                                    subCategoryList.add(scat);
                                }
                                adapterSubCategory = new AdapterSubCategory(subCategoryList, getApplicationContext());
                                recyclerView.setAdapter(adapterSubCategory);

                                // on item list clicked
                                adapterSubCategory.setOnItemClickListener(new AdapterSubCategory.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterSubCategory.SubCategoryViewHolder view, SubCategory obj, int position) {
                                        Toast.makeText(getApplicationContext(), "--"+obj.getCategoryId(), Toast.LENGTH_LONG).show();
                                        subCatId = obj.getCategoryId();
                                        et_add_product_sub_category.setText(obj.getCategoryName());

                                        et_add_product_admin_charge.setText(obj.getAdminCharge());

                                        fetchBrand(mainCategoryId, categoryId, obj.getCategoryId());



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

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("c", URLs.addProductDataCode);
                params.put(URLs.mCat, mainCategoryId);
                params.put(URLs.cat, categoryId);
                return params;
            }
        };
        MyVolleySingleton.getInstance(this).addToRequestQueue(stringRequest2);

    }

    private void fetchBrand(final String mainCategoryId, final String categoryId, final String subCategoryId) {

        tv_selection_text.setText("Select Brand");

        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, URLs.url_add_product_data_brand,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            boolean isMainCategory = jsonObject.getBoolean(URLs.isAddProductData);
                            if (isMainCategory){
                                JSONArray jsonArray = jsonObject.getJSONArray(URLs.keyAddProductDataThisList);
                                for (int i = 0; i < jsonArray.length(); i++){
                                    mcat = new MainCategory();
                                    JSONObject jsonData = jsonArray.getJSONObject(i);
                                    mcat.setCategoryId(jsonData.getString(URLs.keyAddProductDataThisId));
                                    mcat.setCategoryName(jsonData.getString(URLs.keyAddProductDataThisName));
//                                    Toast.makeText(getApplicationContext(), mcat.getCategoryName(),Toast.LENGTH_LONG).show();
                                    brandList.add(mcat);
                                }
                                adapterBrand = new AdapterMainCategory(brandList, getApplicationContext());
                                recyclerView.setAdapter(adapterBrand);

                                // on item list clicked
                                adapterBrand.setOnItemClickListener(new AdapterMainCategory.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterMainCategory.MainCategoryViewHolder view, MainCategory obj, int position) {
                                        Toast.makeText(getApplicationContext(), "--"+obj.getCategoryId(), Toast.LENGTH_LONG).show();
                                        brandId = obj.getCategoryId();
                                        et_add_product_brand.setText(obj.getCategoryName());


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

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("c", URLs.addProductDataCode);
                params.put(URLs.mCat, mainCategoryId);
                params.put(URLs.cat, categoryId);
                params.put(URLs.sCat, subCategoryId);
                return params;
            }
        };
        MyVolleySingleton.getInstance(this).addToRequestQueue(stringRequest3);

    }


    @Override
    public void onClick(View v) {
        if (v == bt_submit_add_product){
            final String strmCat, strCat, strsCat, strBrand;

            strmCat = et_add_product_main_category.getText().toString();
            strCat = et_add_product_category.getText().toString();
            strsCat = et_add_product_sub_category.getText().toString();
            strBrand = et_add_product_brand.getText().toString();

            if (TextUtils.isEmpty(strmCat)){
                et_add_product_main_category.setError("Select Category");
            }else if (TextUtils.isEmpty(strCat)){
                et_add_product_main_category.setError("Select Category");
            }else if (TextUtils.isEmpty(strsCat)){
                et_add_product_main_category.setError("Select Category");
            }else if (TextUtils.isEmpty(strBrand)){
                et_add_product_main_category.setError("Select Category");
            }else {
                final String strAdminCharge = et_add_product_admin_charge.getText().toString();

                dataProccessor = new MySharedPreferences(this);

                /*
                 * Fetching User Id For Recognizing User
                 * So We Can Get Users Info.
                 */
                final String str_unique_id = dataProccessor.getStr(SharedPrefKeys.user_id);

                final String strFirstName, strLastName, strName;

                strFirstName = dataProccessor.getStr(SharedPrefKeys.first_name);
                strLastName = dataProccessor.getStr(SharedPrefKeys.last_name);
                strName = strFirstName+" "+strLastName;


                Intent intent = new Intent(this, UploadProductImage.class);
                intent.putExtra("p name",strProductName);
                intent.putExtra("p info",strProductInfo);
                intent.putExtra("p mrp",strProductMrp);
                intent.putExtra("p selling price",strProductSellingPrice);
                intent.putExtra("m cat",mainCatId);
                intent.putExtra("cat",catId);
                intent.putExtra("s cat",subCatId);
                intent.putExtra("brand",brandId);
                intent.putExtra("name",strName);
                intent.putExtra("uid",str_unique_id);
                intent.putExtra("p gst",strProductGST);
                intent.putExtra("admin charge",strAdminCharge);
                startActivity(intent);
//                Toast.makeText()


            }
        }
    }



}
