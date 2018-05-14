package net.ddns.rapidfill.rapidfilldemoday;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.Serializable;


/**
 * Created by Laurentiu on 5/13/2018.
 */

public class ProductViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public ProductViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setDetails(final Context context, final Product product) {
        TextView product_name = mView.findViewById(R.id.product_name);
        TextView product_description = mView.findViewById(R.id.product_description);
        ImageView product_image = mView.findViewById(R.id.product_image);
        Button product_details = mView.findViewById(R.id.product_details_button);

        product_name.setText(product.getName());
        product_description.setText(product.getDescription());

        Glide.with(context).load(product.getImage()).into(product_image);

        product_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productDetail = new Intent(context, ProductDetails.class);
                productDetail.putExtra("product_name", product.getName());
                productDetail.putExtra("product_price", product.getPrice());
                productDetail.putExtra("product_description", product.getDescription());
                productDetail.putExtra("product_image", product.getImage());
                context.startActivity(productDetail);
            }
        });


    }


}
