package systemplus.com.br.listadecontatos.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static systemplus.com.br.listadecontatos.core.DatabaseConfig.DATABASE_NAME;
import static systemplus.com.br.listadecontatos.core.DatabaseConfig.DATABASE_VERSION;
import static systemplus.com.br.listadecontatos.sql.ContatoSQL.CREATE_TABLE_CONTATO;
import static systemplus.com.br.listadecontatos.sql.ContatoSQL.DROP_TABLE_CONTATO;
import static systemplus.com.br.listadecontatos.sql.EnderecoSQL.CREATE_TABLE_ENDERECO;
import static systemplus.com.br.listadecontatos.sql.EnderecoSQL.DROP_TABLE_ENDERECO;


/**
 * Created by elias on 20/12/17.
 */

public class DataCore extends SQLiteOpenHelper {


    public DataCore(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ENDERECO);
        sqLiteDatabase.execSQL(CREATE_TABLE_CONTATO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE_CONTATO);
        sqLiteDatabase.execSQL(DROP_TABLE_ENDERECO);

        onCreate(sqLiteDatabase);
    }
}
