package com.dolphintechno.dolphindigitalflux.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dolphintechno.dolphindigitalflux.R;
import com.dolphintechno.dolphindigitalflux.data.MySharedPreferences;
import com.dolphintechno.dolphindigitalflux.helper.SharedPrefKeys;
import com.dolphintechno.dolphindigitalflux.utils.Tools;
import com.mikhaellopez.circularimageview.CircularImageView;

public class AddProduct extends AppCompatActivity implements View.OnClickListener {

    MySharedPreferences dataProccessor;

    private ActionBar actionBar;
    private Toolbar toolbar;
    private Menu menu_navigation;
    private DrawerLayout drawer;
    private View navigation_header;
    private boolean is_account_mode = false;
    CircularImageView avatar;

    TextView tv_drawer_header_name, tv_drawer_header_email;
    CircularImageView img_home_screen_profile_pic;

    EditText et_add_product_p_name, et_add_product_p_info, et_add_product_p_mrp, et_add_product_p_selling_price;
    EditText et_add_product_p_gst;

    Button bt_next_add_product;


    String strFirstName;
    String strLastName;
    String strEmail;
    String strName;
    String str_unique_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

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


        et_add_product_p_name = (EditText) findViewById(R.id.et_add_product_p_name);
        et_add_product_p_info = (EditText) findViewById(R.id.et_add_product_p_info);
        et_add_product_p_mrp = (EditText) findViewById(R.id.et_add_product_p_mrp);
        et_add_product_p_selling_price = (EditText) findViewById(R.id.et_add_product_p_selling_price);

        et_add_product_p_gst = (EditText) findViewById(R.id.et_add_product_p_gst);

        bt_next_add_product = (Button) findViewById(R.id.bt_next_add_product);

        bt_next_add_product.setOnClickListener(this);

        initToolbar();
        setInfo();
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


    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Product");
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
        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view_add_product);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout_add_product);
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
        if (v == bt_next_add_product){
            String strProductName, strProductInfo, strProductMrp, strProductSellingPrice, strProductGST;

            strProductName = et_add_product_p_name.getText().toString();
            strProductInfo = et_add_product_p_info.getText().toString();
            strProductMrp = et_add_product_p_mrp.getText().toString();
            strProductSellingPrice = et_add_product_p_selling_price.getText().toString();
            strProductGST = et_add_product_p_gst.getText().toString();

            if (TextUtils.isEmpty(strProductName)){
                et_add_product_p_name.setError("fill Product Name");
                return;
            }else if (TextUtils.isEmpty(strProductInfo)){
                et_add_product_p_info.setError("fill Product Info");
                return;
            }else if (TextUtils.isEmpty(strProductMrp)){
                et_add_product_p_mrp.setError("fill Product Mrp");
                return;
            }else if (TextUtils.isEmpty(strProductSellingPrice)){
                et_add_product_p_selling_price.setError("fill Product Selling Price");
                return;
            }else if (TextUtils.isEmpty(strProductGST)){
                et_add_product_p_gst.setError("fill Product GST");
                return;
            }else {
                Intent intent = new Intent(this, AddProductSelectCategory.class);
                intent.putExtra("Product Name", strProductName);
                intent.putExtra("Product Info", strProductInfo);
                intent.putExtra("Product MRP", strProductMrp);
                intent.putExtra("Product Selling Price", strProductSellingPrice);
                intent.putExtra("Product GST", strProductGST);
                startActivity(intent);
            }
        }
    }
}
