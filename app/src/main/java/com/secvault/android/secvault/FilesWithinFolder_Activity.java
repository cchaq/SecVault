package com.secvault.android.secvault;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.secvault.android.secvault.cryptography.Decryption;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;


public class FilesWithinFolder_Activity extends AppCompatActivity {

    private static final int fileCodeTrue = 1;
    private static final int resultCodeTrue = -1;
    private static final String TAG = "FWF_ACTIVITY";
    private String fullFilePath;

    private  ContentResolver contentResolver;
    private  ArrayAdapter arrayAdapterDirListings;

    private String folderPath;
    private FloatingActionButton buttonAddFile;
    private Uri fileUri;
    private ListView LVfileInFolder;
    private List<String> fileListings;

    private Intent getFromRootDirIntent;
    private Intent openTheFile;

    File_All file_allClass = new File_All();
    FilesWithinFolder filesWithinFolder = new FilesWithinFolder();
    private Decryption decryption = new Decryption();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files_within_folder);
        contentResolver = getApplicationContext().getContentResolver();

        getFromRootDirIntent = getIntent();

        LVfileInFolder = findViewById(R.id.listViewFilesInFolder);
        listFilesInFolder();

        getFolderPath(getFromRootDirIntent);
        setFABtoLoadFile();

        Log.i(TAG,"ON CREATE ACTIVATED!");

    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(TAG,"On resume life cycle activated! ");
        arrayAdapterDirListings.clear();
        listFilesInFolder();
    }


    private void setFABtoLoadFile(){
        buttonAddFile = findViewById(R.id.FAB_add_file);

        buttonAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FABtoLoadFile();
            }
        });
    }

    // https://developer.android.com/training/basics/intents/result
    private void FABtoLoadFile(){

        Intent copyToFile = new Intent(Intent.ACTION_GET_CONTENT);
        copyToFile.setType("*/*");
        startActivityForResult(copyToFile, fileCodeTrue);
    }


    private void setOnClickListenerOnFiles(){
        LVfileInFolder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int fileChosen, long l) {
                getFullFilePath(fileChosen);  //This is to set the global var fullFilePath
                openFile();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent fileSelected){

        if(requestCode == fileCodeTrue) {
            if(resultCode == resultCodeTrue) {
                fileUri = fileSelected.getData();
                file_allClass.passDestFolder(folderPath);
                file_allClass.fromFileGetRealData(fileUri, contentResolver);
            }
        }
    }

    private void listFilesInFolder(){

        fileListings = filesWithinFolder.returnFilesList(getFromRootDirIntent);
        arrayAdapterDirListings = new ArrayAdapter<>(this,
                R.layout.textbox_folder_dir,R.id.textView_dir_string,fileListings);
        LVfileInFolder.setAdapter(arrayAdapterDirListings);
        setOnClickListenerOnFiles();

    }


    public void openFile(){

        decryption.passFullFilePath(fullFilePath);

        /*
        String mimeType = file_allClass.getMimeType(fullFilePath);
        openTheFile = new Intent(Intent.ACTION_VIEW);
        openTheFile.setDataAndType(Uri.parse(fullFilePath), mimeType);
        openTheFile.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivity(openTheFile);

         */

    }

    private void getFullFilePath(int fileChosen){

        fullFilePath =  folderPath + "/" + fileListings.get(fileChosen);

    }

    private void getFolderPath(Intent intentFromRootDir) {

        folderPath = intentFromRootDir.getStringExtra("folderPath");
    }

}