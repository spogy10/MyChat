package poliv.jr.com.mychat.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import poliv.jr.com.mychat.R;
import poliv.jr.com.mychat.client.RequestSender;

/**
 * A login screen that offers login via email/tvPassword.
 */
public class LoginActivity extends AppCompatActivity {





    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // UI references.
    private AutoCompleteTextView tvUserName;
    private EditText tvPassword;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("Paul", "thread started");
                RequestSender r = RequestSender.getInstance();
            }
        }).start();

        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        EmailsViewModel model = ViewModelProviders.of(this, new EmailsViewModelFactory(sharedPreferences, getString(R.string.autocomplete_email_set))).get(EmailsViewModel.class);


        // Set up the login form.

        tvUserName = (AutoCompleteTextView) findViewById(R.id.atvUsername);

        model.getEmails().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {
                addEmailsToAutoComplete(strings);
            }
        });

        tvPassword = (EditText) findViewById(R.id.password);
        tvPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button btSignIn = (Button) findViewById(R.id.btSignIn);
        btSignIn.setOnClickListener(new OnClickListener() {
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
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        tvUserName.setError(null);
        tvPassword.setError(null);

        // Store values at the time of the login attempt.
        String username = tvUserName.getText().toString();
        String password = this.tvPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid tvPassword, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            this.tvPassword.setError(getString(R.string.error_invalid_password));
            focusView = this.tvPassword;
            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            tvUserName.setError(getString(R.string.error_field_required));
            focusView = tvUserName;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            tvUserName.setError(getString(R.string.error_invalid_username));
            focusView = tvUserName;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            saveEmailToAutoCompleteList(username);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    private void saveEmailToAutoCompleteList(String email){
        Set<String> tempSet = sharedPreferences.getStringSet(getString(R.string.autocomplete_email_set), new HashSet<String>());
        tempSet.add(email);
        editor.putStringSet(getString(R.string.autocomplete_email_set), tempSet).apply();
    }

    private boolean isUsernameValid(String username) {

        return username.length() > 2;
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        tvUserName.setAdapter(adapter);
    }


    public static class EmailsViewModel extends ViewModel{

        private MutableLiveData<List<String>> emails;
        private SharedPreferences sharedPreferences;
        private String fileName;

        public EmailsViewModel(SharedPreferences sharedPreferences, String fileName) {
            this.sharedPreferences = sharedPreferences;
            this.fileName = fileName;
        }

        public LiveData<List<String>> getEmails() {
            if(emails == null) {
                emails = new MutableLiveData<>();
                loadEmails();
            }
            return emails;
        }

        private void loadEmails() {

            try {
                emails.setValue(new GetEmailList().execute(sharedPreferences, fileName).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public class EmailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

        private SharedPreferences sharedPreferences;
        private String fileName;


        public EmailsViewModelFactory(SharedPreferences sharedPreferences, String fileName) {
            this.sharedPreferences = sharedPreferences;
            this.fileName = fileName;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new EmailsViewModel(sharedPreferences, fileName);
        }
    }

    private static class GetEmailList extends AsyncTask<Object, Void, List<String>>{

        @Override
        protected List<String> doInBackground(Object... objects) {
            SharedPreferences sharedPreferences = (SharedPreferences) objects[0];
            String fileName = (String) objects[1];
            Set<String> set = sharedPreferences.getStringSet(fileName, new HashSet<String>());

            return new LinkedList<>(set);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String username;
        private final String password;

        UserLoginTask(String email, String password) {
            username = email;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }


            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                tvPassword.setError(getString(R.string.error_incorrect_password));
                tvPassword.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

