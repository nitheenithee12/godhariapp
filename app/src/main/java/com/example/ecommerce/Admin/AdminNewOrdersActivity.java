package com.example.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecommerce.Prevalent.Prevalent;
import com.example.ecommerce.R;
import com.example.ecommerce.data.model.AdminOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity
{
    private RecyclerView ordersList;
    private DatabaseReference ordersRef, adminCart;
    //private  Button showBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        adminCart = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View");

        ordersList = findViewById(R.id.ad_orders);
        //showBtn = (Button) findViewById(R.id.show_order_details);



        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(ordersRef,AdminOrders.class).build();

        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder adminOrdersViewHolder, final int position, @NonNull final AdminOrders adminOrders)
            {
                adminOrdersViewHolder.userName.setText("Name: " + adminOrders.getName());
                adminOrdersViewHolder.userPhone.setText("Phone: " + adminOrders.getPhone());
                adminOrdersViewHolder.userTotalPrice.setText("Amount: ₹" + adminOrders.getTotalAmount());
                adminOrdersViewHolder.userDateTime.setText("Date, Time: " + adminOrders.getDate() + " " + adminOrders.getTime());
                adminOrdersViewHolder.userShippingAddress.setText("Shipping Address: " + adminOrders.getAddress() +"  " + adminOrders.getCity());
                adminOrdersViewHolder.userpincode.setText("PIN code " + adminOrders.getPincode());
                //adminOrdersViewHolder.userName.setText("Name: " + adminOrders.getName());

                adminOrdersViewHolder.showOrdersBtn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        String uID = getRef(position).getKey();

                        Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserProductsActivity.class);
                        intent.putExtra("uid", uID);
                        startActivity(intent);

                    }
                });

                adminOrdersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Yes",
                                        "No"
                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                        builder.setTitle("Have you shipped this order?");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if(i == 0)
                                {
                                    //FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(Prevalent.currentOnlineUser.getPhone()).child("Products").removeValue();
                                    //adminCart.removeValue();
                                    String uID = getRef(position).getKey();

                                    RemoveOder(uID);

                                }
                                else
                                {
                                    Intent intent = new Intent(AdminNewOrdersActivity.this, AdminNewOrdersActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }


                        });

                        builder.show();



                    }
                });


            }

            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                return new AdminOrdersViewHolder(view);
            }
        };

        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    public  static  class AdminOrdersViewHolder extends  RecyclerView.ViewHolder
    {
        public TextView userName, userPhone, userTotalPrice, userDateTime, userShippingAddress, userpincode;
        public Button showOrdersBtn;

        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.aduser_name);
            userTotalPrice = itemView.findViewById(R.id.ordert_product_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_address_city);
            userpincode= itemView.findViewById(R.id.order_pincode);
            showOrdersBtn = itemView.findViewById(R.id.show_order_details);
            userPhone = itemView.findViewById(R.id.order_ph_num);
        }
    }

    private void RemoveOder(String uID)
    {
        ordersRef.child(uID).removeValue();
        adminCart.child(uID).removeValue();
    }
}