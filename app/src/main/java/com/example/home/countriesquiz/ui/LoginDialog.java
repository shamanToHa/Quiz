package com.example.home.countriesquiz.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.EditText;

import com.example.home.countriesquiz.R;

public class LoginDialog extends AppCompatDialogFragment {
    EditText username;
    EditText password;
    private LoginDialogListener listener;

    public interface LoginDialogListener {
        void checkInput(String username, String password);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View view = getActivity().getLayoutInflater().inflate(R.layout.layout_dialog, null);
        username = view.findViewById(R.id.dialog_username);
        password = view.findViewById(R.id.dialog_password);
        builder.setView(view)
                .setNeutralButton("register", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RegisterActivity.start(view.getContext());
                    }
                })
                .setTitle("Login")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = String.valueOf(username.getText());
                        String pass = String.valueOf(password.getText());
                        listener.checkInput(name, pass);
                    }
                });

        setCancelable(false);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (LoginDialogListener) context;
    }
}
