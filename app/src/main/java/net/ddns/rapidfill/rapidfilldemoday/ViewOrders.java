package net.ddns.rapidfill.rapidfilldemoday;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ViewOrders extends AppCompatActivity {

    ListView orderList;
    DatabaseReference db;
    OrderAdaptor orderAdaptor;
    ArrayList<Order> orders;
    FirebaseAuth user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        orderList = findViewById(R.id.user_order_list);
        user = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Orders");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Cart");
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
                Collections.reverse(orders);
                orderAdaptor.setParameters(ViewOrders.this, orders, db);
                orderList.setAdapter(orderAdaptor);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_back) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
