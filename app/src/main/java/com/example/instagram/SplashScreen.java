package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class SplashScreen extends AppCompatActivity {

    ViewSwitcher firstName_Switcher,lastName_Switcher,dob_Switcher;
    TextView firstName_TextView,lastName_TextView,dob_TextView;
    TextView emailIdTextView, usernameTextView;
    EditText firstName_editText,lastName_editText;
    ParseUser currUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

      //  initializeView();


        ParseUser currUser = ParseUser.getCurrentUser();
        final SharedPreferences userProfile= this.getSharedPreferences("userProfile", Context.MODE_PRIVATE);
        if(currUser!=null )
        {
            currUser.fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if(object!=null && e==null)
                    {
                        userProfile.edit().putString("email",object.get("email").toString());
                        userProfile.edit().putString("username",object.get("username").toString());

                        if(object.get("firstName")!=null)
                            userProfile.edit().putString("firstName",object.get("firstName").toString());
                        if(object.get("lastName")!=null)
                            userProfile.edit().putString("lastName",object.get("lastName").toString());
                        if(object.get("dob")!=null)
                            userProfile.edit().putString("dob",object.get("dob").toString());

                        Intent i = new Intent(SplashScreen.this,HomeActivity.class);
                        startActivity(i);
                    }else
                    {
                        Toast.makeText(SplashScreen.this,"Error! "+e.getMessage(),Toast.LENGTH_LONG);
                    }
                }
            });

        }else
        {
            Intent i = new Intent(this,LogIn.class);
            startActivity(i);
        }

        currUser.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

//    public void initializeView()
//    {
//        View v= getLayoutInflater().inflate(R.layout.fragment_profile_tab, null);
//        firstName_Switcher = v.findViewById(R.id.firstName_ViewSwitcher);
//        firstName_TextView = firstName_Switcher.findViewById(R.id.firstName_TextView_ViewSwitcher);
//        firstName_editText = firstName_Switcher.findViewById(R.id.firstName_EditText_ViewSwitcher);
//
//        lastName_Switcher = v.findViewById(R.id.lastName_ViewSwitcher);
//        lastName_TextView = lastName_Switcher.findViewById(R.id.lastName_TextView_ViewSwitcher);
//        lastName_editText = lastName_Switcher.findViewById(R.id.lastName_editText_ViewSwitcher);
//
//        dob_Switcher = v.findViewById(R.id.dob_ViewSwitcher);
//        dob_TextView = dob_Switcher.findViewById(R.id.dob_TextView_ViewSwitcher);
//
//
//        emailIdTextView= v.findViewById(R.id.emailId_TextView);
//        usernameTextView=v.findViewById(R.id.username_TextView);
//    }
}
