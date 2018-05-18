package net.ddns.rapidfill.rapidfilldemoday;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by Laurentiu on 5/17/2018.
 */

public class ProductCartArrayAdapter extends BaseAdapter{

    Context context;
    ArrayList<Product> products;
    boolean isInCartView;
    DatabaseReference db;

    public void setParameters(Context context, ArrayList<Product> products) {
        this.products = products;
        this.context = context;
    }
    public void setParameters(Context context, ArrayList<Product> products, boolean isInCartView, DatabaseReference db) {
        this.products = products;
        this.context = context;
        this.isInCartView = isInCartView;
        this.db = db;
    }


    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View mView, ViewGroup parent) {

        if(mView == null) {
            mView = LayoutInflater.from(context).inflate(R.layout.product_cart_layout, parent, false);
        }

        final Product product = products.get(position);

        TextView product_name = mView.findViewById(R.id.product_name);
        TextView product_price = mView.findViewById(R.id.product_price);
        TextView product_quantity = mView.findViewById(R.id.product_quantity);
        Button product_delete = mView.findViewById(R.id.product_delete);
        ImageView product_image = mView.findViewById(R.id.product_image);
        product_name.setText(product.getName());
        product_price.setText(product.getPrice() + " Lei");
        product_quantity.setText(product.getQuantity() + " bucati");
        Glide.with(context).load(product.getImage()).into(product_image);

        if(isInCartView) {
            product_delete.setVisibility(View.VISIBLE);
            product_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    db.child(product.getName()).removeValue();
                }
            });
        }

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productDetail = new Intent(context, ProductDetails.class);
                productDetail.putExtra("product_name", product.getName());
                productDetail.putExtra("product_price", product.getPrice());
                productDetail.putExtra("product_description", product.getDescription());
                productDetail.putExtra("product_image", product.getImage());
                productDetail.putExtra("product_quantity", product.getQuantity());
                context.startActivity(productDetail);
            }
        });

        return mView;
    }
}
