package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private final ArrayList<User> peopleArrayList;
    Context context;

    public UserAdapter(Context context, ArrayList<User> peopleArrayList) {
        this.context = context;
        this.peopleArrayList = peopleArrayList;
    }

    @NonNull
    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.MyViewHolder holder, int position) {
        String nameString = peopleArrayList.get(position).getName();
        String pictureString = peopleArrayList.get(position).getPicture();
        String usernameString = peopleArrayList.get(position).getUsername();
        String emailString = peopleArrayList.get(position).getEmail();
        String phoneString = peopleArrayList.get(position).getPhone();
        String websiteString = peopleArrayList.get(position).getWebsite();
        if (pictureString == null) {
            pictureString = ("https://robohash.org/" + nameString);
        }
        holder.nameTxt.setText(nameString);
        holder.pictureImage.setTransitionName(pictureString);
        Picasso.get().load(pictureString).resize(100,100).into(holder.pictureImage);
        holder.usernameTxt.setText(usernameString);
        holder.emailTxt.setText(emailString);
        holder.phoneTxt.setText(phoneString);
        holder.websiteTxt.setText(websiteString);
    }

    @Override
    public int getItemCount() {
        return peopleArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView nameTxt;
        private final ImageView pictureImage;
        private final TextView usernameTxt;
        private final TextView emailTxt;
        private final TextView phoneTxt;
        private final TextView websiteTxt;

        public MyViewHolder(final View v) {
            super(v);
            nameTxt = v.findViewById(R.id.name);
            pictureImage = v.findViewById(R.id.picture);
            usernameTxt = v.findViewById(R.id.username);
            emailTxt = v.findViewById(R.id.email);
            phoneTxt = v.findViewById(R.id.phone);
            websiteTxt = v.findViewById(R.id.website);

            v.setOnClickListener(this);
        }

        public void UserProfile(View v) {

            Intent intent;
            String name;
            String picture;
            String username;
            String email;
            String phone;
            String website;

            intent = new Intent(v.getContext(), UserActivity.class);
            name = nameTxt.getText().toString();
            picture = pictureImage.getTransitionName();
            username = usernameTxt.getText().toString();
            email = emailTxt.getText().toString();
            phone = phoneTxt.getText().toString();
            website = websiteTxt.getText().toString();
            intent.putExtra("Name", name);
            intent.putExtra("Picture", picture);
            intent.putExtra("Username", username);
            intent.putExtra("Email", email);
            intent.putExtra("Phone", phone);
            intent.putExtra("Website", website);

            v.getContext().startActivity(intent);
        }

        @Override
        public void onClick(View v) {
            UserProfile(v);
        }
    }
}