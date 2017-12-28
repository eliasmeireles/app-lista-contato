package systemplus.com.br.listadecontatos.dialog;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import systemplus.com.br.listadecontatos.R;

/**
 * Created by elias on 28/12/17.
 */

public class GPSDialog {


    private Activity activity;

    public GPSDialog(Activity activity) {
        this.activity = activity;
    }

    public void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(R.string.gps_disabled)
                .setCancelable(false)
                .setTitle(R.string.gps_disabled_title)
                .setPositiveButton(R.string.gps_active_accept, (dialog, id) -> activity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton(R.string.cancel, (dialog, id) -> dialog.cancel());
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
