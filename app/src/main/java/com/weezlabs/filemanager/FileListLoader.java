package com.weezlabs.filemanager;

import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.weezlabs.filemanager.model.FileItem;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class FileListLoader extends AsyncTaskLoader<List<FileItem>> {
    public static final String DIRECTORY = "com.weezlabs.filemanager.model.DIRECTORY";
    public static final String EMPTY_STRING = "";
    public static final String PARENT_DIR = ". .";
    public static final String VIDEO_TYPE = "video/";
    public static final String IMAGE_TYPE = "image/";
    public static final String AUDIO_TYPE = "audio/";

    private static final String TAG = FileListLoader.class.getSimpleName();
    private String mDirectory;

    public FileListLoader(Context context, Bundle args) {
        super(context);
        if (args != null) {
            mDirectory = args.getString(DIRECTORY);
        }
        if (TextUtils.isEmpty(mDirectory)) {
            mDirectory = MainActivity.ROOT_DIR;
        }
    }

    @Override
    public List<FileItem> loadInBackground() {
        File directory = new File(mDirectory);
        String lastModifiedDate;
        String details;
        String absolutePath;
        String fileName;

        File[] files = directory.listFiles();
        List<FileItem> dirList = new ArrayList<>();
        List<FileItem> fileList = new ArrayList<>();

        for (File file : files) {
            fileName = file.getName();
            lastModifiedDate = getLastModifiedDate(file);
            absolutePath = file.getAbsolutePath();
            if (file.isDirectory()) {
                details = getSubFilesCount(file);
                dirList.add(new FileItem(fileName, details, lastModifiedDate, absolutePath, FileItem.DIRECTORY));
            } else {
                details = getFileLength(file);
                int fileType = getFileType(file);
                if (fileType == FileItem.IMAGE_FILE) {
                    fileList.add(new FileItem(fileName, details, lastModifiedDate, absolutePath, fileType, getImageIdFromMediaStore(file)));
                } else {
                    fileList.add(new FileItem(fileName, details, lastModifiedDate, absolutePath, fileType));
                }
            }
        }
        Collections.sort(dirList);
        Collections.sort(fileList);
        dirList.addAll(fileList);
        if (!directory.getPath().equalsIgnoreCase(MainActivity.ROOT_DIR)) {
            dirList.add(MainActivity.FIRST_POSITION,
                    new FileItem(EMPTY_STRING, PARENT_DIR, EMPTY_STRING, directory.getParent(), FileItem.DIRECTORY_UP));
        }
        return dirList;
    }

    private int getFileType(File file) {
        String type = getMimeType(file);
        if (type == null) {
            return FileItem.FILE;
        } else if (type.startsWith(VIDEO_TYPE)) {
            return FileItem.VIDEO_FILE;
        } else if (type.startsWith(IMAGE_TYPE)) {
            return FileItem.IMAGE_FILE;
        } else if (type.startsWith(AUDIO_TYPE)) {
            return FileItem.AUDIO_FILE;
        } else {
            return FileItem.FILE;
        }

    }

    private String getMimeType(File file) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
        if (extension != null) {
            MimeTypeMap map = MimeTypeMap.getSingleton();
            type = map.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    private long getImageIdFromMediaStore(File file) {
        return getImageIdFromMediaStore(file.getAbsolutePath(), getContext().getContentResolver());
    }

    private long getImageIdFromMediaStore(String filePath, ContentResolver contentResolver) {
        long imageId = FileItem.INCORRECT_ID;
        Log.d(TAG, "Loading file " + filePath);

        Uri imagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Log.d(TAG, "imagesUri = " + imagesUri.toString());

        String[] projection = {MediaStore.Images.ImageColumns._ID};

        Cursor cursor = contentResolver.query(imagesUri, projection, MediaStore.Images.ImageColumns.DATA + " LIKE ?", new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(projection[0]);
            imageId = cursor.getLong(columnIndex);
            Log.d(TAG, "Image ID is " + imageId);
            cursor.close();
        }

        return imageId;
    }

    /**
     * Gets the MediaStore video ID of a given file on external storage
     *
     * @param filePath        The path (on external storage) of the file to resolve the ID of
     * @param contentResolver The content resolver to use to perform the query.
     * @return the video ID as a long
     */
    private long getVideoIdFromMediaStore(String filePath, ContentResolver contentResolver) {
        long videoId = FileItem.INCORRECT_ID;
        Log.d(TAG, "Loading file " + filePath);

        Uri videosUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Log.d(TAG, "videosUri = " + videosUri.toString());

        String[] projection = {MediaStore.Video.VideoColumns._ID};

        Cursor cursor = contentResolver.query(videosUri, projection, MediaStore.Video.VideoColumns.DATA + " LIKE ?", new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(projection[0]);
            videoId = cursor.getLong(columnIndex);
            Log.d(TAG, "Video ID is " + videoId);
            cursor.close();
        }

        return videoId;
    }

    private String getLastModifiedDate(File file) {
        Date lastModifiedDate = new Date(file.lastModified());
        DateFormat dateFormatter = DateFormat.getDateInstance();
        return dateFormatter.format(lastModifiedDate);
    }

    private String getSubFilesCount(File file) {
        File[] subFiles = file.listFiles();
        int countFiles = 0;
        if (subFiles != null) {
            countFiles = subFiles.length;
        }
        return countFiles == 1 ? String.valueOf(countFiles) + getContext().getString(R.string.one_item) :
                String.valueOf(countFiles) + getContext().getString(R.string.many_items);
    }

    private String getFileLength(File file) {
        return file.length() == 1 ? String.valueOf(file.length()) + getContext().getString(R.string.one_byte) :
                String.valueOf(file.length()) + getContext().getString(R.string.many_bytes);
    }


}
