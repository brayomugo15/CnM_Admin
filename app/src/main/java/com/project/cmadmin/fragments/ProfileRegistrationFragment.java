package com.project.cmadmin.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.project.cmadmin.R;
import com.project.cmadmin.utils.BottomNavLocker;
import com.project.cmadmin.utils.ValidationsClass;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

public class ProfileRegistrationFragment extends Fragment {

    private View view;

    private EditText txtName, txtEmail;
    private ShowHidePasswordEditText txtPassword;
    private Button btnRegister;
    private ProgressBar progressBar;

    private String name;

    private ActionBar actionBar;

    private ValidationsClass validate;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_registration, container, false);

        // set
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.hide();

        ((BottomNavLocker) getActivity()).BottomNavLocked(true);

        mAuth = FirebaseAuth.getInstance();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        validate = new ValidationsClass();


        // find view by id
        txtName = view.findViewById(R.id.txt_profile_name);
        txtEmail = view.findViewById(R.id.txt_profile_email);
        txtPassword = view.findViewById(R.id.txt_profile_password);
        btnRegister = view.findViewById(R.id.btn_register);
        progressBar = view.findViewById(R.id.progressbar_profile);

        // on click
        btnRegister.setOnClickListener(register);

        return view;
    }

    private View.OnClickListener register = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            name = txtName.getText().toString().trim();
            String email = txtEmail.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();

            if (!name.isEmpty() || !email.isEmpty() || !password.isEmpty()) {

                if (create(txtEmail, txtPassword)) {
                    register(email, password);
                }
            } else {
                Toast.makeText(getContext(), "Text field is empty", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void register(String email, String password) {

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(getContext(), "Sucessful Registration", Toast.LENGTH_SHORT).show();

                            ProfileRegistrationFragmentDirections.NavigateToProfile action = ProfileRegistrationFragmentDirections.navigateToProfile(name, email);
                            Navigation.findNavController(view).navigate(action);

                            actionBar.show();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                Toast.makeText(getContext(), "Weak Password", Toast.LENGTH_SHORT).show();
                            }

                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(getContext(), "User Already Exists", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


    private boolean create(EditText txtMail, ShowHidePasswordEditText txtPass) {

        boolean valid = false;

        String email = txtMail.getText().toString().trim();
        String password = txtPass.getText().toString().trim();

        validate.setEmail(email);
        validate.setPassword(password);

        switch (validate.validateEmail()) {
            case 0:
                txtMail.setError("Text field is empty");
                txtMail.setFocusable(true);
                valid = false;
                break;
            case 1:
                txtMail.setError("Invalid Email");
                txtMail.setFocusable(true);
                valid = false;
                break;
            case 2:
                switch (validate.validatePassword()) {
                    case 0:
                        txtMail.setError(null);
                        txtPass.setError(null);
                        txtPass.setError("Text field is empty");
                        txtPass.setFocusable(true);
                        valid = false;
                        break;
                    case 1:
                        txtMail.setError(null);
                        txtPass.setError(null);
                        txtPass.setError("Password should be 6 or more characters");
                        txtPass.setFocusable(true);
                        Toast.makeText(getContext(), "Feedback = " + validate.validatePassword(), Toast.LENGTH_LONG).show();
                        valid = false;
                        break;
                    case 2:
                        txtMail.setError(null);
                        txtPass.setError(null);
                        txtPass.setError("At least one uppercase letter, one number");
                        txtPass.setFocusable(true);
                        valid = false;
                        break;
                    case 3:
                        valid = true;
                        txtMail.setError(null);
                        txtPass.setError(null);
                        break;
                }
                break;
        }

        return valid;
    }

}