package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        findViewById(R.id.textViewSignUp).setOnClickListener(this);
        findViewById(R.id.buttonLogIn).setOnClickListener(this);
        findViewById(R.id.editTextPwdLogIn).setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction()== KeyEvent.ACTION_DOWN)
                {
                    onClick(findViewById(R.id.buttonLogIn));
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(final View v) {
        switch(v.getId())
        {
            case R.id.textViewSignUp:
                Intent i = new Intent(LogIn.this,SignUp.class);
                startActivity(i);
                finish();
                break;
            case R.id.buttonLogIn:
                EditText usernameOrEmail = findViewById(R.id.editTextUsernameEmail);
                EditText pwd = findViewById(R.id.editTextPwdLogIn);

                String nameStr = usernameOrEmail.getText().toString();
                String pwdStr = pwd.getText().toString();

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Logging in ...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


                Log.i("app",pwdStr);
                if(nameStr.isEmpty())
                {
                    Toast.makeText(this,"Username/email is Empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pwdStr.isEmpty())
                {
                    Toast.makeText(this,"Password is Empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.show();
                final SharedPreferences userProfile = this.getSharedPreferences("userProfile", Context.MODE_PRIVATE);
                ParseUser.logInInBackground(nameStr, pwdStr, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(e==null && user!=null)
                        {
                            userProfile.edit().putString("email",user.get("email").toString());
                            userProfile.edit().putString("username",user.get("username").toString());
                            userProfile.edit().putString("firstName",user.get("firstName").toString());
                            userProfile.edit().putString("lastName",user.get("lastName").toString());
                            userProfile.edit().putString("dob",user.get("dob").toString());

                            Intent i = new Intent(LogIn.this,HomeActivity.class);
                            startActivity(i);
                        }else
                        {
                            Toast.makeText(LogIn.this,"Error! "+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
        }
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
