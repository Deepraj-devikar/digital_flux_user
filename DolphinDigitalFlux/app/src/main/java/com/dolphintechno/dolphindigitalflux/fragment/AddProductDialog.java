package com.dolphintechno.dolphindigitalflux.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dolphintechno.dolphindigitalflux.R;


public class AddProductDialog extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product_dialog, container, false);
        // Inflate the layout for this fragment



        return view;
    }


}



