package com.weezlabs.filemanager;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;

import com.weezlabs.filemanager.model.FileItem;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class FileListLoader extends AsyncTaskLoader<List<FileItem>> {
    public static final String DIRECTORY = "com.weezlabs.filemanager.model.DIRECTORY";
    private String mDirectory;

    public FileListLoader(Context context, Bundle args) {
        super(context);
        if (args != null) {
            mDirectory = args.getString(DIRECTORY);
        }
        if (TextUtils.isEmpty(mDirectory)) {
            mDirectory = Environment.getExternalStorageDirectory().getPath();
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
                dirList.add(new FileItem(fileName, details, lastModifiedDate, absolutePath, "directory_icon"));
            } else {
                details = getFileLength(file);
                fileList.add(new FileItem(fileName, details, lastModifiedDate, absolutePath, "file_icon"));
            }
        }
        Collections.sort(dirList);
        Collections.sort(fileList);
        dirList.addAll(fileList);
        if (!directory.getName().equalsIgnoreCase(MainActivity.ROOT_DIR)) {
            // TODO: add directory_up item to first position in list
        }
        return dirList;
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
        if (countFiles == 1) {
            return String.valueOf(countFiles) + " Item";
        } else {
            return String.valueOf(countFiles) + " Items";
        }
    }

    private String getFileLength(File file) {
        return file.length() + " Bytes";
    }
}
