package com.weezlabs.filemanager.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;

public class FileOpener {

    public static final String ALL_TYPE = "*/*";

    public static void openFile(Context context, String filePath) throws ActivityNotFoundException {
        File file = new File(filePath);
        openFile(context, file);
    }

    public static void openFile(Context context, File file) throws ActivityNotFoundException {
        Intent intent = getIntentWithDataAndType(file);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static Intent getIntentWithDataAndType(String filePath) {
        File file = new File(filePath);
        return getIntentWithDataAndType(file);
    }

    public static Intent getIntentWithDataAndType(File file) {
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String type = map.getMimeTypeFromExtension(ext);

        if (type == null) {
            type = ALL_TYPE;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.fromFile(file);

        intent.setDataAndType(data, type);
        return intent;
    }
}