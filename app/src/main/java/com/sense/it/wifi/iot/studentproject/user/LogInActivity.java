package com.sense.it.wifi.iot.studentproject.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sense.it.wifi.iot.studentproject.Common;
import com.sense.it.wifi.iot.studentproject.MainActivity;
import com.sense.it.wifi.iot.studentproject.R;
import com.sense.it.wifi.iot.studentproject.SharedPrefManager;

import static com.sense.it.wifi.iot.studentproject.ApiUrl.Firebase_USERS_URL;


public class LogInActivity extends AppCompatActivity {
    private static final String TAG = LogInActivity.class.getSimpleName();
    EditText editTextUsername, editTextPassword;
    private Context mContex = LogInActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(getString(R.string.title_activity_login));

        /*if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }*/

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        if (Common.currentUser != null) {
            editTextUsername.setText(Common.currentUser.getPhone());
            editTextPassword.setText(Common.currentUser.getPassword());
        } else {
            editTextUsername.setText(SharedPrefManager.getInstance(mContex).getPhoneNo());
        }

        //if user presses on login
        //calling the method login
        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        //if user presses on not registered
        findViewById(R.id.textViewRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                finish();
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

    private void userLogin() {
        //first getting the values
        final String username = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();

        //validating inputs
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter your phone no!");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter your password");
            editTextPassword.requestFocus();
            return;
        }

        //if everything is fine start Login
        if (Common.isWifiAvailable(this)) {
            checkUser(username, password);
        } else {
            Common.showWifiSetting(this);
        }


    }

    void checkUser(String enterPhone, String enterPassword) {
        final DatabaseReference table_user = FirebaseDatabase.getInstance().getReference(Firebase_USERS_URL);
        final ProgressDialog progressDialog = new ProgressDialog(mContex);
        progressDialog.setMessage("Please wait!");
        progressDialog.show();
        table_user.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(enterPhone).exists()) {
                    progressDialog.dismiss();
                    User user = dataSnapshot.child(enterPhone).getValue(User.class);
//                    user.setPhoneId(edtPhone.getText().toString());
                    if ((user.getPassword().equals(enterPassword) && (user.getPhone().equals(enterPhone)))) {
                        Common.toastMsg(mContex, "SigInSucceful");
                        Common.currentUser = user;
                        SharedPrefManager.getInstance(mContex).userLogin(user);
                        startActivity(new Intent(mContex, MainActivity.class));
                        finish();
                    } else {
                        Common.toastMsg(mContex, "Phone no. or Password is incorrect..");
                    }
                } else {
                    progressDialog.dismiss();
                    Common.toastMsg(mContex, "Please Sign Up First..!");
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
