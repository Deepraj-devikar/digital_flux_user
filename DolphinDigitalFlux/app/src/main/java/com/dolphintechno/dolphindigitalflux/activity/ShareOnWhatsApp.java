package com.dolphintechno.dolphindigitalflux.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dolphintechno.dolphindigitalflux.R;
import com.dolphintechno.dolphindigitalflux.data.MySharedPreferences;
import com.dolphintechno.dolphindigitalflux.helper.URLs;
import com.dolphintechno.dolphindigitalflux.singleton.MyVolleySingleton;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class ShareOnWhatsApp extends AppCompatActivity implements View.OnClickListener {

    MySharedPreferences dataProccessor;

    private ActionBar actionBar;
    private Toolbar toolbar;
    private Menu menu_navigation;
    private DrawerLayout drawer;
    private View navigation_header;
    private boolean is_account_mode = false;

    Button btn_share_on_whatsapp, btn_share_on_facebook;

    String strName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_on_whats_app);

        btn_share_on_whatsapp = (Button) findViewById(R.id.btn_share_on_whatsapp);

        btn_share_on_facebook = (Button) findViewById(R.id.btn_share_on_facebook);

        btn_share_on_whatsapp.setOnClickListener(this);

        btn_share_on_facebook.setOnClickListener(this);

        initNavigationMenu();

    }

    @Override
    public void onClick(View v) {
        if (v == btn_share_on_whatsapp){

            shareByAndroid();

        }else if (v == btn_share_on_facebook){
            shareByFacebook();
        }
    }

    private void shareByFacebook() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.urlWTA,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("msg fetched")){
                                String strMsg = jsonObject.getString("msg");

                                strMsg = "* I am "+strName+"*,"+strMsg;

                                try {
                                    String result = java.net.URLDecoder.decode(strMsg, StandardCharsets.UTF_8.name());

                                    String msg_url = "http://digitalflux.in/newaccount.php";

                                    Intent share=new Intent(Intent.ACTION_SEND);
                                    share.setType("text/plain");
                                    share.putExtra(Intent.EXTRA_TEXT, msg_url);
                                    share.setPackage("com.facebook.katana"); //Facebook App package
                                    startActivity(Intent.createChooser(share, "Title of the dialog the system will open"));

                                } catch (UnsupportedEncodingException e) {
                                    // not going to happen - value came from JDK's own StandardCharsets
                                }

                            }else {
                                Toast.makeText(getApplicationContext(), "massage not fetched", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MyVolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void shareByAndroid() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.urlWTA,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("msg fetched")){
                                String strMsg = jsonObject.getString("msg");

                                strMsg = "* I am "+strName+"*,"+strMsg;

                                try {
                                    String result = java.net.URLDecoder.decode(strMsg, StandardCharsets.UTF_8.name());

                                    Intent sendIntent = new Intent();
                                    sendIntent.setAction(Intent.ACTION_SEND);
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, result);
                                    sendIntent.setType("text/plain");
                                    sendIntent.setPackage("com.whatsapp");
                                    startActivity(sendIntent);
                                } catch (UnsupportedEncodingException e) {
                                    // not going to happen - value came from JDK's own StandardCharsets
                                }

                            }else {
                                Toast.makeText(getApplicationContext(), "massage not fetched", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MyVolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }



    private void initNavigationMenu() {
        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view_share_on);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_share_on);
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
