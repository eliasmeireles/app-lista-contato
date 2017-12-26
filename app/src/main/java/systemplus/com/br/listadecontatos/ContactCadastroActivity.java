package systemplus.com.br.listadecontatos;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import systemplus.com.br.listadecontatos.core.ContactQuery;
import systemplus.com.br.listadecontatos.intent.ImageSelectIntent;
import systemplus.com.br.listadecontatos.model.Contact;
import systemplus.com.br.listadecontatos.model.Endereco;

import static systemplus.com.br.listadecontatos.extra.AppExtraKey.ADDRESS_EXTRA_KEY;
import static systemplus.com.br.listadecontatos.extra.AppExtraKey.CONTACT_EXTRA_KEY;
import static systemplus.com.br.listadecontatos.file.ImageWriterReader.imageSave;

public class ContactCadastroActivity extends AppCompatActivity {

    private final static int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_PERMISSION_CODE = 354;
    private static final int RESQUEST_CODE_CONTACT_ADDRESS = 421;

    private Address address;
    private EditText contactAddress;
    private View saveContact;
    private Contact contact;
    private EditText contactName;
    private EditText contactPhone;
    private ImageView contactImage;
    private ImageView contactImageSet;
    private String fotoPath;
    private ImageSelectIntent imageSelectIntent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_cadastro);


        Toolbar toolbar = findViewById(R.id.add_new_contact_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(null);

        contactName = findViewById(R.id.contact_name);

        contactPhone = findViewById(R.id.contact_phone);
        contactPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        contactAddress = findViewById(R.id.address_picker);
        saveContact = findViewById(R.id.save_contact);
        contactImage = findViewById(R.id.profile_image);
        contactImageSet = findViewById(R.id.user_set_image);

        Glide.with(this)
                .load(R.drawable.user_image)
                .apply(RequestOptions.circleCropTransform())
                .into(contactImage);

        contactImageSet.setOnClickListener(view -> requestPermission());

        contactAddress.setOnClickListener(view -> {
            startActivityForResult(new Intent(ContactCadastroActivity.this, ContactAddressActivity.class), RESQUEST_CODE_CONTACT_ADDRESS);
        });

        saveContact();
    }

    private void saveContact() {
        saveContact.setOnClickListener(view -> {
            contact = new Contact();
            contact.setNome(contactName.getText().toString().trim());
            contact.setTelefone(contactPhone.getText().toString().trim());

            if (!inputValidation()) {
                imageSave(BitmapFactory.decodeFile(fotoPath));
                contact.setFoto(fotoPath);

                Endereco endereco = new Endereco();
                endereco.setEnderecoInfor(address.getAddressLine(0).toString());
                endereco.setLatitude(address.getLatitude());
                endereco.setLongitude(address.getLongitude());

                contact.setEndereco(endereco);

                ContactQuery contactQuery = new ContactQuery(this, contact);
                contactQuery.insert();

                Intent returnIntent = new Intent();
                returnIntent.putExtra(CONTACT_EXTRA_KEY, contact);
                this.setResult(Activity.RESULT_OK, returnIntent);

                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return true;
    }

    private void requestPermission() {
        imageSelectIntent = new ImageSelectIntent(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_PERMISSION_CODE);
            }
        } else {
            startActivityForResult(imageSelectIntent.getPickImageIntent(), RESULT_LOAD_IMAGE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(imageSelectIntent.getPickImageIntent(), RESULT_LOAD_IMAGE);
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case RESQUEST_CODE_CONTACT_ADDRESS:
                if (requestCode == RESQUEST_CODE_CONTACT_ADDRESS && resultCode == RESULT_OK && null != data) {
                    if (data.getExtras().containsKey(ADDRESS_EXTRA_KEY)) {
                        address = (Address) data.getExtras().get(ADDRESS_EXTRA_KEY);
                        contactAddress.setText(address.getAddressLine(0).toString());
                    }
                }
                break;
            case RESULT_LOAD_IMAGE:
                imageRequestResult(requestCode, resultCode, data);
                break;

        }


    }

    private void imageRequestResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {


            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                fotoPath = cursor.getString(columnIndex);
                cursor.close();

            } else if (requestCode == RESULT_LOAD_IMAGE) {
                fotoPath = imageSelectIntent.getFotoPath();
            }

            Log.e("Path", fotoPath);
            Glide.with(this)
                    .load(BitmapFactory.decodeFile(fotoPath))
                    .apply(RequestOptions.circleCropTransform())
                    .into(contactImage);

        }
    }

    private boolean inputValidation() {
        boolean validation = false;
        if (contact.getNome().isEmpty()) {
            validation = true;
            contactName.setError(getResources().getString(R.string.nome_null));
            contactName.setText("");
        }

        if (contact.getTelefone().isEmpty()) {
            contactPhone.setError(getResources().getString(R.string.phone_null));
            contactPhone.setText("");
            validation = true;
        }

        if (contact.getTelefone().length() < 11) {
            contactPhone.setError(getResources().getString(R.string.phone_number_invalid));
            validation = true;
        }

        if (address == null) {
            contactAddress.setError(getResources().getString(R.string.address_null));
            validation = true;
        }

        if (fotoPath == null) {
            validation = true;
            Toast.makeText(this, getResources().getString(R.string.nedd_to_get_image), Toast.LENGTH_SHORT).show();
        }
        return validation;
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
