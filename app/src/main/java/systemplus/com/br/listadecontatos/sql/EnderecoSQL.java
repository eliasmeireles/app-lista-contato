package systemplus.com.br.listadecontatos.sql;

/**
 * Created by elias on 20/12/17.
 */

public interface EnderecoSQL {

    final String ID_KEY = "_id";
    final String ENDERECO_INFOR_KEY = "enderecoInfor";
    final String LATITUDE_KEY = "latitude";
    final String LONGITUDE_KEY = "longitude";

    String[] COLUMNS = new String[]{ID_KEY, ENDERECO_INFOR_KEY, LATITUDE_KEY, LONGITUDE_KEY};

    String CREATE_TABLE_ENDERECO = "CREATE TABLE endereco (\n" +
            ID_KEY              + " INTEGER     PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,\n" +
            ENDERECO_INFOR_KEY  + " TEXT        NOT NULL,\n" +
            LATITUDE_KEY        + " REAL        NOT NULL,\n" +
            LONGITUDE_KEY       + " REAL        NOT NULL\n" +
            ");";

    String DROP_TABLE_ENDERECO = "DROP TABLE IF EXISTS endereco;";
}
