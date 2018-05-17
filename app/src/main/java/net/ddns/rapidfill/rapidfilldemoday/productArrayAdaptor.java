package net.ddns.rapidfill.rapidfilldemoday;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laurentiu on 5/17/2018.
 */

public class productArrayAdaptor extends BaseAdapter {

    Context context;
    ArrayList<Product> products;

    public void setParameters(Context context, ArrayList<Product> products) {
        this.products = products;
        this.context = context;
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
            mView = LayoutInflater.from(context).inflate(R.layout.product_layout, parent, false);
        }

        final Product product = products.get(position);

        TextView product_name = mView.findViewById(R.id.product_name);
        ImageView product_image = mView.findViewById(R.id.product_image);
        product_name.setText(product.getName());
        Glide.with(context).load(product.getImage()).into(product_image);

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
