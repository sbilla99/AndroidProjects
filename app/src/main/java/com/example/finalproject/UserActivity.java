package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    private static final String TAG = UserActivity.class.getSimpleName();
    private EditText name;
    private ImageView picture;
    private EditText username;
    private EditText email;
    private EditText phone;
    private EditText website;
    String aName;
    String aPicture;
    String aUsername;
    String aEmail;
    String aPhone;
    String aWebsite;
    Notifications notifications;
    NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        name = findViewById(R.id.pName);
        picture = findViewById(R.id.pPicture);
        username = findViewById(R.id.pUsername);
        email = findViewById(R.id.pEmail);
        phone = findViewById(R.id.pPhone);
        website = findViewById(R.id.pWebsite);
        Button saveButton = findViewById(R.id.save);

        aName = getIntent().getStringExtra("Name");
        aPicture = getIntent().getStringExtra("Picture");
        aUsername = getIntent().getStringExtra("Username");
        aEmail = getIntent().getStringExtra("Email");
        aPhone = getIntent().getStringExtra("Phone");
        aWebsite = getIntent().getStringExtra("Website");

        name.setText(aName);
        picture.setTransitionName(aPicture);
        Picasso.get().load(aPicture).resize(500,500).into(picture);
        username.setText(aUsername);
        email.setText(aEmail);
        phone.setText(aPhone);
        website.setText(aWebsite);

        notifications = new Notifications(this);
        notificationManager = NotificationManagerCompat.from(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                MainActivity.isActivityDisplayed = true;
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "in onStart method");
    }

    @Override
    protected void onResume() {
        super.onResume();

        MainActivity.isActivityDisplayed = false;
        notificationManager.cancelAll();

        Log.i(TAG, "in onResume method");
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedpreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String jsonFile = sharedpreferences.getString("Json File", null);
        Gson gson = new Gson();
        Type type = new TypeToken<List<User>>() {}.getType();
        ArrayList<User> peopleArrayList = gson.fromJson(jsonFile, type);
        for (User user : peopleArrayList) {
            if (user.getName().equals(aName)) {
                user.setName(name.getText().toString());
                user.setUsername(username.getText().toString());
                user.setEmail(email.getText().toString());
                user.setPhone(phone.getText().toString());
                user.setWebsite(website.getText().toString());
            }
        }
        SharedPreferences.Editor editor = sharedpreferences.edit();
        jsonFile = gson.toJson(peopleArrayList);
        editor.putString("Json File", jsonFile);
        editor.apply();

        if (!MainActivity.isActivityDisplayed) {
            notifications.createNotificationChannel(getClass(),
                    getIntent().getStringExtra("Name"),
                    getIntent().getStringExtra("Picture"),
                    getIntent().getStringExtra("Username"),
                    getIntent().getStringExtra("Email"),
                    getIntent().getStringExtra("Phone"),
                    getIntent().getStringExtra("Website"));
        }
        notifications.onDestroy();

        Log.i(TAG, "in onPause method");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "in onDestroy method");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("NameKey", name.getText().toString());
        outState.putString("PictureKey", aPicture);
        outState.putString("UsernameKey", username.getText().toString());
        outState.putString("EmailKey", email.getText().toString());
        outState.putString("PhoneKey", phone.getText().toString());
        outState.putString("WebsiteKey", website.getText().toString());

        Log.i(TAG, "in onSaveInstanceState method");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        aName = savedInstanceState.getString("NameKey");
        aPicture = savedInstanceState.getString("PictureKey");
        aUsername = savedInstanceState.getString("UsernameKey");
        aEmail = savedInstanceState.getString("EmailKey");
        aPhone = savedInstanceState.getString("PhoneKey");
        aWebsite = savedInstanceState.getString("WebsiteKey");

        Log.i(TAG, "in onRestoreInstanceState method");
    }
}
