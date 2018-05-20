package net.ddns.rapidfill.rapidfilldemoday;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Laurentiu on 5/20/2018.
 */

public class OrderAdaptor extends BaseAdapter {

        Context context;
        ArrayList<Order> orders;
        DatabaseReference db;

        public void setParameters(Context context, ArrayList<Order> orders, DatabaseReference db) {
            this.orders = orders;
            this.context = context;
            this.db = db;
        }

        @Override
        public int getCount() {
            return orders.size();
        }

        @Override
        public Object getItem(int position) {
            return orders.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View mView, ViewGroup parent) {

            if(mView == null) {
                mView = LayoutInflater.from(context).inflate(R.layout.order_layout, parent, false);
            }

            final Order order = orders.get(position);

            TextView order_email = mView.findViewById(R.id.order_email);
            TextView order_total = mView.findViewById(R.id.order_total);
            TextView order_orders = mView.findViewById(R.id.orders);
            Button order_delete = mView.findViewById(R.id.order_delete);

            order_email.setText(order.getEmail());

            float total = 0;
            String orders = "";
            for (Product item : order.getProducts()) {
                orders += item.getQuantity() + " x " + item.getName() + "\n";
                total += Float.valueOf(item.getPrice());
            }
            order_total.setText(total + "");
            order_orders.setText(orders);

            order_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.child(order.getKey()).removeValue();
                }
            });

            return mView;
        }
}
