package com.secvault.android.secvault;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.secvault.android.secvault.cryptography.Decryption;
import com.secvault.android.secvault.cryptography.FileEncryption;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;


public class FilesWithinFolder_Activity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private static final int fileCodeTrue = 1;
    private static final int resultCodeTrue = -1;
    private static final String TAG = "FWF_ACTIVITY";
    private String fullFilePath;

    private  ContentResolver contentResolver;
    private  ArrayAdapter arrayAdapterDirListings;

    private String folderPath;
    private FloatingActionButton buttonAddFile;
    private Uri fileUri;
    private ListView LVFileInFolder;
    private List<String> fileListings;

    private Intent getFromRootDirIntent;
    private FileEncryption fileEncryption_class;
    private EncryptAll encryptAll_class = new EncryptAll();

    File_All file_allClass = new File_All();
    FilesWithinFolder filesWithinFolder = new FilesWithinFolder();
    private Decryption decryption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files_within_folder);
        contentResolver = getApplicationContext().getContentResolver();

        getFromRootDirIntent = getIntent();

        LVFileInFolder = findViewById(R.id.listViewFilesInFolder);
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

/*
    private void setOnClickListenerOnFiles(){
        LVFileInFolder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int fileChosen, long l) {
                getFullFilePath(fileChosen);  //This is to set the global var fullFilePath
                createNewDecryptionObject();
                openFile();

            }
        });
    }

 */

    private void setOnClickListenerOnFiles(){
        LVFileInFolder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int indexOfFilePicked, long l) {
                showPopup(view);
                getFullFilePath(indexOfFilePicked);
            }
        });
    }

    private void showPopup(View v) {
        PopupMenu filePopupMenu = new PopupMenu(this, v);
        MenuInflater inflater = filePopupMenu.getMenuInflater();
        inflater.inflate(R.menu.file_popup_menu, filePopupMenu.getMenu());
        filePopupMenu.setOnMenuItemClickListener(this);
        filePopupMenu.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.encrypt_file:
                Toast.makeText(this, "The app. is encrypting your file", Toast.LENGTH_SHORT).show();
                this.encryptFile();
                return  true;

            case R.id.get_file_and_name:
                Toast.makeText(this, "Go back and open this folder again...for now", Toast.LENGTH_SHORT).show();
                this.createNewDecryptionObject();
                this.openFile();
                return true;
        }
        return false;
    }


    private void listFilesInFolder(){

        fileListings = filesWithinFolder.returnFilesList(getFromRootDirIntent);
        arrayAdapterDirListings = new ArrayAdapter<>(this,
                R.layout.textbox_folder_dir,R.id.textView_dir_string,fileListings);
        LVFileInFolder.setAdapter(arrayAdapterDirListings);
        setOnClickListenerOnFiles();

    }

    private void createNewDecryptionObject(){
        decryption = new Decryption();
    }

    public void openFile(){

        decryption.passFullFilePath(fullFilePath);

    }

    private void getFullFilePath(int fileChosen){

        fullFilePath =  folderPath + "/" + fileListings.get(fileChosen);

    }

    private void getFolderPath(Intent intentFromRootDir) {

        folderPath = intentFromRootDir.getStringExtra("folderPath");
    }


    //This is a plaster :(
    private void encryptFile(){

        fileEncryption_class = new FileEncryption(fullFilePath,
                encryptAll_class.returnRAFFileOfPassedFilePath(fullFilePath));
    }

}