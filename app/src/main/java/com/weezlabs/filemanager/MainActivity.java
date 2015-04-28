package com.weezlabs.filemanager;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.weezlabs.filemanager.model.Item;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private String mRootDir = Environment.getExternalStorageDirectory().getPath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView filesListView = (ListView) findViewById(R.id.files_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(this, getString(R.string.toast_settings_click), Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // TODO: remove method to AsyncTask
    private void fill(File directory) {
        String lastModifiedDate;
        String details;
        String absolutePath;
        String fileName;

        File[] files = directory.listFiles();
        setTitle(directory.getName());
        List<Item> dirList = new ArrayList<>();
        List<Item> fileList = new ArrayList<>();

        for (File file : files) {
            fileName = file.getName();
            lastModifiedDate = getLastModifiedDate(file);
            absolutePath = file.getAbsolutePath();
            if (file.isDirectory()) {
                details = getSubFilesCount(file);
                dirList.add(new Item(fileName, details, lastModifiedDate, absolutePath, "directory_icon"));
            } else {
                details = getFileLenght(file);
                fileList.add(new Item(fileName, details, lastModifiedDate, absolutePath, "file_icon"));
            }
        }
        Collections.sort(dirList);
        Collections.sort(fileList);
        dirList.addAll(fileList);
        if (!directory.getName().equalsIgnoreCase(mRootDir)){
            // TODO: add directory_up item to first position in list
        }
        // TODO: create Adapter

        // TODO: set Adapter to listview
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

    private String getFileLenght(File file) {
        return file.length() + " Bytes";
    }
}
