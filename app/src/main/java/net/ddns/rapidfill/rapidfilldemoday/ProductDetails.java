package net.ddns.rapidfill.rapidfilldemoday;

import android.content.Intent;
import android.media.Image;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import org.w3c.dom.Text;

public class ProductDetails extends AppCompatActivity {

    TextView product_name;
    TextView product_price;
    TextView product_description;
    ImageView product_image;

    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;



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
        Product product = new Product();
        product.setPrice(intent.getStringExtra("product_price"));
        product.setImage(intent.getStringExtra("product_image"));
        product.setName(intent.getStringExtra("product_name"));
        product.setDescription(intent.getStringExtra("product_description"));

        collapsingToolbarLayout.setTitle(product.getName());

        product_name.setText(product.getName());
        product_description.setText(product.getDescription());
        product_price.setText(product.getPrice());

        Glide.with(this).load(product.getImage()).into(product_image);

        //Add product name to toolbar
    }
}
