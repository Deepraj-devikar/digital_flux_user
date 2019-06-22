package com.dolphintechno.dolphindigitalflux.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

public class Withdraw extends AppCompatActivity implements View.OnClickListener {

    MySharedPreferences dataProccessor;

    private ActionBar actionBar;
    private Toolbar toolbar;
    private Menu menu_navigation;
    private DrawerLayout drawer;
    private View navigation_header;
    private boolean is_account_mode = false;

    boolean fetchedDB = false;

    TextView tv_drawer_header_name, tv_drawer_header_email;
    TextView tv_green_text, tv_yellow_text, tv_red_text;

    EditText et_withdraw_ttl_amt, et_withdraw_amt, et_withdraw_shopping_income, et_withdraw_next_upgrade, et_withdraw_admn_hndl_charge, et_withdraw_tds, et_withdraw_grand_ttl;
    EditText et_withdraw_reason;
    Button bt_submit, bt_go;
    Button btn_add_mny_to_shpng_wlt;

//    String str_withdraw_shopping_income, str_withdraw_next_upgrade, str_withdraw_admn_hndl_charge, str_withdraw_tds;

    String str_ttl_amt, str_green_amt, str_yellow_amt, str_red_amt, str_amt_withdraw, str_shopping_income, str_nxt_upgrade, str_admn_charge, str_tds, str_grand_ttl;

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
        setContentView(R.layout.activity_withdraw);

        dataProccessor = new MySharedPreferences(this);

        /*
         * Fetching User Id For Recognizing User
         * So We Can Get Users Info.
         */
        str_unique_id = dataProccessor.getStr(SharedPrefKeys.user_id);

        tv_green_text = (TextView) findViewById(R.id.tv_green_text);
        tv_yellow_text = (TextView) findViewById(R.id.tv_yellow_text);
        tv_red_text = (TextView) findViewById(R.id.tv_red_text);

        et_withdraw_ttl_amt = (EditText) findViewById(R.id.et_withdraw_ttl_amt);

        fetchDetails();

        et_withdraw_amt = (EditText) findViewById(R.id.et_withdraw_amt);
        et_withdraw_shopping_income = (EditText) findViewById(R.id.et_withdraw_shopping_income);
        et_withdraw_next_upgrade = (EditText) findViewById(R.id.et_withdraw_next_upgrade);
        et_withdraw_admn_hndl_charge = (EditText) findViewById(R.id.et_withdraw_admn_hndl_charge);
        et_withdraw_tds = (EditText) findViewById(R.id.et_withdraw_tds);
        et_withdraw_grand_ttl = (EditText) findViewById(R.id.et_withdraw_grand_ttl);

        bt_submit = (Button) findViewById(R.id.bt_submit);
        bt_go = (Button) findViewById(R.id.bt_go);
        btn_add_mny_to_shpng_wlt = (Button) findViewById(R.id.btn_add_mny_to_shpng_wlt);

        initNavigationMenu();
        avatar = (CircularImageView) navigation_header.findViewById(R.id.avatar);

        et_withdraw_reason = (EditText) findViewById(R.id.et_withdraw_reason);


        setInfo();
        initToolbar();


        bt_go.setOnClickListener(this);
        bt_submit.setOnClickListener(this);
        btn_add_mny_to_shpng_wlt.setOnClickListener(this);


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



    void fetchDetails(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.url_withdraw,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
//                            Toast.makeText(getApplicationContext(), "in response"+response, Toast.LENGTH_LONG).show();
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getBoolean(URLs.keyPerform2IsUserSignup)){

                                JSONObject color_json = jsonObject.getJSONObject(URLs.keyPerform2UserSignup);

                                Float floatTotalAmt = Float.valueOf(color_json.getString(URLs.keyPerform2Wallet));
                                String strTotalAmt = String.format("%.2f", floatTotalAmt);
                                et_withdraw_ttl_amt.setText(strTotalAmt);


                                String strGreen = String.format("%.2f", color_json.getDouble(URLs.keyPerform2PaidAmount));
                                tv_green_text.setText(strGreen);
                                String strYellow = String.format("%.2f", color_json.getDouble(URLs.keyPerform2UnpaidAmount));
                                tv_yellow_text.setText(strYellow);
                                String strRed = String.format("%.2f", color_json.getDouble(URLs.keyPerform2InactiveAmount));
                                tv_red_text.setText(strRed);


                                str_green_amt = strGreen;
                                str_yellow_amt = strYellow;
                                str_red_amt = strRed;

                            }

                            if (jsonObject.getBoolean(URLs.keyPerform2IsFetchedWebDetails)){

                                JSONObject cal_amt_json = jsonObject.getJSONObject(URLs.keyPerform2WebDetails);


                                str_shopping_income = cal_amt_json.getString(URLs.keyPerform2ShopingWallet);
                                str_nxt_upgrade = cal_amt_json.getString(URLs.keyPerform2NextUpgrade);
                                str_admn_charge = cal_amt_json.getString(URLs.keyPerform2AdminCharge);
                                str_tds = cal_amt_json.getString(URLs.keyPerform2TDS);

                            }


