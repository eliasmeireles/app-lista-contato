package systemplus.com.br.listadecontatos.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import systemplus.com.br.listadecontatos.model.Endereco;
import systemplus.com.br.listadecontatos.sql.EnderecoSQL;

import static systemplus.com.br.listadecontatos.core.GetSQLiteDatabase.getSQLiteDatabase;

/**
 * Created by elias on 22/12/17.
 */

public class EnderecoQuery implements DataQuery {
    private Endereco endereco;
    private SQLiteDatabase database;
    private List<Endereco> enderecoList;

    public EnderecoQuery(Context context, Endereco endereco) {
        this.endereco = endereco;
        this.database = getSQLiteDatabase(context);
    }

    @Override
    public List<Endereco> findAll() {
        enderecoList = new ArrayList<>();
        Cursor cursor = database.query(EnderecoSQL.TABLE_NAME_KEY, EnderecoSQL.COLUMNS, null, null, null, null, null, null);

        if (cursor.getCount() > 0) {

            if (cursor.moveToFirst())
                do {  //<---------if you not need the loop you can remove that
                    enderecoList.add(setEnderecoValues(cursor));
                } while (cursor.moveToNext());

        }
        return enderecoList;
    }


    @Override
    public long insert() {
        ContentValues values = new ContentValues();
        values.put(EnderecoSQL.ENDERECO_INFOR_KEY, endereco.getEnderecoInfor());
        values.put(EnderecoSQL.LATITUDE_KEY, endereco.getLatitude());
        values.put(EnderecoSQL.LONGITUDE_KEY, endereco.getLongitude());

        return database.insert(EnderecoSQL.TABLE_NAME_KEY, null, values);
    }

    @Override
    public void update() {
        ContentValues values = new ContentValues();
        values.put(EnderecoSQL.ID_KEY, endereco.getId());
        values.put(EnderecoSQL.ENDERECO_INFOR_KEY, endereco.getEnderecoInfor());
        values.put(EnderecoSQL.LATITUDE_KEY, endereco.getLatitude());
        values.put(EnderecoSQL.LONGITUDE_KEY, endereco.getLongitude());
        database.update(EnderecoSQL.TABLE_NAME_KEY, values, EnderecoSQL.ID_KEY + "=?", new String[]{endereco.getId() + ""});
    }

    @Override
    public void delete() {
        database.delete(EnderecoSQL.TABLE_NAME_KEY, EnderecoSQL.ID_KEY + "=?", new String[]{endereco.getId() + ""});

    }

    //    String[] COLUMNS = new String[]{ID_KEY, ENDERECO_INFOR_KEY, LATITUDE_KEY, LONGITUDE_KEY};
    private Endereco setEnderecoValues(Cursor cursor) {
        Endereco endereco = new Endereco();
        endereco.setId(Long.parseLong(cursor.getString(0)));
        endereco.setEnderecoInfor(cursor.getString(1));
        endereco.setLatitude(Double.valueOf(cursor.getString(2)));
        endereco.setLongitude(Double.valueOf(cursor.getString(3)));

        return endereco;
    }
}
