package systemplus.com.br.listadecontatos.adapter;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import systemplus.com.br.listadecontatos.R;
import systemplus.com.br.listadecontatos.dialog.ContactCustomDialog;
import systemplus.com.br.listadecontatos.dialog.DeletContactDialog;
import systemplus.com.br.listadecontatos.helper.GetMapHelper;
import systemplus.com.br.listadecontatos.model.Contact;
import systemplus.com.br.listadecontatos.model.ContextMenuItem;

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
    public View recyclerViewContextMenu;
    private ContacAdapter contacAdapter;


    public ContactViewHolder(Activity activity, View itemView, List<Contact> contactList, ContacAdapter contacAdapter) {
        super(itemView);
        this.activity = activity;

        contactFoto = itemView.findViewById(R.id.contact_foto);
        contactNome = itemView.findViewById(R.id.contact_name);
        contactTelefone = itemView.findViewById(R.id.contact_phone);
        contactAddress = itemView.findViewById(R.id.contact_address);
        recyclerViewContextMenu = itemView.findViewById(R.id.recycler_view_context_menu);
        this.contacAdapter = contacAdapter;

        recyclerViewContextMenu.setOnClickListener(view -> {
            int pos = getAdapterPosition();

            List<ContextMenuItem> menuItems = new ArrayList<>();
            menuItems.add(new ContextMenuItem(R.drawable.ic_action_go_to_map, "Abrir no mapa"));
            menuItems.add(new ContextMenuItem(R.drawable.ic_action_call, "Ligar"));
            menuItems.add(new ContextMenuItem(R.drawable.ic_action_delete, "Remover"));

            ContextMenuAdapter contextMenuAdapter = new ContextMenuAdapter(activity, menuItems);

            ListPopupWindow listPopupWindow = new ListPopupWindow(activity);
            listPopupWindow.setAdapter(contextMenuAdapter);
            listPopupWindow.setAnchorView(recyclerViewContextMenu);
            listPopupWindow.setWidth((int) (240 * activity.getResources().getDisplayMetrics().density + 0.5f));
            listPopupWindow.setOnItemClickListener((adapterView, view1, position, id) -> {

                switch (position) {
                    case 0:
                        new GetMapHelper(activity).showMap(contactList.get(pos));
                        break;
                    case 1:
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + contactList.get(pos).getTelefone()));
                        activity.startActivity(intent);
                        break;

                    case 2:

                        DialogFragment deletContactDialog = DeletContactDialog.newInstance(contactList.get(pos), contacAdapter, pos);
                        deletContactDialog.setCancelable(false);
                        deletContactDialog.show(activity.getFragmentManager(), "dialog");
                        break;
                }

                listPopupWindow.dismiss();
            });
            listPopupWindow.setModal(true);
            listPopupWindow.show();

        });

        this.contactList = contactList;


        itemView.setOnClickListener(v -> {
            // get position
            int pos = getAdapterPosition();

            // check if item still exists
            if (pos != RecyclerView.NO_POSITION) {
                Contact contact = contactList.get(pos);
                DialogFragment newFragment = ContactCustomDialog.newInstance(contact);
                newFragment.show(activity.getFragmentManager(), "dialog");
            }
        });
    }
}
