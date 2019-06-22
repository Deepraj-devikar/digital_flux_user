package com.dolphintechno.dolphindigitalflux.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dolphintechno.dolphindigitalflux.R;
import com.dolphintechno.dolphindigitalflux.adapter.AdapterMainCategory;
import com.dolphintechno.dolphindigitalflux.helper.URLs;
import com.dolphintechno.dolphindigitalflux.model.MainCategory;
import com.dolphintechno.dolphindigitalflux.singleton.MyVolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectMainCategory extends AppCompatActivity {

    List<MainCategory> mainCategoryList;
    MainCategory mainCategory;

    RecyclerView recyclerViewMainCategory;
    LinearLayoutManager linearLayoutManager;
    AdapterMainCategory adapterMainCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_main_category);

        recyclerViewMainCategory = (RecyclerView) findViewById(R.id.recy_view_sel_main_cat);
        recyclerViewMainCategory.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewMainCategory.setLayoutManager(linearLayoutManager);

        fetchMainCategory();

    }

    private void fetchMainCategory() {

        Toast.makeText(getApplicationContext(), "it is in front", Toast.LENGTH_LONG).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.url_add_product_data_mcat,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isMainCategory = jsonObject.getBoolean(URLs.isAddProductData);
                            if (isMainCategory){
                                mainCategoryList = new ArrayList<>(100);
                                JSONArray jsonArray = jsonObject.getJSONArray(URLs.keyAddProductDataThisList);
                                for (int i = 0; i < jsonArray.length(); i++){
                                    mainCategory = new MainCategory();
                                    JSONObject jsonData = jsonArray.getJSONObject(i);
                                    mainCategory.setCategoryId(jsonData.getString(URLs.keyAddProductDataThisId));
                                    mainCategory.setCategoryName(jsonData.getString(URLs.keyAddProductDataThisName));
                                    Toast.makeText(getApplicationContext(), mainCategory.getCategoryName(),Toast.LENGTH_LONG).show();
                                    mainCategoryList.add(mainCategory);
                                }
                            }
//                            Toast.makeText(getApplicationContext(), "it is here", Toast.LENGTH_LONG);
                            adapterMainCategory = new AdapterMainCategory(mainCategoryList, getApplicationContext());
                            recyclerViewMainCategory.setAdapter(adapterMainCategory);
//                            Toast.makeText(getApplicationContext(), "--"+response, Toast.LENGTH_LONG).show();
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



}
