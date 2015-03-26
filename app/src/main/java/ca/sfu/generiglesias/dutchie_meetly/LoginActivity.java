package ca.sfu.generiglesias.dutchie_meetly;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements MeetlyServer{

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private int loginFlag;
    private boolean cancel;

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
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        View focusView = null;
        cancel = false;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        // Check if password field is empty
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_empty_password));
            focusView = mUsernameView;
            cancel = true;
        }

        try {
            loginFlag = login(username, password);
        } catch (FailedLoginException e) {
            e.printStackTrace();
        }

        if (cancel || loginFlag == 1) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            Toast toast = Toast.makeText(getApplicationContext(), "INVALID username or password",
                    Toast.LENGTH_LONG);
            toast.show();
        } else {
            editor.putString("getUsername", username);
            editor.commit();
            finish();
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @Override
    public int login(String username, String password) throws FailedLoginException {
        return 0;
    }

    @Override
    public int publishEvent(String username, int userToken, String title, Calendar startTime, Calendar endTime, double latitude, double longitude) throws FailedPublicationException {
        return 0;
    }

    @Override
    public void modifyEvent(int eventID, int userToken, String title, Calendar startTime, Calendar endTime, double latitude, double longitude) throws FailedPublicationException {

    }
}



