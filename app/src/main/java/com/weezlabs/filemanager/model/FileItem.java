package com.weezlabs.filemanager.model;


import android.net.Uri;

public class FileItem implements Comparable<FileItem> {
    public static final int DIRECTORY_UP = 1;
    public static final int DIRECTORY = 2;
    public static final int FILE = 3;
    public static final int VIDEO_FILE = 4;
    public static final int IMAGE_FILE = 5;
    public static final int AUDIO_FILE = 6;

    private String mName;
    private String mDetails;
    private String mDate;
    private String mPath;
    private int mFileType;
    private Uri mThumbnailUri;

    public FileItem(String name, String details, String date, String path, int fileType) {
        mName = name;
        mDetails = details;
        mDate = date;
        mPath = path;
        mFileType = fileType;
        mThumbnailUri = null;
    }

    public FileItem(String name, String details, String date, String path, int fileType, Uri thumbnailUri) {
        this(name, details, date, path, fileType);
        mThumbnailUri = thumbnailUri;
    }

    public String getName() {
        return mName;
    }

    public String getDetails() {
        return mDetails;
    }


    public String getPath() {
        return mPath;
    }


    public String getDate() {
        return mDate;
    }


    public int getFileType() {
        return mFileType;
    }


    public Uri getThumbnailUri() {
        return mThumbnailUri;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FileItem{");
        sb.append("mName='").append(mName).append('\'');
        sb.append(", mDetails='").append(mDetails).append('\'');
        sb.append(", mPath='").append(mPath).append('\'');
        sb.append(", mDate='").append(mDate).append('\'');
        sb.append(", mFileType='").append(mFileType).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(FileItem another) {
        if (mName != null) {
            return mName.toLowerCase().compareTo(another.getName().toLowerCase());
        } else {
            throw new IllegalArgumentException();
        }
    }

}
