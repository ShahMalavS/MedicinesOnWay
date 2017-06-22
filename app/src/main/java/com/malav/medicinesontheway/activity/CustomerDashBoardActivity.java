package com.malav.medicinesontheway.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.malav.medicinesontheway.R;
import com.malav.medicinesontheway.adapter.StoreListAdapter;
import com.malav.medicinesontheway.fragment.FragmentDrawer;
import com.malav.medicinesontheway.model.Store;
import com.malav.medicinesontheway.utils.AppUtils;
import com.malav.medicinesontheway.utils.CommonUtils;
import com.malav.medicinesontheway.utils.Constants;
import com.malav.medicinesontheway.utils.JSONfunctions;
import com.malav.medicinesontheway.utils.PlaceJSONParser;
import com.malav.medicinesontheway.utils.QueryMapper;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Dashboard Class - Entry point for User
 * Created by shahmalav on 26/02/17.
 */

public class CustomerDashBoardActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private SharedPreferences someData;
    private Uri mImageCaptureUri;
    private de.hdodenhof.circleimageview.CircleImageView banar1;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<Store> storeList;
    private static String TAG = CustomerDashBoardActivity.class.getSimpleName();
    private String userId;
    private StoreListAdapter storeListAdapter;
    private AppCompatAutoCompleteTextView atvPlaces;
    private PlacesTask placesTask;
    private ParserTask parserTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        someData = getSharedPreferences(Constants.filename, 0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) && !(checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)){
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE }, 1);
            }
        }

        userId = someData.getString("user_id", "");
        //myProfilePic = someData.getString("profilePic", "");
        String myName = someData.getString("name", "");
        String myEmail = someData.getString("emailId", "");
        String mypic = someData.getString("profilePic", "");
        Log.d(TAG, "onCreateView: " + userId);
        Log.d(TAG, "onCreateView: " + mypic);

        atvPlaces = (AppCompatAutoCompleteTextView) findViewById(R.id.atv_places);
        atvPlaces.setThreshold(1);

        atvPlaces.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchAllStores().execute();
            }
        });




        String[] web = CustomerDashBoardActivity.this.getResources().getStringArray(R.array.nav_drawer_labels);

        CustomList adapter1 = new CustomList(CustomerDashBoardActivity.this, web, Constants.imageId);
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter1);

        TextView txtName = (TextView) findViewById(R.id.name);
        txtName.setText("Hi " + myName + "!!");

        new FetchAllStores().execute();

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

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                    try {
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else { //pick from file
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                }
            }
        } );

        final AlertDialog dialog = builder.create();
        banar1 = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.banar1);
        if(CommonUtils.isNotNull(someData.getString("profilePic",""))){
            Glide.with(CustomerDashBoardActivity.this).load(someData.getString("profilePic","")).crossFade(500).into(banar1);
        }
        banar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        TextView drawerName = (TextView) findViewById(R.id.earn);
        drawerName.setText(myName);

        FragmentDrawer drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case PICK_FROM_CAMERA:
                doCrop();
                break;

            case PICK_FROM_FILE:
                mImageCaptureUri = data.getData();
                doCrop();
                break;

            case CROP_FROM_CAMERA:
                Bundle extras = data.getExtras();

                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    banar1.setImageBitmap(photo);
                    uploadMultipart();
                }

                File f = new File(mImageCaptureUri.getPath());
                if (f.exists()){
                    f.delete();
                }

                break;

        }
    }

    public void uploadMultipart() {

        //getting the actual path of the image
        String path = AppUtils.getPath(mImageCaptureUri, CustomerDashBoardActivity.this);

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

    private void doCrop() {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0 );
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();
        } else {
            intent.setData(mImageCaptureUri);
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
            Intent i = new Intent(intent);
            ResolveInfo res	= list.get(0);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            startActivityForResult(i, CROP_FROM_CAMERA);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Errro Logging out.. Please try again", Toast.LENGTH_SHORT).show();
    }

    private class CustomList extends ArrayAdapter<String> {

        private final Activity context;
        private final String[] web;
        private final Integer[] imageId;
        private CustomList(Activity context, String[] web, Integer[] imageId) {
            super(context, R.layout.nav_drawer_row, web);
            this.context = context;
            this.web = web;
            this.imageId = imageId;
        }
        @NonNull
        @Override
        public View getView(final int position, View view, @NonNull ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();

            View rowView = inflater.inflate(R.layout.nav_drawer_row, null, true);

            LinearLayout ll = (LinearLayout) rowView.findViewById(R.id.ll);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
            txtTitle.setText(web[position]);
            imageView.setImageResource(imageId[position]);
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToNewPage(position);
                }
            });
            return rowView;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void navigateToNewPage(int position){

        Intent i = new Intent(CustomerDashBoardActivity.this, CustomerDashBoardActivity.class);
        switch(position){
            case 0:
                /*i = new Intent(CustomerDashBoardActivity.this, CustomerDashBoardActivity.class);
                startActivity(i);*/
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                i = new Intent(CustomerDashBoardActivity.this, MyOrdersActivity.class);
                startActivity(i);
                break;
            case 2:
                i = new Intent(CustomerDashBoardActivity.this, MyPrescriptionActivity.class);
                startActivity(i);
                break;
            case 3:
                i = new Intent(CustomerDashBoardActivity.this, AddReminderActivity.class);
                startActivity(i);
                break;
            case 4:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Click on https://play.google.com/store/apps/details?id=com.company.medicinesonway to download MedicinesOnWay";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Download MedicinesOnWay");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
            case 5:
                i = new Intent(CustomerDashBoardActivity.this, ContactUs.class);
                startActivity(i);
                break;
            case 6:
                i = new Intent(CustomerDashBoardActivity.this, ChangePasswordActivity.class);
                startActivity(i);
                break;
            case 7:
                SharedPreferences.Editor editor = someData.edit();
                editor.remove("role");
                editor.remove("login");
                editor.remove("name");
                editor.remove("profilePic");
                editor.remove("emailId");
                editor.remove("profilePic");
                //editor.remove("token");
                editor.remove("registered");
                editor.remove("user_id");
                editor.remove("login_id");
                editor.remove("wordJsonArray");
                editor.apply();
                editor.commit();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                            }
                        });
                i = new Intent(CustomerDashBoardActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }

    private class FetchAllStores extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            List<NameValuePair> para = new ArrayList<>();

            para.add(new BasicNameValuePair("l_Id", someData.getString("user_id","0")));
            Log.d(TAG, "doInBackground: l_Id: " + someData.getString("user_id","0"));

            try{
                //JSONObject jsonobject = JSONfunctions.makeHttpRequest(QueryMapper.URL_FETCH_QUESTIONS, "POST", para);
                storeList = new ArrayList<>();
                //JSONArray jsonarray = jsonobject.getJSONArray("allStores");
                //for (int i = 0; i < jsonarray.length(); i++)
                //{
                    //jsonobject = jsonarray.getJSONObject(i);
                    Store store = new Store();
                    store.setStoreId("1");
                    store.setStoreName("Sai Krupa Pharmacy");
                    store.setPhoneNumber("9876543211");
                    store.setAddress("A-12, Sai Complex, Pimple Saudagar, Rahatani, Pune");
                    store.setCloseTime("11:00 PM");
                    store.setOpenTime("08:00 AM");
                    store.setEmailId("abc@gmail.com");
                    store.setIs24Hours(0);
                    store.setOpenDays(new int[]{1,1,1,1,1,1,1});
                    store.setPinCode("411027");
                    storeList.add(store);

                    store = new Store();
                    store.setStoreId("2");
                    store.setStoreName("Lotus Pharmacy");
                    store.setPhoneNumber("9876543215");
                    store.setAddress("C-1, Westend Mall, Aundh, Pune");
                    store.setCloseTime("11:00 PM");
                    store.setOpenTime("08:00 AM");
                    store.setEmailId("xyz@gmail.com");
                    store.setIs24Hours(0);
                    store.setOpenDays(new int[]{1,1,1,1,1,1,1});
                    store.setPinCode("411027");
                    storeList.add(store);

                    store = new Store();
                    store.setStoreId("3");
                    store.setStoreName("Jahangir Pharmacy");
                    store.setPhoneNumber("8765309876");
                    store.setAddress("14, Anamika Buildings, Aundh, Pune");
                    store.setCloseTime("11:00 PM");
                    store.setOpenTime("08:00 AM");
                    store.setEmailId("pqr@gmail.com");
                    store.setIs24Hours(0);
                    store.setOpenDays(new int[]{1,1,1,1,1,1,1});
                    store.setPinCode("411027");
                    storeList.add(store);

                    store = new Store();
                    store.setStoreId("4");
                    store.setStoreName("Apolo Pharmacy");
                    store.setPhoneNumber("9876543098");
                    store.setAddress("11, Satya sai clinic, Kalewadi Phata, Rahatani, Pune");
                    store.setCloseTime("11:00 PM");
                    store.setOpenTime("08:00 AM");
                    store.setEmailId("def@gmail.com");
                    store.setIs24Hours(0);
                    store.setOpenDays(new int[]{1,1,1,1,1,1,1});
                    store.setPinCode("411027");
                    storeList.add(store);
               // }
            }catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            ListView listStudents = (ListView) findViewById(R.id.listView);
            storeListAdapter = new StoreListAdapter(CustomerDashBoardActivity.this,storeList);
            listStudents.setAdapter(storeListAdapter);
            listStudents.setTextFilterEnabled(true);
        }
    }

    private class submitAnswer extends AsyncTask<String, String, String> {

        private ProgressDialog progress;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progress = new ProgressDialog(CustomerDashBoardActivity.this);
            progress.setMessage("Submitting Answer...");
            progress.setIndeterminate(false);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> para = new ArrayList<>();
           /* para.add(new BasicNameValuePair("common_L_Id", someData.getString("user_id","")));
            para.add(new BasicNameValuePair("question_id", question.getQuestionId()));
            para.add(new BasicNameValuePair("answer", question.getAnswer()));

            try {
                JSONObject jsonobject = JSONfunctions.makeHttpRequest(QueryMapper.URL_SUBMIT_ANSWER, "POST", para);
                Log.d("Submitting Answer", jsonobject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            progress.dismiss();
        }
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception download url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyD0ErUPee_58ZcR0t9T3NVpZY6tdJdw5fQ";

            String input="";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input+"&"+types+"&"+sensor+"&"+key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

            try{
                // Fetching the data from we service
                data = downloadUrl(url);
                Log.d(TAG, "doInBackground: data: "+data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }
    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[] { "description"};
            int[] to = new int[] { android.R.id.text1 };

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

            // Setting the adapter
            atvPlaces.setAdapter(adapter);
        }
    }

}

