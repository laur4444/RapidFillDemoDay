package net.ddns.rapidfill.rapidfilldemoday;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class CasierOrders extends AppCompatActivity {

    ListView orderList;
    DatabaseReference db;
    OrderAdaptor orderAdaptor;
    ArrayList<Order> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casier_orders);

        orderList = findViewById(R.id.order_list);
        db = FirebaseDatabase.getInstance().getReference("Orders");



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Cash register");
        setSupportActionBar(toolbar);

        orders = new ArrayList<>();
        orderAdaptor = new OrderAdaptor();

        LoadOrders();
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                LoadOrders();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                LoadOrders();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                LoadOrders();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                LoadOrders();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    void LoadOrders() {
        db.orderByChild("date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orders.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Order item = postSnapshot.getValue(Order.class);
                    orders.add(item);
                }
                orderAdaptor.setParameters(CasierOrders.this, orders, db);
                orderList.setAdapter(orderAdaptor);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
