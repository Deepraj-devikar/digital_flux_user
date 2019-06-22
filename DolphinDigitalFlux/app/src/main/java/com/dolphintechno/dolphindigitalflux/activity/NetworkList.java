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
import com.dolphintechno.dolphindigitalflux.adapter.AdapterNetworkListSectioned;
import com.dolphintechno.dolphindigitalflux.adapter.AdapterTeamLevel;
import com.dolphintechno.dolphindigitalflux.data.MySharedPreferences;
import com.dolphintechno.dolphindigitalflux.helper.SharedPrefKeys;
import com.dolphintechno.dolphindigitalflux.helper.URLs;
import com.dolphintechno.dolphindigitalflux.model.TeamLevel;
import com.dolphintechno.dolphindigitalflux.model.TeamMember;
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

public class NetworkList extends AppCompatActivity {

    MySharedPreferences dataProccessor;

    private ActionBar actionBar;
    private Toolbar toolbar;
    private Menu menu_navigation;
    private DrawerLayout drawer;
    private View navigation_header;
    private boolean is_account_mode = false;

    List<TeamMember> team;
    List<TeamLevel> teamLevelList;
    TeamMember teamMember;
    TeamLevel teamLevel;
    TextView tv_drawer_header_name, tv_drawer_header_email;

    String strFirstName, strLastName, strEmail, strName;
    String str_unique_id;

    private View parent_view;

    private RecyclerView recyclerView;
    private RecyclerView recyclerViewLevel;
    private AdapterNetworkListSectioned mAdapter;
    private AdapterTeamLevel adapterTeamLevel;

    String profile_pic_name;
    CircularImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_list);
        parent_view = findViewById(android.R.id.content);

        initNavigationMenu();
        avatar = (CircularImageView) navigation_header.findViewById(R.id.avatar);

        dataProccessor = new MySharedPreferences(this);

        /*
         * Fetching User Id For Recognizing User
         * So We Can Get Users Info.
         */
        str_unique_id = dataProccessor.getStr(SharedPrefKeys.user_id);

        tv_drawer_header_name = (TextView) findViewById(R.id.tv_drawer_header_name);
        tv_drawer_header_email = (TextView) findViewById(R.id.tv_drawer_header_email);

        team = new ArrayList<>();
        teamLevelList = new ArrayList<>();

        fetchTeam();
        initToolbar();
        initComponent();

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
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Team");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
    }

    private void fetchTeam(){
        StringRequest stringRequestTeam = new StringRequest(Request.Method.POST, URLs.url_team_fetching_team,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(getApplicationContext(), "--"+response, Toast.LENGTH_LONG).show();
                        try {
                            JSONArray jsonArray = new JSONArray(response);



                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONArray levelJsonArr = jsonArray.getJSONArray(i);
                                int levelLength = levelJsonArr.length();
                                teamMember = new TeamMember();
                                teamLevel = new TeamLevel();
                                teamMember.setUniqueId("--");
                                teamMember.setFirstName("Level "+(i + 1));
                                teamLevel.setLevelNumber(String.valueOf(i+1));
                                teamMember.setLastName("Members Count "+levelLength);
                                teamLevel.setMemberCount(String.valueOf(levelLength));
                                teamMember.setFullName("--");
                                teamMember.setSponserId("--");
                                teamMember.setSection(true);
                                team.add(teamMember);
                                teamLevelList.add(teamLevel);
                                for (int j = 0; j < levelLength; j++){
                                    JSONObject jsonMemberDetail = levelJsonArr.getJSONObject(j);
                                    teamMember = new TeamMember();
                                    teamMember.setUniqueId(jsonMemberDetail.getString(URLs.keyTeamUniqueId));
                                    teamMember.setFirstName(jsonMemberDetail.getString(URLs.keyTeamFirstName));
                                    teamMember.setLastName(jsonMemberDetail.getString(URLs.keyTeamLastName));
                                    teamMember.setFullName(teamMember.getFirstName()+" "+teamMember.getLastName());
                                    teamMember.setSponserId(jsonMemberDetail.getString(URLs.keyTeamSponserId));
                                    teamMember.setSection(false);
                                    team.add(teamMember);
                                }
                            }
                            //set data and list adapter
                            mAdapter = new AdapterNetworkListSectioned(getApplicationContext(), team);
                            adapterTeamLevel = new AdapterTeamLevel(teamLevelList, getApplicationContext());
                            recyclerViewLevel.setAdapter(adapterTeamLevel);
                            recyclerView.setAdapter(mAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), String.valueOf(error), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(URLs.keyTeamUserId_send, str_unique_id);
                return params;
            }
        };
        MyVolleySingleton.getInstance(this).addToRequestQueue(stringRequestTeam);
    }

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerViewLevel = (RecyclerView) findViewById(R.id.recy_view_level);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewLevel.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerViewLevel.setHasFixedSize(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_search_setting, menu);
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
        updateCounter(nav_view);
        menu_navigation = nav_view.getMenu();

        // navigation header
        navigation_header = nav_view.getHeaderView(0);
    }

    private void onItemNavigationClicked(MenuItem item) {

        tv_drawer_header_name = (TextView) navigation_header.findViewById(R.id.tv_drawer_header_name);

        tv_drawer_header_email = (TextView) navigation_header.findViewById(R.id.tv_drawer_header_email);

        if (!is_account_mode) {
            navigate(item.getTitle());
            drawer.closeDrawers();

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
