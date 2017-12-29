package systemplus.com.br.listadecontatos.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import systemplus.com.br.listadecontatos.R;
import systemplus.com.br.listadecontatos.adapter.ContacAdapter;
import systemplus.com.br.listadecontatos.core.ContactQuery;
import systemplus.com.br.listadecontatos.helper.GetMapHelper;
import systemplus.com.br.listadecontatos.model.Contact;

import static systemplus.com.br.listadecontatos.extra.AppExtraKey.CONTACT_EXTRA_KEY;
import static systemplus.com.br.listadecontatos.file.ImageWriterReader.PATH_FOLDER;

/**
 * Created by elias on 26/12/17.
 */

public class DeletContactDialog extends DialogFragment {

    private static final String CONTACT_ADAPTER_KEY = "CONTACT_ADAPTER";
    private static final String CONTACT_POSITION_KEY = "CONTACT_POSTION";

    private static ContacAdapter contacAdapter;
    private static int positon;
    private Contact contact;

    public static DeletContactDialog newInstance(Contact contact, ContacAdapter contacAdapter, int positon) {
        DeletContactDialog customDialog = new DeletContactDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putSerializable(CONTACT_EXTRA_KEY, contact);
        args.putSerializable(CONTACT_ADAPTER_KEY, contacAdapter);
        args.putSerializable(CONTACT_POSITION_KEY, positon);
        customDialog.setArguments(args);

        return customDialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        contact = (Contact) getArguments().get(CONTACT_EXTRA_KEY);
        contacAdapter = (ContacAdapter) getArguments().get(CONTACT_ADAPTER_KEY);
        positon = (int) getArguments().get(CONTACT_POSITION_KEY);


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View inflateView = inflater.inflate(R.layout.delet_contact_dialog_layout, null);
        builder.setView(inflateView);

        setContactDataOnViews(inflateView);

        builder.setPositiveButton(R.string.delet_contact_ask, (dialog, id) -> {
            ContactQuery contactQuery = new ContactQuery(getActivity(), contact);
            contactQuery.delete();


            contacAdapter.getContactList().remove(positon);
            contacAdapter.notifyItemRemoved(positon);
            contacAdapter.notifyItemRangeChanged(positon, contacAdapter.getContactList().size());
        });


        builder.setNegativeButton(R.string.cancel, (dialog, id) -> DeletContactDialog
                .this.getDialog().cancel());

        return builder.create();
    }

    private void setContactDataOnViews(View inflateView) {
        ImageView contactFoto = inflateView.findViewById(R.id.contact_foto);
        TextView contactNome = inflateView.findViewById(R.id.contact_name);
        TextView contactTelefone = inflateView.findViewById(R.id.contact_phone);
        TextView contactAddress = inflateView.findViewById(R.id.contact_address);

        Glide.with(this)
                .load(new File(PATH_FOLDER + contact.getFoto()))
                .apply(RequestOptions
                        .circleCropTransform()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(contactFoto);


        contactNome.setText(contact.getNome());
        contactTelefone.setText(contact.getTelefone());
        contactAddress.setText(contact.getEndereco().getEnderecoInfor());
    }
}
