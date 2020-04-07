package com.example.instagram;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTab extends Fragment {

    private ListView userListView;
    ArrayList<String> parseUsersArray;
    ArrayAdapter arrayAdapter;
    public UsersTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_users_tab, container, false);

        userListView = v.findViewById(R.id.listView_UsersList);
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());

        parseUsersArray=new ArrayList<>();
        arrayAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,parseUsersArray);
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(objects!=null && e==null)
                {
                    for(ParseUser user:objects)
                    {
                        parseUsersArray.add(user.getUsername());
                    }
                    userListView.setAdapter(arrayAdapter);
                }
            }
        });


        return v;
    }
}


