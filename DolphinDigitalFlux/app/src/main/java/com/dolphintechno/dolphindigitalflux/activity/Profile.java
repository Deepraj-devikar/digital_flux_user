package com.dolphintechno.dolphindigitalflux.activity;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.dolphintechno.dolphindigitalflux.R;
import com.dolphintechno.dolphindigitalflux.data.MySharedPreferences;
import com.dolphintechno.dolphindigitalflux.helper.SharedPrefKeys;
import com.dolphintechno.dolphindigitalflux.utils.Tools;
import com.mikhaellopez.circularimageview.CircularImageView;

public class Profile extends AppCompatActivity {

    private ActionBar actionBar;
    private Toolbar toolbar;
    private Menu menu_navigation;
    private DrawerLayout drawer;
    private View navigation_header;
    private boolean is_account_mode = false;

    TextView tv_drawer_header_name, tv_drawer_header_email;

    String strFirstName, strLastName, strEmail, strName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tv_drawer_header_name = (TextView) findViewById(R.id.tv_drawer_header_name);
        tv_drawer_header_email = (TextView) findViewById(R.id.tv_drawer_header_email);



        initToolbar();
        initComponent();
        initNavigationMenu();

        setInfo();
    }

    private void setInfo() {

        MySharedPreferences dataProccessor = new MySharedPreferences(this);

        strFirstName = dataProccessor.getStr(SharedPrefKeys.first_name);
        strLastName = dataProccessor.getStr(SharedPrefKeys.last_name);
        strEmail = dataProccessor.getStr(SharedPrefKeys.email_id);

        strName = strFirstName+" "+strLastName;

//        tv_drawer_header_name.setText(strName);
//        tv_drawer_header_email.setText(strEmail);

        tv_drawer_header_name = (TextView) navigation_header.findViewById(R.id.tv_drawer_header_name);

        tv_drawer_header_email = (TextView) navigation_header.findViewById(R.id.tv_drawer_header_email);

        tv_drawer_header_name.setText(strName);
        tv_drawer_header_email.setText(strEmail);

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.purple_600);
    }

    private void initComponent() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_setting, menu);
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

        tv_drawer_header_name = (TextView) navigation_header.findViewById(R.id.tv_drawer_header_name);

        tv_drawer_header_email = (TextView) navigation_header.findViewById(R.id.tv_drawer_header_email);

        if (!is_account_mode) {
//            Toast.makeText(getApplicationContext(), item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
//            actionBar.setTitle(item.getTitle());
            drawer.closeDrawers();
            navigate(item.getTitle());
        } else {
//            TextView name = (TextView) navigation_header.findViewById(R.id.name);
//            TextView email = (TextView) navigation_header.findViewById(R.id.email);
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
//
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


}
