package com.dolphintechno.dolphindigitalflux.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dolphintechno.dolphindigitalflux.R;
import com.dolphintechno.dolphindigitalflux.model.TeamLevel;

import java.util.List;

public class AdapterTeamLevel extends RecyclerView.Adapter<AdapterTeamLevel.LevelViewHolder> {

    List<TeamLevel> teamLevelList;
    Context context;

    public AdapterTeamLevel(List<TeamLevel> teamLevelList, Context context) {
        this.teamLevelList = teamLevelList;
        this.context = context;
    }

    @NonNull
    @Override
    public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_level, viewGroup, false);
        return new LevelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelViewHolder levelViewHolder, int i) {
        TeamLevel teamLevel = teamLevelList.get(i);

        levelViewHolder.tv_item_lvl_level_number.setText(teamLevel.getLevelNumber());
        levelViewHolder.tv_item_lvl_member_count.setText(teamLevel.getMemberCount());

        if (teamLevel.getMemberCount().equals("0")){
            levelViewHolder.lyt_level.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return teamLevelList.size();
    }

    public class LevelViewHolder extends RecyclerView.ViewHolder {

        TextView tv_item_lvl_level_number, tv_item_lvl_member_count;
        LinearLayout lyt_level;

        public LevelViewHolder(@NonNull View itemView) {
            super(itemView);

            lyt_level = (LinearLayout) itemView.findViewById(R.id.lyt_level);

            tv_item_lvl_level_number = (TextView) itemView.findViewById(R.id.tv_item_lvl_level_number);
            tv_item_lvl_member_count = (TextView) itemView.findViewById(R.id.tv_item_lvl_member_count);

        }
    }
}
