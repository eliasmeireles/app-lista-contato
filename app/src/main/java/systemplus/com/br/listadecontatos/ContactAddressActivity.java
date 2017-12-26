package systemplus.com.br.listadecontatos;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import static systemplus.com.br.listadecontatos.extra.AppExtraKey.ADDRESS_EXTRA_KEY;

public class ContactAddressActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Address address;
    private View addressPicker;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_address);

        Toolbar toolbar = findViewById(R.id.contact_address_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(null);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        addressPicker = findViewById(R.id.address_picker);

        addressPicker.setOnClickListener(view -> {
            LatLng target = mMap.getCameraPosition().target;

            try {
                address = getAddress(target.latitude, target.longitude);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Intent returnIntent = new Intent();
            returnIntent.putExtra(ADDRESS_EXTRA_KEY, address);
            setResult(Activity.RESULT_OK, returnIntent);

            finish();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getResources().getString(R.string.need_to_add_permisson_lotcation), Toast.LENGTH_LONG).show();
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
        }

    }

    private Address getAddress(double latitude, double longitude) throws IOException {
        Address address = null;

        Geocoder geocoder = new Geocoder(this);

        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

        if (addresses.size() > 0) {
            address = addresses.get(0);
        }

        return address;
    }
}
