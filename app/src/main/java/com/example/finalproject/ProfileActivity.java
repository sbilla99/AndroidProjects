package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private Button signOutButton;
    private GoogleSignInClient googleSignInClient;
    private RecyclerView profileView;
    private OkHttpClient client;
    private Gson gson;
    private static ArrayList<User> peopleArrayList;
    private GoogleSignInAccount googleSignInAccount;
    private User googlePerson;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileView = findViewById(R.id.profile_view);
        signOutButton = findViewById(R.id.sign_out);

        client = new OkHttpClient();
        gson = new Gson();
        peopleArrayList = new ArrayList<>();

        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (googleSignInAccount != null) {
            String personName = googleSignInAccount.getDisplayName();
            String personEmail = googleSignInAccount.getEmail();
            googlePerson = new User(personName, personEmail);
        }

        try {
            run();
            peopleArrayList.add(googlePerson);
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter = new UserAdapter(this, peopleArrayList);
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
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInClient.signOut();
        Toast.makeText(ProfileActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
