package ca.sfu.generiglesias.dutchie_meetly;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity{

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private int loginFlag;
    private boolean cancel;
    private MeetlyServerImpl server;
    int userId;
    String username, password;
    boolean loginSuccessful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        server = new MeetlyServerImpl();
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {

        SharedPreferences UserNamePref = getSharedPreferences("UserName", MODE_PRIVATE);
        SharedPreferences.Editor editor = UserNamePref.edit();

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        username = mUsernameView.getText().toString();
        password = mPasswordView.getText().toString();

        cancel = false;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        // Check if password field is empty
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_empty_password));
            cancel = true;
        }

        // Uses the login function from the interface class
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    SharedPreferences UserNamePref = getSharedPreferences("UserName", MODE_PRIVATE);
                    SharedPreferences.Editor editor = UserNamePref.edit();
                    userId = server.login(username, password);
                    editor.putInt("getUserToken", userId);
                    editor.commit();
                    loginSuccessful = true;
                } catch (MeetlyServer.FailedLoginException e) {
                    e.printStackTrace();
                    loginSuccessful = false;
                }
            }
        }).start();

        if(!cancel && loginSuccessful)
        {
            editor.putString("getUsername", username);
            editor.commit();
            ListEventsActivity.currentUsername.setText(username);
            Intent in = new Intent();
            setResult(1, in);
            finish();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.error_invalid_username_password,
                    Toast.LENGTH_LONG);
            toast.show();
        }


        if (cancel || loginFlag == 1) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.

        } else {

        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}



