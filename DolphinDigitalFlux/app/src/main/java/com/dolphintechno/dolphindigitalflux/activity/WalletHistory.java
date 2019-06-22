package com.dolphintechno.dolphindigitalflux.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
import com.dolphintechno.dolphindigitalflux.R;
import com.dolphintechno.dolphindigitalflux.adapter.AdapterWalletHistoryTransaction;
import com.dolphintechno.dolphindigitalflux.data.MySharedPreferences;
import com.dolphintechno.dolphindigitalflux.helper.SharedPrefKeys;
import com.dolphintechno.dolphindigitalflux.helper.URLs;
import com.dolphintechno.dolphindigitalflux.model.WalletHistoryTransaction;
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

//import android.support.design.widget.TabLayout;

public class WalletHistory extends AppCompatActivity implements View.OnClickListener {

    MySharedPreferences dataProccessor;

    private ActionBar actionBar;
    private Toolbar toolbar;
    private Menu menu_navigation;
    private DrawerLayout drawer;
    private View navigation_header;
    private boolean is_account_mode = false;

    WalletHistoryTransaction walletHistoryTransaction;

    List<WalletHistoryTransaction> creditWalletHistoryTransactionList, debitWalletHistoryTransactionList, walletHistoryTransactionList;

    CardView cv_wallet_history_content_main, cv_wallet_history_content_credit, cv_wallet_history_content_debit;

    TextView tv_wallet_history_wallet_balance, tv_wallet_history_credit_count, tv_wallet_history_debit_count, tv_drawer_header_name, tv_drawer_header_email;

    String strWalletBalance, strFirstName, strLastName, strEmail, strName;
    String str_unique_id;

    private static final String keyCredit = "show credit transaction", keyDebit = "show debit transaction", keyAll = "show all transaction";

    boolean isWalletBalanceFetch, isDataFetch;

//    private TabLayout tab_layout;

    String profile_pic_name;
    CircularImageView avatar;

    RecyclerView recyclerView;
    AdapterWalletHistoryTransaction adapterWalletHistoryTransaction;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_history);

        initNavigationMenu();

        dataProccessor = new MySharedPreferences(this);

        /*
         * Fetching User Id For Recognizing User
         * So We Can Get Users Info.
         */
        str_unique_id = dataProccessor.getStr(SharedPrefKeys.user_id);

        creditWalletHistoryTransactionList = new ArrayList<>();
        debitWalletHistoryTransactionList = new ArrayList<>();
        walletHistoryTransactionList = new ArrayList<>();

        avatar = (CircularImageView) navigation_header.findViewById(R.id.avatar);
        cv_wallet_history_content_main = (CardView) findViewById(R.id.cv_wallet_history_content_main);
        cv_wallet_history_content_credit = (CardView) findViewById(R.id.cv_wallet_history_content_credit);
        cv_wallet_history_content_debit = (CardView) findViewById(R.id.cv_wallet_history_content_debit);

        tv_wallet_history_wallet_balance = (TextView) findViewById(R.id.tv_wallet_history_wallet_balance);
        tv_wallet_history_credit_count = (TextView) findViewById(R.id.tv_wallet_history_credit_count);
        tv_wallet_history_debit_count = (TextView) findViewById(R.id.tv_wallet_history_debit_count);
        tv_drawer_header_name = (TextView) findViewById(R.id.tv_drawer_header_name);
        tv_drawer_header_email = (TextView) findViewById(R.id.tv_drawer_header_email);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_wallet_history);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        getWalletHistoryTransactionData(keyAll);

        initToolbar();


        setInfo();

        cv_wallet_history_content_main.setOnClickListener(this);
        cv_wallet_history_content_credit.setOnClickListener(this);
        cv_wallet_history_content_debit.setOnClickListener(this);

    }

    void getWalletHistoryTransactionData(final String key){
        String strUrl = URLs.url_wallet_history+"?uid="+str_unique_id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, strUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject myJson = new JSONObject(response);
                            isWalletBalanceFetch = myJson.getBoolean(URLs.keyIsWalletBalanceFetch);
                            if (isWalletBalanceFetch){
                                strWalletBalance = myJson.getString(URLs.keyWalletBalance);
                                Float floatWalletBalance = Float.valueOf(strWalletBalance);
                                String strBal = String.format("%.2f", floatWalletBalance);
                                tv_wallet_history_wallet_balance.setText(strBal);
                            }
                            isDataFetch = myJson.getBoolean(URLs.keyIsDataFetch);
                            if (isDataFetch){
                                tv_wallet_history_credit_count.setText(String.valueOf(myJson.getInt(URLs.KeyCreditCount)));
                                tv_wallet_history_debit_count.setText(String.valueOf(myJson.getInt(URLs.keyDebitCount)));
                                JSONArray myJsonArray = myJson.getJSONArray(URLs.keyDataArray);
                                for (int i = 0; i < myJsonArray.length(); i++){
                                    JSONObject myJsonData = myJsonArray.getJSONObject(i);
                                    walletHistoryTransaction = new WalletHistoryTransaction();
                                    walletHistoryTransaction.setWalletHistoryId(myJsonData.getString(URLs.keyWHId));
                                    walletHistoryTransaction.setTransaction(myJsonData.getString(URLs.keyTransaction));
                                    walletHistoryTransaction.setDoneBy(myJsonData.getString(URLs.keyDoneBy));
                                    walletHistoryTransaction.setAmount(myJsonData.getInt(URLs.keyAmount));
                                    walletHistoryTransaction.setReason(myJsonData.getString(URLs.keyReason));
                                    walletHistoryTransaction.setBalance(myJsonData.getInt(URLs.keyBalance));
                                    walletHistoryTransaction.setDateTime(myJsonData.getString(URLs.keyDate));
                                    if (walletHistoryTransaction.getTransaction().equals(URLs.keyCreditTransaction)){
                                        creditWalletHistoryTransactionList.add(walletHistoryTransaction);
                                    }else if (walletHistoryTransaction.getTransaction().equals(URLs.keyDebitTransaction)){
                                        debitWalletHistoryTransactionList.add(walletHistoryTransaction);
                                    }
                                    walletHistoryTransactionList.add(walletHistoryTransaction);
                                }
                                adapterWalletHistoryTransaction = new AdapterWalletHistoryTransaction(getApplicationContext());
                                recyclerView.setAdapter(adapterWalletHistoryTransaction);
                                if (key.equals(keyAll)){
                                    adapterWalletHistoryTransaction.addAll(walletHistoryTransactionList);
                                }else if (key.equals(keyCredit)){
                                    adapterWalletHistoryTransaction.addAll(creditWalletHistoryTransactionList);
                                }else if (key.equals(keyDebit)){
                                    adapterWalletHistoryTransaction.addAll(debitWalletHistoryTransactionList);
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
                params.put(URLs.keyUniqueId, str_unique_id);
                return params;
            }
        };
        MyVolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.light_blue_500), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        Tools.setSystemBarColor(this, R.color.grey_5);
        Tools.setSystemBarLight(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.light_blue_500));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
        } else if (item.getTitle().equals("Refresh")){
            startActivity(new Intent(this, WalletHistory.class));
            finish();
        } else{
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
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
//            Toast.makeText(getApplicationContext(), item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
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
        if (v == cv_wallet_history_content_main){
            getWalletHistoryTransactionData(keyAll);
        }else if (v == cv_wallet_history_content_credit){
            getWalletHistoryTransactionData(keyCredit);
        } else if (v == cv_wallet_history_content_debit) {
            getWalletHistoryTransactionData(keyDebit);
        }
    }
}
