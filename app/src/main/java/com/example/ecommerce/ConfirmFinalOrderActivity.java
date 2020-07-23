package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity
{
    private EditText nameEditText, phoneEditText, addressEditText, pincode, cityEditText;
    private Button confirmOrderBtn;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private String phoneNo, message,msg;

    private String totalAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        totalAmount = getIntent().getStringExtra("Total Price");

        Toast.makeText(ConfirmFinalOrderActivity.this, "Total price is Rs:" + totalAmount, Toast.LENGTH_SHORT).show();


        setContentView(R.layout.activity_confirm_final_order);
        confirmOrderBtn = (Button) findViewById(R.id.confirm_final_order_btn);
        nameEditText = (EditText) findViewById(R.id.shipment_name);
        phoneEditText = (EditText) findViewById(R.id.shipment_phone);
        addressEditText = (EditText) findViewById(R.id.shipment_address);
        cityEditText = (EditText) findViewById(R.id.shipment_city_name);
        pincode = (EditText) findViewById(R.id.shipment_zipcode);




        confirmOrderBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                check();
            }
        });


    }

    private void check()
    {
        if(TextUtils.isEmpty(nameEditText.getText().toString()) || nameEditText.length() < 5)
        {
            Toast.makeText(this, "Please provide your full name.", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(phoneEditText.getText().toString()) || phoneEditText.length() < 10)
        {
            Toast.makeText(this, "Please provide correct phone number.", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            Toast.makeText(this, "Please provide your city name.", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(addressEditText.getText().toString()) || addressEditText.length() < 12)
        {
            Toast.makeText(this, "Please provide full address.", Toast.LENGTH_SHORT).show();
        }

        else
        {
            if(TextUtils.isEmpty(pincode.getText().toString()) || pincode.length() < 6)
            {
                Toast.makeText(this, "Please check the pin-code.", Toast.LENGTH_SHORT).show();

            }

            else if(TextUtils.equals(pincode.getText().toString(),"517501") | TextUtils.equals(pincode.getText().toString(),"517502") | TextUtils.equals(pincode.getText().toString(),"517505") | TextUtils.equals(pincode.getText().toString(),"517503") | TextUtils.equals(pincode.getText().toString(),"517501"))
            {
                //Toast.makeText(this, "Your area is not serviceable.", Toast.LENGTH_SHORT).show();
                ConfirmOrder();
                //num.equals("517501") | num.equals("517502") | num.equals("517505") | num.equals("517507")|

            }
            else
            {
                //ConfirmOrder();
                Toast.makeText(this, "Your area is not serviceable.", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void ConfirmOrder()
    {
        final String saveCurrentDate, saveCurrentTime;


        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, YYYY");
        saveCurrentDate = currentDate.format(calForDate.getTime());


        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        HashMap<String, Object> orderMap = new HashMap<>();

        orderMap.put("totalAmount", totalAmount);
        orderMap.put("name", nameEditText.getText().toString());
        orderMap.put("phone", phoneEditText.getText().toString());
        orderMap.put("address", addressEditText.getText().toString());
        orderMap.put("city", cityEditText.getText().toString());
        orderMap.put("pincode", pincode.getText().toString());
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);
        orderMap.put("status", "not Shipped");

        ordersRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(Prevalent.currentOnlineUser.getPhone()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                sendSMSMessage();
                                Toast.makeText(ConfirmFinalOrderActivity.this, "Your final order has been placed successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            }


                        }
                    });
                }

            }
        });

    }
    protected void sendSMSMessage() {
        phoneNo = "+91" + phoneEditText.getText().toString();
         msg = "Your order has been placed successfully.";
        //message = msg;

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, msg, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Message didn't sent...", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }
}