package com.secvault.android.secvault;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

//https://www.youtube.com/watch?v=ARezg1D9Zd0

public class PasswordDialogFragment extends DialogFragment {

    private EditText editTextPassword;
    private String passwordFromUser;


    NoticeDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View password_dialog_view = inflater.inflate(R.layout.password_dialog,null);

        editTextPassword = password_dialog_view.findViewById(R.id.crypto_password);

        builder.setView(password_dialog_view)
                .setTitle(R.string.password_dialog_title)
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(R.string.enter_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        passwordFromUser = editTextPassword.getText().toString();
                        listener.sendPassword(passwordFromUser);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (NoticeDialogListener) context;

        } catch (ClassCastException e) { //This means that once you implement this into a class, you have to add NoticeDialogListener
            throw new ClassCastException(context.toString() + " Must implement NoticeDialogListener");
        }
    }

    public interface NoticeDialogListener {
         void sendPassword(String password);
    }

}
