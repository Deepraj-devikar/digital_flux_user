package com.dolphintechno.dolphindigitalflux.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dolphintechno.dolphindigitalflux.R;
import com.dolphintechno.dolphindigitalflux.model.MainCategory;

import java.util.List;

public class AdapterMainCategory extends RecyclerView.Adapter<AdapterMainCategory.MainCategoryViewHolder> {

    List<MainCategory> mainCategoryList;
    Context context;
    private OnItemClickListener mOnItemClickListener;


    public AdapterMainCategory(List<MainCategory> mainCategoryList, Context context) {
        this.mainCategoryList = mainCategoryList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(MainCategoryViewHolder view, MainCategory obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }


    @NonNull
    @Override
    public MainCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main_category, viewGroup, false);

        return new MainCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MainCategoryViewHolder mainCategoryViewHolder, final int i) {

        final MainCategory mainCategory = mainCategoryList.get(i);

        mainCategoryViewHolder.tv_main_category.setText(mainCategory.getCategoryName());

        mainCategoryViewHolder.tv_main_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == mainCategoryViewHolder.tv_main_category){
                    mOnItemClickListener.onItemClick(mainCategoryViewHolder, mainCategoryList.get(i), i);
                }
            }
        });

//        mainCategoryViewHolder.tv_main_category.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mOnItemClickListener != null) {
//                    mOnItemClickListener.onItemClick(view, mainCategoryList.get(i), i);
//
//                    Intent intent = new Intent(context, ShopSubCategory.class);
//                    intent.putExtra(IntentKeys.keyShopCategoryId, mainCategory.getCategoryId());
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
////                        finish();
//                }
//
//            }
//
//
//        });

    }




    @Override
    public int getItemCount() {
        return mainCategoryList.size();
    }

    public class MainCategoryViewHolder extends RecyclerView.ViewHolder {

        TextView tv_main_category;

        public MainCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_main_category = (TextView) itemView.findViewById(R.id.tv_main_category);

        }
    }
}