                            fetchedDB = true;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error"+error, Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("uid", str_unique_id);
                return params;
            }
        };
        MyVolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Withdraw");
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

    private void showStateDialog(final View v) {
        final String[] array = new String[]{
                "Arizona", "California", "Florida", "Massachusetts", "New York", "Washington"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("State");
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void showCountryDialog(final View v) {
        final String[] array = new String[]{
                "United State", "Germany", "United Kingdom", "Australia"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Country");
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }


    private void initNavigationMenu() {
        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view_withdraw);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_withdraw);
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

//        String.format("%.2f", i2);

        if (v == bt_go){
            str_amt_withdraw = et_withdraw_amt.getText().toString();


            if (fetchedDB){
                if (!TextUtils.isEmpty(str_amt_withdraw)){
                    float amount = Float.valueOf(str_amt_withdraw);
                    String strAmount = String.format("%.2f", amount);
                    et_withdraw_amt.setText(strAmount);

                    float per_shopping_income = Float.valueOf(str_shopping_income);
                    float shopping_income = amount * (per_shopping_income / 100);
//                    Toast.makeText(getApplicationContext(), String.valueOf(shopping_income), Toast.LENGTH_LONG).show();
                    String strShoppingIncome = String.format("%.2f", shopping_income);
                    et_withdraw_shopping_income.setText(strShoppingIncome);

                    float per_nxt_upgrade = Float.valueOf(str_nxt_upgrade);
                    float nxt_upgrade = amount * (per_nxt_upgrade / 100);
                    String strNxtUpgrade= String.format("%.2f", nxt_upgrade);
                    et_withdraw_next_upgrade.setText(strNxtUpgrade);

                    float per_admn_charge = Float.valueOf(str_admn_charge);
                    float admn_charge = amount * (per_admn_charge / 100);
                    String strAdmnCharge= String.format("%.2f", admn_charge);
                    et_withdraw_admn_hndl_charge.setText(strAdmnCharge);

                    float per_tds = Float.valueOf(str_tds);
                    float tds = amount * (per_tds / 100);
                    String strTDS= String.format("%.2f", tds);
                    et_withdraw_tds.setText(strTDS);

                    float grand_ttl = amount + shopping_income + nxt_upgrade + admn_charge + tds;
                    String strGrandTtl= String.format("%.2f", grand_ttl);
                    et_withdraw_grand_ttl.setText(strGrandTtl);

                    float green_amt = Float.valueOf(str_green_amt);
                    if (grand_ttl < green_amt){
                        bt_go.setVisibility(View.GONE);
                        bt_submit.setVisibility(View.VISIBLE);
                    }else {
                        et_withdraw_amt.setError("Decrease Amount");
                    }

                }else {
                    et_withdraw_amt.setError("Enter Amount");
                }
            }else {
                Toast.makeText(this, "Wait For While", Toast.LENGTH_LONG).show();
            }

        }else if (v == bt_submit){
            performWithdraw();
        }else if (v == btn_add_mny_to_shpng_wlt){
            startActivity(new Intent(this, MoneyToShoppingWallet.class));
        }
    }

    private void performWithdraw() {

        Toast.makeText(this, "in perform withdraw", Toast.LENGTH_LONG).show();

        StringRequest finalStringRequest = new StringRequest(Request.Method.POST, URLs.url_final_withdraw,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject  = new JSONObject(response);

                            if (jsonObject.getBoolean(URLs.keyFinalWithdrawCheckWithdrawRequest)){
//                                Toast.makeText(getApplicationContext(), "Set Withdraw Request", Toast.LENGTH_LONG).show();
                                if (jsonObject.getBoolean(URLs.keyFinalWithdrawCheckUpdateWltAmt)){
//                                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                                    if (jsonObject.getBoolean(URLs.keyFinalWithdrawCheckWltHistory)){
//                                        Toast.makeText(getApplicationContext(), "Transaction Complete", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                                    }

                                }

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
                params.put(URLs.keyFinalWithdrawUserId, str_unique_id);
                params.put(URLs.keyFinalWithdrawGrandTtl, et_withdraw_grand_ttl.getText().toString());
                params.put(URLs.keyFinalWithdrawAdmnChrg, et_withdraw_admn_hndl_charge.getText().toString());
                params.put(URLs.keyFinalWithdrawTDS, et_withdraw_tds.getText().toString());
                params.put(URLs.keyFinalWithdrawReason, et_withdraw_reason.getText().toString());
                return params;
            }
        };
        MyVolleySingleton.getInstance(this).addToRequestQueue(finalStringRequest);

    }
}
