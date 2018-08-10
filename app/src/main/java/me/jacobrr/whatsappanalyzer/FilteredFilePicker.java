package me.jacobrr.whatsappanalyzer;

import android.support.annotation.NonNull;
import com.nononsenseapps.filepicker.FilePickerFragment;

import java.io.File;

/**
 * Created by jacob on 12/11/2015.
 */
public class FilteredFilePicker extends FilePickerFragment {
    // File extension to filter on
    private static final String EXTENSION = ".txt";

    /**
     * @param file
     * @return The file extension. If file has no extension, it returns null.
     */
    private String getExtension(@NonNull File file) {
        String path = file.getPath();
        int i = path.lastIndexOf(".");
        if (i < 0) {
            return null;
        } else {
            return path.substring(i);
        }
    }

    @Override
    protected boolean isItemVisible(final File file) {
        if (!isDir(file) && (mode == MODE_FILE || mode == MODE_FILE_AND_DIR)) {
            return EXTENSION.equalsIgnoreCase(getExtension(file));
        }
        return isDir(file);
    }
}
