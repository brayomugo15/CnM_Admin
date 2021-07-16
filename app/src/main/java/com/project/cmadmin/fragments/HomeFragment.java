package com.project.cmadmin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.cmadmin.R;
import com.project.cmadmin.adapters.HomeAdapter;
import com.project.cmadmin.models.Order;

public class HomeFragment extends Fragment {

    private View view;

    private RecyclerView recyclerView;

    private String email, companyid;

    private FirebaseUser user;
    private DatabaseReference reference, companyreference;
    private FirebaseRecyclerOptions<Order> options;

    private HomeAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        // set
        companyreference = FirebaseDatabase.getInstance().getReference().child("Profile");
        reference = FirebaseDatabase.getInstance().getReference().child("Orders");

        user = FirebaseAuth.getInstance().getCurrentUser();

        email = user.getEmail();

        // find view by id
        recyclerView = view.findViewById(R.id.recyclerview_orders);

        // load
        loadData();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }


    private void loadRecyclerData() {
        options = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(reference.orderByChild("companyid").equalTo(companyid), Order.class)
                .build();

        adapter = new HomeAdapter(options, getActivity());
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void loadData() {
        companyreference.orderByChild("companyemail").equalTo(email).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot != null) {
                    companyid = snapshot.child("companyid").getValue().toString();
                }

                loadRecyclerData();
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
}