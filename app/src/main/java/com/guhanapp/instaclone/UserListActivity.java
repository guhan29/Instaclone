package com.guhanapp.instaclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        listView = findViewById(R.id.usersListView);
        ArrayList<String> usernames = new ArrayList<String>();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, usernames);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.addAscendingOrder("username");

        query.findInBackground((objects, e) -> {
            if (e == null) {
                if (objects.size() > 0) {
                    for (ParseUser user : objects) {
                        usernames.add(user.getUsername());
                    }
                    listView.setAdapter(arrayAdapter);
                }
            } else {
                e.printStackTrace();
            }
        });
    }
}