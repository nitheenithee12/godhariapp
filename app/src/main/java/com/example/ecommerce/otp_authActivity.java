package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class otp_authActivity extends AppCompatActivity {

    private Button return_to_main_menu;
    private TextView text1, text2;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_auth);


        return_to_main_menu = (Button) findViewById(R.id.retuning_button);
        text1 = (TextView) findViewById(R.id.text_view);
        text2 = (TextView) findViewById(R.id.text_view2);





        return_to_main_menu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(otp_authActivity.this, MainActivity.class);
                startActivity(intent);
            }


        });


    }



    //These are the objects needed
    //It is the verification id that will be sent to the user







        //getting mobile number from the previous activity
    }
