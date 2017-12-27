package systemplus.com.br.listadecontatos.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import systemplus.com.br.listadecontatos.R;
import systemplus.com.br.listadecontatos.model.Contact;

import static systemplus.com.br.listadecontatos.extra.AppExtraKey.CONTACT_EXTRA_KEY;

/**
 * Created by elias on 26/12/17.
 */

public class ContactCustomDialog extends DialogFragment {

    private Contact contact;

    public static ContactCustomDialog newInstance(Contact contact) {
        ContactCustomDialog customDialog = new ContactCustomDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putSerializable(CONTACT_EXTRA_KEY,  contact);
        customDialog.setArguments(args);

        return customDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        contact = (Contact) getArguments().get(CONTACT_EXTRA_KEY);


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View inflateView = inflater.inflate(R.layout.contact_dialog_layout, null);
        builder.setView(inflateView);

        setContactDataOnViews(inflateView);

        builder.setPositiveButton(R.string.abrir_map, (dialog, id) -> {

            String geo = "geo:" + contact.getEndereco().getLatitude() + contact.getEndereco().getLongitude() +
                    "?q=" + contact.getEndereco().getEnderecoInfor();

            showMap(Uri.parse(geo));
        });


        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
            ContactCustomDialog.this.getDialog().cancel();
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void setContactDataOnViews(View inflateView) {
        ImageView contactFoto = inflateView.findViewById(R.id.contact_foto);
        TextView contactNome = inflateView.findViewById(R.id.contact_name);
        TextView contactTelefone = inflateView.findViewById(R.id.contact_phone);
        TextView contactAddress = inflateView.findViewById(R.id.contact_address);

        Glide.with(this)
                .load(new File(contact.getFoto()))
                .apply(RequestOptions.circleCropTransform())
                .into(contactFoto);


        contactNome.setText(contact.getNome());
        contactTelefone.setText(contact.getTelefone());
        contactAddress.setText(contact.getEndereco().getEnderecoInfor());
    }


    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            getActivity().startActivity(intent);
        }
    }
}
