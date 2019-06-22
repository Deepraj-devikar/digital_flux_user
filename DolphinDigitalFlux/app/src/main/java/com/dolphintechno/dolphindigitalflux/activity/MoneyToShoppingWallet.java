package com.dolphintechno.dolphindigitalflux.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.dolphintechno.dolphindigitalflux.singleton.MyVolleySingleton;
import com.dolphintechno.dolphindigitalflux.utils.Tools;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MoneyToShoppingWallet extends AppCompatActivity implements View.OnClickListener {

    MySharedPreferences dataProccessor;

    private ActionBar actionBar;
    private Toolbar toolbar;
    private Menu menu_navigation;
    private DrawerLayout drawer;
    private View navigation_header;
    private boolean is_account_mode = false;

    boolean fetchedDB = false;

    TextView tv_drawer_header_name, tv_drawer_header_email;

    EditText et_to_shpng_wlt_ttl_amt, et_to_shpng_wlt_withdraw_amt;

    Button bt_to_shpng_wlt_submit;

    TextView tv_to_shpng_wlt_green_text, tv_to_shpng_wlt_yellow_text, tv_to_shpng_wlt_red_text;

    String strFirstName;
    String strLastName;
    String strEmail;
    String strName;
    String str_unique_id;

    String profile_pic_name;
    CircularImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_to_shopping_wallet);

        dataProccessor = new MySharedPreferences(this);

        /*
         * Fetching User Id For Recognizing User
         * So We Can Get Users Info.
         */
        str_unique_id = dataProccessor.getStr(SharedPrefKeys.user_id);

        tv_to_shpng_wlt_green_text = (TextView) findViewById(R.id.tv_to_shpng_wlt_green_text);
        tv_to_shpng_wlt_yellow_text = (TextView) findViewById(R.id.tv_to_shpng_wlt_yellow_text);
        tv_to_shpng_wlt_red_text = (TextView) findViewById(R.id.tv_to_shpng_wlt_red_text);

        et_to_shpng_wlt_ttl_amt = (EditText) findViewById(R.id.et_to_shpng_wlt_ttl_amt);
        et_to_shpng_wlt_withdraw_amt = (EditText) findViewById(R.id.et_to_shpng_wlt_withdraw_amt);

        bt_to_shpng_wlt_submit = (Button) findViewById(R.id.bt_to_shpng_wlt_submit);


        initNavigationMenu();
        avatar = (CircularImageView) navigation_header.findViewById(R.id.avatar);

        bt_to_shpng_wlt_submit.setOnClickListener(this);

        fetchDetails();

        setInfo();
        initToolbar();





    }

    void fetchDetails(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.url_Withdrawal_short_info,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean(URLs.keyWithdrawShortInfoCheck)){
                                JSONObject dataJson = jsonObject.getJSONObject(URLs.keyWithdrawShortInfoFetch);

                                Float floatTotalAmt = Float.valueOf(dataJson.getString(URLs.keyWithdrawShortInfoWlt));
                                String strTotalAmt = String.format("%.2f", floatTotalAmt);
                                et_to_shpng_wlt_ttl_amt.setText(strTotalAmt);

                                String strGreen = String.format("%.2f", dataJson.getDouble(URLs.keyWithdrawShortInfoPaidAmt));
                                tv_to_shpng_wlt_green_text.setText(strGreen);
                                String strYellow = String.format("%.2f", dataJson.getDouble(URLs.keyWithdrawShortInfoUnPaidAmt));
                                tv_to_shpng_wlt_yellow_text.setText(strYellow);
                                String strRed = String.format("%.2f", dataJson.getDouble(URLs.keyWithdrawShortInfoInactiveAmt));
                                tv_to_shpng_wlt_red_text.setText(strRed);

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
                params.put(URLs.keyWithdrawShortInfoUId, str_unique_id);
                return params;
            }
        };
        MyVolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }



    void performOperation(){

        Float floatTotalAmt = Float.valueOf(et_to_shpng_wlt_ttl_amt.getText().toString());
        Float floatGreenBal = Float.valueOf(tv_to_shpng_wlt_green_text.getText().toString());
        final Float floatAmt = Float.valueOf(et_to_shpng_wlt_withdraw_amt.getText().toString());


        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URLs.url_move_amt_to_shpng_wlt,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getBoolean(URLs.keyToShpngWltFineBal)){
                                if (jsonObject.getBoolean(URLs.keyToShpngWltIsUpdtWltAmt)){
                                    if (jsonObject.getBoolean(URLs.keyToShpngWltIsSetWltHstry)){
                                        Toast.makeText(getApplicationContext(), "Transaction Done Succesfully", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                                    }else {
                                        Toast.makeText(getApplicationContext(), "Transaction Half Done", Toast.LENGTH_LONG).show();
                                    }
                                }else {
                                    Toast.makeText(getApplicationContext(), "Balance is in wallet But Transaction not done", Toast.LENGTH_LONG).show();
                                }
                            }else {
                                Toast.makeText(getApplicationContext(), "Wallet not have balnce", Toast.LENGTH_LONG).show();
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
                params.put(URLs.keyToShpngWltUID, str_unique_id);
                params.put(URLs.keyToShpngWltAmount, String.valueOf(floatAmt));
                return params;
            }
        };
        MyVolleySingleton.getInstance(this).addToRequestQueue(stringRequest1);



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





    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Transfer To Shopping Wallet");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }





    private void initNavigationMenu() {
        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view_money_to_shopping_wallet);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_money_to_shopping_wallet);
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
        if (v == bt_to_shpng_wlt_submit){
            performOperation();
        }
    }
}
