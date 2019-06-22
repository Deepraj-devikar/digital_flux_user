package com.dolphintechno.dolphindigitalflux.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.dolphintechno.dolphindigitalflux.data.MySharedPreferences;
import com.dolphintechno.dolphindigitalflux.helper.IntentKeys;
import com.dolphintechno.dolphindigitalflux.helper.SharedPrefKeys;
import com.dolphintechno.dolphindigitalflux.helper.URLs;
import com.dolphintechno.dolphindigitalflux.model.SpecifiedProductDetail;
import com.dolphintechno.dolphindigitalflux.singleton.MyVolleySingleton;
import com.dolphintechno.dolphindigitalflux.utils.Tools;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ShopingProductDetail extends AppCompatActivity implements View.OnClickListener {

    private ActionBar actionBar;
    private Toolbar toolbar;
    private Menu menu_navigation;
    private DrawerLayout drawer;
    private View navigation_header;
    private boolean is_account_mode = false;

    MenuItem badgeMenuItem;

    TextView tv_drawer_header_name, tv_drawer_header_email;

    TextView tv_shoping_product_detail_name, tv_shoping_product_detail_seller_name, tv_shoping_product_detail_brand_id, tv_shoping_product_detail_mrp, tv_shoping_product_detail_long_info, tv_shoping_product_detail_shipping_charge;
    TextView tv_shoping_product_detail_quantity;
    SpecifiedProductDetail specifiedProductDetail;

    ImageView img_view[], image;

    String strFirstName, strLastName, strEmail, strName, strProductId, strUrlGetPtoductDetails, strCodeVal, strImg[] = new String[3];

    String strUserId, strStatus, strQuantity;

    String profile_pic_name;
    CircularImageView avatar;

    boolean isProductDetailsFetch, isProductsInCart;

    int numberOfProductsInCart;


    private View parent_view;
    private TextView tv_qty;

    private static int[] array_color_fab = {
            R.id.fab_color_blue,
            R.id.fab_color_pink,
            R.id.fab_color_grey,
            R.id.fab_color_green
    };

    private static int[] array_size_bt = {
            R.id.bt_size_s,
            R.id.bt_size_m,
            R.id.bt_size_l,
            R.id.bt_size_xl
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoping_product_detail);
        parent_view = findViewById(R.id.parent_view);

        initNavigationMenu();
        avatar = (CircularImageView) navigation_header.findViewById(R.id.avatar);


        Intent comingIntent = getIntent();
        strProductId = comingIntent.getStringExtra(IntentKeys.keyProductKeyForShopingProductDetail);
        strImg[0] = comingIntent.getStringExtra(IntentKeys.keyProductImgOne);
        strImg[1] = comingIntent.getStringExtra(IntentKeys.keyProductImgTwo);
        strImg[2] = comingIntent.getStringExtra(IntentKeys.keyProductImgThree);
        specifiedProductDetail = new SpecifiedProductDetail();

        tv_drawer_header_name = (TextView) findViewById(R.id.tv_drawer_header_name);
        tv_drawer_header_email = (TextView) findViewById(R.id.tv_drawer_header_email);

        img_view = new ImageView[3];

        img_view[0] = (ImageView) findViewById(R.id.image_1);
        img_view[1] = (ImageView) findViewById(R.id.image_2);
        img_view[2] = (ImageView) findViewById(R.id.image_3);

        image = (ImageView) findViewById(R.id.image);

        tv_shoping_product_detail_name = (TextView) findViewById(R.id.tv_shoping_product_detail_name);
        tv_shoping_product_detail_seller_name = (TextView) findViewById(R.id.tv_shoping_product_detail_seller_name);
        tv_shoping_product_detail_brand_id = (TextView) findViewById(R.id.tv_shoping_product_detail_brand_id);
        tv_shoping_product_detail_mrp = (TextView) findViewById(R.id.tv_shoping_product_detail_mrp);
        tv_shoping_product_detail_long_info = (TextView) findViewById(R.id.tv_shoping_product_detail_long_info);
        tv_shoping_product_detail_shipping_charge = (TextView) findViewById(R.id.tv_shoping_product_detail_shipping_charge);
        tv_shoping_product_detail_quantity = (TextView) findViewById(R.id.tv_shoping_product_detail_quantity);

        fetchThreeProductImage();
        fetchSpecifiedImage(0);

        initToolbar();
        initComponent();


        setInfo();
        fetchProductDetails();
        img_view[0].setOnClickListener(this);
        img_view[1].setOnClickListener(this);
        img_view[2].setOnClickListener(this);
    }


    void fetchCartProductNumber(){
        MySharedPreferences dataProccessor = new MySharedPreferences(this);
        final String strUId = dataProccessor.getStr(SharedPrefKeys.user_id);
        String strCodeVal = "&"+URLs.keyCartProductCountCode+"="+URLs.cartProductCountCode;
        String strUrlCartProductCount = URLs.url_cart_product_count+"?"+URLs.keyCartProductCountUserId+"="+strUId+strCodeVal;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, strUrlCartProductCount,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isFoundCartProductQuantity = jsonObject.getBoolean(URLs.keyIsFoundCartProductQuantity);
                            if (isFoundCartProductQuantity){
                                int cartProductQuantity = jsonObject.getInt(URLs.keyCartProductCountQuantity);
                                badgeMenuItem.setIcon(buildCounterDrawable(cartProductQuantity, R.drawable.ic_shopping_cart));
                            }else{
                                badgeMenuItem.setIcon(buildCounterDrawable(0, R.drawable.ic_shopping_cart));
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
                params.put(URLs.keyCartProductCountUserId, strUId);
                params.put(URLs.keyCartProductCountCode, URLs.cartProductCountCode);
                return params;
            }
        };
        MyVolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    void fetchProductDetails(){
        MySharedPreferences dataProccessor = new MySharedPreferences(this);
        final String strUId = dataProccessor.getStr(SharedPrefKeys.user_id);
        strCodeVal = "&"+URLs.KeyProductDetailsCode+"="+URLs.productDetailsCode;
        strCodeVal = "&"+URLs.keyProductId_sending+"="+strProductId+strCodeVal;
        strUrlGetPtoductDetails = URLs.url_product_details+"?"+URLs.keyProductDetailsUserId+"="+strUId+strCodeVal;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, strUrlGetPtoductDetails,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject myJson = new JSONObject(response);
                            isProductDetailsFetch = myJson.getBoolean(URLs.keyProductDetailsFetch);
                            if (isProductDetailsFetch){
                                JSONObject myJsonData = myJson.getJSONObject(URLs.keyProductDetailsData);
                                specifiedProductDetail.setProductId(myJsonData.getString(URLs.keyProductDetailProductId_getting));
                                specifiedProductDetail.setProductName(myJsonData.getString(URLs.keyProductDetailsProductName));
                                specifiedProductDetail.setLongInfo(myJsonData.getString(URLs.keyProductDetailsProductLongInfo));
                                specifiedProductDetail.setMrp(myJsonData.getString(URLs.keyProductDetailsProductMrp));
                                specifiedProductDetail.setBrandId(myJsonData.getString(URLs.keyProductDetailsProductBrandId));
                                specifiedProductDetail.setSellerName(myJsonData.getString(URLs.keyProductDetailsProductSellerName));
                                specifiedProductDetail.setShippingCharge(myJsonData.getString(URLs.keyProductDetailsProductShippingCharge));

                                tv_shoping_product_detail_name.setText(specifiedProductDetail.getProductName());
                                tv_shoping_product_detail_seller_name.setText(specifiedProductDetail.getSellerName());
                                tv_shoping_product_detail_brand_id.setText(specifiedProductDetail.getBrandId());
                                tv_shoping_product_detail_mrp.setText(specifiedProductDetail.getMrp());
                                tv_shoping_product_detail_long_info.setText(specifiedProductDetail.getLongInfo());
                                tv_shoping_product_detail_shipping_charge.setText(specifiedProductDetail.getShippingCharge());
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
                params.put(URLs.keyProductDetailsUserId, strUId);
                params.put(URLs.keyProductId_sending, strProductId);
                params.put(URLs.KeyProductDetailsCode, URLs.productDetailsCode);
                return params;
            }
        };
        MyVolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void fetchSpecifiedImage(int imageNumber){
        final String stringImageUrl = URLs.url_product_iamge + strImg[imageNumber];
        ImageRequest imageRequest = new ImageRequest(stringImageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                image.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_INSIDE, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);
    }

    private void fetchThreeProductImage(){
        final String strUrlImg[] = new String[3];
        strUrlImg[0] = URLs.url_product_iamge + strImg[0];
        strUrlImg[1] = URLs.url_product_iamge + strImg[1];
        strUrlImg[2] = URLs.url_product_iamge + strImg[2];

        ImageRequest imageRequest[] = new ImageRequest[3];
        for (int i = 0; i < 3; i++){
            final int finalI = i;
            imageRequest[i] = new ImageRequest(strUrlImg[i], new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    img_view[finalI].setImageBitmap(response);
                }
            }, 0, 0, ImageView.ScaleType.CENTER_INSIDE, Bitmap.Config.RGB_565,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
        }
        for (int i = 0; i < 3; i++){
            MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest[i]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart_setting, menu);
        badgeMenuItem = menu.findItem(R.id.action_cart);
        fetchCartProductNumber();
        return true;
    }

    private void setInfo() {
        MySharedPreferences dataProccessor = new MySharedPreferences(this);
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

    void productAddToCart(){
        MySharedPreferences sharedPreferences = new MySharedPreferences(this);
        strUserId = sharedPreferences.getStr(SharedPrefKeys.user_id);
        strStatus = "Product add to cart";
        strQuantity = tv_shoping_product_detail_quantity.getText().toString();
        StringRequest strReqAddToCart = new StringRequest(Request.Method.POST, URLs.url_add_to_cart,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject myJson = new JSONObject(response);
                            boolean isAddToCartDone = myJson.getBoolean(URLs.keyIsAddToCartDone);
                            if (isAddToCartDone){
                                toastIconSuccess();
                                startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(), myJson.getString(URLs.keyAddToCartErrorMsg), Toast.LENGTH_LONG).show();
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
                params.put(URLs.keyAddToCartUserId, strUserId);
                params.put(URLs.kaeAddToCartProductId, strProductId);
                params.put(URLs.keyAddToCartStatus, strStatus);
                params.put(URLs.keyAddToCartQuantity, strQuantity);
                return params;
            }
        };
        MyVolleySingleton.getInstance(this).addToRequestQueue(strReqAddToCart);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Product Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
    }

    private void initComponent() {
        tv_qty = (TextView) findViewById(R.id.tv_shoping_product_detail_quantity);
        ((FloatingActionButton) findViewById(R.id.fab_qty_sub)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(tv_qty.getText().toString());
                if (qty > 1) {
                    qty--;
                    tv_qty.setText(qty + "");
                }
            }
        });

        ((FloatingActionButton) findViewById(R.id.fab_qty_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(tv_qty.getText().toString());
                if (qty < 10) {
                    qty++;
                    tv_qty.setText(qty + "");
                }
            }
        });



        ((AppCompatButton) findViewById(R.id.btn_shoping_product_detail_add_to_cart)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(parent_view, "Add to Cart", Snackbar.LENGTH_SHORT).show();

                productAddToCart();

            }
        });
    }


    public void setSize(View v) {
        Button bt = (Button) v;
        bt.setEnabled(false);
        bt.setTextColor(Color.WHITE);
        for (int id : array_size_bt) {
            if (v.getId() != id) {
                Button bt_unselect = (Button) findViewById(id);
                bt_unselect.setEnabled(true);
                bt_unselect.setTextColor(Color.BLACK);
            }
        }
    }

    public void setColor(View v) {
        ((FloatingActionButton) v).setImageResource(R.drawable.ic_done);
        for (int id : array_color_fab) {
            if (v.getId() != id) {
                ((FloatingActionButton) findViewById(id)).setImageResource(android.R.color.transparent);
            }
        }
    }



    private Drawable buildCounterDrawable(int i, int ic_shopping_cart) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.menu_item_custom_icon_cart, null);
        view.setBackgroundResource(ic_shopping_cart);
        if (i == 0) {
            View counterTextPanel = view.findViewById(R.id.rl_badge);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.tv_badge_count);
            textView.setText("" + i);
        }
        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return new BitmapDrawable(getResources(), bitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            drawer.openDrawer(GravityCompat.START);
            finish();
        } else {
            if (item.getTitle().equals("Cart")){
                startActivity(new Intent(this, Cart.class));
            }else {
                Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void initNavigationMenu() {
        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        tv_drawer_header_name = (TextView) navigation_header.findViewById(R.id.tv_drawer_header_name);
        tv_drawer_header_email = (TextView) navigation_header.findViewById(R.id.tv_drawer_header_email);

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


    private void toastIconSuccess() {
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.toast_icon_text, null);
        ((TextView) custom_view.findViewById(R.id.message)).setText("Product Added to Cart");
        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_done);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.green_500));

        toast.setView(custom_view);
        toast.show();
    }

    private void updateCounter(NavigationView nav) {
        if (is_account_mode) return;
        Menu m = nav.getMenu();
    }

    @Override
    public void onClick(View v) {
        if (v == img_view[0]){
            fetchSpecifiedImage(0);
        }else if (v == img_view[1]){
            fetchSpecifiedImage(1);
        }else if (v == img_view[2]){
            fetchSpecifiedImage(2);
        }
    }
}
