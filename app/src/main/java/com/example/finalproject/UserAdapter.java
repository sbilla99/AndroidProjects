package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTxt;
        private final TextView emailTxt;

        public MyViewHolder(final View v) {
            super(v);
            nameTxt = v.findViewById(R.id.name);
            emailTxt = v.findViewById(R.id.email);
        }
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
        String emailString = peopleArrayList.get(position).getEmail();
        holder.nameTxt.setText(nameString);
        holder.emailTxt.setText(emailString);
    }

    @Override
    public int getItemCount() {
        return peopleArrayList.size();
    }
}
