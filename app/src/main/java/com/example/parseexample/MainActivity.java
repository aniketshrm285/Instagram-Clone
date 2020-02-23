package com.example.parseexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener{

    EditText username ;
    EditText password;

    Boolean logIn;

    TextView textView;

    Button button;


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
            logIn(v);
        }

        return false;
    }

    public void showUsers(){
        Intent intent = new Intent(getApplicationContext(),UsersListActivity.class);
        startActivity(intent);
    }

    public void backgroundTap(View view){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }

    public void logIn(View view){
        if(logIn){
            final String user = username.getText().toString();
            final String pass = password.getText().toString();

            if(user.length() == 0 || pass.length() == 0){
                Toast.makeText(this, "Invalid username or password!", Toast.LENGTH_SHORT).show();
            }
            else{
                ParseUser.logInInBackground(user, pass, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user != null){
                            Log.i("LogIn","Successfully");
                            Toast.makeText(MainActivity.this, "LogIn Successfully", Toast.LENGTH_SHORT).show();
                            showUsers();
                        }
                        else{
                            Log.i("LogIn","Unsuccessful" + " " + "try with different username or signUp");
                            Toast.makeText(MainActivity.this, "LogIn Unsuccessful" + " " + "try with different username or signUp", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        else{
            final String user = username.getText().toString();
            final String pass = password.getText().toString();

            if(user.length() == 0 || pass.length() == 0){
                Toast.makeText(this, "Invalid username or password!", Toast.LENGTH_SHORT).show();
            }
            else{
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("username", user);

                query.countInBackground(new CountCallback(){

                    @Override
                    public void done(int count, ParseException e) {
                        // TODO Auto-generated method stub
                        if (e == null) {
                            if(count!=0){
                                Toast.makeText(MainActivity.this, "Username already exists, try logging in or use a different username!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                ParseUser newUser = new ParseUser();
                                newUser.setUsername(user);
                                newUser.setPassword(pass);

                                newUser.signUpInBackground(new SignUpCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e == null){
                                            Log.i("Signup","Successful");
                                            Toast.makeText(MainActivity.this, "SignUp Successful, Now you can Log In", Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }
                        }
                    }

                });
            }
        }
    }

    public void changeFunc(View view){
        if(logIn){
            button.setText("Sign Up");
            logIn=false;
            textView.setText("or, Log In");
        }
        else{
            button.setText("Log In");
            logIn=true;
            textView.setText("or, Sign Up");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Instagram");

        Log.i("Info","Starting");

        username = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        button = findViewById(R.id.button);
        logIn = true;
        textView = findViewById(R.id.textView);

        password.setOnKeyListener(this);

        if(ParseUser.getCurrentUser()!=null){
            showUsers();
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}
