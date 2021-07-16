package com.project.cmadmin.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.cmadmin.R;


public class ViewProductFragment extends Fragment {

    private View view;

    private TextView tvPlanName, tvPlanDesc;
    private ImageView imgPlan;

    private String productname;

    private ViewProductFragmentArgs args;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_product, container, false);

        // set
        args = ViewProductFragmentArgs.fromBundle(getArguments());

        productname = args.getProductName();

        // find view by id
        tvPlanName = view.findViewById(R.id.tv_view_planname);
        tvPlanDesc = view.findViewById(R.id.tv_view_desc);
        imgPlan = view.findViewById(R.id.img_view_plan);

        // set / load

        if (productname.equals("apartment")) {
            imgPlan.setImageResource(R.drawable.apartment);
        } else if (productname.equals("bungalow")) {
            imgPlan.setImageResource(R.drawable.bungalow);
        } else if (productname.equals("mansionette")) {
            imgPlan.setImageResource(R.drawable.mansionette);
        }

        // set click

        return view;
    }
}