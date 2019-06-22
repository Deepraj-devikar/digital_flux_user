package com.dolphintechno.dolphindigitalflux.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dolphintechno.dolphindigitalflux.R;
import com.dolphintechno.dolphindigitalflux.model.SubCategory;

import java.util.List;

public class AdapterSubCategory extends RecyclerView.Adapter<AdapterSubCategory.SubCategoryViewHolder>{

    List<SubCategory> subCategoryList;
    Context context;

    private OnItemClickListener mOnItemClickListener;

    public AdapterSubCategory(List<SubCategory> subCategoryList, Context context) {
        this.subCategoryList = subCategoryList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(SubCategoryViewHolder view, SubCategory obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main_category, viewGroup, false);

        return new SubCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubCategoryViewHolder subCategoryViewHolder, final int i) {

        final SubCategory subCategory = subCategoryList.get(i);

        subCategoryViewHolder.tv_main_category.setText(subCategory.getCategoryName());

        subCategoryViewHolder.tv_main_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == subCategoryViewHolder.tv_main_category){
                    mOnItemClickListener.onItemClick(subCategoryViewHolder, subCategoryList.get(i), i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return subCategoryList.size();
    }

    public class SubCategoryViewHolder extends RecyclerView.ViewHolder {

        TextView tv_main_category;

        public SubCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_main_category = (TextView) itemView.findViewById(R.id.tv_main_category);

        }
    }
}
