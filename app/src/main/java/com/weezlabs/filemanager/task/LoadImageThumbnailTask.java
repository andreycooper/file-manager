package com.weezlabs.filemanager.task;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.util.Log;

import com.weezlabs.filemanager.model.FileItem;
import com.weezlabs.filemanager.util.ViewHolder;


public class LoadImageThumbnailTask extends LoadDrawableTask {


    private static final String LOG_TAG = LoadImageThumbnailTask.class.getSimpleName();

    public LoadImageThumbnailTask(Context context, ViewHolder holder, FileItem fileItem, int position) {
        super(context, holder, fileItem, position);
    }

    @Override
    protected Drawable doInBackground(Void... params) {
        Log.d(LOG_TAG, "start loading thumb in AsyncTask");
        return new BitmapDrawable(mContext.getResources(), getImageThumbnailDrawable(mFileItem.getFileId()));
    }

    private Bitmap getImageThumbnailDrawable(long imageId) {
        ContentResolver contentResolver = mContext.getContentResolver();
        Bitmap bitmap = null;

        if (imageId != FileItem.INCORRECT_ID) {
            bitmap = MediaStore.Images.Thumbnails.getThumbnail(contentResolver, imageId, MediaStore.Images.Thumbnails.MICRO_KIND, null);
        }

        return bitmap;
    }
}
