package com.dolphintechno.dolphindigitalflux.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import com.dolphintechno.dolphindigitalflux.model.TeamMember;
import com.dolphintechno.dolphindigitalflux.singleton.MyVolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterNetworkListSectioned extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_SECTION = 0;

    private List<TeamMember> team = new ArrayList<>();
    private Context ctx;

    public AdapterNetworkListSectioned (Context context, List<TeamMember> team) {
        this.team = team;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
//        public ImageView image;
        public TextView tv_team_member_name;
        public View lyt_parent;
        public ImageButton img_btn_wa;

        public OriginalViewHolder(View v) {
            super(v);
//            image = (ImageView) v.findViewById(R.id.image);
            tv_team_member_name = (TextView) v.findViewById(R.id.tv_team_member_name);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            img_btn_wa = (ImageButton) v.findViewById(R.id.img_btn_wa);
        }
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lyt_section;
        public TextView title_section, tv_section_count;

        public SectionViewHolder(View v) {
            super(v);
            lyt_section = (LinearLayout) v.findViewById(R.id.lyt_section);
            title_section = (TextView) v.findViewById(R.id.title_section);
            tv_section_count = (TextView) v.findViewById(R.id.tv_section_count);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_member, parent, false);
            vh = new OriginalViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false);
            vh = new SectionViewHolder(v);
        }
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final TeamMember teamMember = team.get(position);
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            MySharedPreferences dataProccessor = new MySharedPreferences(ctx);

            final String strUserId = dataProccessor.getStr(SharedPrefKeys.user_id);

            view.tv_team_member_name.setText(teamMember.getFullName());

//            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    sendWhatsappMsg(strUserId, teamMember.getUniqueId());
//                }
//            });

            view.img_btn_wa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendWhatsappMsg(strUserId, teamMember.getUniqueId());
                }
            });

        } else {
            SectionViewHolder view = (SectionViewHolder) holder;
            view.title_section.setText(teamMember.getFirstName());
            view.tv_section_count.setText(teamMember.getLastName());

            if (teamMember.getLastName().equals("Members Count "+"0")){
                view.lyt_section.setVisibility(View.GONE);
            }
        }
    }

    void sendWhatsappMsg(final String strUserId, final String strMemberId){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.url_team_msg,
                new Response.Listener<String>() {
                    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {

//                        Toast.makeText(ctx, ""+response, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObject = new JSONObject(response);


                            if (jsonObject.getBoolean(URLs.keyTeamMsg2PerfOpr)){

                                String strMob = "91"+jsonObject.getString("mobile");
                                String strMsg = jsonObject.getString("msg");

                                String result = java.net.URLDecoder.decode(strMsg, StandardCharsets.UTF_8.name());

//                                        Toast.makeText(ctx, "http://api.whatsapp.com/send?phone="+strMob+"&text="+result, Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+strMob+"&text="+result));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                ctx.startActivity(intent);



                            }else {

                                Toast.makeText(ctx, "recently sended message", Toast.LENGTH_LONG).show();
                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
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
                params.put("uid", strUserId);
                params.put("mid", strMemberId);
                return params;
            }
        };
        MyVolleySingleton.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    @Override
    public int getItemCount() {
        return team.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.team.get(position).section ? VIEW_SECTION : VIEW_ITEM;
    }


}
