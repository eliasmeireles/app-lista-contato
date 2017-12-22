package systemplus.com.br.listadecontatos.file;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by elias on 21/12/17.
 */

public class ImageWriterReader {

    private static final String PATH_FOLDER = "/storage/emulated/0/Android/data/systemplus.com.br.listadecontatos/files/";
    private static final String JPG_IMAGE = ".jpg";

    public static void imageSave(Bitmap imageToSave) {

        File file = new File(new File(PATH_FOLDER), System.currentTimeMillis() + JPG_IMAGE);
        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
