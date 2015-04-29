package com.weezlabs.filemanager.model;


public class FileItem implements Comparable<FileItem> {
    public static final int DIRECTORY_UP = 1;
    public static final int DIRECTORY = 2;
    public static final int FILE = 3;
    String mName;
    String mDetails;
    String mDate;
    String mPath;
    int mIconType;

    public FileItem(String name, String details, String date, String path, int iconType) {
        mName = name;
        mDetails = details;
        mDate = date;
        mPath = path;
        mIconType = iconType;
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

    public int getIconType() {
        return mIconType;
    }

    public void setIconType(int iconType) {
        mIconType = iconType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FileItem{");
        sb.append("mName='").append(mName).append('\'');
        sb.append(", mDetails='").append(mDetails).append('\'');
        sb.append(", mPath='").append(mPath).append('\'');
        sb.append(", mDate='").append(mDate).append('\'');
        sb.append(", mIconType='").append(mIconType).append('\'');
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
