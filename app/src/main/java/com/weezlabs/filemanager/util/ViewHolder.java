package com.weezlabs.filemanager.util;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.weezlabs.filemanager.R;


public class ViewHolder {
    public ImageView icon;
    public TextView title;
    public TextView details;
    public TextView date;
    public int position;

    public ViewHolder(View view) {
        icon = (ImageView) view.findViewById(R.id.icon_view);
        title = (TextView) view.findViewById(R.id.title_view);
        details = (TextView) view.findViewById(R.id.details_view);
        date = (TextView) view.findViewById(R.id.date_view);
    }
}
