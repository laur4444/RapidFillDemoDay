package net.ddns.rapidfill.rapidfilldemoday;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    Context context;

    private GoogleMap mMap;
    AlertDialog commandDialog;

    SpeechActions speech;
    TextToSpeech toSpeech;

    FloatingActionButton fab;

    //Location

    private LocationManager locationManager;
    private LocationListener locationListener;
    private double user_lat, user_long;

    private Circle circle;
    private boolean added = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        toSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = toSpeech.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "language not supported");
                        Toast.makeText(Home.this, "Nu merge comanda vocala", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        speech = new SpeechActions(toSpeech, context);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                /*Intent cart = new Intent(context, Cart.class);
                startActivity(cart);*/
                getSpeechInput(view);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // MAP si LOCATION
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                user_lat = location.getLatitude();
                user_long = location.getLongitude();
                if (added) {
                    circle.remove();
                } else {
                    LatLng pos = new LatLng(user_lat, user_long);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 16));
                }
                CircleOptions circleOptions = new CircleOptions()
                        .center(new LatLng(user_lat, user_long))
                        .radius(3);

                circle = mMap.addCircle(circleOptions);
                added = true;
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
            }, 10);
            Toast.makeText(this, "Nu ai voie!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            configureLocation();
        }


    }

    public void configureLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "Nu ai voie!", Toast.LENGTH_SHORT).show();
            return;
        }
        locationManager.requestLocationUpdates("gps", 5000, 5, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    configureLocation();
                }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_toolbar) {
            Intent intent = new Intent(this, CasierOrders.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id == R.id.nav_products) {
            Intent mainMenu = new Intent(context, MainMenu.class);
            startActivity(mainMenu);
        }
        else if (id == R.id.nav_map) {

        } else if (id == R.id.nav_cart) {
            Intent cart = new Intent(context, Cart.class);
            startActivity(cart);
        } else if (id == R.id.nav_orders) {
            Intent cart = new Intent(context, ViewOrders.class);
            startActivity(cart);
        } else if (id == R.id.nav_log_out) {
            this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getSpeechInput(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);

        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(context, "No speech support on this", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if(resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    speech.GetInput(result.get(0));
                }
        }
    }
    @Override
    protected void onDestroy() {
        if(toSpeech != null) {
            toSpeech.stop();
            toSpeech.shutdown();
        }
        super.onDestroy();
    }


    // Harta
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        double[] latitudine = {44.420, 44.431};
        double[] longitudine = {26.088, 26.110};
        String[] numeBenzinarie = {"OMV Strada Gazelei", "OMV Bulevardul Corneliu Coposu"};
        for (int i = 0 ; i < 2 ; ++i){
            LatLng coord = new LatLng(latitudine[i], longitudine[i]);
            Marker marker = mMap.addMarker(new MarkerOptions().position(coord).title(numeBenzinarie[i]));
            marker.setTag("" + i + "_" + numeBenzinarie[i]);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coord, 16));

        }

        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String oras = (String) marker.getTag();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 16));
        //confirmCommand(oras);
        return false;
    }

    void confirmCommand(String oras){
        final AlertDialog.Builder commandDialogAux;
        final View markerBenzinarie;
        TextView benzinarieText;
        Button confirmCommandButton;
        Button cancelButton;

        commandDialogAux = new AlertDialog.Builder(this);
        markerBenzinarie = getLayoutInflater().inflate(R.layout.marker_benzinarie, null);
        benzinarieText = (TextView) markerBenzinarie.findViewById(R.id.benzinarieTextView);
        confirmCommandButton = (Button) markerBenzinarie.findViewById(R.id.btnComanda);
        cancelButton = (Button) markerBenzinarie.findViewById(R.id.btnCancel);

        final String tokenize[] = oras.split("_");

        benzinarieText.setText(tokenize[1]);
        confirmCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Comanda " + tokenize[0], Toast.LENGTH_SHORT).show();
                commandDialog.dismiss();
                fab.show();
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
