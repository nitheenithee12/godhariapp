package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;



import android.content.Intent;
import java.util.concurrent.TimeUnit;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity
{
    private Button CreateAccountButton, GenerateOTP;
    //Button otp_button;
    private EditText InputName, InputPhoneNumber, InputPassword, OTP;
    private ProgressDialog loadingBar;

    private String phoneNumber, otp;
    FirebaseAuth auth;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    private String verificationCode;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //findViews();

        StartFirebaseLogin();



        CreateAccountButton = (Button) findViewById(R.id.register);
        InputName = (EditText) findViewById(R.id.Name1);
        InputPassword = (EditText) findViewById(R.id.new_passkey1);
        InputPhoneNumber = (EditText) findViewById(R.id.register_phone);
        OTP = (EditText) findViewById(R.id.verify_OTP) ;
        GenerateOTP = (Button) findViewById(R.id.otp_button);


        loadingBar = new ProgressDialog(this);

        GenerateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phoneNumber = "+91" + InputPhoneNumber.getText().toString();

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,
                        60,
                        TimeUnit.SECONDS,
                        RegisterActivity.this,
                        mCallback
                );
            }
        });

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp = OTP.getText().toString();

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);

                SigninWithPhone(credential);



                //startActivity(CreateAccount(), SigninWithPhone(credential));
                //CreateAccount();
            }
        });



    }


    private void StartFirebaseLogin(){
        auth = FirebaseAuth.getInstance();

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential ) {
                Toast.makeText(RegisterActivity.this, "Verification completed/ Mobile number already exists.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(RegisterActivity.this, "Verification failed", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onCodeSent(String s,PhoneAuthProvider.ForceResendingToken forceResendingToken){
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(RegisterActivity.this,"Code sent", Toast.LENGTH_SHORT).show();
            }
        };

    }



    private  void SigninWithPhone(PhoneAuthCredential credential){

        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //startActivity(new Intent(RegisterActivity.this, otp_authActivity.class));
                            //finish();
                            CreateAccount();


                        } else{
                            Toast.makeText(RegisterActivity.this,"Incorrect OTP", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, RegisterActivity.class));
                            finish();
                        }

                    }

                });

    }

   // private void findViews() {
        ////btnGenerateOTP=findViewById(R.id.btn_generate_otp);
        //btnSignIn=findViewById(R.id.btn_sign_in);

       // etPhoneNumber=findViewById(R.id.et_phone_number);
        //etOTP=findViewById(R.id.et_otp);
   // }





    //String phone = InputPhoneNumber.getText().toString();

    private void CreateAccount()
    {
        String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(name) || name.length()<4)
        {
            Toast.makeText(this, "Please enter valid input...", Toast.LENGTH_SHORT).show();
        }
        else if (phone.isEmpty() || phone.length() < 10)
        {

            Toast.makeText(this, "Please enter a valid phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password) || password.length() < 8)
        {
            Toast.makeText(this, "Please enter password...", Toast.LENGTH_SHORT).show();
        }
        else
        {

            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatephoneNumber(name, phone, password);
        }
    }



    private void ValidatephoneNumber(final String name, final String phone, final String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("password", password);
                    userdataMap.put("name", name);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, otp_authActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "This " + phone + " already exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();

                    Intent intent1 = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}