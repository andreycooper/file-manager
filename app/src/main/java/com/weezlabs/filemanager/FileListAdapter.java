package com.weezlabs.filemanager;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.weezlabs.filemanager.model.FileItem;

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
        holder.details.setText(fileItem.getDetails());
        holder.date.setText(fileItem.getDate());
        switch (fileItem.getIconType()) {
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
                // TODO: get icon from associated app
                holder.icon.setImageResource(R.drawable.ic_default_file);
                break;
            default:
                break;
        }

        return convertView;
    }

    private SpannableString getBoldString(String title) {
        SpannableString spannableString = new SpannableString(title);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length(), 0);
        return spannableString;
    }

    static class ViewHolder {
        ImageView icon;
        TextView title;
        TextView details;
        TextView date;

        public ViewHolder(View view) {
            icon = (ImageView) view.findViewById(R.id.icon_view);
            title = (TextView) view.findViewById(R.id.title_view);
            details = (TextView) view.findViewById(R.id.details_view);
            date = (TextView) view.findViewById(R.id.date_view);
        }
    }
}
