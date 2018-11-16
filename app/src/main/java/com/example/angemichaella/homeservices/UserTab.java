package com.example.angemichaella.homeservices;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserTab extends Fragment {

    ListView users_list;

    List<User> users;
    DatabaseReference databaseUsers;

    String username;

    public static UserTab newInstance(String username) {
        UserTab myUserTab = new UserTab();

        Bundle args = new Bundle();
        args.putString("username", username);
        myUserTab.setArguments(args);



        return myUserTab;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_tab, container, false);

        username = getArguments().getString("username");

        TextView textViewWelcomeAdmin = (TextView)view.findViewById(R.id.textViewWelcomeAdmin);
        textViewWelcomeAdmin.setText("Hey, " + username + "!");

        users_list = (ListView) view.findViewById(R.id.users_list);

        users = new ArrayList<>(0);

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        databaseUsers.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                users.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    User user = postSnapshot.getValue(User.class);
                    if(!user.getType().equals("Admin")) {
                        users.add(user);
                    }
                }
                if(getActivity()!= null) {
                    UserList userAdapter = new UserList(getActivity(), users);
                    users_list.setAdapter(userAdapter);
                }
            }
            public void onCancelled(DatabaseError databaseError){

            }
        });
    }

}
