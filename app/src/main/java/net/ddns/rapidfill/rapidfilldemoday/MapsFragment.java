package net.ddns.rapidfill.rapidfilldemoday;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Laurentiu on 5/18/2018.
 */

public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback {

        GoogleMap mGoogleMap;

        @Override
        public void onResume() {
            super.onResume();

            setUpMapIfNeeded();
        }

        private void setUpMapIfNeeded() {

            if (mGoogleMap == null) {
                getMapAsync(this);
            }
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            LatLng sydney = new LatLng(-34, 151);
            //mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            //mGoogleMap.addMarker(new MarkerOptions().position(sydney));
        }
}
