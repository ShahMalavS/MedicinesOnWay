package com.malav.medicinesontheway.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.malav.medicinesontheway.R;
import com.malav.medicinesontheway.adapter.StoreListAdapter;
import com.malav.medicinesontheway.model.Order;
import com.malav.medicinesontheway.model.Store;
import com.malav.medicinesontheway.model.User;
import com.malav.medicinesontheway.utils.CommonUtils;
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

public class OrderConfirmedActivity extends AppCompatActivity {

    private SharedPreferences someData;
    private User user = new User();
    private Order order = new Order();
    private Store store = new Store();
    private String pres_id, storeId;
    private String TAG = "OrderConfirmedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        someData = getSharedPreferences(Constants.filename, 0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Payment Info");

        pres_id = getIntent().getExtras().getString("pres_id");
        storeId = getIntent().getExtras().getString("storeId");
        Log.d(TAG, "onCreate: pres_id: "+pres_id);
        order.setPrescriptionId(pres_id);

        store.setIsHomeDelivery(1);

        final RadioGroup rgPaymentMode = (RadioGroup) findViewById(R.id.radioGroup);
        final RadioGroup rgDeliveryMode = (RadioGroup) findViewById(R.id.radioGroupDelivery);
        RadioButton rbCOD = (RadioButton) findViewById(R.id.cod);
        RadioButton rbOnline = (RadioButton) findViewById(R.id.online);
        RadioButton rbPickUp = (RadioButton) findViewById(R.id.pickup);
        RadioButton rbHomeDelivery = (RadioButton) findViewById(R.id.homeDelivery);
        final LinearLayout llHomeDelivery = (LinearLayout) findViewById(R.id.llHomeDelivery);
        final LinearLayout llPickUp = (LinearLayout) findViewById(R.id.llPickup);
        Button btnSubmit = (Button) findViewById(R.id.submit);
        final EditText edtTime = (EditText) findViewById(R.id.pickup_time);
        final EditText edtName = (EditText) findViewById(R.id.name);
        final EditText edtAddress = (EditText) findViewById(R.id.address);
        final EditText edtLandmark = (EditText) findViewById(R.id.landmark);
        final EditText edtPincode = (EditText) findViewById(R.id.pincode);
        final EditText edtPhoneNumber = (EditText) findViewById(R.id.phone_number);

        if(store.getIsHomeDelivery()==0){
            rbHomeDelivery.setEnabled(Boolean.FALSE);
        }else{
            rbHomeDelivery.setEnabled(Boolean.TRUE);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CommonUtils.equalIgnoreCase(order.getDeliveryMode(),"PickUp")){
                    order.setPickUpTime(edtTime.getText().toString());
                }else{
                    order.setPhoneNumber(edtPhoneNumber.getText().toString());
                    order.setStringAddress(edtAddress.getText().toString());
                    order.setPincode(edtPincode.getText().toString());
                    order.setLandmark(edtLandmark.getText().toString());
                    order.setCustomerName(edtName.getText().toString());
                }
                new orderMedicines().execute();
                /*Intent i = new Intent(OrderConfirmedActivity.this, MyOrdersActivity.class);
                startActivity(i);
                finish();*/
            }
        });

        rgDeliveryMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                View radioButton = rgDeliveryMode.findViewById(checkedId);
                int index = rgDeliveryMode.indexOfChild(radioButton);

                switch (index) {
                    case 0:
                        llPickUp.setVisibility(View.VISIBLE);
                        llHomeDelivery.setVisibility(View.GONE);
                        order.setDeliveryMode("PickUp");
                        break;
                    case 1:
                        llPickUp.setVisibility(View.GONE);
                        llHomeDelivery.setVisibility(View.VISIBLE);
                        order.setDeliveryMode("HomeDelivery");
                        break;
                }
            }
        });

        rgPaymentMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                View radioButton = rgPaymentMode.findViewById(checkedId);
                int index = rgPaymentMode.indexOfChild(radioButton);

                switch (index) {
                    case 0:
                        order.setPaymentMode("Online");
                        break;
                    case 1:
                        order.setPaymentMode("COD");
                        break;
                }
            }
        });
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

    private class orderMedicines extends AsyncTask<String, String, String> {

        private String orderid="0";
        private int success=0;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            List<NameValuePair> para = new ArrayList<>();

            para.add(new BasicNameValuePair("user_id", someData.getString("user_id","0")));
            para.add(new BasicNameValuePair("delivery_mode", order.getDeliveryMode()));
            if(CommonUtils.equalIgnoreCase(order.getDeliveryMode(),"PickUp")){
                para.add(new BasicNameValuePair("pickup_time", order.getPickUpTime()));
            }else{
                para.add(new BasicNameValuePair("customer_name", order.getCustomerName()));
                para.add(new BasicNameValuePair("address", order.getStringAddress()));
                para.add(new BasicNameValuePair("landmark", order.getLandmark()));
                para.add(new BasicNameValuePair("pincode", order.getPincode()));
                para.add(new BasicNameValuePair("phone", order.getPhoneNumber()));
            }
            para.add(new BasicNameValuePair("payment_mode", order.getPaymentMode()));
            para.add(new BasicNameValuePair("store_id", storeId));
            para.add(new BasicNameValuePair("pres_id", order.getPrescriptionId()));

            Log.d(TAG, "doInBackground: l_Id: " + someData.getString("user_id","0"));

            try{
                JSONObject jsonobject = JSONfunctions.makeHttpRequest(QueryMapper.URL_MAKE_ORDER, "POST", para);

                    orderid = jsonobject.getString("order_id");
                    success = jsonobject.getInt("success");
            }catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            if(success!=0) {
                Toast.makeText(OrderConfirmedActivity.this, "Your order is successful.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(OrderConfirmedActivity.this, MyOrdersActivity.class);
                startActivity(i);
                finish();
            }else{
                Toast.makeText(OrderConfirmedActivity.this, "Order failed. PLease try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
