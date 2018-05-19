package net.ddns.rapidfill.rapidfilldemoday;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Marker mSydney;
    AlertDialog commandDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34 , 151);
        mSydney = mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mSydney.setTag("1_Sydney");
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        String oras = (String) marker.getTag();
        confirmCommand(oras);
        return false;
    }

    void confirmCommand(String oras){
        final AlertDialog.Builder commandDialogAux;
        final View markerBenzinarie;
        TextView benzinarieText;
        Button confirmCommandButton;
        Button cancelButton;

        commandDialogAux = new AlertDialog.Builder(MapsActivity.this);
        markerBenzinarie = getLayoutInflater().inflate(R.layout.marker_benzinarie, null);
        benzinarieText = (TextView) markerBenzinarie.findViewById(R.id.benzinarieTextView);
        confirmCommandButton = (Button) markerBenzinarie.findViewById(R.id.btnComanda);
        cancelButton = (Button) markerBenzinarie.findViewById(R.id.btnCancel);

        final String tokenize[] = oras.split("_");

        benzinarieText.setText(tokenize[1]);
        confirmCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MapsActivity.this, "Comanda " + tokenize[0], Toast.LENGTH_SHORT).show();

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commandDialog.dismiss();
            }
        });
        commandDialogAux.setView(markerBenzinarie);
        commandDialog = commandDialogAux.create();
        commandDialog.setCancelable(true);
        commandDialog.show();
    }
}
