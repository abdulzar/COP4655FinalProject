package com.sense.it.wifi.iot.studentproject.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sense.it.wifi.iot.studentproject.Common;
import com.sense.it.wifi.iot.studentproject.R;
import com.sense.it.wifi.iot.studentproject.SharedPrefManager;
import com.sense.it.wifi.iot.studentproject.user.User;

import static com.sense.it.wifi.iot.studentproject.ApiUrl.Firebase_USERS_URL;

public class ProfileFragment extends Fragment {

    public static final String TAG = ProfileFragment.class.getSimpleName();
    EditText editTextUsername, editTextEmail, editTextPassword, editTextPhone;
    RadioGroup radioGroupGender;
    RadioButton genderRadio;
    View root;
    User updateInfoUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_profile, container, false);
        intiViews();
        return root;
    }

    void intiViews() {

        updateInfoUser = new User();
        editTextUsername = root.findViewById(R.id.editTextUsername);
        editTextEmail = root.findViewById(R.id.editTextEmail);
        editTextPassword = root.findViewById(R.id.editTextPassword);
        editTextPhone = root.findViewById(R.id.editTextPhone);
        radioGroupGender = root.findViewById(R.id.radioGender);
        editTextPhone.setKeyListener(null);

        root.findViewById(R.id.buttonUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if user pressed on button register
                //here we will register the user to server
                if (Common.isWifiAvailable(getActivity())) {
                    updateUser();

                } else {
                    Common.showWifiSetting(getActivity());
                }
            }
        });

        setUser(Common.currentUser);
    }

    private void setUser(User currentUser) {
        editTextUsername.setText(currentUser.getUsername());
        editTextEmail.setText(currentUser.getEmail());
        editTextPassword.setText(currentUser.getPassword());
        editTextPhone.setText(currentUser.getPhone());
        String gender = currentUser.getGender();
        if (gender.equals("Male")) {
            genderRadio = root.findViewById(R.id.radioButtonMale);
            genderRadio.setChecked(true);
        } else {
            genderRadio = root.findViewById(R.id.radioButtonFemale);
            genderRadio.setChecked(true);
        }


    }

    void updateUser() {
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();
        String gender;
        if (radioGroupGender.getCheckedRadioButtonId() == R.id.radioButtonMale) {
            gender = "Male";
        } else {
            gender = "Female";
        }
        updateInfoUser.setUsername(username);
        updateInfoUser.setPassword(password);
        updateInfoUser.setGender(gender);
        updateInfoUser.setEmail(email);
        updateInfoUser.setPhone(phone);
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Updating User Profile...");
        pDialog.show();
        final DatabaseReference table_user = FirebaseDatabase.getInstance().getReference(Firebase_USERS_URL);

        table_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(updateInfoUser.getPhone()).exists()) {
                    Common.toastMsg(getActivity(), "Profile is Update!");
                    table_user.child(updateInfoUser.getPhone()).removeValue();  //delete old profile
                    table_user.child(updateInfoUser.getPhone()).setValue(updateInfoUser);  //insert new profile
                    SharedPrefManager.getInstance(getActivity()).userLogin(updateInfoUser);
                    setUser(updateInfoUser);
                    Common.currentUser = updateInfoUser;
                    pDialog.dismiss();
                } else {
                    pDialog.dismiss();
                    Common.toastMsg(getActivity(), "Can't Update Profile");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pDialog.dismiss();
                Common.toastMsg(getActivity(), "Database Error!");
            }
        });

    }

}