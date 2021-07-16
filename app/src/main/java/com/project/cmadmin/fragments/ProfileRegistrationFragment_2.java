package com.project.cmadmin.fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.cmadmin.R;
import com.project.cmadmin.models.CompanyProfile;
import com.project.cmadmin.utils.BottomNavLocker;
import com.project.cmadmin.utils.ValidationsClass;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class ProfileRegistrationFragment_2 extends Fragment {

    private View view;

    private EditText txtPhone, txtLocation, txtMotto;
    private TextView tvUrl;
    private Button btnUpload, btnRegister;

    private ProfileRegistrationFragment_2Args args;

    private String name, email, phoneno, location, motto;
    private int SELECT_PICTURE = 200;
    private Uri selectedImageUri;

    private DatabaseReference reference;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private ValidationsClass validate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_registration_2, container, false);

        // set
        ((BottomNavLocker) getActivity()).BottomNavLocked(true);

        reference = FirebaseDatabase.getInstance().getReference().child("Profile");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        validate = new ValidationsClass();

        args = ProfileRegistrationFragment_2Args.fromBundle(getArguments());

        name = args.getName();
        email = args.getEmail();

        // find view by id
        txtPhone = view.findViewById(R.id.txt_companyphone);
        txtLocation = view.findViewById(R.id.txt_companylocation);
        txtMotto = view.findViewById(R.id.txt_companymotto);

        tvUrl = view.findViewById(R.id.tv_companyphoto_url);

        btnUpload = view.findViewById(R.id.btn_uploadimage);
        btnRegister = view.findViewById(R.id.btn_register_profile);

        // set click
        btnUpload.setOnClickListener(upload);
        btnRegister.setOnClickListener(register);

        return view;
    }


    private View.OnClickListener register = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            phoneno = txtPhone.getText().toString().trim();
            location = txtLocation.getText().toString().trim();
            motto = txtMotto.getText().toString().trim();

            if (!phoneno.isEmpty() || !location.isEmpty() || !motto.isEmpty()) {
                if (selectedImageUri != null) {
                    uploadToFirebase(selectedImageUri);
                } else {
                    Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Fill in all the details", Toast.LENGTH_SHORT).show();
            }
        }
    };


    private View.OnClickListener upload = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            imageChooser();
        }
    };

    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                tvUrl.setText(selectedImageUri.getPath());
                this.selectedImageUri = selectedImageUri;
            }
        }
    }

    private void uploadToFirebase(Uri uri) {

        ProgressDialog pd = new ProgressDialog(getContext());
        final StorageReference ref = storageReference.child("ProfileImages/" + System.currentTimeMillis() + "." + getFileExtension(uri));

        pd.setTitle("Creating New Profile");
        pd.setMessage("Saving data....");

        pd.show();

        ref.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String uuid = UUID.randomUUID().toString();

                        CompanyProfile profile = new CompanyProfile(uuid, name, email, uri.toString(), location, phoneno, 0, 0);
                        reference.push().setValue(profile);

                        ProfileRegistrationFragment_2Directions.NavigateToQualifications action = ProfileRegistrationFragment_2Directions.navigateToQualifications(uuid);
                        Navigation.findNavController(view).navigate(action);
                        Toast.makeText(getContext(), "Successful Registration", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getContext(), "Error in uploading", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private boolean register(EditText txtNo, EditText txtMail) {
        boolean valid = false;

        String phone = txtNo.getText().toString().trim();
        String email = txtMail.getText().toString().trim();

        validate.setPhoneNo(phone);
        validate.setEmail(email);


        switch (validate.validateEmail()) {
            case 0:
                txtNo.setError(null);
                txtMail.setError("Text field is empty");
                txtMail.setFocusable(true);
                valid = false;
                break;
            case 1:
                txtNo.setError(null);
                txtMail.setError("Invalid Email");
                txtMail.setFocusable(true);
                valid = false;
                break;
            case 2:
                txtMail.setError(null);
                txtNo.setError(null);
                valid = true;
                break;
        }

        return valid;
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cr = (getActivity()).getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
}