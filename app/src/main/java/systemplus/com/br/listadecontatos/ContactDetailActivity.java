package systemplus.com.br.listadecontatos;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import systemplus.com.br.listadecontatos.model.Contact;

import static systemplus.com.br.listadecontatos.extra.AppExtraKey.CONTACT_EXTRA_KEY;

public class ContactDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        Toolbar toolbar = findViewById(R.id.contact_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(null);


        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey(CONTACT_EXTRA_KEY)) {
            contact = (Contact) bundle.get(CONTACT_EXTRA_KEY);
            ImageView contactProfile = findViewById(R.id.profile_image);


            Glide.with(this)
                    .load(BitmapFactory.decodeFile(contact.getFoto()))
                    .into(contactProfile);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        } else {
            finish();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return true;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (contact != null) {
            LatLng contactAddress = new LatLng(contact.getEndereco().getLatitude(), contact.getEndereco().getLongitude());
            mMap.addMarker(new MarkerOptions().position(contactAddress).title(contact.getNome()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(contactAddress));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(contactAddress, 16.0f));
        }
    }
}
