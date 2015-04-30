package com.weezlabs.filemanager.model;


public class FileItem implements Comparable<FileItem> {
    public static final int DIRECTORY_UP = 1;
    public static final int DIRECTORY = 2;
    public static final int FILE = 3;
    public static final int VIDEO_FILE = 4;
    public static final int IMAGE_FILE = 5;
    public static final int AUDIO_FILE = 6;
    public static final int INCORRECT_ID = -1;

    String mName;
    String mDetails;
    String mDate;
    String mPath;
    int mFileType;
    long mFileId;

    public FileItem(String name, String details, String date, String path, int fileType) {
        mName = name;
        mDetails = details;
        mDate = date;
        mPath = path;
        mFileType = fileType;
        mFileId = INCORRECT_ID;
    }

    public FileItem(String name, String details, String date, String path, int fileType, long fileId) {
        this(name, details, date, path, fileType);
        mFileId = fileId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDetails() {
        return mDetails;
    }

    public void setDetails(String details) {
        mDetails = details;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public int getFileType() {
        return mFileType;
    }

    public void setFileType(int fileType) {
        mFileType = fileType;
    }

    public long getFileId() {
        return mFileId;
    }

    public void setFileId(long fileId) {
        mFileId = fileId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FileItem{");
        sb.append("mName='").append(mName).append('\'');
        sb.append(", mDetails='").append(mDetails).append('\'');
        sb.append(", mDate='").append(mDate).append('\'');
        sb.append(", mPath='").append(mPath).append('\'');
        sb.append(", mFileType=").append(mFileType);
        sb.append(", mFileId=").append(mFileId);
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
