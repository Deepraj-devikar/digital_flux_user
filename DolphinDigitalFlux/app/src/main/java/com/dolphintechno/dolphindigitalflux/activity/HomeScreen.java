package com.dolphintechno.dolphindigitalflux.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.dolphintechno.dolphindigitalflux.data.MySharedPreferences;
import com.dolphintechno.dolphindigitalflux.helper.BottomNavigationViewHelper;
import com.dolphintechno.dolphindigitalflux.helper.SharedPrefKeys;
import com.dolphintechno.dolphindigitalflux.helper.URLs;
import com.dolphintechno.dolphindigitalflux.singleton.MyVolleySingleton;
import com.dolphintechno.dolphindigitalflux.utils.Tools;
import com.dolphintechno.dolphindigitalflux.utils.ViewAnimation;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {

    MySharedPreferences dataProccessor;

    private ActionBar actionBar;
    private Toolbar toolbar;
    private Menu menu_navigation;
    private DrawerLayout drawer;
    private View navigation_header;
    private boolean is_account_mode = false;
    CircularImageView avatar;

    private BottomNavigationView bottomNavigation;
    private NestedScrollView nested_scroll_view;

    TextView tv_home_screen_name, tv_home_screen_addr;
    CircularImageView img_home_screen_profile_pic;

    TextView tv_drawer_header_name, tv_drawer_header_email;

    TextView tv_home_screen_wallet_toggel, tv_home_screen_team_toggel, tv_home_screen_plan_toggel;
    TextView tv_home_screen_add_new_toggel, tv_home_screen_add_product_toggel, tv_home_screen_shop_toggel;

    ImageButton img_btn_home_screen_whatsapp, img_btn_home_screen_facebook, img_btn_home_screen_Training_videos;


    String strFirstName;
    String strLastName;
    String strEmail;
    String strName;
    String str_unique_id;
    String wallet_data_found;
    String profile_pic_name;
    String balance;
    String idStaus;
    String team_data_found;
    String members;
    int products;
    String storedProfilePic;

    boolean yes;

    FloatingActionButton fab_home_screen_wallet, fab_home_screen_team, fab_home_screen_plan, fab_home_screen_shop, fab_home_screen_news, fab_home_screen_share;

    CardView card_home_screen_wallet, card_home_screen_team, card_home_screen_plan, card_home_screen_shop, card_home_screen_news;
    CardView card_home_screen_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        initNavigationMenu();

        dataProccessor = new MySharedPreferences(this);

        /*
         * Fetching User Id For Recognizing User
         * So We Can Get Users Info.
         */
        str_unique_id = dataProccessor.getStr(SharedPrefKeys.user_id);

        /*
         * Storing Views Ids Which Is Required
         */
        img_home_screen_profile_pic = (CircularImageView) findViewById(R.id.img_home_screen_profile_pic);
        avatar = (CircularImageView) navigation_header.findViewById(R.id.avatar);

        img_btn_home_screen_whatsapp = (ImageButton) findViewById(R.id.img_btn_home_screen_whatsapp);
        img_btn_home_screen_facebook = (ImageButton) findViewById(R.id.img_btn_home_screen_facebook);
        img_btn_home_screen_Training_videos = (ImageButton) findViewById(R.id.img_btn_home_screen_Training_videos);

        card_home_screen_wallet = (CardView) findViewById(R.id.card_home_screen_wallet);
        card_home_screen_team = (CardView) findViewById(R.id.card_home_screen_team);
        card_home_screen_plan = (CardView) findViewById(R.id.card_home_screen_plan);
        card_home_screen_shop = (CardView) findViewById(R.id.card_home_screen_shop);
        card_home_screen_news = (CardView) findViewById(R.id.card_home_screen_news);
        card_home_screen_share = (CardView) findViewById(R.id.card_home_screen_share);

        fab_home_screen_wallet = (FloatingActionButton) findViewById(R.id.fab_home_screen_wallet);
        fab_home_screen_team = (FloatingActionButton) findViewById(R.id.fab_home_screen_team);
        fab_home_screen_plan = (FloatingActionButton) findViewById(R.id.fab_home_screen_plan);
        fab_home_screen_shop = (FloatingActionButton) findViewById(R.id.fab_home_screen_shop);
        fab_home_screen_news = (FloatingActionButton) findViewById(R.id.fab_home_screen_news);
        fab_home_screen_share = (FloatingActionButton) findViewById(R.id.fab_home_screen_share);

        card_home_screen_wallet.setOnClickListener(this);
        card_home_screen_team.setOnClickListener(this);
        card_home_screen_plan.setOnClickListener(this);
        card_home_screen_shop.setOnClickListener(this);
        card_home_screen_news.setOnClickListener(this);
        card_home_screen_share.setOnClickListener(this);

        fab_home_screen_wallet.setOnClickListener(this);
        fab_home_screen_team.setOnClickListener(this);
        fab_home_screen_plan.setOnClickListener(this);
        fab_home_screen_shop.setOnClickListener(this);
        fab_home_screen_news.setOnClickListener(this);
        fab_home_screen_share.setOnClickListener(this);

        img_btn_home_screen_whatsapp.setOnClickListener(this);
        img_btn_home_screen_facebook.setOnClickListener(this);
        img_btn_home_screen_Training_videos.setOnClickListener(this);

        img_home_screen_profile_pic.setOnClickListener(this);

        tv_home_screen_name = (TextView) findViewById(R.id.tv_home_screen_name);
        tv_home_screen_addr = (TextView) findViewById(R.id.tv_home_screen_addr);
        tv_drawer_header_name = (TextView) findViewById(R.id.tv_drawer_header_name);
        tv_drawer_header_email = (TextView) findViewById(R.id.tv_drawer_header_email);

        tv_home_screen_wallet_toggel = (TextView) findViewById(R.id.tv_home_screen_wallet_toggel);
        tv_home_screen_team_toggel = (TextView) findViewById(R.id.tv_home_screen_team_toggel);
        tv_home_screen_plan_toggel = (TextView) findViewById(R.id.tv_home_screen_plan_toggel);
        tv_home_screen_shop_toggel = (TextView) findViewById(R.id.tv_home_screen_shop_toggel);
        tv_home_screen_add_product_toggel = (TextView) findViewById(R.id.tv_home_screen_add_product_toggel);
        tv_home_screen_add_new_toggel = (TextView) findViewById(R.id.tv_home_screen_add_new_toggel);




        getToggelData();

        initToolbar();

        setInfo();

    }

    private void setInfo() {
        MySharedPreferences dataProccessor = new MySharedPreferences(this);

        strFirstName = dataProccessor.getStr(SharedPrefKeys.first_name);
        strLastName = dataProccessor.getStr(SharedPrefKeys.last_name);
        strEmail = dataProccessor.getStr(SharedPrefKeys.email_id);
        strName = strFirstName+" "+strLastName;

        tv_home_screen_name.setText(strName);
        tv_home_screen_addr.setText(strEmail);

        tv_drawer_header_name = (TextView) navigation_header.findViewById(R.id.tv_drawer_header_name);
        tv_drawer_header_email = (TextView) navigation_header.findViewById(R.id.tv_drawer_header_email);
        tv_drawer_header_name.setText(strName);
        tv_drawer_header_email.setText(strEmail);

        nested_scroll_view = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                ViewAnimation.fadeOutIn(nested_scroll_view);
                return true;
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
        }  else{
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Home");
        Tools.setSystemBarColor(this, R.color.deep_orange_500);
    }

    @Override
    public void onClick(View v) {
        if (v == fab_home_screen_wallet || v == card_home_screen_wallet){
            startActivity(new Intent(this, WalletHistory.class));
        }else if (v == fab_home_screen_team || v == card_home_screen_team){
            startActivity(new Intent(this, NetworkList.class));
        }else if (v == fab_home_screen_plan || v == card_home_screen_plan){
            startActivity(new Intent(this, Withdraw.class));
        }else if (v == fab_home_screen_shop || v == card_home_screen_shop){
            startActivity(new Intent(this, Shop.class));
        }else if (v == fab_home_screen_news || v == card_home_screen_news){
            startActivity(new Intent(this, AddProduct.class));
        } else if ((v == fab_home_screen_share) || v == card_home_screen_share) {
            startActivity(new Intent(this, AddNew.class));
        }else if (v == img_btn_home_screen_whatsapp){
            shareByWhatsApp();
        }else if (v == img_btn_home_screen_facebook){
            shareByFacebook();
        }else if (v == img_btn_home_screen_Training_videos){
            startActivity(new Intent(this, TrainingVideos.class));
        }else if (v == img_home_screen_profile_pic){
            startActivity(new Intent(this, ChangeProfilePic.class));
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

                                    String msg_url = "http://www.digitalflux.in/newaccount.php";

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

    private void shareByWhatsApp() {

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

    public void getToggelData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.url_home_sreen,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject myJson = new JSONObject(response);
                            wallet_data_found = myJson.getString(URLs.wallet_data_found);
                            if (wallet_data_found.equals("yes")){
                                yes = true;
                                profile_pic_name = myJson.getString(URLs.home_screen_profile_pic);
                                balance = myJson.getString(URLs.balance);
                                idStaus = myJson.getString(URLs.idStatus);
                                tv_home_screen_wallet_toggel.setText(balance);
                                tv_home_screen_plan_toggel.setText(myJson.getString(URLs.withdraw));
                                dataProccessor.setStr(SharedPrefKeys.id_status, idStaus);
                                dataProccessor.setStr(SharedPrefKeys.profile_pic, profile_pic_name);
                            }
                            team_data_found = myJson.getString(URLs.team_data_found);
                            if (team_data_found.equals("yes")){
                                yes = true;
                                members = myJson.getString((URLs.members));
                                tv_home_screen_team_toggel.setText(members);
                            }
                            products = myJson.getInt(URLs.products);
                            tv_home_screen_shop_toggel.setText(String.valueOf(products));
                            tv_home_screen_add_product_toggel.setText(myJson.getString(URLs.user_added_product));
                            tv_home_screen_add_new_toggel.setText(myJson.getString(URLs.added_user_count));

                            String strUrlAvtarImg = URLs.url_profile_pic+profile_pic_name;
//                            Toast.makeText(getApplicationContext(), "--"+strUrlAvtarImg, Toast.LENGTH_LONG).show();
                            ImageRequest imageRequest = new ImageRequest(strUrlAvtarImg, new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    avatar.setImageBitmap(response);
                                    img_home_screen_profile_pic.setImageBitmap(response);
                                }
                            }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565,
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    });
                            MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(imageRequest);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("homescreen error", String.valueOf(error));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(URLs.unique_id, str_unique_id);
                params.put(URLs.code_value_key, URLs.code_value);
                return params;
            }
        };
        MyVolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
