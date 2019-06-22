package com.dolphintechno.dolphindigitalflux.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.dolphintechno.dolphindigitalflux.model.NumberInfo;
import com.dolphintechno.dolphindigitalflux.singleton.MyVolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterNumberInfo extends RecyclerView.Adapter<AdapterNumberInfo.NumberInfoViewHolder>{

    List<NumberInfo> numberInfoList;
    Context context;

    public AdapterNumberInfo(List<NumberInfo> numberInfoList, Context context) {
        this.numberInfoList = numberInfoList;
        this.context = context;
    }

    @NonNull
    @Override
    public NumberInfoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.number_info, viewGroup, false);
        return new NumberInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NumberInfoViewHolder numberInfoViewHolder, int i) {

        final NumberInfo numberInfo = numberInfoList.get(i);

        numberInfoViewHolder.tv_num_info_member_name.setText(numberInfo.getPName());

        numberInfoViewHolder.lyt_num_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(numberInfo.getMobile(), numberInfo.getPName());
            }
        });

        numberInfoViewHolder.img_btn_num_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(numberInfo.getMobile(), numberInfo.getPName());
            }
        });

    }

    private void sendMessage(final String strMob, final String strMName) {



        MySharedPreferences dataProccessor;
        final String str_unique_id;

        dataProccessor = new MySharedPreferences(context);

        /*
         * Fetching User Id For Recognizing User
         * So We Can Get Users Info.
         */
        str_unique_id = dataProccessor.getStr(SharedPrefKeys.user_id);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.url_whtp_nos_msg,
                new Response.Listener<String>() {
                    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

//                            Toast.makeText(context, response, Toast.LENGTH_LONG).show();

                            if (jsonObject.getBoolean(URLs.keyIsPerformOpr)){

                                String strMob1 = "91"+strMob;

                                String strMsg = jsonObject.getString(URLs.keyWhtpNoMsg);

                                String result = java.net.URLDecoder.decode(strMsg, StandardCharsets.UTF_8.name());

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+strMob1+"&text="+result));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);

                            }else {
                                Toast.makeText(context, "recently sended message", Toast.LENGTH_LONG).show();
                            }

//                            String strMob = "91"+jsonObject.getString("mobile");
//                            String strMsg = jsonObject.getString("msg");
//
//                            String result = java.net.URLDecoder.decode(strMsg, StandardCharsets.UTF_8.name());
//
////                                        Toast.makeText(ctx, "http://api.whatsapp.com/send?phone="+strMob+"&text="+result, Toast.LENGTH_LONG).show();
//
//                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+strMob+"&text="+result));
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, String.valueOf(error), Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(URLs.keyWhtpNoUniqueId, str_unique_id);
                params.put(URLs.keyWhtpNoSName, strMName);
                return params;
            }
        };
        MyVolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    @Override
    public int getItemCount() {
        return numberInfoList.size();
    }

    public class NumberInfoViewHolder extends RecyclerView.ViewHolder {

        TextView tv_num_info_member_name;
        ImageButton img_btn_num_info;
        LinearLayout lyt_num_info;

        public NumberInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_num_info_member_name = (TextView) itemView.findViewById(R.id.tv_num_info_member_name);
            img_btn_num_info = (ImageButton) itemView.findViewById(R.id.img_btn_num_info);
            lyt_num_info = (LinearLayout) itemView.findViewById(R.id.lyt_num_info);
        }
    }
}
