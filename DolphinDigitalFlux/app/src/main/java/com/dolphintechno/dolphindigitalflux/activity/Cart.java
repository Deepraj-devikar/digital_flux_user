package com.dolphintechno.dolphindigitalflux.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.balysv.materialripple.MaterialRippleLayout;
import com.dolphintechno.dolphindigitalflux.R;
import com.dolphintechno.dolphindigitalflux.adapter.AdapterCart;
import com.dolphintechno.dolphindigitalflux.data.MySharedPreferences;
import com.dolphintechno.dolphindigitalflux.helper.SharedPrefKeys;
import com.dolphintechno.dolphindigitalflux.helper.URLs;
import com.dolphintechno.dolphindigitalflux.model.CartProduct;
import com.dolphintechno.dolphindigitalflux.singleton.MyVolleySingleton;
import com.dolphintechno.dolphindigitalflux.utils.Tools;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart extends AppCompatActivity implements View.OnClickListener {

    MySharedPreferences dataProccessor;

    private ActionBar actionBar;
    private Toolbar toolbar;
    private Menu menu_navigation;
    private DrawerLayout drawer;
    private View navigation_header;
    private boolean is_account_mode = false;

    List<CartProduct> cartProductList;
    CartProduct cartProduct;
    RecyclerView recycler_view_cart;
    AdapterCart adapterCart;
    LinearLayoutManager linearLayoutManager;

    TextView tv_drawer_header_name, tv_drawer_header_email;
    TextView tv_cart_btn_checkout, tv_cart_total;

    MaterialRippleLayout material_ripple_lauout_cart_btn_checkout;

    String strFirstName;
    String strLastName;
    String strEmail;
    String strName;
    String str_unique_id;

    String profile_pic_name;
    CircularImageView avatar;

    int totalQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initNavigationMenu();
        avatar = (CircularImageView) navigation_header.findViewById(R.id.avatar);

        dataProccessor = new MySharedPreferences(this);

        /*
         * Fetching User Id For Recognizing User
         * So We Can Get Users Info.
         */
        str_unique_id = dataProccessor.getStr(SharedPrefKeys.user_id);

        totalQuantity = 0;

        material_ripple_lauout_cart_btn_checkout = (MaterialRippleLayout) findViewById(R.id.material_ripple_lauout_cart_btn_checkout);

        tv_cart_btn_checkout = (TextView) findViewById(R.id.tv_cart_btn_checkout);
        tv_cart_total = (TextView) findViewById(R.id.tv_cart_total);

        cartProductList = new ArrayList<>();
        recycler_view_cart = (RecyclerView) findViewById(R.id.recycler_view_cart);
        recycler_view_cart.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recycler_view_cart.setLayoutManager(linearLayoutManager);


        setInfo();
        fetchCartItems();
        initToolbar();

        material_ripple_lauout_cart_btn_checkout.setOnClickListener(this);
        tv_cart_btn_checkout.setOnClickListener(this);


    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.grey_60), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.grey_5);
        Tools.setSystemBarLight(this);
    }

    private void setInfo() {
        strFirstName = dataProccessor.getStr(SharedPrefKeys.first_name);
        strLastName = dataProccessor.getStr(SharedPrefKeys.last_name);
        strEmail = dataProccessor.getStr(SharedPrefKeys.email_id);
        strName = strFirstName+" "+strLastName;

        tv_drawer_header_name = (TextView) navigation_header.findViewById(R.id.tv_drawer_header_name);
        tv_drawer_header_email = (TextView) navigation_header.findViewById(R.id.tv_drawer_header_email);
        tv_drawer_header_name.setText(strName);
        tv_drawer_header_email.setText(strEmail);


        profile_pic_name = dataProccessor.getStr(SharedPrefKeys.profile_pic);

        String strUrlAvtarImg = URLs.url_profile_pic+profile_pic_name;
//                            Toast.makeText(getApplicationContext(), "--"+strUrlAvtarImg, Toast.LENGTH_LONG).show();
        ImageRequest imageRequest = new ImageRequest(strUrlAvtarImg, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                avatar.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);
    }

    void fetchCartItems(){
        String strCodeVal = "&"+URLs.keyCartCode+"="+URLs.cartCode;
        String strUrlGetCartItemes = URLs.url_cart+"?"+URLs.keyCartUserId_send+"="+str_unique_id+strCodeVal;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, strUrlGetCartItemes,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        TypedArray drw_arr = getApplicationContext().getResources().obtainTypedArray(R.array.shop_product_image);
                        totalQuantity = 0;
                        try {
                            JSONObject response_json = new JSONObject(response);
                            JSONObject jsonData, jsonItemInfo;
                            boolean isProductInCart = response_json.getBoolean(URLs.kayIsProductInCart);
                            if (isProductInCart){
                                JSONArray json_array = response_json.getJSONArray(URLs.kayCartDataArr);
                                for (int i = 0; i < json_array.length(); i++){
                                    jsonData = json_array.getJSONObject(i);
                                    boolean isFoundCartProductInfo = jsonData.getBoolean(URLs.keyIsFoundCartProductInfo);
                                    if (isFoundCartProductInfo){
                                        cartProduct = new CartProduct();
                                        jsonItemInfo = jsonData.getJSONObject(URLs.kayCartProductInfo);
                                        cartProduct.image = drw_arr.getResourceId(i, -1);
                                        cartProduct.setProductId(jsonItemInfo.getString(URLs.kayCartProductId));
                                        cartProduct.setProductName(jsonItemInfo.getString(URLs.kayCartProductName));
                                        cartProduct.setProductMrp(jsonItemInfo.getString(URLs.kayCartProductMrp));
                                        cartProduct.setProductSellerName(jsonItemInfo.getString(URLs.kayCartProductSellerName));
                                        cartProduct.setProductShippingCharge(jsonItemInfo.getString(URLs.kayCartProductShippingCharge));
                                        cartProduct.setImgNameDB(jsonItemInfo.getString(URLs.keyCartProductImageName));
                                        cartProduct.setProductQuantity(jsonItemInfo.getString(URLs.kayCartProductQuantity));
                                        cartProduct.imageDrw = getApplicationContext().getResources().getDrawable(cartProduct.image);
                                        totalQuantity = totalQuantity + Integer.parseInt(cartProduct.getProductQuantity());
                                        cartProductList.add(cartProduct);
                                    }
                                }
                                adapterCart = new AdapterCart(cartProductList, getApplicationContext());
                                recycler_view_cart.setAdapter(adapterCart);
                                tv_cart_total.setText(String.valueOf(totalQuantity));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), String.valueOf(error), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(URLs.keyCartUserId_send, str_unique_id);
                params.put(URLs.keyCartCode, URLs.cartCode);
                return params;
            }
        };
        MyVolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_setting, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.grey_60));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            drawer.openDrawer(GravityCompat.START);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }



    private void initNavigationMenu() {
        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view_cart);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_cart);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                updateCounter(nav_view);
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                onItemNavigationClicked(item);
                return true;
            }
        });

        updateCounter(nav_view);
        menu_navigation = nav_view.getMenu();

        // navigation header
        navigation_header = nav_view.getHeaderView(0);
    }

    private void onItemNavigationClicked(MenuItem item) {



        if (!is_account_mode) {
            drawer.closeDrawers();
            navigate(item.getTitle());

        } else {
            CircularImageView avatar = (CircularImageView) navigation_header.findViewById(R.id.avatar);
            switch (item.getItemId()) {
                case 1000:
                    avatar.setImageResource(R.drawable.photo_male_5);
                    break;
                case 2000:
                    avatar.setImageResource(R.drawable.photo_male_2);
                    break;
                default:
                    Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void navigate(CharSequence title) {
        if (title.equals("Dashboard")){
            Intent intent = new Intent(this, HomeScreen.class);
            startActivity(intent);
        }else if (title.equals("Wallet")){
            Intent intent = new Intent(this, WalletHistory.class);
            startActivity(intent);
        }else if (title.equals("Team")){
            Intent intent = new Intent(this, NetworkList.class);
            startActivity(intent);
        }else if (title.equals("Add User")){
            Intent intent = new Intent(this, AddNew.class);
            startActivity(intent);
        }else if (title.equals("Withdraw")){
            Intent intent = new Intent(this, Withdraw.class);
            startActivity(intent);
        }else if (title.equals("Shop")){
            Intent intent = new Intent(this, Shop.class);
            startActivity(intent);
        }else if (title.equals("Cart")){
            Intent intent = new Intent(this, Cart.class);
            startActivity(intent);
        }else if (title.equals("Add Product")){
            Intent intent = new Intent(this, AddProduct.class);
            startActivity(intent);
        }else if (title.equals("Attendance")){
            Intent intent = new Intent(this, Attendance.class);
            startActivity(intent);
        }else if (title.equals("Taks")){
            Intent intent = new Intent(this, Task.class);
            startActivity(intent);
        }else if (title.equals("FAQ")){
            Intent intent = new Intent(this, HomeScreen.class);
            startActivity(intent);
//            sendWhatsappContacts();
        }else if (title.equals("Company")){
            Intent intent = new Intent(this, Company.class);
            startActivity(intent);
        }else if (title.equals("Share")){
            Intent intent = new Intent(this, ShareOnWhatsApp.class);
            startActivity(intent);
        }else if (title.equals("Contacts")){
            Intent intent = new Intent(this, WhatsappNos.class);
            startActivity(intent);
        }else if (title.equals("Add Contact")){
            Intent intent = new Intent(this, AddWhatsappNumber.class);
            startActivity(intent);
        }else if (title.equals("Rate Us")){
            Intent intent = new Intent(this, RateUs.class);
            startActivity(intent);
        }else if (title.equals("Help Line")){
            Intent intent = new Intent(this, HomeScreen.class);
            startActivity(intent);
        }else if (title.equals("Logout")){
            Intent intent = new Intent(this, Logout.class);
            startActivity(intent);
        }else if (title.equals("Settings")){
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        }else if (title.equals("Help &amp; Feedback")){
            Intent intent = new Intent(this, HomeScreen.class);
            startActivity(intent);
        }
    }

    private void updateCounter(NavigationView nav) {
        if (is_account_mode) return;
        Menu m = nav.getMenu();
    }

    @Override
    public void onClick(View v) {
        if (v == material_ripple_lauout_cart_btn_checkout){
            startActivity(new Intent(this, CartCheckout.class));
        }else if (v == tv_cart_btn_checkout){
            startActivity(new Intent(this, CartCheckout.class));
        }
    }
}
