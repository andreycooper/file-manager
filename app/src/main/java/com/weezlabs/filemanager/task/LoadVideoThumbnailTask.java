package com.weezlabs.filemanager.task;


import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;

import com.weezlabs.filemanager.model.FileItem;
import com.weezlabs.filemanager.util.ViewHolder;

public class LoadVideoThumbnailTask extends LoadDrawableTask {
    public LoadVideoThumbnailTask(Context context, ViewHolder holder, FileItem fileItem, int position) {
        super(context, holder, fileItem, position);
    }

    @Override
    protected Drawable doInBackground(Void... params) {
        return new BitmapDrawable(mContext.getResources(), getVideoThumbnailDrawable(mFileItem.getFileId()));
    }

    private Bitmap getVideoThumbnailDrawable(long videoId) {
        ContentResolver contentResolver = mContext.getContentResolver();
        Bitmap bitmap = null;

        if (videoId != FileItem.INCORRECT_ID) {
            bitmap = MediaStore.Video.Thumbnails.getThumbnail(contentResolver, videoId, MediaStore.Video.Thumbnails.MICRO_KIND, null);
        }

        return bitmap;
    }
}
