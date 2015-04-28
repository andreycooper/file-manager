package com.weezlabs.filemanager.model;


public class Item implements Comparable<Item> {
    String mName;
    String mDetails;
    String mDate;
    String mPath;
    String mIcon;

    public Item(String name, String details, String date, String path, String icon) {
        mName = name;
        mDetails = details;
        mDate = date;
        mPath = path;
        mIcon = icon;
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

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Item{");
        sb.append("mName='").append(mName).append('\'');
        sb.append(", mDetails='").append(mDetails).append('\'');
        sb.append(", mPath='").append(mPath).append('\'');
        sb.append(", mDate='").append(mDate).append('\'');
        sb.append(", mIcon='").append(mIcon).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(Item another) {
        if (mName != null) {
            return mName.toLowerCase().compareTo(another.getName().toLowerCase());
        } else {
            throw new IllegalArgumentException();
        }
    }

}
