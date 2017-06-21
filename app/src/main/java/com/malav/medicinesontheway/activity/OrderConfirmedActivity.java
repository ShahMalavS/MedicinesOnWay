package com.malav.medicinesontheway.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.malav.medicinesontheway.R;
import com.malav.medicinesontheway.model.Order;
import com.malav.medicinesontheway.model.Store;
import com.malav.medicinesontheway.model.User;
import com.malav.medicinesontheway.utils.Constants;

/**
 * Created by shahmalav on 20/06/17.
 */

public class OrderConfirmedActivity extends AppCompatActivity {

    private SharedPreferences someData;
    private User user = new User();
    private Order order = new Order();
    private Store store = new Store();

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

        if(store.getIsHomeDelivery()==0){
            rbHomeDelivery.setEnabled(Boolean.FALSE);
        }else{
            rbHomeDelivery.setEnabled(Boolean.TRUE);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderConfirmedActivity.this, MyOrdersActivity.class);
                startActivity(i);
                finish();
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
}
