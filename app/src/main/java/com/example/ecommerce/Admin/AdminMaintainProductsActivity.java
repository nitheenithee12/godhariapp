package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity
{

    private Button applyChangesBtn, deletePBtn;
    private EditText namepd, pricepd, descriptionpd;
    private ImageView imageView;//need to remember

    private String productID = "";
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productID = getIntent().getStringExtra("pid");//need to remember so
        productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);//need to remember so


        applyChangesBtn = findViewById(R.id.product_change_btn);
        descriptionpd = findViewById(R.id.product_description_edit);
        pricepd = findViewById(R.id.product_price_edit);
        namepd = findViewById(R.id.product_name_edit);
        imageView = findViewById(R.id.product_image_edit);//need to remember
        deletePBtn = findViewById(R.id.product_delete_btn);

        displaySpecificProductInfo();


        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                applyChanges();

            }
        });

        deletePBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                deleteThisProduct();

            }
        });
    }

    private void deleteThisProduct()
    {
        productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {

                Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminCategoryActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(AdminMaintainProductsActivity.this, "The product is deleted Successfully.", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void applyChanges()
    {
        String Name = namepd.getText().toString();
        String pricE = pricepd.getText().toString();
        String pDescription = descriptionpd.getText().toString();

        if(Name.equals(""))
        {
            Toast.makeText(this, "Make sure you have entered the product name.", Toast.LENGTH_SHORT).show();
        }
        else if(pricE.equals(""))
        {
            Toast.makeText(this, "Make sure you have entered the product price.", Toast.LENGTH_SHORT).show();
        }
        else if(pDescription.equals(""))
        {
            Toast.makeText(this, "Make sure you have entered the product description.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productID);
            productMap.put("description", pDescription);
            productMap.put("price", pricE);// if required need to change some id's.
            productMap.put("pname", Name);


            productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(AdminMaintainProductsActivity.this, "Changes applied successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            });
        }
    }

    private void displaySpecificProductInfo()
    {
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    String Pdname = snapshot.child("pname").getValue().toString();
                    String pdprice = snapshot.child("price").getValue().toString();
                    String pddescription = snapshot.child("description").getValue().toString();
                    String pdimage = snapshot.child("image").getValue().toString();//need to remember

                    namepd.setText(Pdname);
                    pricepd.setText(pdprice);
                    descriptionpd.setText(pddescription);
                    Picasso.get().load(pdimage).into(imageView);//need to remember
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }
}