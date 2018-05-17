package net.ddns.rapidfill.rapidfilldemoday;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Cart extends AppCompatActivity {

    DatabaseReference db;
    FirebaseAuth user;
    RecyclerView resultCart;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        user = FirebaseAuth.getInstance();

        resultCart = findViewById(R.id.listCart);
        resultCart.setHasFixedSize(true);
        resultCart.setLayoutManager(new LinearLayoutManager(this));

        context = this;
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
        db = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Cart");
        Query products = db.orderByChild("name").startAt("");

        FirebaseRecyclerAdapter<Product, ProductViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(
                Product.class,
                R.layout.product_layout,
                ProductViewHolder.class,
                products

        ) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {

                viewHolder.setDetails(context, model);

            }
        };
        resultCart.setAdapter(firebaseRecyclerAdapter);
    }
}
