package com.malav.medicinesontheway.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.malav.medicinesontheway.R;
import com.malav.medicinesontheway.custom_font.MyEditText;
import com.malav.medicinesontheway.custom_font.MyTextView;
import com.malav.medicinesontheway.model.User;
import com.malav.medicinesontheway.utils.AppUtils;
import com.malav.medicinesontheway.utils.CommonUtils;
import com.malav.medicinesontheway.utils.Constants;
import com.malav.medicinesontheway.utils.JSONfunctions;
import com.malav.medicinesontheway.utils.QueryMapper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = LoginActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private MyEditText mEmailView, mPasswordView;
    private MaterialDialog dialogForgotPass;
    SharedPreferences someData;
    private CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 7;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        someData = getSharedPreferences(Constants.filename, 0);

        if (someData.contains("login") && someData.getString("login", "").equalsIgnoreCase("true")) {
            if (someData.getString("role", "").equalsIgnoreCase("U")) {
                Intent i = new Intent(LoginActivity.this, CustomerDashBoardActivity.class);
                startActivity(i);
                finish();
            } else if(someData.getString("role", "").equalsIgnoreCase("P")){
                Intent i = new Intent(LoginActivity.this, CustomerDashBoardActivity.class);
                startActivity(i);
                finish();
            } else if(someData.getString("role", "").equalsIgnoreCase("D")){
                Intent i = new Intent(LoginActivity.this, CustomerDashBoardActivity.class);
                startActivity(i);
                finish();
            }else{
                Intent i = new Intent(LoginActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                Toast.makeText(this, "Some Error. Please try again..!!", Toast.LENGTH_SHORT).show();
            }
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        ImageView btFacebook = (ImageView) findViewById(R.id.btfb);
        ImageView btGmail = (ImageView) findViewById(R.id.btgp);
        ImageView btTwitter = (ImageView) findViewById(R.id.bttw);

        btGmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(AppUtils.isOnline(LoginActivity.this)) {
                    signIn();
                }else{
                    Toast.makeText(LoginActivity.this, "Connectivity Issues. You are Offline", Toast.LENGTH_SHORT).show();
                }
            }
        });

        MyTextView btSignUp = (MyTextView)findViewById(R.id.sign_up);
        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        btFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AppUtils.isOnline(LoginActivity.this)) {
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                            Arrays.asList("user_photos", "email", "user_birthday", "public_profile")
                    );
                }else{
                    Toast.makeText(LoginActivity.this, "Connectivity Issues. You are Offline", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up the login form.
        mEmailView = (MyEditText) findViewById(R.id.email);

        mPasswordView = (MyEditText) findViewById(R.id.password);

        MyTextView mEmailSignInButton = (MyTextView) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AppUtils.isOnline(LoginActivity.this)) {
                    attemptLogin();
                }else {
                    Toast.makeText(LoginActivity.this, "Connectivity Issues. You are Offline", Toast.LENGTH_SHORT).show();
                }
            }
        });

        MyTextView forgotPass = (MyTextView) findViewById(R.id.forgot_pass);

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForgotPass = new MaterialDialog.Builder(LoginActivity.this)
                        .title("Enter EmailId")
                        .inputType(InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                        .positiveText("Update")
                        .input("Type here", "", false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                if(CommonUtils.isNotNull(input.toString()))
                                new UpdatePassword(input.toString()).execute();
                            }
                        }).show();
            }
        });

        callbackManager = CallbackManager.Factory.create();
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }
        LoginManager.getInstance().registerCallback(
                callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        final AccessToken accessToken = loginResult.getAccessToken();
                        final User newUser = new User();
                        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                                newUser.setEmail(user.optString("email"));
                                newUser.setUserName(user.optString("name"));
                                String id = user.optString("id");
                                newUser.setProfilePic("http://graph.facebook.com/" + id + "/picture?type=large");

                                if (CommonUtils.isNotNull(newUser.getEmail())) {
                                    new CheckUserInDB(newUser).execute();
                                }
                            }
                        });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,link,gender,birthday,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d(TAG, "onError: " + exception);
                        BackgroundMail.newBuilder(LoginActivity.this)
                                .withUsername("noreply.holistree@gmail.com")
                                .withPassword("malavjaini")
                                .withMailto("malavshah502@gmail.com")
                                .withSubject("Facebook Login Error")
                                .withBody(exception.toString())
                                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                                    @Override
                                    public void onSuccess() {
                                        //do some magic
                                    }
                                })
                                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                                    @Override
                                    public void onFail() {
                                        //do some magic
                                    }
                                })
                                .send();

                        if (AccessToken.getCurrentAccessToken() != null) {
                            LoginManager.getInstance().logOut();
                        }
                    }
                }
        );


    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            User user = new User();
            user.setUserName(acct.getDisplayName());
            if(acct.getPhotoUrl()!=null) {
                user.setProfilePic(acct.getPhotoUrl().toString());
            }
            user.setEmail(acct.getEmail());
            if (CommonUtils.isNotNull(user.getEmail())) {
                new CheckUserInDB(user).execute();
            }
        } else {
            Toast.makeText(this, "Some Error occoured. Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    private void attemptLogin() {

        mEmailView.setError(null);
        mPasswordView.setError(null);
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            new UserLoginTask(email, password).execute();
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }

    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail, mPassword;
        private String success = "0";
        ProgressDialog progress;
        private User user = new User();

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(LoginActivity.this);
            progress.setMessage("Changing Password...");
            progress.setIndeterminate(false);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                List<NameValuePair> para = new ArrayList<>();
                para.add(new BasicNameValuePair("emailId", mEmail));
                para.add(new BasicNameValuePair("password", mPassword));
                Log.d("request!", "starting");

                JSONObject json = JSONfunctions.makeHttpRequest(QueryMapper.URL_LOGIN, "POST", para);
                Log.d("Login attempt", json.toString());
                success = json.getString("success");
                if (success.equalsIgnoreCase("1")) {
                    user.setRole(json.getString("user_role"));
                    user.setUserName(json.getString("user_name"));
                    user.setProfilePic(json.getString("user_pic"));
                    user.setUserId(json.getString("user_id"));
                    user.setEmail(mEmail);
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                SharedPreferences.Editor editor = someData.edit();
                editor.putString("emailId", user.getEmail());
                editor.putString("role", user.getRole());
                editor.putString("login", "true");
                editor.putString("name", user.getUserName());
                editor.putString("profilePic", user.getProfilePic());
                editor.putString("user_id", user.getUserId());
                editor.apply();
                editor.commit();
                Intent i = null;
                if (CommonUtils.equalIgnoreCase(user.getRole(), "U")) {
                    i = new Intent(LoginActivity.this, CustomerDashBoardActivity.class);
                } else if(CommonUtils.equalIgnoreCase(user.getRole(), "P")){
                    i = new Intent(LoginActivity.this, CustomerDashBoardActivity.class);
                } else if(CommonUtils.equalIgnoreCase(user.getRole(), "D")){
                    i = new Intent(LoginActivity.this, CustomerDashBoardActivity.class);
                } else{
                    i = new Intent(LoginActivity.this, LoginActivity.class);
                }
                startActivity(i);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
            progress.dismiss();
        }
    }

    private class UpdatePassword extends AsyncTask<String, String, String> {

        String success, emailId;

        UpdatePassword(String emailId){
            this.emailId = emailId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> para = new ArrayList<NameValuePair>();
            para.add(new BasicNameValuePair("email", emailId));

            JSONObject json = JSONfunctions.makeHttpRequest(QueryMapper.URL_FORGOT_PASSWORD, "POST", para);
            Log.d("Login attempt", json.toString());

            try {
                success = json.getString("success");
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: ", e);
            }

            return success;
        }

        @Override
        protected void onPostExecute(String file_url) {
            if (!CommonUtils.equalIgnoreCase(success, "0")) {
                sendTestEmail(emailId, success);
            }
            dialogForgotPass.dismiss();
            Toast.makeText(LoginActivity.this, "Please check your Email", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendTestEmail(String toEmailId, String password) {
        BackgroundMail.newBuilder(this)
                .withUsername("noreply.holistree@gmail.com")
                .withPassword("malavjaini")
                .withMailto(toEmailId)
                .withSubject("Your Password")
                .withBody("Your password for HolisTree app is " + password + ". Change the password once you login.")
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        //do some magic
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        //do some magic
                    }
                })
                .send();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_blank, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class CheckUserInDB extends AsyncTask<Void, Void, Boolean> {

        private String success = "0";
        private User user;
        ProgressDialog progress;

        CheckUserInDB(User user) {
            this.user = user;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(LoginActivity.this);
            progress.setMessage("Logging In...");
            progress.setIndeterminate(false);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                List<NameValuePair> para = new ArrayList<NameValuePair>();
                para.add(new BasicNameValuePair("emailId", user.getEmail()));
                para.add(new BasicNameValuePair("name", user.getUserName()));
                para.add(new BasicNameValuePair("pic", user.getProfilePic()));
                Log.d("request!", "starting");

                JSONObject json = JSONfunctions.makeHttpRequest(QueryMapper.URL_CHECK_EMAIL, "POST", para);
                Log.d("Login attempt", json.toString());
                success = json.getString("success");
                if (success.equalsIgnoreCase("1")) {
                    user.setRole(json.getString("user_role"));
                    user.setUserName(json.getString("user_name"));
                    user.setProfilePic(json.getString("user_pic"));
                    user.setUserId(json.getString("user_id"));
                    user.setPhoneNumber(json.getString("user_phone"));
                    user.setEmail(json.getString("user_email"));
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                BackgroundMail.newBuilder(LoginActivity.this)
                        .withUsername("noreply.holistree@gmail.com")
                        .withPassword("malavjaini")
                        .withMailto("malavshah502@gmail.com")
                        .withSubject("Facebook Login Error")
                        .withBody(e.toString())
                        .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                            @Override
                            public void onSuccess() {
                                //do some magic
                            }
                        })
                        .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                            @Override
                            public void onFail() {
                                //do some magic
                            }
                        })
                        .send();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            progress.dismiss();
            if (success) {
                SharedPreferences.Editor editor = someData.edit();
                editor.putString("emailId", user.getEmail());
                editor.putString("role", user.getRole());
                editor.putString("login", "true");
                editor.putString("name", user.getUserName());
                editor.putString("profilePic", user.getProfilePic());
                editor.putString("user_id", user.getUserId());
                editor.apply();
                editor.commit();
                Intent i = null;
                if (CommonUtils.equalIgnoreCase(user.getRole(), "U")) {
                    i = new Intent(LoginActivity.this, CustomerDashBoardActivity.class);
                } else if(CommonUtils.equalIgnoreCase(user.getRole(), "P")){
                    i = new Intent(LoginActivity.this, CustomerDashBoardActivity.class);
                } else if(CommonUtils.equalIgnoreCase(user.getRole(), "D")){
                    i = new Intent(LoginActivity.this, CustomerDashBoardActivity.class);
                } else{
                    i = new Intent(LoginActivity.this, LoginActivity.class);
                }
                startActivity(i);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Some Error happened. Please try again", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
