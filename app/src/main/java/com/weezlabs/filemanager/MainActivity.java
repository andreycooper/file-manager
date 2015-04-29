package com.weezlabs.filemanager;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.weezlabs.filemanager.model.FileItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<FileItem>> {
    public static final String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();
    private static final int FILE_LIST_LOADER = 15051982;

    private String mCurrentDir = ROOT_DIR;
    private ListView mFilesListView;
    private FileListAdapter mFileListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFileListAdapter = new FileListAdapter(this, new ArrayList<FileItem>());
        mFilesListView = (ListView) findViewById(R.id.files_list);

        final LoaderManager.LoaderCallbacks<List<FileItem>> callbacks = this;
        mFilesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: update mCurrentDir and restart Loader
                Bundle args = new Bundle();
                args.putString(FileListLoader.DIRECTORY, mCurrentDir);
                getLoaderManager().restartLoader(FILE_LIST_LOADER, args, callbacks);
            }
        });

        Bundle args = new Bundle();
        args.putString(FileListLoader.DIRECTORY, mCurrentDir);
        getLoaderManager().initLoader(FILE_LIST_LOADER, args, this);
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
        // TODO: update mCurrentDir to parent directoryand restart Loader

        super.onBackPressed();
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
            mFileListAdapter.clear();
            mFileListAdapter.addAll(data);
            mFileListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<FileItem>> loader) {

    }
}
