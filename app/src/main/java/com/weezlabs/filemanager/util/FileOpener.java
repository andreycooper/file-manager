package com.weezlabs.filemanager.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;

public class FileOpener {

    public static final String ALL_APPS = "*/*";

    public static void openFile(Context context, String filePath) throws ActivityNotFoundException {
        File file = new File(filePath);
        openFile(context, file);
    }

    public static void openFile(Context context, File file) throws ActivityNotFoundException {
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String type = map.getMimeTypeFromExtension(ext);

        if (type == null) {
            type = ALL_APPS;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.fromFile(file);

        intent.setDataAndType(data, type);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}