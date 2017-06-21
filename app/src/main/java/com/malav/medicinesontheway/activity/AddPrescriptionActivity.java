package com.malav.medicinesontheway.activity;

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

import com.malav.medicinesontheway.R;
import com.malav.medicinesontheway.utils.AppUtils;
import com.malav.medicinesontheway.utils.CommonUtils;
import com.malav.medicinesontheway.utils.Constants;
import com.malav.medicinesontheway.utils.QueryMapper;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.util.List;
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

        String storeName = getIntent().getExtras().getString("storeName");
        String storeId = getIntent().getExtras().getString("storeId");
        TextView txtStoreName = (TextView) findViewById(R.id.name);
        txtStoreName.setText(storeName);

        final EditText edtTitle = (EditText) findViewById(R.id.title);

        Button btnSubmit = (Button) findViewById(R.id.submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommonUtils.hasText(edtTitle.getText().toString()) && mImageCaptureUri!=null && CommonUtils.hasText(mImageCaptureUri.toString())){
                    Intent i = new Intent(AddPrescriptionActivity.this, OrderConfirmedActivity.class);
                    startActivity(i);
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

            new MultipartUploadRequest(this, uploadId, QueryMapper.URL_UPLOAD_IMAGE)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", "name") //Adding text parameter to the request
                    .addParameter("user_id", someData.getString("login_id","0")) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

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
