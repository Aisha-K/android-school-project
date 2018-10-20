package com.example.angemichaella.homeservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;


import java.util.List;



public class UserList extends ArrayAdapter<User>{


    private Activity context;
    List<User> users;

    public UserList(Activity context, List<User> users){
        super(context, R.layout.activity_user_list_layout, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_user_list_layout, null, true);

        TextView textViewUsername = (TextView) listViewItem.findViewById(R.id.textViewUsername);
        TextView textViewUsertype = (TextView) listViewItem.findViewById(R.id.textViewUsertype);

        User user = users.get(position);
        textViewUsername.setText(user.getUsername());
        textViewUsertype.setText(user.getType());
        return listViewItem;
    }
}



