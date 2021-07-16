package com.project.cmadmin.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.cmadmin.R;
import com.project.cmadmin.models.CompanyPlans;
import com.project.cmadmin.models.CompanyQualification;
import com.project.cmadmin.models.CompanyServices;
import com.project.cmadmin.utils.BottomNavLocker;

public class ProductFragment extends Fragment {

    private View view;

    private CheckBox chkBungalow, chkApartment, chkMansionette;
    private TextView tvCompanyName, tvBungalow, tvApartment, tvMansionette;
    private Spinner spinnerProductDuration;
    private EditText txtProductCost, txtProductConsultationFee;
    private Button btnProduct;

    private LinearLayout lytProduct;

    private ProductFragmentArgs args;

    private String productname, companyid, companyname, insurancetype, insurancepolicy, iso, plan;
    private boolean planning, construction, fabrication;

    private DatabaseReference profilereference, planreference, qualificationreference, servicereference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product, container, false);

        // set
        ((BottomNavLocker) getActivity()).BottomNavLocked(true);

        args = ProductFragmentArgs.fromBundle(getArguments());
        companyid = args.getCompanyId();
        insurancetype = args.getInsuranceType();
        insurancepolicy = args.getInsurancePolicy();
        iso = args.getIso();
        planning = args.getPlanning();
        construction = args.getConstruction();
        fabrication = args.getFabrication();

        profilereference = FirebaseDatabase.getInstance().getReference().child("Profile");
        planreference = FirebaseDatabase.getInstance().getReference().child("CompanyPlans");
        qualificationreference = FirebaseDatabase.getInstance().getReference().child("CompanyQualification");
        servicereference = FirebaseDatabase.getInstance().getReference().child("CompanyServices");

        // find view by id
        lytProduct = view.findViewById(R.id.lyt_product);

        chkBungalow = view.findViewById(R.id.chk_bungalow);
        chkApartment = view.findViewById(R.id.chk_apartment);
        chkMansionette = view.findViewById(R.id.chk_mansionette);

        spinnerProductDuration = view.findViewById(R.id.spinner_product_duration);
        txtProductCost = view.findViewById(R.id.txt_product_costduration);
        txtProductConsultationFee = view.findViewById(R.id.txt_product_consultationfee);

        tvCompanyName = view.findViewById(R.id.tv_product_companyname);
        tvBungalow = view.findViewById(R.id.tv_bungalow);
        tvApartment = view.findViewById(R.id.tv_apartment);
        tvMansionette = view.findViewById(R.id.tv_mansionette);

        btnProduct = view.findViewById(R.id.btn_product);

        // set
        profilereference.orderByChild("companyid").equalTo(companyid).addChildEventListener(new ChildEventListener() {
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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.duration_months, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerProductDuration.setAdapter(adapter);

        // set on click
        tvBungalow.setOnClickListener(bungalowListener);
        tvApartment.setOnClickListener(apartmentListener);
        tvMansionette.setOnClickListener(mansionetteListener);

        btnProduct.setOnClickListener(productListener);

        return view;
    }


    private View.OnClickListener bungalowListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            productname = "Bungalow";

            ProductFragmentDirections.NavigateToViewProduct action = ProductFragmentDirections.navigateToViewProduct(productname);
            Navigation.findNavController(view).navigate(action);
        }
    };

    private View.OnClickListener apartmentListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            productname = "Apartment";

            ProductFragmentDirections.NavigateToViewProduct action = ProductFragmentDirections.navigateToViewProduct(productname);
            Navigation.findNavController(view).navigate(action);
        }
    };

    private View.OnClickListener mansionetteListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            productname = "Mansionette";

            ProductFragmentDirections.NavigateToViewProduct action = ProductFragmentDirections.navigateToViewProduct(productname);
            Navigation.findNavController(view).navigate(action);
        }
    };

    private View.OnClickListener productListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String duration = spinnerProductDuration.getSelectedItem().toString();
            String costpermonth = txtProductCost.getText().toString().trim();
            String consultationfee = txtProductConsultationFee.getText().toString().trim();

            if (chkBungalow.isChecked() || chkMansionette.isChecked() || chkApartment.isChecked()) {

                if (!costpermonth.isEmpty() && !consultationfee.isEmpty()) {
                    CompanyServices services = new CompanyServices(companyid, planning, construction, fabrication);

                    int durationmonth = Integer.parseInt(spinnerProductDuration.getSelectedItem().toString());
                    int costmonth = Integer.parseInt(costpermonth);
                    int fee = Integer.parseInt(consultationfee);

                    servicereference.push().setValue(services).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                CompanyQualification qualification = new CompanyQualification(companyid, insurancepolicy, insurancetype, iso);

                                qualificationreference.push().setValue(qualification).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {

                                            if (chkBungalow.isChecked()) {
                                                plan = "bungalow";

                                                CompanyPlans plans = new CompanyPlans(companyid, plan, durationmonth, costmonth, fee);
                                                planreference.push().setValue(plans);
                                            }

                                            if (chkMansionette.isChecked()) {
                                                plan = "mansionette";

                                                CompanyPlans plans = new CompanyPlans(companyid, plan, durationmonth, costmonth, fee);
                                                planreference.push().setValue(plans);
                                            }

                                            if (chkApartment.isChecked()) {
                                                plan = "apartment";

                                                CompanyPlans plans = new CompanyPlans(companyid, plan, durationmonth, costmonth, fee);
                                                planreference.push().setValue(plans);
                                            }

                                            Toast.makeText(getContext(), "Succesful", Toast.LENGTH_SHORT).show();

                                            ((BottomNavLocker) getActivity()).BottomNavLocked(false);
                                            Navigation.findNavController(view).navigate(ProductFragmentDirections.navigateToDashboard());


                                        } else {
                                            Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Fill in all the details", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "You should Check at least one product", Toast.LENGTH_SHORT).show();
            }
        }
    };
}