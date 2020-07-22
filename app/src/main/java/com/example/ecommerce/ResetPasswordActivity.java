package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.Admin.AdminCategoryActivity;
import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.data.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText resetPhone, resetOTP;
    private Button otpGenerate, verify, moreInf;
    FirebaseAuth auth;
    private String phoneNumber, otp;
    private String verificationCode;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        resetPhone = findViewById(R.id.reg_phone_v);
        resetOTP = findViewById(R.id.reg_txt);
        //otpGenerate = findViewById(R.id.btn_reg_v);
        verify = findViewById(R.id.reg_btn);
        moreInf = findViewById(R.id.more_inf);


        moreInf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ResetPasswordActivity.this, moreinformationActivity.class);
                startActivity(intent);
            }
        });



        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


                resetUser();

            }
        });
    }
    private void resetUser()
    {
         String phone = resetPhone.getText().toString();
         String name = resetOTP.getText().toString();

        if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            AllowAccessToAccount(phone, name);
        }
    }


    private void AllowAccessToAccount(final String phone, final  String name)
    {



        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child("Users").child(phone).exists())
                {
                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone))
                    {
                        if(usersData.getName().equals(name))
                        {
                            Toast.makeText(ResetPasswordActivity.this, "Your number is verified, now you can change mobile number.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ResetPasswordActivity.this, ResetmainActivity.class);
                            intent.putExtra("phone", phone);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(ResetPasswordActivity.this, "Please check your name.", Toast.LENGTH_SHORT).show();
                        }


                    }

                }
                else
                {
                    Toast.makeText(ResetPasswordActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

}