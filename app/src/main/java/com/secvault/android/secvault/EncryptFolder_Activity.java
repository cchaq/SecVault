package com.secvault.android.secvault;

import android.content.Intent;
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

import com.secvault.android.secvault.cryptography.FileDecryption;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class EncryptFolder_Activity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, PasswordDialogFragment.NoticeDialogListener {

    private static final String TAG = "Encrypt folder class";
    private static final String decryptToast = "File is being decrypted, go to the decrypted folder to see the file.";

    private  ArrayAdapter arrayAdapterDirListings;
    private Intent getFromRootDirIntent;
    private List<String> fileListings;
    private ListView LVFileInFolder;
    private String folderPath;
    private String fullFilePath;

    private FilesWithinFolder filesWithinFolder = new FilesWithinFolder();
    private FileDecryption fileDecryption_class;
    private EncryptAll encryptAll_class = new EncryptAll();
    private String passwordFromUser;
    private PasswordDialogFragment passwordDialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files_within_folder);
        LVFileInFolder = findViewById(R.id.listViewFilesInFolder);
        getFromRootDirIntent = getIntent();

        this.listFilesInFolder();
        this.getFolderPath(getFromRootDirIntent);

    }


    private void listFilesInFolder(){

        fileListings = filesWithinFolder.returnFilesList(getFromRootDirIntent);
        arrayAdapterDirListings = new ArrayAdapter<>(this,
                R.layout.textbox_folder_dir,R.id.textView_dir_string,fileListings);
        LVFileInFolder.setAdapter(arrayAdapterDirListings);
        this.setOnClickListenerOnFiles();

    }


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
        inflater.inflate(R.menu.decrypt_menu, filePopupMenu.getMenu());
        filePopupMenu.setOnMenuItemClickListener(this);
        filePopupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.decryptFile:
                this.passwordDialog();
                Toast.makeText(this, decryptToast, Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    private void getFolderPath(Intent intentFromRootDir) {

        folderPath = intentFromRootDir.getStringExtra("folderPath");
    }

    private void getFullFilePath(int fileChosen){

        fullFilePath =  folderPath + "/" + fileListings.get(fileChosen);

    }

    private void passwordDialog(){
        passwordDialogFragment = new PasswordDialogFragment();
        passwordDialogFragment.show(getSupportFragmentManager(), "Password dialog");
    }

    private void decryptFile(){

        fileDecryption_class = new FileDecryption();

        try {
            fileDecryption_class.decryptFile(fullFilePath,
                    encryptAll_class.returnRAFFileOfPassedFilePath(fullFilePath),passwordFromUser);

        }catch (Exception e){
            Log.i(TAG,e.toString());
        }
    }

    @Override
    public void sendPassword(String password) {
        passwordFromUser = password;
        this.decryptFile();
    }
}
