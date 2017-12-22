package systemplus.com.br.listadecontatos.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import systemplus.com.br.listadecontatos.model.Contact;
import systemplus.com.br.listadecontatos.model.Endereco;
import systemplus.com.br.listadecontatos.sql.ContatoSQL;

import static systemplus.com.br.listadecontatos.core.GetSQLiteDatabase.getSQLiteDatabase;

/**
 * Created by elias on 22/12/17.
 */

public class ContactQuery implements DataQuery {

    private Context context;
    private Contact contact;
    private SQLiteDatabase database;
    private List<Contact> contactList;
    private List<Endereco> enderecoList;

    public ContactQuery(Context context, Contact contact) {
        this.context = context;
        this.contact = contact;
        this.database = getSQLiteDatabase(context);
    }

    @Override
    public List<Contact> find() {
        EnderecoQuery enderecoQuery = new EnderecoQuery(context, null);
        enderecoList = enderecoQuery.find();

        contactList = new ArrayList<>();
        Cursor cursor = database.query("contato", ContatoSQL.COLUMNS, null, null, null, null, null, null);

        if (cursor.getCount() > 0) {

            if (cursor.moveToFirst())
                do {  //<---------if you not need the loop you can remove that
                    contactList.add(getContactValues(cursor));
                } while (cursor.moveToNext());

        }

        return contactList;
    }

    private Contact getContactValues(Cursor cursor) {
        Contact contact = new Contact();
//        String[] COLUMNS = new String[]{ID_KEY, NOME_KEY, FOTO_KEY, TELEFONE_KEY, ENDERECO_KEY};

        contact.setId(Long.parseLong(cursor.getString(0)));
        contact.setNome(cursor.getString(1));
        contact.setFoto(cursor.getString(2));
        contact.setTelefone(cursor.getString(3));
        long enderecoId = Long.parseLong(cursor.getString(4));

        for (Endereco e : enderecoList) {
            if (e.getId() == enderecoId) {
                contact.setEndereco(e);
                break;
            }
        }
        return contact;
    }

    @Override
    public long insert() {
        EnderecoQuery enderecoQuery = new EnderecoQuery(context, contact.getEndereco());
        contact.getEndereco().setId(enderecoQuery.insert());


        ContentValues values = new ContentValues();
        values.put("nome", contact.getNome());
        values.put("foto", contact.getFoto());
        values.put("telefone", contact.getTelefone());
        values.put("endereco_id", contact.getEndereco().getId());

        return database.insert("contato", null, values);
    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }
}
