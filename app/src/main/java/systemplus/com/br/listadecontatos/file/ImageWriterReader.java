package systemplus.com.br.listadecontatos.file;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by elias on 21/12/17.
 */

public class ImageWriterReader {

    public static final String PATH_FOLDER = "/storage/emulated/0/Android/data/systemplus.com.br.listadecontatos/files/";

    public static String imageSave(Bitmap imageToSave, String fileName) {

        File file = new File(new File(PATH_FOLDER), fileName);
        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 20, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file.getName();
    }
}
