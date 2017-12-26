package systemplus.com.br.listadecontatos;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import systemplus.com.br.listadecontatos.core.ContactQuery;
import systemplus.com.br.listadecontatos.model.Contact;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private final static int APP_PERMISION_FINE_LOCATION = 101;
    private List<Contact> contactList;
    private NavigationView navigationView;
    private DrawerLayout drawer;
        private GoogleMap mMap;
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
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        getContactListFromDatabase();

    }

    private void getContactListFromDatabase() {
        ContactQuery contactQuery = new ContactQuery(this, null);
        contactList = contactQuery.find();
    }

    @Override
    protected void onResume() {
        super.onResume();

        navigationView.getMenu().findItem(R.id.contact_list).setVisible(true);
        navigationView.getMenu().findItem(R.id.got_to_home).setVisible(false);
        getContactListFromDatabase();

        addContactsOnMap();

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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

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
            case R.id.add_new_contact:
                startActivity(new Intent(HomeActivity.this, ContactCadastroActivity.class));
                break;

            case R.id.contact_list:
                startActivity( new Intent(HomeActivity.this, ListContactAcitivity.class));
                break;
            default:
                break;
        }

        closeDrawer(drawer);
        return true;
    }

    private void mapView() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "É preciso ativar o serviço de localização!", Toast.LENGTH_LONG).show();
        } else {
            mMap.setMyLocationEnabled(true);
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16.0f));
                        }
                    });

            addContactsOnMap();
        }
    }

    private void addContactsOnMap() {
        if (mMap != null) {
            if (contactList != null && contactList.size() > 0) {
                for (Contact co : contactList) {

                    LatLng contadoLocation = new LatLng(co.getEndereco().getLatitude(), co.getEndereco().getLongitude());
                    mMap.addMarker(new MarkerOptions().position(contadoLocation).title(co.getNome()));
                }
            }
        }
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, APP_PERMISION_FINE_LOCATION);
            }
        }

    }
    
    private void closeDrawer(DrawerLayout drawer) {
        drawer.closeDrawer(GravityCompat.START);
    }
}
