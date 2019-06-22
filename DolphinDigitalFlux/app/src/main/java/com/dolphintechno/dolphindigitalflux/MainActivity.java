package com.dolphintechno.dolphindigitalflux;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dolphintechno.dolphindigitalflux.R;
import com.dolphintechno.dolphindigitalflux.activity.HomeScreen;
import com.dolphintechno.dolphindigitalflux.activity.Login;
import com.dolphintechno.dolphindigitalflux.data.MySharedPreferences;
import com.dolphintechno.dolphindigitalflux.helper.SharedPrefKeys;

public class MainActivity extends AppCompatActivity {

    private boolean is_previously_loged_in = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MySharedPreferences sharedPreferences = new MySharedPreferences(this);//For Fetching Previously Saved Data
        is_previously_loged_in = sharedPreferences.getBool(SharedPrefKeys.is_set_user_id);

        if (is_previously_loged_in){
            /*#
             * If Person Is Previously Loged In
             * Then Directly Go To Home Screen
             */
            startActivity(new Intent(this, HomeScreen.class));
            finish();
//            Toast.makeText(getApplicationContext(), "it is previously loged in", Toast.LENGTH_LONG).show();
        }else {
            /*
             * If Person Is Not Previosly Loged In
             * Then First Go To Login Page Or Signup Page
             */
            startActivity(new Intent(this, Login.class));
            finish();
//            Toast.makeText(getApplicationContext(), "is not previously loged in", Toast.LENGTH_LONG).show();
        }

    }
}
