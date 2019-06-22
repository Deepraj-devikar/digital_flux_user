package com.dolphintechno.dolphindigitalflux.data;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.content.res.AppCompatResources;

import com.dolphintechno.dolphindigitalflux.R;
import com.dolphintechno.dolphindigitalflux.model.People;
import com.dolphintechno.dolphindigitalflux.model.ShopCategory;
import com.dolphintechno.dolphindigitalflux.model.ShopProduct;
import com.dolphintechno.dolphindigitalflux.utils.Tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataGenerator {

    /**
     * Generate dummy data people
     *
     * @param ctx android context
     * @return list of object
     */
    public static List<People> getPeopleData(Context ctx) {
        List<People> items = new ArrayList<>();
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.people_images);
        String name_arr[] = ctx.getResources().getStringArray(R.array.people_names);

        for (int i = 0; i < drw_arr.length(); i++) {
            People obj = new People();
            obj.image = drw_arr.getResourceId(i, -1);
            obj.name = name_arr[i];
            obj.email = Tools.getEmailFromName(obj.name);
            obj.imageDrw = ctx.getResources().getDrawable(obj.image);
            items.add(obj);
        }
        Collections.shuffle(items);
        return items;
    }

    public static List<String> getStringsMonth(Context ctx) {
        List<String> items = new ArrayList<>();
        String arr[] = ctx.getResources().getStringArray(R.array.month);
        for (String s : arr) items.add(s);
        Collections.shuffle(items);
        return items;
    }

    /**
     * Generate dummy data shopping category
     *
     * @param ctx android context
     * @return list of object
     */
    public static List<ShopCategory> getShoppingCategory(Context ctx) {
        List<ShopCategory> items = new ArrayList<>();
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.shop_category_icon);
        TypedArray drw_arr_bg = ctx.getResources().obtainTypedArray(R.array.shop_category_bg);
        String title_arr[] = ctx.getResources().getStringArray(R.array.shop_category_title);
        String brief_arr[] = ctx.getResources().getStringArray(R.array.shop_category_brief);
        for (int i = 0; i < drw_arr.length(); i++) {
            ShopCategory obj = new ShopCategory();
            obj.image = drw_arr.getResourceId(i, -1);
            obj.image_bg = drw_arr_bg.getResourceId(i, -1);
            obj.title = title_arr[i];
            obj.brief = brief_arr[i];
            obj.imageDrw = AppCompatResources.getDrawable(ctx, obj.image);
            items.add(obj);
        }
        return items;
    }

    /**
     * Generate dummy data shopping product
     *
     * @param ctx android context
     * @return list of object
     */
    public static List<ShopProduct> getShoppingProduct(Context ctx) {
        List<ShopProduct> items = new ArrayList<>();
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.shop_product_image);
        String title_arr[] = ctx.getResources().getStringArray(R.array.shop_product_title);
        String price_arr[] = ctx.getResources().getStringArray(R.array.shop_product_price);
        for (int i = 0; i < drw_arr.length(); i++) {
            ShopProduct obj = new ShopProduct();
            obj.image = drw_arr.getResourceId(i, -1);
            obj.productName = title_arr[i];
            obj.productMrp = price_arr[i];
            obj.imageDrw = ctx.getResources().getDrawable(obj.image);
            items.add(obj);
        }
        return items;
    }

}
