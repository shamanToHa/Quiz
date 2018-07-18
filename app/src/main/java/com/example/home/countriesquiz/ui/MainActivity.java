package com.example.home.countriesquiz.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.home.countriesquiz.R;
import com.example.home.countriesquiz.model.Country;
import com.example.home.countriesquiz.model.User;
import com.example.home.countriesquiz.network.Retrofit;
import com.example.home.countriesquiz.util.UserLocalStore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements LoginDialog.LoginDialogListener {

    private LinkedList<Country> countries;
    private LinkedList<Country> answers;
    public static final String COUNTRY_STORAGE_NAME = "Countries";
    private Country country;
    @BindView(R.id.question)
    TextView question;
    @BindView(R.id.answer)
    EditText answer;
    private boolean userIsLoggedIn;
    private static SharedPreferences countryLocalDatabase;
    private static Type itemsListType = new TypeToken<List<Country>>() {
    }.getType();

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        countries = new LinkedList<>();
        answers = new LinkedList<>();
        countryLocalDatabase = getSharedPreferences(COUNTRY_STORAGE_NAME, Context.MODE_PRIVATE);
        if (countryLocalDatabase.contains(COUNTRY_STORAGE_NAME)) {
            countries.addAll(loadCountries());
            Collections.shuffle(countries);
        } else getCountriesFromNet();
        if (!userIsLoggedIn) {
            new LoginDialog().show(getSupportFragmentManager(), "tag");
        }
        askQuestion();
    }

    @OnClick(R.id.check)
    public void checkQuestion() {
        if (country.capital.equalsIgnoreCase(String.valueOf(answer.getText()))) {
            answers.add(country);
            Toast.makeText(this, "Correct " + country.capital, Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "Wrong  " + country.capital, Toast.LENGTH_SHORT).show();
        answer.setText("");
        askQuestion();
    }

    @Override
    public void checkInput(String username, String password) {
        if (checkUser(new User(username, password))) {
            userIsLoggedIn = true;
        } else {
            Toast.makeText(this, "incorrect name or password", Toast.LENGTH_SHORT).show();
            new LoginDialog().show(getSupportFragmentManager(), "tag");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("userLoggedIn", userIsLoggedIn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            userIsLoggedIn = savedInstanceState.getBoolean("userLoggedIn");
        }
    }

    private void getCountriesFromNet() {
        Retrofit.getCountries(new Callback<List<Country>>() {
            @Override
            public void success(List<Country> countriesList, Response response) {
                countries.addAll(countriesList);
                Collections.shuffle(countries);
                storeCountries(countries);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private List<Country> loadCountries() {
        countryLocalDatabase = getSharedPreferences(COUNTRY_STORAGE_NAME, Context.MODE_PRIVATE);
        return new Gson().fromJson(countryLocalDatabase.getString(COUNTRY_STORAGE_NAME, ""), itemsListType);
    }

    private void storeCountries(List<Country> countries) {
        countryLocalDatabase = getSharedPreferences(COUNTRY_STORAGE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = countryLocalDatabase.edit()
                .putString(COUNTRY_STORAGE_NAME, new Gson().toJson(countries));
        editor.apply();
    }


    private void askQuestion() {
        if (!countries.isEmpty()) {
            country = countries.pollFirst();
            question.setText(String.format("%s %s", getString(R.string.question), country.name));
        }
    }

    private boolean checkUser(User user) {
        List<User> users = UserLocalStore.getUserList(this);
        for (User u : users) {
            if (u.getName().equalsIgnoreCase(user.getName()) && u.getPassword()
                    .equals(UserLocalStore.getMd5Hash(user.getPassword()))) {
                return true;
            }
        }
        return false;
    }
}
