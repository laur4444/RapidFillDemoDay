package net.ddns.rapidfill.rapidfilldemoday;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by Laurentiu on 5/19/2018.
 */

public class SpeechActions {

    DatabaseReference db;
    DatabaseReference db_products;
    DatabaseReference db_cart;
    DatabaseReference db_orders;

    FirebaseAuth user;
    TextToSpeech toSpeech;

    ArrayList<Product> products;
    ArrayList<Product> products_cart;
    Context context;

    public SpeechActions(TextToSpeech speak, Context context) {
        db = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance();
        this.toSpeech = speak;
        this.context = context;
        products = new ArrayList<>();
        products_cart = new ArrayList<>();
        db_products = FirebaseDatabase.getInstance().getReference("Products");
        db_cart = db.child("Users").child(user.getUid()).child("Cart");
        db_orders = FirebaseDatabase.getInstance().getReference().child("Orders").push();

        db_cart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                products_cart.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Product item = postSnapshot.getValue(Product.class);
                    products_cart.add(item);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        GetProducts();
    }
    private void GetProducts() {
        db_products.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Product item = postSnapshot.getValue(Product.class);
                    products.add(item);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private Product Search(String text) {
        ArrayList<Product> results = new ArrayList<>();
        for(Product item : products) {
            if(item.getName().toLowerCase().contains(text.toLowerCase())) {
                return item;
            }
        }
        return null;
    }
    private void AddToCart(final Product item) {
        db_cart.child(item.getName()).child("quantity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    item.setQuantity(dataSnapshot.getValue().toString());
                } else {
                    item.setQuantity("0");
                }
                int quantity = Integer.valueOf(item.getQuantity());
                float price = Float.valueOf(item.getPrice());
                if(quantity != 0){
                    price /= quantity;
                }
                quantity++;
                price *= quantity;
                item.setPrice(price + "");
                item.setQuantity(quantity + "");
                db_cart.child(item.getName()).setValue(item);
                db_cart.child(item.getName()).child("quantity").removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    void RemoveFromCart(Product product) {
        db_cart.child(product.getName()).removeValue();
    }

    void GetInput(String input) {
        input = input.toLowerCase();
        Toast.makeText(context, input, Toast.LENGTH_SHORT).show();
        if(input.contains("insert") || input.contains("insect") ) {
            StringTokenizer st = new StringTokenizer(input);
            while(st.hasMoreTokens()) {
                Product product = Search(st.nextToken());
                if(product != null) {
                    AddToCart(product);
                    //Toast.makeText(context, "Added to cart " + product.getName(), Toast.LENGTH_SHORT).show();
                    toSpeech.speak("I have added " + product.getName() + " to your cart!", TextToSpeech.QUEUE_ADD, null);
                    return;
                }
            }
            toSpeech.speak("I didn't find this product!", TextToSpeech.QUEUE_ADD, null);
            return;
        }
        if(input.contains("remove")) {
            StringTokenizer st = new StringTokenizer(input);
            while(st.hasMoreTokens()) {
                Product product = Search(st.nextToken());
                if(product != null) {
                    RemoveFromCart(product);
                    toSpeech.speak("I have removed " + product.getName() + " from your cart!", TextToSpeech.QUEUE_ADD, null);
                    break;
                }
            }
            return;
        }
        if(input.contains("send")) {
            if(products_cart.isEmpty()) {
                toSpeech.speak("Your cart is empty!", TextToSpeech.QUEUE_ADD, null);
                return;
            }
            Date now = Calendar.getInstance().getTime();
            Order order = new Order(now, products_cart);
            db_orders.setValue(order);
            db_cart.removeValue();
            toSpeech.speak("I have sent the order to your Gas Station! You will pick it up in RESPOND ORDER GET TIME minutes!", TextToSpeech.QUEUE_ADD, null);
            return;
        }
        toSpeech.speak("I didn't quite got what you said!", TextToSpeech.QUEUE_ADD, null);
    }

}
