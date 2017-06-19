package com.malav.medicinesontheway.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.malav.medicinesontheway.R;
import com.malav.medicinesontheway.custom_font.MyEditText;
import com.malav.medicinesontheway.custom_font.MyTextView;
import com.malav.medicinesontheway.model.User;
import com.malav.medicinesontheway.utils.CommonUtils;
import com.malav.medicinesontheway.utils.JSONfunctions;
import com.malav.medicinesontheway.utils.QueryMapper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shahmalav on 18/12/16.
 */

public class SignUpActivity extends AppCompatActivity {

    private MyEditText etPhoneNum, etPassword, etRePassword, etEmailId, etDOB, etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        MyTextView btSignIn = (MyTextView) findViewById(R.id.signin1);
        MyTextView btSignUp = (MyTextView) findViewById(R.id.signUp);
        etPhoneNum = (MyEditText) findViewById(R.id.phoneNum);
        etPassword = (MyEditText) findViewById(R.id.password);
        etRePassword = (MyEditText) findViewById(R.id.rePassword);
        etEmailId = (MyEditText) findViewById(R.id.emailId);
        etName = (MyEditText) findViewById(R.id.name);
        etDOB = (MyEditText) findViewById(R.id.dob);

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkForSignUp = 1;
                String sEmailId = etEmailId.getText().toString();
                String sPassword = etPassword.getText().toString();
                String sPhoneNum = etPhoneNum.getText().toString();
                String sRePassword = etRePassword.getText().toString();
                String sName = etName.getText().toString();
                String sDOB = etDOB.getText().toString();
                if(CommonUtils.isNull(sEmailId)){
                    etEmailId.setError("Cannot be left Blank");
                    checkForSignUp = 0;
                }
                if(CommonUtils.isNull(sPassword)){
                    etPassword.setError("Cannot be left Blank");
                    checkForSignUp = 0;
                }
                if(CommonUtils.isNull(sPhoneNum)){
                    etPhoneNum.setError("Cannot be left Blank");
                    checkForSignUp = 0;
                }
                if(CommonUtils.isNull(sRePassword)){
                    etRePassword.setError("Cannot be left Blank");
                    checkForSignUp = 0;
                }else{
                    if(!CommonUtils.equalIgnoreCase(sPassword, sRePassword)){
                        etRePassword.setError("Re-Enter Password should match Password");
                        checkForSignUp = 0;
                    }
                }
                if(CommonUtils.isNull(sName)){
                    etName.setError("Cannot be left Blank");
                    checkForSignUp = 0;
                }
                if(CommonUtils.isNull(sDOB)){
                    etDOB.setError("Cannot be left Blank");
                    checkForSignUp = 0;
                }
                if(checkForSignUp==1){
                    User user = new User();
                    user.setUserName(sName);
                    user.setEmail(sEmailId);
                    user.setPassword(sPassword);
                    user.setDob(sDOB);
                    user.setPhoneNumber(sPhoneNum);
                    new SignUp(user).execute();
                }
            }
        });

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private class SignUp extends AsyncTask<Void, Void, Boolean> {

        private String success = "0";
        private User user;


        SignUp(User user) {
            this.user = user;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                List<NameValuePair> para = new ArrayList<>();
                para.add(new BasicNameValuePair("emailId", user.getEmail()));
                para.add(new BasicNameValuePair("password", user.getPassword()));
                para.add(new BasicNameValuePair("dob", user.getDob()));
                para.add(new BasicNameValuePair("phone", user.getPhoneNumber()));
                para.add(new BasicNameValuePair("name", user.getUserName()));
                Log.d("request!", "starting");

                JSONObject json = JSONfunctions.makeHttpRequest(QueryMapper.URL_REGISTER_NEW_USER, "POST", para);
                Log.d("Login attempt", json.toString());
                success = json.getString("success");
                if (!success.equalsIgnoreCase("0")) {
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
                finish();
            } else {
                Toast.makeText(SignUpActivity.this, "Some Error happened. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
