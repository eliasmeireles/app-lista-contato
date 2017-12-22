package systemplus.com.br.listadecontatos;

import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import systemplus.com.br.listadecontatos.fragment.ContactCadastro;

public class AddNewCotactActivity extends AppCompatActivity {

    private Address address;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_cotact);


        Toolbar toolbar = findViewById(R.id.add_new_contact_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentTransaction fragmentTransaction = getFragmentTransaction();
        ContactCadastro contactCadastro = new ContactCadastro();
        fragmentTransaction.replace(R.id.frame_laytou_new_user, contactCadastro);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public FragmentTransaction getFragmentTransaction() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.beginTransaction();
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
