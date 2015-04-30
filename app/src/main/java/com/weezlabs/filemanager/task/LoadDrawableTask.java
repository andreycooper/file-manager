package com.weezlabs.filemanager.task;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.weezlabs.filemanager.R;
import com.weezlabs.filemanager.model.FileItem;
import com.weezlabs.filemanager.util.ViewHolder;

import java.lang.ref.WeakReference;

abstract public class LoadDrawableTask extends AsyncTask<Void, Void, Drawable> {

    protected Context mContext;
    protected WeakReference<ViewHolder> mHolderWeakReference;
    protected FileItem mFileItem;
    protected int mPosition;

    public LoadDrawableTask(Context context, ViewHolder holder, FileItem fileItem, int position) {
        mContext = context.getApplicationContext();
        mHolderWeakReference = new WeakReference<>(holder);
        mFileItem = fileItem;
        mPosition = position;
    }

    @Override
    protected void onPostExecute(Drawable icon) {
        ViewHolder holder = mHolderWeakReference.get();
        if (holder != null && holder.position == mPosition) {
            if (icon != null) {
                holder.icon.setImageDrawable(icon);
            } else {
                holder.icon.setImageResource(R.drawable.ic_default_file);
            }
        }
    }
}