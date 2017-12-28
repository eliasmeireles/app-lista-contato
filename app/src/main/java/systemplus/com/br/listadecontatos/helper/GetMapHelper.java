package systemplus.com.br.listadecontatos.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import systemplus.com.br.listadecontatos.model.Contact;

/**
 * Created by elias on 27/12/17.
 */

public class GetMapHelper {


    private Context context;

    public GetMapHelper(Context context) {
        this.context = context;
    }

    public void showMap(Contact contact) {
        String geo = "geo:" + contact.getEndereco().getLatitude() + contact.getEndereco().getLongitude() +
                "?q=" + contact.getEndereco().getEnderecoInfor();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(geo));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
}
