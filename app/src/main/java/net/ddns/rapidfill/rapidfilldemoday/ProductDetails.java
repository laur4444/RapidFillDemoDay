package net.ddns.rapidfill.rapidfilldemoday;

import android.content.Intent;
import android.media.Image;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ProductDetails extends AppCompatActivity {

    TextView product_name;
    TextView product_price;
    TextView product_description;
    ImageView product_image;

    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    DatabaseReference db;
    FirebaseAuth user;

    final Product product = new Product();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        numberButton = findViewById(R.id.number_button);
        btnCart = findViewById(R.id.btnCart);


        product_name = findViewById(R.id.detailed_product_name);
        product_description = findViewById(R.id.detailed_product_description);
        product_price = findViewById(R.id.detailed_product_price);
        product_image = findViewById(R.id.detailed_product_image);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        Intent intent = getIntent();

        product.setPrice(intent.getStringExtra("product_price"));
        product.setImage(intent.getStringExtra("product_image"));
        product.setName(intent.getStringExtra("product_name"));
        product.setDescription(intent.getStringExtra("product_description"));

        user = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Cart");
        db.child(product.getName()).child("quantity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    numberButton.setNumber(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        numberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                int price = Integer.valueOf(product.getPrice());
                price /= oldValue;
                price *= newValue;
                product.setPrice(price + "");
                product.setQuantity(newValue + "");
                product_price.setText(product.getPrice());
            }
        });


        collapsingToolbarLayout.setTitle(product.getName());

        product_name.setText(product.getName());
        product_description.setText(product.getDescription());
        product_price.setText(product.getPrice());

        Glide.with(this).load(product.getImage()).into(product_image);


        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.setQuantity(numberButton.getNumber().toString());
                String toShow = product.getName() + " a fost adaugat in cos!";
                Toast.makeText(v.getContext(), toShow , Toast.LENGTH_SHORT).show();
                db.child(product.getName()).setValue(product);
                finish();
            }
        });
    }
}
