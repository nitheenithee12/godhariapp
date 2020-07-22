package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class moreinformationActivity extends AppCompatActivity {

    private TextView contacth, contact;
    private Button setlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moreinformation);

        contacth = findViewById(R.id.information);
        contact = findViewById(R.id.information_contact);
        setlogin = findViewById(R.id.btn_detailed);

        setlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(moreinformationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}