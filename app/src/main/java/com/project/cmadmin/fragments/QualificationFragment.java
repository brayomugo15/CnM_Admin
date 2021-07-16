package com.project.cmadmin.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.cmadmin.R;
import com.project.cmadmin.utils.BottomNavLocker;

import java.util.ArrayList;
import java.util.Arrays;


public class QualificationFragment extends Fragment {

    private View view;

    private Spinner spinnerInsuranceType;
    private TextView tvCompanyName;
    private EditText txtInsurancePolicy, txtISO;
    private Button btnInsurance;

    private QualificationFragmentArgs args;

    private String companyid, name;

    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_qualification, container, false);

        // set
        ((BottomNavLocker) getActivity()).BottomNavLocked(true);

        args = QualificationFragmentArgs.fromBundle(getArguments());
        companyid = args.getCompanyId();

        reference = FirebaseDatabase.getInstance().getReference().child("Profile");


        // find view by id
        tvCompanyName = view.findViewById(R.id.tv_qualification_companyname);
        spinnerInsuranceType = view.findViewById(R.id.spinner_insurancetype);
        txtInsurancePolicy = view.findViewById(R.id.txt_insurancepolicy);
        txtISO = view.findViewById(R.id.txt_isocertification);
        btnInsurance = view.findViewById(R.id.btn_qualification);

        // set
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.insurance_array, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerInsuranceType.setAdapter(adapter);

        reference.orderByChild("companyid").equalTo(companyid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot != null) {
                    name = snapshot.child("companyname").getValue().toString();
                }

                tvCompanyName.setText(name);
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
        btnInsurance.setOnClickListener(qualificationListener);

        return view;
    }

    private View.OnClickListener qualificationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String insurancetype = spinnerInsuranceType.getSelectedItem().toString();
            String insurancepolicy = txtInsurancePolicy.getText().toString().trim();
            String iso = txtISO.getText().toString().trim();

            if (!insurancepolicy.isEmpty() || !iso.isEmpty()) {

                QualificationFragmentDirections.NavigateToService action =
                        QualificationFragmentDirections.navigateToService(companyid, insurancetype, insurancepolicy, iso);
                Navigation.findNavController(view).navigate(action);

            } else {
                Toast.makeText(getContext(), "Fill in all the details", Toast.LENGTH_SHORT).show();
            }
        }
    };

}