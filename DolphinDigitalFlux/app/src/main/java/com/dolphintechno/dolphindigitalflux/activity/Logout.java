package com.dolphintechno.dolphindigitalflux.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dolphintechno.dolphindigitalflux.R;
import com.dolphintechno.dolphindigitalflux.data.MySharedPreferences;
import com.dolphintechno.dolphindigitalflux.helper.SharedPrefKeys;

public class Logout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        MySharedPreferences dataProccessor = new MySharedPreferences(this);
        dataProccessor.setBool(SharedPrefKeys.is_set_user_id, false);

        startActivity(new Intent(this, Login.class));

    }
}
