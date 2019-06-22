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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.dolphintechno.dolphindigitalflux.helper.SharedPrefKeys;
import com.dolphintechno.dolphindigitalflux.helper.URLs;
import com.dolphintechno.dolphindigitalflux.singleton.MyVolleySingleton;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RateUs extends AppCompatActivity implements View.OnClickListener {

    private ActionBar actionBar;
    private Toolbar toolbar;
    private Menu menu_navigation;
    private DrawerLayout drawer;
    private View navigation_header;
    private boolean is_account_mode = false;

    String strUniqueId;
    String strFirstName;
    String strLastName;
    String strEmail;
    String strName;
    String strRating;
    String strComment;
    Boolean isTaskDone;

    String profile_pic_name;
    CircularImageView avatar;

    TextView tv_drawer_header_name, tv_drawer_header_email;
    EditText et_rate_us_comment;

    Button btn_rate_us_submit;
    ImageButton img_btn_rate_us_rate1, img_btn_rate_us_rate2, img_btn_rate_us_rate3, img_btn_rate_us_rate4, img_btn_rate_us_rate5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);

        initNavigationMenu();
        avatar = (CircularImageView) navigation_header.findViewById(R.id.avatar);


        strRating = "0";

        strComment = "not filled";

        et_rate_us_comment = (EditText) findViewById(R.id.et_rate_us_comment);

        btn_rate_us_submit = (Button) findViewById(R.id.btn_rate_us_submit);
        img_btn_rate_us_rate1 = (ImageButton) findViewById(R.id.img_btn_rate_us_rate1);
        img_btn_rate_us_rate2 = (ImageButton) findViewById(R.id.img_btn_rate_us_rate2);
        img_btn_rate_us_rate3 = (ImageButton) findViewById(R.id.img_btn_rate_us_rate3);
        img_btn_rate_us_rate4 = (ImageButton) findViewById(R.id.img_btn_rate_us_rate4);
        img_btn_rate_us_rate5 = (ImageButton) findViewById(R.id.img_btn_rate_us_rate5);

        btn_rate_us_submit.setOnClickListener(this);
        img_btn_rate_us_rate1.setOnClickListener(this);
        img_btn_rate_us_rate2.setOnClickListener(this);
        img_btn_rate_us_rate3.setOnClickListener(this);
        img_btn_rate_us_rate4.setOnClickListener(this);
        img_btn_rate_us_rate5.setOnClickListener(this);

        setInfo();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initNavigationMenu() {
        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view_rate_us);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_rate_us);
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

        // open drawer at start
//        drawer.openDrawer(GravityCompat.START);
        updateCounter(nav_view);
        menu_navigation = nav_view.getMenu();

        // navigation header
        navigation_header = nav_view.getHeaderView(0);
//        (navigation_header.findViewById(R.id.bt_account)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                boolean is_hide = Tools.toggleArrow(view);
//                is_account_mode = is_hide;
//                menu_navigation.clear();
//                if (is_hide) {
//                    menu_navigation.add(1, 1000, 100, "evans.collins@mail.com").setIcon(R.drawable.ic_account_circle);
//                    menu_navigation.add(1, 2000, 100, "adams.green@mail.com").setIcon(R.drawable.ic_account_circle);
//                    menu_navigation.add(1, 1, 100, "Add account").setIcon(R.drawable.ic_add);
//                    menu_navigation.add(1, 2, 100, "Manage accounts").setIcon(R.drawable.ic_settings);
//                } else {
//                    nav_view.inflateMenu(R.menu.menu_navigation_drawer_mail);
//                    updateCounter(nav_view);
//                }
//            }
//        });
    }

    private void onItemNavigationClicked(MenuItem item) {



        if (!is_account_mode) {
//            Toast.makeText(getApplicationContext(), item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
//            actionBar.setTitle(item.getTitle());
            drawer.closeDrawers();
            navigate(item.getTitle());


        } else {
            CircularImageView avatar = (CircularImageView) navigation_header.findViewById(R.id.avatar);
            switch (item.getItemId()) {
                case 1000:
//                    name.setText("Evans Collins");
//                    email.setText(item.getTitle().toString());
                    avatar.setImageResource(R.drawable.photo_male_5);
                    break;
                case 2000:
//                    name.setText("Adams Green");
//                    email.setText(item.getTitle().toString());
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
//        ((TextView) m.findItem(R.id.nav_all_inbox).getActionView().findViewById(R.id.text)).setText("75");
//        ((TextView) m.findItem(R.id.nav_inbox).getActionView().findViewById(R.id.text)).setText("68");

//        TextView badgePrioInbx = (TextView) m.findItem(R.id.nav_priority_inbox).getActionView().findViewById(R.id.text);
//        badgePrioInbx.setText("3 new");
//        badgePrioInbx.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
//
//        TextView badgeSocial = (TextView) m.findItem(R.id.nav_social).getActionView().findViewById(R.id.text);
//        badgeSocial.setText("51 new");
//        badgeSocial.setBackgroundColor(getResources().getColor(R.color.green_500));
//
//        ((TextView) m.findItem(R.id.nav_spam).getActionView().findViewById(R.id.text)).setText("13");
    }


    private void setInfo() {
        MySharedPreferences dataProccessor = new MySharedPreferences(this);

        strFirstName = dataProccessor.getStr(SharedPrefKeys.first_name);
        strLastName = dataProccessor.getStr(SharedPrefKeys.last_name);
        strEmail = dataProccessor.getStr(SharedPrefKeys.email_id);

        strUniqueId = dataProccessor.getStr(SharedPrefKeys.user_id);

        strName = strFirstName+" "+strLastName;

//        tv_home_screen_name.setText(strName);
//        tv_home_screen_addr.setText(strEmail);
//        tv_drawer_header_name.setText(strName);
//        tv_drawer_header_email.setText(strEmail);


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


    @Override
    public void onClick(View v) {
        if (v == btn_rate_us_submit){

            sending();

        }else if (v == img_btn_rate_us_rate1){
            strRating = "1";
        }else if (v == img_btn_rate_us_rate2){
            strRating = "2";
        }else if (v == img_btn_rate_us_rate3){
            strRating = "3";
        }else if (v == img_btn_rate_us_rate4){
            strRating = "4";
        }else if (v == img_btn_rate_us_rate5){
            strRating = "5";
        }
    }

    private void sending() {

        strComment = et_rate_us_comment.getText().toString();



        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.url_rate_us,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                            progressBar.setVisibility(View.GONE);

                        Toast.makeText(getApplicationContext(), "in response"+response+"..", Toast.LENGTH_LONG).show();

                        try {
                            JSONObject myJson = new JSONObject(response);




                            isTaskDone = myJson.getBoolean(URLs.rate_us_task_done);

                            if (isTaskDone){
                                startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//
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
                params.put(URLs.rate_us_unique_id, strUniqueId);
                params.put(URLs.rate_us_name, strName);
                params.put(URLs.rate_us_email, strEmail);
                params.put(URLs.rate_us_rate, strRating);
                params.put(URLs.rate_us_comment, strComment);
                return params;
            }
        };

        MyVolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }




}
