package com.project.cmadmin.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.cmadmin.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ViewOrderFragment extends Fragment {

    private View view;

    private TextView tvContractName, tvContractType, tvContractDate, tvContractPayment, tvContractPlanName, tvContractLocation, tvContractDuration, tvContractCost;
    private Button btnApprove, btnReject;

    private String email, companyid, refKey, ordername, orderno_status, orderdate, planname, location, duration;
    private int totalcost;

    private ViewOrderFragmentArgs args;

    private DatabaseReference reference, companyreference;
    private FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_order, container, false);

        // set
        reference = FirebaseDatabase.getInstance().getReference().child("Orders");
        companyreference = FirebaseDatabase.getInstance().getReference().child("Profile");

        args = ViewOrderFragmentArgs.fromBundle(getArguments());
        refKey = args.getRefKey();

        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();

        // find view by id
        tvContractName = view.findViewById(R.id.tv_vieworder_name);
        tvContractType = view.findViewById(R.id.tv_vieworder_type);
        tvContractDate = view.findViewById(R.id.tv_vieworder_date);
        tvContractPayment = view.findViewById(R.id.tv_vieworder_payment);
        tvContractPlanName = view.findViewById(R.id.tv_vieworder_planname);
        tvContractDuration = view.findViewById(R.id.tv_vieworder_duration);
        tvContractCost = view.findViewById(R.id.tv_vieworder_totalcost);
        tvContractLocation = view.findViewById(R.id.tv_vieworder_location);
        btnApprove = view.findViewById(R.id.btn_vieworder_approve);
        btnReject = view.findViewById(R.id.btn_vieworder_reject);

        // set / load
        loadData();

        // click listener
        btnApprove.setOnClickListener(approveListener);
        btnReject.setOnClickListener(rejectListener);

        return view;
    }


    private void loadData() {
        reference.orderByKey().equalTo(refKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot != null) {
                    ordername = snapshot.child("orderno").getValue().toString();
                    orderdate = snapshot.child("orderdate").getValue().toString();
                    orderno_status = snapshot.child("orderno_status").getValue().toString();
                    planname = snapshot.child("planname").getValue().toString();
                    duration = snapshot.child("duration").getValue().toString();
                    location = snapshot.child("location").getValue().toString();
                    totalcost = Integer.parseInt(snapshot.child("cost").getValue().toString());

                    String [] split = orderno_status.split("_");
                    String status = split[1].toString();

                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    String formatdate = "";
                    try {
                        Date date = format.parse(orderdate);
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh.mm a");
                        String strDate = formatter.format(date);
                        formatdate = strDate;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (status.equals("0")) {
                        tvContractType.setText("New");
                        tvContractType.setTextColor(getContext().getResources().getColor(R.color.dark_gray));
                    } else if (status.equals("1")){
                        tvContractType.setText("Approved");
                        tvContractType.setTextColor(getContext().getResources().getColor(R.color.dark_green));
                    }

                    tvContractName.setText(ordername);
                    tvContractDate.setText(formatdate);
                    tvContractPlanName.setText(planname);
                    tvContractPayment.setText("Paid");
                    tvContractDuration.setText(duration + " months");
                    tvContractLocation.setText(location);
                    tvContractCost.setText(String.valueOf(totalcost));
                }
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
    }


    private View.OnClickListener approveListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            reference.orderByKey().equalTo(refKey).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot != null) {
                        reference.child(refKey).child("orderno_status").setValue(ordername + "_1").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Approval successful", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Server error : Failed to approve", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
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
        }
    };


    private View.OnClickListener rejectListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            reference.orderByKey().equalTo(refKey).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot != null) {
                        reference.child(refKey).child("orderno_status").setValue(ordername + "_2").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Contract rejected", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Server error : Failed to approve", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
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
        }
    };
}