package com.madinnovations.recipekeeper.model.dao.impl.json;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.madinnovations.recipekeeper.R;
import com.madinnovations.recipekeeper.model.exceptions.DataException;
import com.madinnovations.recipekeeper.model.utils.DataConstants;
import com.madinnovations.recipekeeper.model.utils.FileUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import javax.inject.Singleton;

/**
 * Created by madanle on 4/29/16.
 */
@Singleton
public class RecipeKeeperFileHelper {
    private Context context;
    private String documentsDirName;

    /**
     * Creates a RecipeKeeperFileHelper instance for the given android {@link Context} and documents directory name.
     *
     * @param context  the android Context
     * @param documentsDirName  the documents directory name
     */
    public RecipeKeeperFileHelper(Context context, String documentsDirName) {
        this.context = context;
        this.documentsDirName = documentsDirName;
    }

    /**
     * Gets the next available instance id.
     *
     * @param dir  the directory where the new id will be used
     * @return  the next available id value
     */
    public long getNextAvailableId(String dir) {
        long lastId = 0L;
        long id;

        Collection<File> files = getFiles(dir, "*.json");
        for(File file : files) {
            String fileName = file.getName();
            fileName = file.getName().substring(0, fileName.indexOf("."));
            try {
                id = Long.valueOf(fileName);
                if(id > lastId) {
                    lastId = id;
                }
            } catch (NumberFormatException ex) {
                // ignore file if filename before first "." cannot be converted to a long value
            }
        }
        return lastId + 1;
    }

    /**
     * Writes data to a file.
     *
     * @param useExternalStorage  true if the file should be written to external storage, otherwise false
     * @param fileName  the name of the file including any path information relative to the applications root directory
     * @param contents  the contents of the file to be written
     * @return  true if the file was successfully written, otherwise false.
     */
    public boolean writeFile(boolean useExternalStorage, String fileName, byte[] contents) {
        return false;
    }

    public Collection<File> getFiles(String relativePath, final String matchString) throws DataException {
        File targetDir;
        File[] files;
        Collection<File> objects = new ArrayList<>();

        targetDir = getTargetDir(false, relativePath);
        files = targetDir.listFiles(new FilenameFilter() {
            Pattern regex = null;

            @Override
            public boolean accept(File dir, String filename) {
                if(matchString != null && regex == null) {
                    regex = Pattern.compile(matchString);
                }
                return matchString == null || regex.matcher(filename).matches();
            }
        });

        if(files != null) {
            objects.addAll(Arrays.asList(files));
        }

        return objects;
    }

    private String getDocumentsDir() {
        if(documentsDirName == null) {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                documentsDirName = Environment.DIRECTORY_DCIM;
            }
            else {
                documentsDirName = Environment.DIRECTORY_DOCUMENTS;
            }
        }
        return documentsDirName;
    }

    private File getExternalFilesDir() throws DataException {
        File dir = context.getExternalFilesDir(getDocumentsDir());
        if(dir == null) {
            throw new DataException(R.string.exception_externalStorageNotAvailable);
        }
        return dir;
    }

    private File getTargetDir(boolean useExternalStorage, String relativePath) {
        File appDir, targetDir;

        if(!useExternalStorage) {
            appDir = context.getFilesDir();
        }
        else {
            appDir = getExternalFilesDir();
        }

        if(relativePath != null && relativePath.length() > 0) {
            targetDir = new File(appDir, relativePath);
        }
        else {
            targetDir = appDir;
        }
        if(!targetDir.mkdirs() && !targetDir.exists()) {
            throw new DataException(R.string.exception_dirNotCreated,
                    targetDir.getAbsoluteFile().getName());
        }
        return targetDir;
    }
}
