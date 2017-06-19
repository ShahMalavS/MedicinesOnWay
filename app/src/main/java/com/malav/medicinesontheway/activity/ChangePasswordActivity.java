package com.malav.medicinesontheway.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.malav.medicinesontheway.R;
import com.malav.medicinesontheway.utils.Constants;
import com.malav.medicinesontheway.utils.JSONfunctions;
import com.malav.medicinesontheway.utils.QueryMapper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shahmalav on 03/03/17.
 */

public class ChangePasswordActivity extends ActionBarActivity {

    private EditText emailId,oldPassword,newPassword, verifyPassword;
    private String text_emailId, text_oldPassword, text_newPassword, text_verifyPassword, success, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences someData = getSharedPreferences(Constants.filename, 0);
        text_emailId = someData.getString("emailId", "");

        emailId = (EditText) findViewById(R.id.email_id);
        oldPassword = (EditText) findViewById(R.id.old_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        verifyPassword = (EditText) findViewById(R.id.verify_password);
        Button changePassword = (Button) findViewById(R.id.change_password);

        emailId.setText(text_emailId);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_emailId = emailId.getText().toString();
                text_newPassword = newPassword.getText().toString();
                text_oldPassword = oldPassword.getText().toString();
                text_verifyPassword = verifyPassword.getText().toString();

                if(text_newPassword.equals(text_verifyPassword)) {
                    new changePassword().execute();
                }else{
                    Toast.makeText(ChangePasswordActivity.this, "New Password and Verify Password should be same", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private class changePassword extends AsyncTask<String, String, String>
    {
        int flag = 0;
        JSONObject jsonobject;
        JSONArray jsonarray;
        ProgressDialog progress;

        protected void onPreExecute()
        {
            super.onPreExecute();
            progress = new ProgressDialog(ChangePasswordActivity.this);
            progress.setMessage("Changing Password...");
            progress.setIndeterminate(false);
            progress.setCancelable(true);
            progress.show();
        }

        protected String doInBackground(String... params)
        {
            List<NameValuePair> para = new ArrayList<NameValuePair>();
            para.add(new BasicNameValuePair("emailId", text_emailId));
            para.add(new BasicNameValuePair("oldPassword", text_oldPassword));
            para.add(new BasicNameValuePair("newPassword", text_newPassword));
            para.add(new BasicNameValuePair("verifyPassword", text_verifyPassword));

            Log.d("email!", text_emailId);
            Log.d("old!", text_oldPassword);
            Log.d("new!", text_newPassword);
            Log.d("verify!", text_verifyPassword);
            Log.d("request!", "starting");

            try{
                JSONObject json = JSONfunctions.makeHttpRequest(QueryMapper.URL_CHANGE_PASSWORD, "POST", para);
                Log.d("Login attempt", json.toString());
                success = json.getString("success");
                message = json.getString("message");
            }catch(JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url)
        {
            progress.dismiss();
            Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_LONG).show();
            if(!success.equalsIgnoreCase("0")){
                /*Intent i = new Intent(ChangePasswordActivity.this, CustomerDashBoardActivity.class);
                startActivity(i);*/
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_blank, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}