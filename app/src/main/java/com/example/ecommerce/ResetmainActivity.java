package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.Prevalent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ResetmainActivity extends AppCompatActivity {

    private Button updateBtn;
    private EditText UpdatePassword;
    private String checker = "";
    private String track = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetmain);

        updateBtn = findViewById(R.id.update_btn_last);
        UpdatePassword = findViewById(R.id.update_password);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                updatePassword();

            }
        });

    }

    private void updatePassword()
    {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        track = getIntent().getStringExtra("phone");


        HashMap<String, Object> useMap = new HashMap<>();
        useMap.put("password", UpdatePassword.getText().toString());

        ref.child(track).updateChildren(useMap);


        Toast.makeText(ResetmainActivity.this, "Your password updated successfully.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ResetmainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();



        //startActivity(new Intent(ResetmainActivity.this, LoginActivity.class));
        //Toast.makeText(ResetmainActivity.this, "Your password updated successfully.", Toast.LENGTH_SHORT).show();
        //finish();
    }
}