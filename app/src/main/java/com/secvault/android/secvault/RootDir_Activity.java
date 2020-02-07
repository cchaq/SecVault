package com.secvault.android.secvault;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class RootDir_Activity extends AppCompatActivity {

    private static final String TAG = "RootDir_Class";
    Folders folders = new Folders();
    ListView listViewRootDir;
    List<String> dirListings;

    private Intent fromMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_dir);
        listFolders();
        getIntentFromMainActivity();
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(TAG,"On resume life cycle activated! ");
        dirListings.clear();
        listFolders();
    }

    private void getIntentFromMainActivity(){
        fromMainActivity = getIntent();
        hasFolderBeenCreated(fromMainActivity.getStringExtra("folderName"));
    }

    private void hasFolderBeenCreated(String folderName) {

        if (!folderName.isEmpty()){
            folders.createFolder(this, folderName);
            Toast.makeText(this, "Folder has been created", Toast.LENGTH_SHORT).show();
        }
    }

    //listView vs recyclerView vs adapterView??
    private void listFolders(){

        listViewRootDir = findViewById(R.id.listViewRootDir);
        dirListings = folders.returnListOfDirs();

        ArrayAdapter arrayAdapterDirListings = new ArrayAdapter<>(this,
                R.layout.textbox_folder_dir,R.id.textView_dir_string,dirListings);
        listViewRootDir.setAdapter(arrayAdapterDirListings);
        setFolderPathOnClickListener();
    }

    private void setFolderPathOnClickListener(){
        listViewRootDir.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int arrayIndex, long l) {
                Log.i(TAG,arrayIndex + "= i and  j = " + l);
                Log.i(TAG,dirListings.get(arrayIndex));
                goIntoTheFolder(dirListings.get(arrayIndex));
            }
        });
    }

    private void goIntoTheFolder(String folderPath){
        Intent goToFilesActivity = new Intent(this, FilesWithinFolder_Activity.class);
        goToFilesActivity.putExtra("folderPath",folderPath);
        startActivity(goToFilesActivity);
    }
}
