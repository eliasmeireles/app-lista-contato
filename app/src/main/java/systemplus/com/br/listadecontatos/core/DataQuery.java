package systemplus.com.br.listadecontatos.core;

import java.util.List;

import systemplus.com.br.listadecontatos.model.Contact;

/**
 * Created by elias on 13/12/17.
 */

public interface DataQuery {

    List<?> find();
    long insert();
    void update();
    void delete();
}
