package com.dolphintechno.dolphindigitalflux.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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
import com.dolphintechno.dolphindigitalflux.R;
import com.dolphintechno.dolphindigitalflux.data.MySharedPreferences;
import com.dolphintechno.dolphindigitalflux.helper.SharedPrefKeys;
import com.dolphintechno.dolphindigitalflux.helper.URLs;
import com.dolphintechno.dolphindigitalflux.model.CartCheckoutDetails;
import com.dolphintechno.dolphindigitalflux.singleton.MyVolleySingleton;
import com.dolphintechno.dolphindigitalflux.utils.Tools;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CartCheckout extends AppCompatActivity {

    MySharedPreferences dataProccessor;

    private ActionBar actionBar;
    private Toolbar toolbar;
    private Menu menu_navigation;
    private DrawerLayout drawer;
    private View navigation_header;
    private boolean is_account_mode = false;

    TextView tv_drawer_header_name, tv_drawer_header_email;

    TextView tv_cart_checkout_name, tv_cart_checkout_address, tv_cart_checkout_state, tv_cart_checkout_district, tv_cart_checkout_city;
    TextView tv_cart_checkout_pincode, tv_cart_checkout_mobile, tv_cart_checkout_email;

    String strFirstName;
    String strLastName;
    String strEmail;
    String strName;
    String str_unique_id;

    String profile_pic_name;
    CircularImageView avatar;

    String strCheckoutMsg;

    CartCheckoutDetails cartCheckoutDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_checkout);

        initNavigationMenu();
        avatar = (CircularImageView) navigation_header.findViewById(R.id.avatar);

        dataProccessor = new MySharedPreferences(this);

        /*
         * Fetching User Id For Recognizing User
         * So We Can Get Users Info.
         */
        str_unique_id = dataProccessor.getStr(SharedPrefKeys.user_id);

        cartCheckoutDetails = new CartCheckoutDetails();

        tv_cart_checkout_name = (TextView) findViewById(R.id.tv_cart_checkout_name);
        tv_cart_checkout_address = (TextView) findViewById(R.id.tv_cart_checkout_address);
        tv_cart_checkout_state = (TextView) findViewById(R.id.tv_cart_checkout_state);
        tv_cart_checkout_district = (TextView) findViewById(R.id.tv_cart_checkout_district);
        tv_cart_checkout_city = (TextView) findViewById(R.id.tv_cart_checkout_city);
        tv_cart_checkout_pincode = (TextView) findViewById(R.id.tv_cart_checkout_pincode);
        tv_cart_checkout_mobile = (TextView) findViewById(R.id.tv_cart_checkout_mobile);
        tv_cart_checkout_email = (TextView) findViewById(R.id.tv_cart_checkout_email);

        strCheckoutMsg = new String();

        initToolbar();
        initComponent();
        setInfo();
        fetchCartCheckoutDetails();
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

    void fetchCartCheckoutDetails(){
        String strCodeVal = "&"+URLs.keyCartCheckoutCode+"="+URLs.CartCheckoutCode;
        String strUrl = URLs.url_fetch_cart_checkout_details+"?"+URLs.keyCartCheckoutUserId_send+"="+str_unique_id+strCodeVal;

        cartCheckoutDetails.setUserId(str_unique_id);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, strUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isFoundCartCheckoutDetails = jsonObject.getBoolean(URLs.keyIsCartCheckoutDetailsFound);
                            if (isFoundCartCheckoutDetails){
                                JSONObject jsonData = jsonObject.getJSONObject(URLs.keyCartCheckoutData);
                                cartCheckoutDetails.setFirstName(jsonData.getString(URLs.keyCartCheckoutFirstName));
                                cartCheckoutDetails.setLastName(jsonData.getString(URLs.keyCartCheckoutLastName));
                                cartCheckoutDetails.setFullName(cartCheckoutDetails.getFirstName()+" "+cartCheckoutDetails.getLastName());
                                cartCheckoutDetails.setMobile(jsonData.getString(URLs.keyCartCheckoutMobile));
                                cartCheckoutDetails.setPersonEmail(jsonData.getString(URLs.keyCartCheckoutEmail));
                                cartCheckoutDetails.setAddress(jsonData.getString(URLs.keyCartCheckoutAddress));
                                cartCheckoutDetails.setState(jsonData.getString(URLs.keyCartCheckoutState));
                                cartCheckoutDetails.setDistrict(jsonData.getString(URLs.keyCartCheckoutDistrict));
                                cartCheckoutDetails.setCity(jsonData.getString(URLs.keyCartCheckoutCity));
                                cartCheckoutDetails.setPincode(jsonData.getString(URLs.keyCartCheckoutPincode));

                                tv_cart_checkout_name.setText(cartCheckoutDetails.getFullName());
                                tv_cart_checkout_address.setText(cartCheckoutDetails.getAddress());
                                tv_cart_checkout_state.setText(cartCheckoutDetails.getState());
                                tv_cart_checkout_district.setText(cartCheckoutDetails.getDistrict());
                                tv_cart_checkout_city.setText(cartCheckoutDetails.getCity());
                                tv_cart_checkout_pincode.setText(cartCheckoutDetails.getPincode());
                                tv_cart_checkout_mobile.setText(cartCheckoutDetails.getMobile());
                                tv_cart_checkout_email.setText(cartCheckoutDetails.getPersonEmail());
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
                params.put(URLs.keyCartCheckoutUserId_send, str_unique_id);
                params.put(URLs.keyCartCheckoutCode, URLs.CartCheckoutCode);
                return params;
            }
        };
        MyVolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Checkout");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
    }

    private void initComponent() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
        }else if (item.getTitle().equals("Done")){
            checkoutAndPay();
            finish();
        }else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkoutAndPay() {
        StringRequest strqst = new StringRequest(Request.Method.POST, URLs.url_checkout_opration,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "in response"+response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            strCheckoutMsg = jsonObject.getString(URLs.keyCheckoutMsg);
                            if (strCheckoutMsg.equals(URLs.checkoutProductNotFound)){
                            }else if (strCheckoutMsg.equals(URLs.checkoutUserNotFound)){
                            }else if (strCheckoutMsg.equals(URLs.checkoutTransactionDone)){
                            }else if (strCheckoutMsg.equals(URLs.checkoutTransactionHalfDone)){
                            }else if (strCheckoutMsg.equals(URLs.checkoutTransactionNotDone)){
                            }else if (strCheckoutMsg.equals(URLs.checkoutNotHaveBalance)){
                            }else {
                            }
                            Toast.makeText(getApplicationContext(), strCheckoutMsg, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), (CharSequence) error, Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(URLs.keyCheckoutUsrId_send, str_unique_id);
                params.put(URLs.keyCheckoutcode, URLs.checkoutCodeValue);
                return params;
            }
        };
        MyVolleySingleton.getInstance(this).addToRequestQueue(strqst);
    }

    private void initNavigationMenu() {
        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view_cart_checkout);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_cart_checkout);
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

}
