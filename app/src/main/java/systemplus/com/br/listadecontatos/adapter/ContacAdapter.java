package systemplus.com.br.listadecontatos.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import systemplus.com.br.listadecontatos.R;
import systemplus.com.br.listadecontatos.model.Contact;

import static systemplus.com.br.listadecontatos.file.ImageWriterReader.PATH_FOLDER;

/**
 * Created by elias on 22/12/17.
 */

public class ContacAdapter extends RecyclerView.Adapter<ContactViewHolder> implements Serializable {

    private Activity activity;
    private List<Contact> contactList;

    public ContacAdapter(Activity activity, List<Contact> contactList) {
        this.activity = activity;
        this.contactList = contactList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_layout, parent, false);
        return new ContactViewHolder(activity, itemView, contactList, this);
    }



    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {

        Glide.with(activity)
                .load(new File(PATH_FOLDER + contactList.get(position).getFoto()))
                .apply(RequestOptions
                        .circleCropTransform()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(holder.contactFoto);


        holder.contactNome.setText(contactList.get(position).getNome());
        holder.contactTelefone.setText(contactList.get(position).getTelefone());
        holder.contactAddress.setText(contactList.get(position).getEndereco().getEnderecoInfor());

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public List<Contact> getContactList() {
        return contactList;
    }
}
