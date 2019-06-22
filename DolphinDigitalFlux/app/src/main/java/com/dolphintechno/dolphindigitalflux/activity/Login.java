package com.dolphintechno.dolphindigitalflux.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dolphintechno.dolphindigitalflux.R;
import com.dolphintechno.dolphindigitalflux.data.MySharedPreferences;
import com.dolphintechno.dolphindigitalflux.helper.SharedPrefKeys;
import com.dolphintechno.dolphindigitalflux.helper.URLs;
import com.dolphintechno.dolphindigitalflux.singleton.MyVolleySingleton;
import com.dolphintechno.dolphindigitalflux.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText txt_inp_et__usrid_eml_mo, txt_inp_et__psw;

    private View parent_view;

    private Button btn_login_sign_in;

    private String str_usrid_eml_mo;
    private String str_psw;
    private String user_id;
    private boolean data_found;
    private String f_name;
    private String l_name;
    private String email;
    private String QR_link;

    boolean yes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        parent_view = findViewById(android.R.id.content);

        //getting IDs
        btn_login_sign_in = (Button) findViewById(R.id.btn_login_sign_in);
        txt_inp_et__usrid_eml_mo = (TextInputEditText) findViewById(R.id.txt_inp_et__usrid_eml_mo);
        txt_inp_et__psw = (TextInputEditText) findViewById(R.id.txt_inp_et__psw);

        //click Listenr
        btn_login_sign_in.setOnClickListener(this);

        Tools.setSystemBarColor(this);

    }

    @Override
    public void onClick(View v) {
        if (v == btn_login_sign_in){
            login();
//            startActivity(new Intent(this, AddWebView.class));
        }
    }

    private void login() {
        str_usrid_eml_mo = txt_inp_et__usrid_eml_mo.getText().toString();
        str_psw = txt_inp_et__psw.getText().toString();
        if (!str_usrid_eml_mo.isEmpty() && !str_psw.isEmpty()){
            //write connection code

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.url_login,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            progressBar.setVisibility(View.GONE);
//                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
//                            Toast.makeText(getApplicationContext(), "--"+response, Toast.LENGTH_LONG).show();
                            try {
                                JSONObject myJson = new JSONObject(response);
                                data_found = myJson.getBoolean(URLs.data_found);
                                user_id = myJson.getString(URLs.user_id);
                                f_name = myJson.getString(URLs.first_name);
                                l_name = myJson.getString(URLs.last_name);
                                email = myJson.getString(URLs.email_id);
                                QR_link = myJson.getString(URLs.QR_link);

                                if (data_found){

                                    MySharedPreferences dataProccessor = new MySharedPreferences(getApplicationContext());
                                    dataProccessor.setStr(SharedPrefKeys.user_id, user_id);
                                    dataProccessor.setBool(SharedPrefKeys.is_set_user_id, true);
                                    dataProccessor.setStr(SharedPrefKeys.first_name, f_name);
                                    dataProccessor.setStr(SharedPrefKeys.last_name, l_name);
                                    dataProccessor.setStr(SharedPrefKeys.email_id, email);
                                    dataProccessor.setStr(SharedPrefKeys.QR_link, QR_link);
                                    startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                                    finish();

                                }else {
                                    Toast.makeText(getApplicationContext(), "user id and password not found", Toast.LENGTH_LONG).show();
                                }




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("r_id", str_usrid_eml_mo);
                    params.put("pw", str_psw);
                    return params;
                }
            };
            MyVolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


        }
    }
}
