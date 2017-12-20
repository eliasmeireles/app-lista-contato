package systemplus.com.br.listadecontatos.fragment;

import android.location.Address;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import systemplus.com.br.listadecontatos.AddNewCotactActivity;
import systemplus.com.br.listadecontatos.R;
import systemplus.com.br.listadecontatos.model.Contact;

/**
 * Created by elias on 20/12/17.
 */

public class ContactCadastro extends Fragment {

    private View inflaterView;
    private View locationPicker;
    private Address address;
    private Contact contact;
    private EditText contactName;
    private EditText contactPhone;
    private TextView addressStreet;
    private TextView addressNumeber;
    private TextView addressCity;
    private TextView addressState;
    private TextView addressCountry;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        inflaterView = inflater.inflate((R.layout.contact_cadastro_layout), container, false);

        contact = new Contact();


        contactName = inflaterView.findViewById(R.id.contact_name);
        contactPhone= inflaterView.findViewById(R.id.contact_phone);


        locationPicker = inflaterView.findViewById(R.id.address_picker);

        addressStreet = inflaterView.findViewById(R.id.address_street);
        addressNumeber = inflaterView.findViewById(R.id.address_number);
        addressState = inflaterView.findViewById(R.id.address_state);
        addressCity = inflaterView.findViewById(R.id.address_city);
        addressCountry = inflaterView.findViewById(R.id.address_country);



        locationPicker.setOnClickListener(view -> {
            FragmentTransaction fragmentTransaction = getFragmentTransaction();
            UserAddressFragment userAddressFragment = new UserAddressFragment();
            fragmentTransaction.replace(R.id.frame_laytou_new_user, userAddressFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return inflaterView;
    }

    private FragmentTransaction getFragmentTransaction() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        return fragmentManager.beginTransaction();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (((AddNewCotactActivity) getActivity()).getAddress() != null) {
            address = ((AddNewCotactActivity) getActivity()).getAddress();
            Log.e("Local", address.toString());
            addressStreet.setText(address.getAddressLine(0).toString());
            addressNumeber.setText("Número: "+ address.getFeatureName());
            addressCity.setText(address.getLocality());
            addressCountry.setText(address.getCountryName());
            addressState.setText(address.getAdminArea());
        }
    }
}
