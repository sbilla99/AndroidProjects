package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    private OkHttpClient client;
    private Gson gson;
    private static ArrayList<User> peopleArrayList;
    private User googlePerson;
    public static boolean isDisplayed = false;
    Notifications notifications;
    NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Log.i(TAG, "in onCreate method");

        RecyclerView profileView = findViewById(R.id.profile_view);
        Button signOutButton = findViewById(R.id.sign_out);

        client = new OkHttpClient();
        gson = new Gson();
        peopleArrayList = new ArrayList<>();

        notifications = new Notifications(this);
        notificationManager = NotificationManagerCompat.from(this);

        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (googleSignInAccount != null) {
            String personName = googleSignInAccount.getDisplayName();
            String personPicture = String.valueOf(googleSignInAccount.getPhotoUrl());
            googlePerson = new User(personName, personPicture);
        }

        SharedPreferences sharedpreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        isDisplayed = sharedpreferences.getBoolean("isDestroyedCalled", isDisplayed);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove("isDestroyedCalled");
        editor.apply();

        if (!isDisplayed) {
            try {
                run();
                peopleArrayList.add(googlePerson);
            } catch (Exception e) {
                e.printStackTrace();
            }
            isDisplayed = true;
        }
        else {
            sharedpreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
            String jsonFile = sharedpreferences.getString("Json File", null);
            Gson gson = new Gson();
            Type type = new TypeToken<List<User>>() {}.getType();
            peopleArrayList = gson.fromJson(jsonFile, type);
        }

        UserAdapter adapter = new UserAdapter(this, peopleArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        profileView.setLayoutManager(layoutManager);
        profileView.setItemAnimator(new DefaultItemAnimator());
        profileView.setAdapter(adapter);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_out:
                        signOut();
                        break;
                }
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
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Gson gson = new Gson();
        String jsonFile = gson.toJson(peopleArrayList);
        editor.putString("Json File", jsonFile);
        editor.apply();

        if(!MainActivity.isActivityDisplayed) {
            notifications.createNotificationChannel(getClass());
            notifications.onDestroy();
        }

        Log.i(TAG, "in onPause method");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "in onDestroy method");
    }

    public void run() {
        Request request = new Request.Builder().url("http://jsonplaceholder.typicode.com/users").build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                User[] users = gson.fromJson(response.body().string(), User[].class);
                ProfileActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (User user : users) {
                            peopleArrayList.add(user);
                        }
                    }
                });
            }
        });
    }

    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInClient.signOut();
        Toast.makeText(ProfileActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        MainActivity.isActivityDisplayed = true;
        startActivity(intent);
        finish();
    }
}
