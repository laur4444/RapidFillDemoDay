package net.ddns.rapidfill.rapidfilldemoday;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Laurentiu on 5/15/2018.
 */

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public AdapterView.OnItemClickListener itemClickListener;
    public TextView product_name;
    public TextView product_price;
    public ImageView product_count;

    public CartViewHolder(View itemView) {
        super(itemView);
        product_name = itemView.findViewById(R.id.cart_item_name);
        product_count = itemView.findViewById(R.id.cart_item_count);
        product_price = itemView.findViewById(R.id.cart_item_price);

    }

    @Override
    public void onClick(View v) {

    }
}


public class CartAdaptor extends RecyclerView.Adapter<CartViewHolder>{
    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
