package com.malav.medicinesontheway.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.malav.medicinesontheway.R;
import com.malav.medicinesontheway.utils.AppUtils;
import com.malav.medicinesontheway.utils.CommonUtils;
import com.malav.medicinesontheway.utils.Constants;
import com.malav.medicinesontheway.utils.QueryMapper;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by shahmalav on 18/06/17.
 */

public class AddPrescriptionActivity extends AppCompatActivity {

    private SharedPreferences someData;
    private ImageView imgPrescription;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    private Uri mImageCaptureUri;
    private String TAG = "AddPrescription";
    private Bitmap bitmap;
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    private EditText edtTitle;
    private String storeId="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prescription);
        someData = getSharedPreferences(Constants.filename, 0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Prescription");
        storeId = getIntent().getExtras().getString("storeId");

        String storeName = getIntent().getExtras().getString("storeName");
        String storeId = getIntent().getExtras().getString("storeId");
        TextView txtStoreName = (TextView) findViewById(R.id.name);
        txtStoreName.setText(storeName);

        edtTitle = (EditText) findViewById(R.id.title);

        Button btnSubmit = (Button) findViewById(R.id.submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommonUtils.hasText(edtTitle.getText().toString()) && mImageCaptureUri!=null && CommonUtils.hasText(mImageCaptureUri.toString())){
                    /*Intent i = new Intent(AddPrescriptionActivity.this, OrderConfirmedActivity.class);
                    startActivity(i);*/
                    uploadImage();
                }else{
                    Toast.makeText(AddPrescriptionActivity.this, "Please add prescription and title", Toast.LENGTH_SHORT).show();
                }
            }
        });


        imgPrescription = (ImageView) findViewById(R.id.prescription);
        final String [] items = new String [] {"Take from camera", "Select from gallery"};
        ArrayAdapter<String> adapter = new ArrayAdapter<> (this, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder	= new AlertDialog.Builder(this);

        builder.setTitle("Select Image");
        builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialog, int item ) { //pick from camera
                if (item == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                            "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

                    Log.d(TAG, "onClick: mImageCaptureUri: "+mImageCaptureUri);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                    try {
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else { //pick from file
                    Log.d(TAG, "onClick: from file");
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                }
            }
        } );

        final AlertDialog dialog = builder.create();
        imgPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case PICK_FROM_CAMERA:

                break;

            case PICK_FROM_FILE:
                Log.d(TAG, "onActivityResult: pick from file: "+mImageCaptureUri);
                Log.d(TAG, "onActivityResult: data: "+data);
                mImageCaptureUri = data.getData();
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageCaptureUri);
                    imgPrescription.setImageBitmap(bitmap);
                }catch(Exception e){
                    Log.e(TAG, "onActivityResult: exception: ", e);
                }

                break;

            case CROP_FROM_CAMERA:
                Log.d(TAG, "onActivityResult: data: "+data);
                Bundle extras = data.getExtras();

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    imgPrescription.setImageBitmap(photo);
                    uploadMultipart();
                }

                File f = new File(mImageCaptureUri.getPath());
                if (f.exists()){
                    f.delete();
                }
            break;
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, QueryMapper.URL_UPLOAD_PRESCRIPTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        Log.d(TAG, "onResponse: "+s);
                        //Showing toast message of the response
                        Toast.makeText(AddPrescriptionActivity.this, s , Toast.LENGTH_LONG).show();
                        JSONObject json = null;
                        String pres_id="0";
                        try {
                            json = new JSONObject(s);
                            pres_id = json.getString("pres_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent i = new Intent(AddPrescriptionActivity.this, OrderConfirmedActivity.class);
                        i.putExtra("pres_id", pres_id);
                        i.putExtra("storeId", storeId);
                        startActivity(i);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        Log.e(TAG, "onErrorResponse: ", volleyError);

                        //Showing toast
                        Toast.makeText(AddPrescriptionActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
                String name = edtTitle.getText().toString().trim();

                //Creating parameters
                Map<String,String> params = new Hashtable<>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name);
                params.put("cust_id", someData.getString("user_id","0"));

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void doCrop() {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        Log.d(TAG, "doCrop: indide crop");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0 );
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();
        } else {
            intent.setData(mImageCaptureUri);
            Log.d(TAG, "doCrop: uri"+mImageCaptureUri);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
            Intent i = new Intent(intent);
            ResolveInfo res	= list.get(0);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            startActivityForResult(i, CROP_FROM_CAMERA);
        }
    }

    public void uploadMultipart() {

        //getting the actual path of the image
        String path = AppUtils.getPath(mImageCaptureUri, AddPrescriptionActivity.this);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            uploadImage();

            /*new MultipartUploadRequest(this, uploadId, QueryMapper.URL_UPLOAD_IMAGE)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", "name") //Adding text parameter to the request
                    .addParameter("user_id", someData.getString("login_id","0")) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload*/

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
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
}
