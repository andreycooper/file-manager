package com.weezlabs.filemanager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.weezlabs.filemanager.model.FileItem;
import com.weezlabs.filemanager.util.FileOpener;

import java.io.File;
import java.util.List;


public class FileListAdapter extends ArrayAdapter<FileItem> {
    private static final String LOG_TAG = FileListAdapter.class.getSimpleName();
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
        fillRow(holder, fileItem);

        return convertView;
    }

    private void fillRow(final ViewHolder holder, final FileItem fileItem) {
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
                new LoadIconTask(getContext(), fileItem.getPath(), holder, getPosition(fileItem))
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
                break;

            case FileItem.IMAGE_FILE:
                holder.title.setText(fileItem.getName());
                if (fileItem.getThumbnailUri() != null) {
                    Picasso.with(getContext())
                            .load(fileItem.getThumbnailUri())
                            .fit()
                            .centerCrop()
                            .placeholder(R.drawable.ic_image_file)
                            .into(holder.icon);
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
                // TODO: get thumbnailUri for video
                holder.icon.setImageResource(R.drawable.ic_video_file);
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

    static class LoadIconTask extends AsyncTask<Void, Void, Drawable> {

        private Context mContext;
        private String mFilePath;
        // TODO: maybe save holder in final WeakReference<ViewHolder> field?
        private ViewHolder holder;
        private int mPosition;

        public LoadIconTask(Context context, String filePath, ViewHolder holder, int position) {
            mContext = context;
            mFilePath = filePath;
            this.holder = holder;
            mPosition = position;
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

        private Intent getIntentWithDataAndType(String filePath){
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
            return getPreferredActivityIcon(mContext, mFilePath);
        }

        @Override
        protected void onPostExecute(Drawable icon) {
            if (holder.position == mPosition) {
                if (icon != null) {
                    holder.icon.setImageDrawable(icon);
                } else {
                    holder.icon.setImageResource(R.drawable.ic_default_file);
                }
            }
        }
    }

    static class ViewHolder {
        ImageView icon;
        TextView title;
        TextView details;
        TextView date;
        int position;

        public ViewHolder(View view) {
            icon = (ImageView) view.findViewById(R.id.icon_view);
            title = (TextView) view.findViewById(R.id.title_view);
            details = (TextView) view.findViewById(R.id.details_view);
            date = (TextView) view.findViewById(R.id.date_view);
        }
    }
}
