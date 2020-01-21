package com.secvault.android.secvault;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;

public class MainActivity extends AppCompatActivity  implements LifecycleObserver {

    EditText editTextFolderName;
    Button submitHomeScreen;
    Button myFolders;
    Permissions permissions = new Permissions();
    Folders foldersClass = new Folders();

    private String folderName;
    private Intent toRootDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submitHomeScreen = findViewById(R.id.buttonHomeScreenSubmit);
        editTextFolderName = findViewById(R.id.nameInputForCreateFolder);
        myFolders = findViewById(R.id.buttonListFolders);
        toRootDir = new Intent(MainActivity.this, RootDir_Activity.class);

        permissions.checkPermissions(this);

        foldersClass.createExternalFolder();

        setAllButtonsOnClickListener();
    }

    private void setAllButtonsOnClickListener() {

        myFolders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRootDirIntentOnly();
            }
        });

        submitHomeScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                folderName = editTextFolderName.getText().toString();
                isFolderNameEmpty();
            }
        });
    }

    private void isFolderNameEmpty(){
        if(folderName.isEmpty()) {
            Toast.makeText(this, "Enter a new folder name, please", Toast.LENGTH_SHORT).show();
        }else{
            startRootDirIntentWithNewFolderName();
        }
    }

    private void startRootDirIntentOnly(){
        toRootDir.putExtra("folderName","");
        startActivity(toRootDir);
    }

    private void startRootDirIntentWithNewFolderName(){
        toRootDir.putExtra("folderName",folderName);
        startActivity(toRootDir);
    }
}
