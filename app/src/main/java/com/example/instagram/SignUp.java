package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        findViewById(R.id.imageButtonBack).setOnClickListener(this);
        findViewById(R.id.buttonSignUp).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.imageButtonBack :
                Log.i("app","in switch1");
                Intent i = new Intent(SignUp.this,LogIn.class);
                startActivity(i);
                finish();
                break;

            case R.id.buttonSignUp:
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Signing up!");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                EditText email = findViewById(R.id.editTextEmail);
                EditText username = findViewById(R.id.editTextUsername);
                EditText pwd = findViewById(R.id.editTextPwd);

                String emailStr = email.getText().toString();
                String usernameStr = username.getText().toString();
                String pwdStr = pwd.getText().toString();

                if(emailStr.isEmpty() || usernameStr.isEmpty() || pwdStr.isEmpty())
                {
                    if(email.getText().toString().isEmpty())
                        Toast.makeText(SignUp.this,"Email is empty",Toast.LENGTH_SHORT).show();
                    if(username.getText().toString().isEmpty())
                        Toast.makeText(SignUp.this,"Username is empty",Toast.LENGTH_SHORT).show();
                    if(pwd.getText().toString().isEmpty())
                        Toast.makeText(SignUp.this,"Password is empty",Toast.LENGTH_SHORT).show();

                    String str =email.getText().toString()+" "+username.getText().toString()+" "+pwd.getText().toString();
                    Log.i("app",str);
                    return;
                }

                final ParseUser user = new ParseUser();
                user.setEmail(emailStr);
                user.setUsername(usernameStr);
                user.setPassword(pwdStr);

                progressDialog.show();

                final SharedPreferences userProfile = this.getSharedPreferences("userProfile", Context.MODE_PRIVATE);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                            userProfile.edit().putString("email",user.get("email").toString());
                            userProfile.edit().putString("username",user.get("username").toString());
                            userProfile.edit().putString("firstName",user.get("firstName").toString());
                            userProfile.edit().putString("lastName",user.get("lastName").toString());
                            userProfile.edit().putString("dob",user.get("dob").toString());

                            Intent i = new Intent(SignUp.this,HomeActivity.class);
                            startActivity(i);

                            Toast.makeText(SignUp.this, "Sign Up Successful ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUp.this, "Error !" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

                break;
        }
    }

    public void save(View view)
    {
//        // Adding Object
//        ParseObject obj1 = new ParseObject("DeleteMe");
//        obj1.put("Key1",34);
//        obj1.put("Key1",23);
//        obj1.put("Key2","man");
//        obj1.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e==null)
//                    FancyToast.makeText(SignUp.this,"Data Save",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
////                else
////                    FancyToast.makeText(SignUp.this,e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true);
//            }
//        });

//        //Getting Single object
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("DeleteMe");
//        query.getInBackground("V5bpsZE8K3", new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject object, ParseException e) {
//                if(object!=null && e==null)
//                     Toast.makeText(SignUp.this,object.get("Key2").toString(),Toast.LENGTH_LONG).show();
//                else
//                     Toast.makeText(SignUp.this,e.getMessage(),Toast.LENGTH_LONG).show();
//            }
//        });

        //Getting all object
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("DeleteMe");
//        query.whereLessThan("Key1",50).whereGreaterThan("Key1",23);
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> objects, ParseException e) {
//                if(e==null)
//                for(ParseObject obj: objects)
//                {
//                    Log.i("app",obj.get("Key2").toString()+"\t"+obj.getNumber("Key1")+"");
//                }
//            }
//        });
    }
    public void hideKeyboard(View v)
    {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e)
        {

        }
    }
}
