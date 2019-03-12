package com.malav.medicinesontheway.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;

import com.malav.medicinesontheway.R;
import com.malav.medicinesontheway.adapter.MyOrdersAdapter;
import com.malav.medicinesontheway.adapter.MyPrescriptionAdapter;
import com.malav.medicinesontheway.model.Prescription;
import com.malav.medicinesontheway.model.Store;
import com.malav.medicinesontheway.utils.Constants;
import com.malav.medicinesontheway.utils.JSONfunctions;
import com.malav.medicinesontheway.utils.QueryMapper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shahmalav on 20/06/17.
 */

public class MyPrescriptionActivity extends AppCompatActivity {

    private SharedPreferences someData;
    private ArrayList<Prescription> prescriptionList;
    private static String TAG = MyPrescriptionActivity.class.getSimpleName();
    private String userId;
    private MyPrescriptionAdapter myPrescriptionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_prescription);
        someData = getSharedPreferences(Constants.filename, 0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Prescriptions");

        new FetchMyPrescriptions().execute();

    }

    private class FetchMyPrescriptions extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            List<NameValuePair> para = new ArrayList<>();

            para.add(new BasicNameValuePair("user_id", someData.getString("user_id","0")));
            Log.d(TAG, "doInBackground: l_Id: " + someData.getString("user_id","0"));

            try{
                JSONObject jsonobject = JSONfunctions.makeHttpRequest(QueryMapper.URL_FETCH_MY_PRESCRIPTIONS, "POST", para);
                prescriptionList = new ArrayList<>();
                JSONArray jsonarray = jsonobject.getJSONArray("myPrescriptions");
                for (int i = 0; i < jsonarray.length(); i++)
                {
                    jsonobject = jsonarray.getJSONObject(i);

                    Prescription prescription= new Prescription();
                    prescription.setUser_id(jsonobject.getString("user_id"));
                    prescription.setPres_id(jsonobject.getString("pres_id"));
                    prescription.setPres_url(jsonobject.getString("pres_url"));
                    prescription.setPres_name(jsonobject.getString("pres_name"));

                    prescriptionList.add(prescription);
                 }
            }catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            ListView listStudents = (ListView) findViewById(R.id.listView);
            myPrescriptionAdapter = new MyPrescriptionAdapter(MyPrescriptionActivity.this,prescriptionList);
            listStudents.setAdapter(myPrescriptionAdapter);
            listStudents.setTextFilterEnabled(true);
        }
    }

    /*@Override
    public Intent getParentActivityIntent() {

        Intent newIntent=null;
        try {
            //you need to define the class with package name
            newIntent = new Intent(MyOrdersActivity.this, Class.forName("com.malav.medicinesontheway.activity.CustomerDashBoardActivity"));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return newIntent;
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_blank, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(), CustomerDashBoardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), CustomerDashBoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
