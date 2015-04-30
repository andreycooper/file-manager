package com.weezlabs.filemanager.task;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;

import com.weezlabs.filemanager.model.FileItem;
import com.weezlabs.filemanager.util.ViewHolder;


public class LoadImageThumbnailTask extends LoadDrawableTask {


    public LoadImageThumbnailTask(Context context, ViewHolder holder, FileItem fileItem, int position) {
        super(context, holder, fileItem, position);
    }

    @Override
    protected Drawable doInBackground(Void... params) {
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
