package com.weezlabs.filemanager.task;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.weezlabs.filemanager.model.FileItem;
import com.weezlabs.filemanager.util.ViewHolder;

import java.io.File;

/**
 * Created by student on 30.04.15.
 */
public class LoadAppIconTask extends LoadDrawableTask {

    private static final String LOG_TAG = LoadAppIconTask.class.getSimpleName();

    public LoadAppIconTask(Context context, ViewHolder holder, FileItem fileItem, int position) {
        super(context, holder, fileItem, position);
    }


    private Drawable getPreferredActivityIcon(Context context, String filePath) {
        Drawable icon = null;
        final PackageManager pm = context.getPackageManager();
        try {
            Intent intent = getIntentWithDataAndType(filePath);
            if (intent != null) {
                icon = pm.getActivityIcon(getIntentWithDataAndType(filePath));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Component name not found!");
        }
        return icon;
    }

    private Intent getIntentWithDataAndType(String filePath) {
        File file = new File(filePath);
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String type = map.getMimeTypeFromExtension(ext);

        if (type == null) {
            return null;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.fromFile(file);

        intent.setDataAndType(data, type);
        return intent;
    }

    @Override
    protected Drawable doInBackground(Void... params) {
        return getPreferredActivityIcon(mContext, mFileItem.getPath());
    }
}
