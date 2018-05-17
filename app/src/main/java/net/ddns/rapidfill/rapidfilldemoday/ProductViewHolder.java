package net.ddns.rapidfill.rapidfilldemoday;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.Serializable;


/**
 * Created by Laurentiu on 5/13/2018.
 */

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;
    Context context;
    Product product;

    public ProductViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        itemView.setOnClickListener(this);
    }

    public void setDetails(final Context context, final Product product) {
        TextView product_name = mView.findViewById(R.id.product_name);
        ImageView product_image = mView.findViewById(R.id.product_image);
        this.context = context;
        this.product = product;
        product_name.setText(product.getName());
        Glide.with(context).load(product.getImage()).into(product_image);

    }


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
}
