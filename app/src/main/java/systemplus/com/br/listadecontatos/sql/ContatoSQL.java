package systemplus.com.br.listadecontatos.sql;

/**
 * Created by elias on 20/12/17.
 */

public interface ContatoSQL {

    final String TABLE_NAME_KEY = "contato";
    final String ID_KEY = "_id";
    final String FOTO_KEY = "foto";
    final String NOME_KEY = "nome";
    final String ENDERECO_KEY = "endereco_id";
    final String TELEFONE_KEY = "telefone";

    String[] COLUMNS = new String[]{ID_KEY, NOME_KEY, FOTO_KEY, TELEFONE_KEY, ENDERECO_KEY};

    String CREATE_TABLE_CONTATO = "CREATE TABLE " + TABLE_NAME_KEY + "(\n" +
            ID_KEY          + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE NOT NULL,\n" +
            FOTO_KEY        + " TEXT        NOT NULL,\n" +
            NOME_KEY        + " TEXT        NOT NULL,\n" +
            TELEFONE_KEY    + " TEXT        NOT NULL,\n" +
            ENDERECO_KEY    + " INTEGER UNIQUE      NOT NULL,\n" +
            "  FOREIGN KEY (" + ENDERECO_KEY + ") REFERENCES " + EnderecoSQL.TABLE_NAME_KEY + "(_id) \n" +
            "  ON DELETE CASCADE ON UPDATE NO ACTION\n" +
            ");";

    String DROP_TABLE_CONTATO = "DROP TABLE IF EXISTS " + TABLE_NAME_KEY + ";";
}
