package com.weezlabs.filemanager;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.squareup.picasso.Picasso;
import com.weezlabs.filemanager.model.FileItem;
import com.weezlabs.filemanager.task.LoadAppIconTask;
import com.weezlabs.filemanager.task.LoadImageThumbnailTask;
import com.weezlabs.filemanager.util.ViewHolder;

import java.io.File;
import java.util.List;


public class FileListAdapter extends ArrayAdapter<FileItem> {
    LayoutInflater mLayoutInflater;

    public FileListAdapter(Context context, List<FileItem> objects) {
        super(context, R.layout.item_row, objects);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_row, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final FileItem fileItem = getItem(position);
        holder.position = position;
        fillRow(holder, fileItem, position);

        return convertView;
    }

    private void fillRow(final ViewHolder holder, final FileItem fileItem, int position) {
        holder.details.setText(fileItem.getDetails());
        holder.date.setText(fileItem.getDate());
        switch (fileItem.getFileType()) {

            case FileItem.DIRECTORY_UP:
                holder.title.setText(fileItem.getName());
                holder.details.setText(getBoldString(fileItem.getDetails()));
                holder.icon.setImageResource(R.drawable.ic_folder_up);
                break;

            case FileItem.DIRECTORY:
                // SpannableString works better than setTypeface()
                holder.title.setText(getBoldString(fileItem.getName()));
                holder.icon.setImageResource(R.drawable.ic_folder);
                break;

            case FileItem.FILE:
                holder.title.setText(fileItem.getName());
                new LoadAppIconTask(getContext(), holder, fileItem, position)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;

            case FileItem.IMAGE_FILE:
                holder.title.setText(fileItem.getName());
                if (fileItem.getFileId() != FileItem.INCORRECT_ID) {
                    new LoadImageThumbnailTask(getContext(), holder, fileItem, position)
                            .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    Picasso.with(getContext())
                            .load(new File(fileItem.getPath()))
                            .fit()
                            .centerCrop()
                            .placeholder(R.drawable.ic_image_file)
                            .into(holder.icon);
                }
                break;

            case FileItem.VIDEO_FILE:
                holder.title.setText(fileItem.getName());
                if (fileItem.getFileId() != FileItem.INCORRECT_ID) {
                    // TODO: start LoadVideoThumbnailTask like LoadImageThumbnailTask
                } else {
                    holder.icon.setImageResource(R.drawable.ic_video_file);
                }
                break;

            case FileItem.AUDIO_FILE:
                holder.title.setText(fileItem.getName());
                holder.icon.setImageResource(R.drawable.ic_audio_file);

            default:
                break;
        }
    }

    private SpannableString getBoldString(String title) {
        SpannableString spannableString = new SpannableString(title);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length(), 0);
        return spannableString;
    }

}
