package systemplus.com.br.listadecontatos.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import systemplus.com.br.listadecontatos.ContactDetailActivity;
import systemplus.com.br.listadecontatos.R;
import systemplus.com.br.listadecontatos.model.Contact;

import static systemplus.com.br.listadecontatos.extra.AppExtraKey.CONTACT_EXTRA_KEY;

/**
 * Created by elias on 22/12/17.
 */

class ContactViewHolder extends RecyclerView.ViewHolder {

    private Activity activity;
    public ImageView contactFoto;
    public TextView contactNome;
    public TextView contactTelefone;
    public TextView contactAddress;
    public List<Contact> contactList;


    public ContactViewHolder(Activity activity, View itemView, List<Contact> contactList) {
        super(itemView);
        this.activity = activity;

        contactFoto = itemView.findViewById(R.id.contact_foto);
        contactNome = itemView.findViewById(R.id.contact_name);
        contactTelefone = itemView.findViewById(R.id.contact_phone);
        contactAddress = itemView.findViewById(R.id.contact_address);
        this.contactList = contactList;


        itemView.setOnClickListener(v -> {
            // get position
            int pos = getAdapterPosition();

            // check if item still exists
            if (pos != RecyclerView.NO_POSITION) {
                Contact contact = contactList.get(pos);
                Intent intent = new Intent(activity, ContactDetailActivity.class);
                intent.putExtra(CONTACT_EXTRA_KEY, contact);
                activity.startActivity(intent);
            }
        });
    }
}
