package com.example.home.countriesquiz.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.example.home.countriesquiz.R;
import com.example.home.countriesquiz.model.User;
import com.example.home.countriesquiz.util.UserLocalStore;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.username_text)
    EditText username;
    @BindView(R.id.password_text)
    EditText password;


    public static void start(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_signUp)
    public void registerUser() {
        if (String.valueOf(username.getText()).equals("")) {
            Toast.makeText(this, R.string.input_login, Toast.LENGTH_SHORT).show();
        } else if (String.valueOf(password.getText()).equals("")) {
            Toast.makeText(this, R.string.input_pass, Toast.LENGTH_SHORT).show();
        } else if (checkUsername(String.valueOf(username.getText()))) {
            Toast.makeText(this, R.string.user_exist, Toast.LENGTH_SHORT).show();
        } else {
            UserLocalStore.storeUser(new User(String.valueOf(username.getText()),
                    String.valueOf(password.getText())), this);
            MainActivity.start(this);
        }
    }


    @OnClick(R.id.register_to_login)
    public void loginUser() {
        new LoginDialog().show(getSupportFragmentManager(), "tag");
    }

    private boolean checkUsername(String name) {
        List<User> users = UserLocalStore.getUserList(this);
        for (User user : users) {
            if (name.equalsIgnoreCase(user.getName())) {
                return true;
            }
        }
        return false;
    }

}
