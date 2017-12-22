package systemplus.com.br.listadecontatos.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import systemplus.com.br.listadecontatos.AddNewCotactActivity;
import systemplus.com.br.listadecontatos.R;
import systemplus.com.br.listadecontatos.core.ContactQuery;
import systemplus.com.br.listadecontatos.intent.ImageSelectIntent;
import systemplus.com.br.listadecontatos.model.Contact;
import systemplus.com.br.listadecontatos.model.Endereco;

import static android.app.Activity.RESULT_OK;

/**
 * Created by elias on 20/12/17.
 */

public class ContactCadastro extends Fragment {

    private final static int RESULT_LOAD_IMAGE = 1;

    private View inflaterView;
    private EditText contactAddress;
    private View saveContact;
    private Address address;
    private Contact contact;
    private EditText contactName;
    private EditText contactPhone;
    private ImageView contactImage;
    private ImageView contactImageSet;
    private String fotoPath;
    private ImageSelectIntent imageSelectIntent;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        inflaterView = inflater.inflate((R.layout.contact_cadastro_layout), container, false);

        contactName = inflaterView.findViewById(R.id.contact_name);

        contactPhone = inflaterView.findViewById(R.id.contact_phone);
        contactPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        contactAddress = inflaterView.findViewById(R.id.address_picker);
        saveContact = inflaterView.findViewById(R.id.save_contact);
        contactImage = inflaterView.findViewById(R.id.profile_image);
        contactImageSet = inflaterView.findViewById(R.id.user_set_image);

        Glide.with(getContext())
                .load(R.drawable.user_image)
                .apply(RequestOptions.circleCropTransform())
                .into(contactImage);

        contactImageSet.setOnClickListener(view -> requestPermission());
        saveContact.setOnClickListener(view -> {
            contact = new Contact();
            contact.setNome(contactName.getText().toString().trim());
            contact.setTelefone(contactPhone.getText().toString().trim());
            if (!inputValidation()) {

                contact.setFoto(fotoPath);

                Endereco endereco = new Endereco();
                endereco.setEnderecoInfor(address.getAddressLine(0).toString());
                endereco.setLatitude(address.getLatitude());
                endereco.setLongitude(address.getLongitude());

                contact.setEndereco(endereco);

                ContactQuery contactQuery = new ContactQuery(getContext(), contact);
                contactQuery.insert();

                Toast.makeText(getContext(), "Contado salvo com sucesso!", Toast.LENGTH_LONG).show();

                getActivity().onBackPressed();
            }

        });

        contactAddress.setOnClickListener(view -> {
            FragmentTransaction fragmentTransaction = getAddNewContactActivity().getFragmentTransaction();
            UserAddressFragment userAddressFragment = new UserAddressFragment();
            fragmentTransaction.replace(R.id.frame_laytou_new_user, userAddressFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return inflaterView;
    }

    private boolean inputValidation() {
        boolean validation = false;
        if (contact.getNome().isEmpty()) {
            validation = true;
            contactName.setError("Campo obrigatório!");
            contactName.setText("");
        }

        if (contact.getTelefone().isEmpty()) {
            contactPhone.setError("O telefone é obrigatório!");
            if (contact.getTelefone().length() < 12) {
                contactPhone.setError("Número inválido!");
            }
            contactPhone.setText("");
            validation = true;
        }

        if (contact.getTelefone().length() < 12) {
            contactPhone.setError("Número inválido!");
            validation = true;
        }

        if (address == null) {
            contactAddress.setError("Endereço obrigatório!");
            validation = true;
        }

        if (fotoPath == null) {
            validation = true;
            Toast.makeText(getContext(), "Escolha ou tire uma foto para o contato!", Toast.LENGTH_SHORT).show();
        }
        return validation;
    }


    private void requestPermission() {
        imageSelectIntent = new ImageSelectIntent(getContext());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 354);
        } else {
            startActivityForResult(imageSelectIntent.getPickImageIntent(), RESULT_LOAD_IMAGE);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                fotoPath = cursor.getString(columnIndex);
                cursor.close();

//                imageSave(BitmapFactory.decodeFile(fotoPath));
            } else if (requestCode == RESULT_LOAD_IMAGE) {
                fotoPath = imageSelectIntent.getFotoPath();
            }

            Log.e("Path", fotoPath);
            Glide.with(getContext())
                    .load(BitmapFactory.decodeFile(fotoPath))
                    .apply(RequestOptions.circleCropTransform())
                    .into(contactImage);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case RESULT_LOAD_IMAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(imageSelectIntent.getPickImageIntent(), RESULT_LOAD_IMAGE);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getAddNewContactActivity().getAddress() != null) {
            address = (getAddNewContactActivity()).getAddress();
            contactAddress.setText(address.getAddressLine(0).toString());
        }

        if (fotoPath != null) {
            Glide.with(getContext())
                    .load(BitmapFactory.decodeFile(fotoPath))
                    .apply(RequestOptions.circleCropTransform())
                    .into(contactImage);
        }
    }

    private AddNewCotactActivity getAddNewContactActivity() {
        return (AddNewCotactActivity) getActivity();
    }


}
