package net.ddns.rapidfill.rapidfilldemoday;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Cart extends AppCompatActivity {

    DatabaseReference db;
    FirebaseAuth user;
    Context context;
    ArrayList<Product> products;
    ListView resultList;
    ProductCartArrayAdapter productAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        user = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Cart");

        context = this;

        products = new ArrayList<>();
        resultList = findViewById(R.id.result_list);
        productAdapter = new ProductCartArrayAdapter();

        showCart();

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                showCart();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                showCart();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                showCart();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                showCart();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showCart();
            }
        });
    }

    private void showCart() {

        db.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                products.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Product item = postSnapshot.getValue(Product.class);
                    products.add(item);
                }
                productAdapter.setParameters(Cart.this, products);
                resultList.setAdapter(productAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
