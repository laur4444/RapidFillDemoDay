package net.ddns.rapidfill.rapidfilldemoday;

import android.content.Context;
import android.content.Intent;
import android.os.RecoverySystem;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity {

    private EditText searchField;
    private Button searchButton;
    private Button testBtn;

    private RecyclerView resultList;

    DatabaseReference db;

    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        db = FirebaseDatabase.getInstance().getReference("Products");

        searchField = findViewById(R.id.search_field);
        searchButton = findViewById(R.id.search_button);
        resultList = findViewById(R.id.result_list);

        testBtn = findViewById(R.id.test_btn);

        resultList.setHasFixedSize(true);
        resultList.setLayoutManager(new LinearLayoutManager(this));

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchText = searchField.getText().toString();
                firebaseProductSearch(searchText);
            }
        });
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent test = new Intent(MainMenu.this, Home.class );
                MainMenu.this.startActivity(test);
            }
        });

        //Search
        materialSearchBar = findViewById(R.id.searchBar);
        materialSearchBar.setHint("Search your favorite product");
        materialSearchBar.setSpeechMode(false);
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest = new ArrayList<>();
                for(String search : suggestList) {
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                String s = enabled ? "enabled" : "disabled";
                    Toast.makeText(MainMenu.this, "Search " + s, Toast.LENGTH_SHORT).show();
               if(!enabled)
                   firebaseProductSearch("");
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                firebaseProductSearch(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
        materialSearchBar.setSuggstionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {
                String selectedSuggestion = (String) materialSearchBar.getLastSuggestions().get(position);
                firebaseProductSearch(selectedSuggestion);
                materialSearchBar.setText(selectedSuggestion);
                materialSearchBar.hideSuggestionsList();
                materialSearchBar.disableSearch();
            }

            @Override
            public void OnItemDeleteListener(int position, View v) {

            }
        });



    }
    private void loadSuggest() {
        db.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Product item = postSnapshot.getValue(Product.class);
                    suggestList.add(item.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void firebaseProductSearch(String searchText) {

        Query firebaseSearch = db.orderByChild("name")
                .startAt(searchText);

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
