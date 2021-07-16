package com.project.cmadmin.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.cmadmin.R;
import com.project.cmadmin.utils.BottomNavLocker;

public class ServiceFragment extends Fragment {

    private View view;

    private TextView tvCompanyName;
    private CheckBox chkPlanning, chkConstruction, chkFabrication;
    private Button btnService;

    private ServiceFragmentArgs args;

    private String companyid, companyname, insurancetype, insurancepolicy, iso;

    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_service, container, false);

        // set
        ((BottomNavLocker) getActivity()).BottomNavLocked(true);

        args = ServiceFragmentArgs.fromBundle(getArguments());
        companyid = args.getCompanyId();
        insurancetype = args.getInsuranceType();
        insurancepolicy = args.getInsurancePolicy();
        iso = args.getIso();

        reference = FirebaseDatabase.getInstance().getReference().child("Profile");

        // find view by id
        tvCompanyName = view.findViewById(R.id.tv_service_companyname);
        chkPlanning = view.findViewById(R.id.chk_planning);
        chkConstruction = view.findViewById(R.id.chk_construction);
        chkFabrication = view.findViewById(R.id.chk_fabrication);

        btnService = view.findViewById(R.id.btn_service);

        // set
        reference.orderByChild("companyid").equalTo(companyid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot != null) {
                    companyname = snapshot.child("companyname").getValue().toString();
                }
                tvCompanyName.setText(companyname);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // set click
        btnService.setOnClickListener(serviceListener);

        return view;
    }

    private View.OnClickListener serviceListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            boolean planning = false, construction = false, fabrication = false;

            if (chkPlanning.isChecked()) {
                planning = true;
            }

            if (chkConstruction.isChecked()) {
                construction = true;
            }

            if (chkFabrication.isChecked()) {
                fabrication = true;
            }

            ServiceFragmentDirections.NavigateToProduct action =
                    ServiceFragmentDirections.navigateToProduct(companyid, insurancetype, insurancepolicy, iso);
            action.setPlanning(planning);
            action.setConstruction(construction);
            action.setFabrication(fabrication);

            Navigation.findNavController(view).navigate(action);
        }
    };
}