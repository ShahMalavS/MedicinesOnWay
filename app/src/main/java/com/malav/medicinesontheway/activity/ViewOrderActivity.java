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
import com.malav.medicinesontheway.model.Address;
import com.malav.medicinesontheway.model.Order;
import com.malav.medicinesontheway.model.Store;
import com.malav.medicinesontheway.utils.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shahmalav on 21/06/17.
 */

public class ViewOrderActivity extends AppCompatActivity {

    private SharedPreferences someData;
    private static String TAG = MyOrdersActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        someData = getSharedPreferences(Constants.filename, 0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("View Order");

        new FetchOrder().execute();

    }

    private class FetchOrder extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            List<NameValuePair> para = new ArrayList<>();

            para.add(new BasicNameValuePair("l_Id", someData.getString("user_id","0")));
            Log.d(TAG, "doInBackground: l_Id: " + someData.getString("user_id","0"));

            try{
                //JSONObject jsonobject = JSONfunctions.makeHttpRequest(QueryMapper.URL_FETCH_QUESTIONS, "POST", para);
                //JSONArray jsonarray = jsonobject.getJSONArray("allStores");
                //for (int i = 0; i < jsonarray.length(); i++)
                //{
                //jsonobject = jsonarray.getJSONObject(i);

                Address address = new Address("11, Satya sai clinic, Kalewadi Phata, Rahatani, Pune", "Kalewadi Phata", "", "Pune", "Shivrajnagar", "411027");

                Order order = new Order();
                order.setOrderId("4567");
                order.setUserId("1");
                order.setStoreId("2");
                order.setStoreName("Appolo Pharmacy");
                order.setStoreNumber("9999999999");
                order.setPhoneNumber("9876543211");
                order.setAddress(address);
                order.setPaymentMode("Online");
                order.setDeliveryMode("PickUp");
                order.setEmailId("abc@gmail.com");
                order.setDeliveryDate("24-06-2017");
                order.setDeliveryTime("16:30");
                order.setIsBillGenerated(1);
                order.setIsOrderComplete(0);
                order.setIsOrderConfirmed(1);
                order.setIsOrderExecuted(1);
                order.setIsPaymentDone(0);
                order.setAmount(587);


                // }
            }catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {

        }
    }

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
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
