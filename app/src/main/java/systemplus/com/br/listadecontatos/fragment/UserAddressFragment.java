package systemplus.com.br.listadecontatos.fragment;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import systemplus.com.br.listadecontatos.AddNewCotactActivity;
import systemplus.com.br.listadecontatos.R;

/**
 * Created by elias on 20/12/17.
 */

public class UserAddressFragment extends Fragment {

    private GoogleMap mMap;
    private Address address;
    private FusedLocationProviderClient mFusedLocationClient;
    private View addressPicker;
    private View inflaterView;
    private SupportMapFragment mSupportMapFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        inflaterView = inflater.inflate((R.layout.activity_user_address), container, false);
        mapStart();


        addressPicker = inflaterView.findViewById(R.id.address_picker);

        addressPicker.setOnClickListener(view -> {
            LatLng target = mMap.getCameraPosition().target;

            try {
                address = getAddress(target.latitude, target.longitude);

                if (address != null) {
                    ((AddNewCotactActivity) getActivity()).setAddress(address);
                    getActivity().onBackPressed();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        getLocation();

        return inflaterView;
    }

    private void mapStart() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mSupportMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, mSupportMapFragment).commit();
        }

        if (mSupportMapFragment != null) {
            mSupportMapFragment.getMapAsync(googleMap -> {
                if (googleMap != null) {

                    mMap = googleMap;

                    // Add a marker in Sydney and move the camera

                    if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {
                        return;
                    } else {
                        mMap.setMyLocationEnabled(true);
                    }

                }

            });
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16.0f));
                    }
                });
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

    private Address getAddress(double latitude, double longitude) throws IOException {
        Address address = null;

        Geocoder geocoder = new Geocoder(getContext());

        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

        if (addresses.size() > 0) {
            address = addresses.get(0);
        }

        return address;
    }

}
