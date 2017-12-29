package systemplus.com.br.listadecontatos.core;

import java.util.List;

/**
 * Created by elias on 13/12/17.
 */

public interface DataQuery {

    List<?> findAll();
    long insert();
    void update();
    void delete();
}
