package com.weezlabs.filemanager;

import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.Loader;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.weezlabs.filemanager.model.FileItem;
import com.weezlabs.filemanager.util.FileOpener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<FileItem>> {
    public static final String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
    public static final int FIRST_POSITION = 0;

    private static final String ROOT_FOLDER_LABEL = "/";
    private static final int FILE_LIST_LOADER = 15051982;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private FileListAdapter mFileListAdapter;
    private List<FileItem> mFileItemList;
    private ListView mFilesListView;

    private HashMap<String, Integer> mPositionsMap = new HashMap<>();
    private String mCurrentDir = ROOT_DIR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFileItemList = new ArrayList<>();
        mFileListAdapter = new FileListAdapter(this, mFileItemList);
        mFilesListView = (ListView) findViewById(R.id.files_list);
        mFilesListView.setAdapter(mFileListAdapter);

        mFilesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileItem fileItem = mFileItemList.get(position);
                switch (mFileItemList.get(position).getFileType()) {
                    case FileItem.DIRECTORY_UP:
                        updateState(fileItem.getPath(), position);
                        useLoader(mCurrentDir);
                        break;

                    case FileItem.DIRECTORY:
                        updateState(fileItem.getPath(), position);
                        useLoader(mCurrentDir);
                        break;

                    default:
                        try {
                            FileOpener.openFile(getApplicationContext(), fileItem.getPath());
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                            Log.e(LOG_TAG, "Cant start app for file " + fileItem.getName());
                        }
                        break;
                }
            }
        });

        updateState(ROOT_DIR, FIRST_POSITION);
        useLoader(mCurrentDir);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFileItemList.isEmpty()) {
            useLoader(mCurrentDir);
        }
    }

    private void updateState(String directory, int position) {
        mPositionsMap.put(mCurrentDir, position);
        mCurrentDir = directory;
    }

    private void useLoader(String directory) {
        setTitle(getReadableTitle(directory));
        Bundle args = new Bundle();
        args.putString(FileListLoader.DIRECTORY, directory);
        Loader<List<FileItem>> loader = getLoaderManager().getLoader(FILE_LIST_LOADER);
        if (loader == null) {
            loader = getLoaderManager().initLoader(FILE_LIST_LOADER, args, this);
        } else {
            loader = getLoaderManager().restartLoader(FILE_LIST_LOADER, args, this);
        }
        loader.forceLoad();
    }

    private String getReadableTitle(String directory) {
        String title = directory.substring(ROOT_DIR.length(), directory.length());
        return TextUtils.isEmpty(title) ? ROOT_FOLDER_LABEL : title;
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

    @Override
    public void onBackPressed() {
        File directory = new File(mCurrentDir);
        if (!directory.getPath().equalsIgnoreCase(ROOT_DIR)) {
            mCurrentDir = directory.getParent();
            useLoader(mCurrentDir);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public Loader<List<FileItem>> onCreateLoader(int id, Bundle args) {
        Loader<List<FileItem>> loader = null;
        if (id == FILE_LIST_LOADER) {
            loader = new FileListLoader(this, args);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<FileItem>> loader, List<FileItem> data) {
        if (loader.getId() == FILE_LIST_LOADER) {
            mFileItemList.clear();
            mFileItemList.addAll(data);
            mFileListAdapter.notifyDataSetChanged();
            if (mPositionsMap.get(mCurrentDir) != null) {
                mFilesListView.setSelection(mPositionsMap.get(mCurrentDir));
            } else {
                mFilesListView.setSelection(FIRST_POSITION);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<FileItem>> loader) {

    }
}
