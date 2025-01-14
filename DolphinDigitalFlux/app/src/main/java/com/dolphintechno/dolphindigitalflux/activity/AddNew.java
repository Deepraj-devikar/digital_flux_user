package com.dolphintechno.dolphindigitalflux.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
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

public class AddNew extends AppCompatActivity implements View.OnClickListener {

    MySharedPreferences dataProccessor;

    private ActionBar actionBar;
    private Toolbar toolbar;
    private Menu menu_navigation;
    private DrawerLayout drawer;
    private View navigation_header;
    private boolean is_account_mode = false;

    String strFirstName;
    String strLastName;
    String strEmail;
    String strName;
    String str_unique_id;

    String profile_pic_name;
    CircularImageView avatar;

    TextView tv_drawer_header_name, tv_drawer_header_email;

    EditText et_add_new_first_name, et_add_new_last_name, et_add_new_mobile, et_add_new_email, et_add_new_city;

    FloatingActionButton fab_done;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        initNavigationMenu();
        avatar = (CircularImageView) navigation_header.findViewById(R.id.avatar);

        dataProccessor = new MySharedPreferences(this);

        /*
         * Fetching User Id For Recognizing User
         * So We Can Get Users Info.
         */
        str_unique_id = dataProccessor.getStr(SharedPrefKeys.user_id);

        et_add_new_first_name = (EditText) findViewById(R.id.et_add_new_first_name);
        et_add_new_last_name = (EditText) findViewById(R.id.et_add_new_last_name);
        et_add_new_mobile = (EditText) findViewById(R.id.et_add_new_mobile);
        et_add_new_email = (EditText) findViewById(R.id.et_add_new_email);
        et_add_new_city = (EditText) findViewById(R.id.et_add_new_city);

        fab_done = (FloatingActionButton) findViewById(R.id.fab_done);

        fab_done.setOnClickListener(this);

        setInfo();
        initToolbar();
        initComponent();
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

    private void initNavigationMenu() {
        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view_add_new);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_add_new);
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


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.deep_purple_800);
    }

    private void initComponent() {
        (findViewById(R.id.fab_done)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();

                final String strFirstName, strLastName, strMobile, strEmail, strCity;

                strFirstName = et_add_new_first_name.getText().toString();
                strLastName = et_add_new_last_name.getText().toString();
                strMobile = et_add_new_mobile.getText().toString();
                strEmail = et_add_new_email.getText().toString();
                strCity = et_add_new_city.getText().toString();


                if (TextUtils.isEmpty(strEmail)){
                    et_add_new_email.setError("* required");
                }else if (TextUtils.isEmpty(strMobile)){
                    et_add_new_mobile.setError("* required");
                }else {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.url_add_new,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                                    try {

                                        JSONObject jsonObject = new JSONObject(response);
                                        Toast.makeText(getApplicationContext(), "--"+jsonObject.getString(URLs.keyAddNewMassage), Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), HomeScreen.class));
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
                            params.put(URLs.keyAddNewUniqueId, str_unique_id);
                            params.put(URLs.keyAddNewFirstName, strFirstName);
                            params.put(URLs.keyAddNewLastName, strLastName);
                            params.put(URLs.keyAddNewMobile, strMobile);
                            params.put(URLs.keyAddNewEmail, strEmail);
                            params.put(URLs.keyAddNewCity, strCity);
                            return params;
                        }
                    };
                    MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                }
            }
        });

//        (findViewById(R.id.et_age)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showAgeDialog(v);
//            }
//        });
//
//        (findViewById(R.id.et_gender)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showGenderDialog(v);
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_notif_setting, menu);
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

    private void showAgeDialog(final View v) {
        final String[] array = new String[]{
                "< 17", "17-25", "26-40", "40 <"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Age");
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        if (v == fab_done){
            final String strFirstName, strLastName, strMobile, strEmail, strCity;

            strFirstName = et_add_new_first_name.getText().toString();
            strLastName = et_add_new_last_name.getText().toString();
            strMobile = et_add_new_mobile.getText().toString();
            strEmail = et_add_new_email.getText().toString();
            strCity = et_add_new_city.getText().toString();


            if (TextUtils.isEmpty(strEmail)){
                et_add_new_email.setError("* required");
            }else if (TextUtils.isEmpty(strMobile)){
                et_add_new_mobile.setError("* required");
            }else {
                Toast.makeText(getApplicationContext(), " in else", Toast.LENGTH_LONG).show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.url_add_new,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), "--"+response, Toast.LENGTH_LONG).show();
                                try {

                                    JSONObject jsonObject = new JSONObject(response);

                                    startActivity(new Intent(getApplicationContext(),HomeScreen.class));
                                    finish();
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
                        params.put(URLs.keyAddNewUniqueId, str_unique_id);
                        params.put(URLs.keyAddNewFirstName, strFirstName);
                        params.put(URLs.keyAddNewLastName, strLastName);
                        params.put(URLs.keyAddNewMobile, strMobile);
                        params.put(URLs.keyAddNewEmail, strEmail);
                        params.put(URLs.keyAddNewCity, strCity);
                        return params;
                    }
                };
                MyVolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
            }
        }
    }
}
