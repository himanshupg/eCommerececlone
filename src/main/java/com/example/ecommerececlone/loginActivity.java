package com.example.ecommerececlone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerececlone.Models.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginActivity extends AppCompatActivity {

    private EditText InputPhoneNumber,InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;

    private String parentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton =(Button) findViewById(R.id.login_btn);
        InputPhoneNumber =(EditText) findViewById(R.id.login_phone_number_input);
        InputPassword =(EditText) findViewById(R.id.login_password_input);
        loadingBar = new ProgressDialog(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginUser();
            }
        });
    }

    private void LoginUser() {
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter  password", Toast.LENGTH_SHORT).show();
        }

        loadingBar.setTitle("Login Account");
        loadingBar.setMessage("Please wait while we are checking the credentials");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        AlloeAccessToAccount(phone, password);
        
    }

    private void AlloeAccessToAccount(String phone, String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             if(dataSnapshot.child(parentDbName).child(phone).exists()){
                 Users userData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                 if(userData.getPhone().equals(phone))
                 {
                     if(userData.getPassword().equals(password))
                     {
                         Toast.makeText(loginActivity.this, "Logged in successfully....", Toast.LENGTH_SHORT).show();
                         loadingBar.dismiss();

                         Intent intent = new Intent(loginActivity.this, HomeActivity.class);
                         startActivity(intent);

                     }
                     else{
                         loadingBar.dismiss();
                         Toast.makeText(loginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                     }
                 }
             }
             else{
                 Toast.makeText(loginActivity.this, "Account with this "+ phone +" do not exist", Toast.LENGTH_SHORT).show();
                 loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}