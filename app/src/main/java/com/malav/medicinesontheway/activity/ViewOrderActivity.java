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
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.malav.medicinesontheway.R;
import com.malav.medicinesontheway.adapter.MyOrdersAdapter;
import com.malav.medicinesontheway.custom_font.MyTextView;
import com.malav.medicinesontheway.model.Address;
import com.malav.medicinesontheway.model.Order;
import com.malav.medicinesontheway.model.Store;
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
 * Created by shahmalav on 21/06/17.
 */

public class ViewOrderActivity extends AppCompatActivity {

    private SharedPreferences someData;
    private static String TAG = MyOrdersActivity.class.getSimpleName();
    private MyTextView txtOrderNumber, txtDate, txtDeliveryMode, txtDeliveryInfo, txtPaymentMode,
                txtPayNow, txtOrderStatus, txtViewPrescription, txtViewBill;
    private String orderId;

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

        orderId = getIntent().getExtras().getString("orderId");

        txtOrderNumber = (MyTextView) findViewById(R.id.txtOrderNumber);
        txtDate = (MyTextView) findViewById(R.id.txtDate);
        txtDeliveryMode = (MyTextView) findViewById(R.id.txtOrderDeliveryMode);
        txtDeliveryInfo = (MyTextView) findViewById(R.id.txtOrderDeliveryInfo);
        txtPaymentMode = (MyTextView) findViewById(R.id.txtPaymentStatus);
        txtPayNow = (MyTextView) findViewById(R.id.txtPayNow);
        txtOrderStatus = (MyTextView) findViewById(R.id.txtOrderStatus);
        txtViewPrescription = (MyTextView) findViewById(R.id.txtViewPrescription);
        txtViewBill = (MyTextView) findViewById(R.id.txtViewBill);

        new FetchOrder().execute();

        txtPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewOrderActivity.this,PaymentActivity.class);
                startActivity(i);
            }
        });

    }

    private class FetchOrder extends AsyncTask<String, String, String> {

        Order order = new Order();

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            List<NameValuePair> para = new ArrayList<>();

            para.add(new BasicNameValuePair("order_id", orderId));

            try{
                JSONObject jsonobject = JSONfunctions.makeHttpRequest(QueryMapper.URL_FETCH_PARTICULAR_ORDER, "POST", para);
                JSONArray jsonarray = jsonobject.getJSONArray("allOrders");
                for (int i = 0; i < jsonarray.length(); i++)
                {
                    jsonobject = jsonarray.getJSONObject(i);

                    Address address = new Address("11, Satya sai clinic, Kalewadi Phata, Rahatani, Pune", "Kalewadi Phata", "", "Pune", "Shivrajnagar", "411027");



                    order.setStoreNumber("9999999999");
                    order.setPhoneNumber("9876543211");
                    order.setAddress(address);
                    order.setEmailId("abc@gmail.com");

                    order.setOrderId(jsonobject.getString("trans_id"));
                    order.setUserId(jsonobject.getString("user_id"));
                    order.setStoreId(jsonobject.getString("medi_id"));
                    order.setPrescriptionId(jsonobject.getString("pres_id"));
                    order.setAmount(jsonobject.getInt("amount"));
                    order.setPaymentMode(jsonobject.getString("payment_type"));
                    order.setDeliveryMode(jsonobject.getString("delivery_type"));
                    order.setPrescriptionURL(jsonobject.getString("pres_url"));
                    order.setStoreName(jsonobject.getString("store_name"));
                    order.setStoreAddress(jsonobject.getString("store_address"));
                    order.setIsPaymentDone(jsonobject.getInt("payment_completed"));
                    order.setIsOrderConfirmed(jsonobject.getInt("order_confirmed"));
                    order.setIsOrderComplete(jsonobject.getInt("order_delivered"));
                    order.setIsOrderExecuted(jsonobject.getInt("order_ready"));
                    order.setIsBillGenerated(jsonobject.getInt("order_confirmed"));
                    order.setStringAddress(jsonobject.getString("final_address"));
                    order.setDeliveryTime(jsonobject.getString("delivery_time"));
                    order.setDeliveryDate(jsonobject.getString("delivery_date"));
                    order.setPickUpTime(jsonobject.getString("pickup_time"));


                }
            }catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            txtOrderNumber.setText(order.getOrderId());
            if(CommonUtils.equalIgnoreCase(order.getPaymentMode(),"0")){
                txtPaymentMode.setText("COD");
            }else{
                txtPaymentMode.setText("Online");
            }

            if(CommonUtils.equalIgnoreCase(order.getDeliveryTime(),"0")){
                txtDeliveryMode.setText("PickUp");
            }else{
                txtDeliveryMode.setText("Home Delivery");
            }

            if(order.getIsOrderComplete()==1){
                txtOrderStatus.setText("Order Complete");
            }else if(order.getIsOrderExecuted()==1){
                txtOrderStatus.setText("Order Ready");
            }else if(order.getIsOrderConfirmed()==1){
                txtOrderStatus.setText("Order Confirmed");
            }else {
                txtOrderStatus.setText("Order Pending");
            }

            if(order.getIsBillGenerated()==1 && order.getIsPaymentDone()==0){
                txtPayNow.setVisibility(View.GONE);
            }else if(order.getIsBillGenerated()==1){
                txtPayNow.setVisibility(View.VISIBLE);
            }else{
                txtPayNow.setVisibility(View.GONE);
            }

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
