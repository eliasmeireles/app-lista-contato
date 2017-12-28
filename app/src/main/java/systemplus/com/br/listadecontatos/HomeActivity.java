package systemplus.com.br.listadecontatos;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import systemplus.com.br.listadecontatos.core.ContactQuery;
import systemplus.com.br.listadecontatos.dialog.ContactCustomDialog;
import systemplus.com.br.listadecontatos.dialog.GPSDialog;
import systemplus.com.br.listadecontatos.model.Contact;

import static systemplus.com.br.listadecontatos.helper.CheckPermition.notLocationPemirtion;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private final static int APP_PERMISION_FINE_LOCATION = 101;
    private List<Contact> contactList;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private GoogleMap mMap;
    private Location location;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(null);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        getSuporMaptFragment();


        getContactListFromDatabase();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getSuporMaptFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();

        navigationView.getMenu().findItem(R.id.contact_list).setVisible(true);
        navigationView.getMenu().findItem(R.id.got_to_home).setVisible(false);


        getContactListFromDatabase();

        addContactsOnMap();

        if (!notLocationPemirtion(this)) {
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                new GPSDialog(this).buildAlertMessageNoGps();
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            closeDrawer(drawer);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (notLocationPemirtion(this)) {
            requestPermission();
        } else {
            addContactsOnMap();
            mapView();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.contact_list:
                startActivity(new Intent(HomeActivity.this, ListContactAcitivity.class));
                break;
            default:
                break;
        }

        closeDrawer(drawer);
        return true;
    }

    private void mapView() {
        if (notLocationPemirtion(this)) {
            Toast.makeText(this, getResources().getString(R.string.need_to_add_permisson_lotcation), Toast.LENGTH_LONG).show();

        } else {
            setMyLocation();
            addContactsOnMap();
        }
    }

    private void addContactsOnMap() {
        if (mMap != null) {

            mMap.clear();

            if (contactList != null && contactList.size() > 0) {
                for (Contact co : contactList) {

                    LatLng contadoLocation = new LatLng(co.getEndereco().getLatitude(), co.getEndereco().getLongitude());
                    Marker marker = mMap.addMarker(new MarkerOptions().position(contadoLocation).title(co.getNome()));

                    marker.setTag(co);
                    mMap.setOnMarkerClickListener(marker1 -> {

                        Contact contact = (Contact) marker1.getTag();

                        DialogFragment newFragment = ContactCustomDialog.newInstance(contact);

                        newFragment.show(getFragmentManager(), "dialog");

                        return false;
                    });
                }
            }
        }

    }

    private void getSuporMaptFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case APP_PERMISION_FINE_LOCATION:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setMyLocation();
                } else {
                    startActivity(new Intent(HomeActivity.this, ListContactAcitivity.class));
                }

                break;
        }
    }

    @SuppressLint("MissingPermission")
    private void setMyLocation() {
        mMap.setMyLocationEnabled(true);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16.0f));
                    }
                });
    }


    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, APP_PERMISION_FINE_LOCATION);
            }
        }

    }

    private void getContactListFromDatabase() {
        ContactQuery contactQuery = new ContactQuery(this, null);
        contactList = contactQuery.findAll();

        if (contactList == null || contactList.size() == 0) {
            startActivity(new Intent(HomeActivity.this, ListContactAcitivity.class));
        }
    }

    private void closeDrawer(DrawerLayout drawer) {
        drawer.closeDrawer(GravityCompat.START);
    }
}
