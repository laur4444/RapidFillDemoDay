package net.ddns.rapidfill.rapidfilldemoday;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

    //private RecyclerView resultList;
    private ListView resultList;
    private ArrayList<Product> products;
    private productArrayAdaptor productAdapter;

    DatabaseReference db;

    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        db = FirebaseDatabase.getInstance().getReference("Products");

        resultList = findViewById(R.id.result_list);
        products = new ArrayList<>();
        loadSuggest();
        productAdapter = new productArrayAdaptor();


        //Search
        materialSearchBar = findViewById(R.id.searchBar);
        materialSearchBar.setHint("Search your favorite product");
        materialSearchBar.setSpeechMode(false);

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
                firebaseProductSearch(selectedSuggestion);
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
                    products.add(item);
                    suggestList.add(item.getName());
                }
                productAdapter.setParameters(MainMenu.this, products);
                resultList.setAdapter(productAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void firebaseProductSearch(String searchText) {
        ArrayList<Product> results = new ArrayList<>();
        for(Product item : products) {
            if(item.getName().toLowerCase().contains(searchText.toLowerCase())) {
                results.add(item);
            }
        }
        productAdapter.setParameters(MainMenu.this, results);
        resultList.setAdapter(productAdapter);
    }
}
