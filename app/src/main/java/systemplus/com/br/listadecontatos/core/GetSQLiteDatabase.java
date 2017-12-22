package systemplus.com.br.listadecontatos.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by elias on 13/12/17.
 */

public class GetSQLiteDatabase {

    public static SQLiteDatabase getSQLiteDatabase(Context context) {
        DataCore dataCore = new DataCore(context);

        return dataCore.getWritableDatabase();
    }
}
