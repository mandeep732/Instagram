package com.example.instagram;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.Calendar;
import java.util.concurrent.Callable;

import bolts.Task;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment implements View.OnClickListener {


    public ProfileTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_profile_tab, container, false);


        final ViewSwitcher firstName_Switcher,lastName_Switcher,dob_Switcher;
        final EditText firstName_editText,lastName_editText;
        final TextView firstName_TextView,lastName_TextView,dob_TextView;

        final  Button editButton = v.findViewById(R.id.edit_button);

        firstName_Switcher = v.findViewById(R.id.firstName_ViewSwitcher);
        firstName_TextView = firstName_Switcher.findViewById(R.id.firstName_TextView_ViewSwitcher);
        firstName_editText = firstName_Switcher.findViewById(R.id.firstName_EditText_ViewSwitcher);

        lastName_Switcher = v.findViewById(R.id.lastName_ViewSwitcher);
        lastName_TextView = lastName_Switcher.findViewById(R.id.lastName_TextView_ViewSwitcher);
        lastName_editText = lastName_Switcher.findViewById(R.id.lastName_editText_ViewSwitcher);

        dob_Switcher = v.findViewById(R.id.dob_ViewSwitcher);
        dob_TextView = dob_Switcher.findViewById(R.id.dob_TextView_ViewSwitcher);

        final TextView emailIdTextView, usernameTextView;
        emailIdTextView= v.findViewById(R.id.emailId_TextView);
        usernameTextView=v.findViewById(R.id.username_TextView);


        final ParseUser currUser = ParseUser.getCurrentUser();
        final SharedPreferences sharedPreferences= v.getContext().getSharedPreferences("my", Context.MODE_PRIVATE);
        if(sharedPreferences.contains("email") && sharedPreferences.contains("username")
                && sharedPreferences.contains("firstName") && sharedPreferences.contains("lastName") && sharedPreferences.contains("dob"))
        {

            emailIdTextView.setText(sharedPreferences.getString("email","NAN"));
            usernameTextView.setText(sharedPreferences.getString("username","NAN"));
            firstName_TextView.setText(sharedPreferences.getString("firstName","NAN"));
            lastName_TextView.setText(sharedPreferences.getString("lastName","NAN"));
            dob_TextView.setText(sharedPreferences.getString("dob","NAN"));
        }else{
            currUser.fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if(object!=null && e==null)
                    {
                        emailIdTextView.setText(object.get("email").toString());
                        usernameTextView.setText(object.get("username").toString());
                        if(object.get("firstName")!=null)
                            firstName_TextView.setText(object.get("firstName").toString());
                        if(object.get("lastName")!=null)
                            lastName_TextView.setText(object.get("lastName").toString());
                        if(object.get("dob")!=null)
                            dob_TextView.setText(object.get("dob").toString());
                    }else
                    {
                        Toast.makeText(getContext(),"Error! "+e.getMessage(),Toast.LENGTH_LONG);
                    }
                }
            });
        }


        dob_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String action = editButton.getText().toString();
                if(action.toUpperCase().equals("UPDATE"))
                {
                    Calendar calender = Calendar.getInstance();
                    int year,month,day;
                    String dataStrTextView = dob_TextView.getText().toString();
                    if(dataStrTextView.isEmpty())
                    {
                        year= calender.get(Calendar.YEAR);
                        month=calender.get(Calendar.MONTH);
                        day=calender.get(Calendar.DAY_OF_MONTH);
                    }else
                    {
                        String date[]=dataStrTextView.trim().split("/");
                        day=Integer.parseInt(date[0]);
                        month=Integer.parseInt(date[1]);
                        year=Integer.parseInt(date[2]);
                    }

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker arg0,
                                              int year, int month, int day) {

                            String dateStr = new StringBuilder().append(day).append("/").append(month+1).append("/").append(year)+"";
                            dob_TextView.setText(dateStr);
                        }
                    } ,year,month,day);
                    datePickerDialog.show();
                }
            }
        });

        v.findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String action = editButton.getText().toString();

                if(action.toUpperCase().equals("EDIT")){
                    editButton.setText("UPDATE");

                    firstName_editText.setText(firstName_TextView.getText()+"");
                    lastName_editText.setText(lastName_TextView.getText()+"");

                    firstName_Switcher.showNext();
                    lastName_Switcher.showNext();
                    dob_Switcher.showNext();
                }else
                {
                    final String firstName = firstName_editText.getText()+"";
                    final String lastName = lastName_editText.getText()+"";
                    final String dob = dob_TextView.getText().toString();

                    currUser.put("firstName",firstName);
                    currUser.put("lastName",lastName);
                    currUser.put("dob",dob);

                    currUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null)
                            {
                                firstName_TextView.setText(firstName);
                                lastName_TextView.setText(lastName);
                                dob_TextView.setText(dob);

                                sharedPreferences.edit().putString("firstName",firstName);
                                sharedPreferences.edit().putString("lastName",lastName);
                                sharedPreferences.edit().putString("dob",dob);
                                Toast.makeText(getContext(),"Profile is updated",Toast.LENGTH_SHORT).show();
                            }else
                            {
                                Toast.makeText(getContext(),"Update Error!"+e.getMessage(),Toast.LENGTH_SHORT).show();
                                Log.i("app","update Error");
                                firstName_TextView.setText(sharedPreferences.getString("firstName","NAN"));
                                lastName_TextView.setText(sharedPreferences.getString("lastName","NAN"));
                                dob_TextView.setText(sharedPreferences.getString("dob","NAN"));
                            }
                        }
                    });


                    firstName_Switcher.showNext();
                    lastName_Switcher.showNext();
                    dob_Switcher.showNext();
                    editButton.setText("EDIT");
                }
            }
        });
        v.findViewById(R.id.logout_button).setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.logout_button:
                ParseUser currUser = new ParseUser();
                if(currUser.getCurrentUser()!=null)
                {
                    ParseUser.logOutInBackground(new LogOutCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null)
                            {

                                final SharedPreferences sharedPreferences= getContext().getSharedPreferences("my", Context.MODE_PRIVATE);
                                sharedPreferences.edit().remove("email");
                                sharedPreferences.edit().remove("username");
                                sharedPreferences.edit().remove("firstName");
                                sharedPreferences.edit().remove("lastName");
                                sharedPreferences.edit().remove("dob");

                                Intent i = new Intent(getActivity(),LogIn.class);
                                startActivity(i);
                                getActivity().finish();
                            }else
                            {
                                Toast.makeText(getContext(),"LogOut Error! "+e.getMessage(),Toast.LENGTH_LONG);
                            }

                        }
                    });

                }
                break;
        }
    }
}
