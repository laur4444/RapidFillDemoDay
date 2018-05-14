package net.ddns.rapidfill.rapidfilldemoday;

import android.os.RecoverySystem;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainMenu extends AppCompatActivity {

    private EditText searchField;
    private Button searchButton;

    private RecyclerView resultList;

    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        db = FirebaseDatabase.getInstance().getReference("Products");

        searchField = findViewById(R.id.search_field);
        searchButton = findViewById(R.id.search_button);
        resultList = findViewById(R.id.result_list);

        resultList.setHasFixedSize(true);
        resultList.setLayoutManager(new LinearLayoutManager(this));

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchText = searchField.getText().toString();
                firebaseProductSearch(searchText);
            }
        });

    }

    private void firebaseProductSearch(String searchText) {

        Query firebaseSearch = db.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerAdapter<Product, ProductViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(
                Product.class,
                R.layout.product_layout,
                ProductViewHolder.class,
                firebaseSearch

        ) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {

                viewHolder.setDetails(MainMenu.this, model);

            }
        };

        resultList.setAdapter(firebaseRecyclerAdapter);
    }

}
