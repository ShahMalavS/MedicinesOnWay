package com.malav.medicinesontheway.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.malav.medicinesontheway.R;

import static android.Manifest.permission.INTERNET;

/**
 * Created by shahmalav on 20/08/16.
 */

public class ContactUs extends AppCompatActivity {

    TextView email, website, blog;
    ImageView emailImage, faImage, twImage, gpImage, igImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        email = (TextView) findViewById(R.id.email_id);
        website = (TextView) findViewById(R.id.website);
        blog = (TextView) findViewById(R.id.blog);
        emailImage = (ImageView) findViewById(R.id.icon_email);
        faImage = (ImageView) findViewById(R.id.icon_fa);
        twImage = (ImageView) findViewById(R.id.icon_tw);
        igImage = (ImageView) findViewById(R.id.icon_ig);
        gpImage = (ImageView) findViewById(R.id.icon_gp);


        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { email.getText().toString().trim() });// selected email adress.
                startActivity(emailIntent);
            }
        });

        emailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { email.getText().toString().trim() });// selected email adress.
                startActivity(emailIntent);
            }
        });

        faImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 0;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(INTERNET) == PackageManager.PERMISSION_GRANTED){
                        Intent intent = new Intent(ContactUs.this, ContactUsWebView.class);
                        intent.putExtra("websiteUrl", "https://www.facebook.com/holistree");
                        intent.putExtra("title", "Facebook");
                        startActivity(intent);
                    }else{
                        requestPermissions(new String[]{INTERNET}, 0);
                    }
                }else{
                    Intent intent = new Intent(ContactUs.this, ContactUsWebView.class);
                    intent.putExtra("websiteUrl", "https://www.facebook.com/holistree");
                    intent.putExtra("title", "Facebook");
                    startActivity(intent);
                }
            }
        });

        igImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 0;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(INTERNET) == PackageManager.PERMISSION_GRANTED){
                        Intent intent = new Intent(ContactUs.this, ContactUsWebView.class);
                        intent.putExtra("websiteUrl", "https://www.instagram.com/holistree/");
                        intent.putExtra("title", "Instagram");
                        startActivity(intent);
                    }else{
                        requestPermissions(new String[]{INTERNET}, 0);
                    }
                }else{
                    Intent intent = new Intent(ContactUs.this, ContactUsWebView.class);
                    intent.putExtra("websiteUrl", "https://www.instagram.com/holistree/");
                    intent.putExtra("title", "Instagram");
                    startActivity(intent);
                }
            }
        });

        twImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 0;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(INTERNET) == PackageManager.PERMISSION_GRANTED){
                        Intent intent = new Intent(ContactUs.this, ContactUsWebView.class);
                        intent.putExtra("websiteUrl", "https://twitter.com/theholistree");
                        intent.putExtra("title", "Twitter");
                        startActivity(intent);
                    }else{
                        requestPermissions(new String[]{INTERNET}, 0);
                    }
                }else{
                    Intent intent = new Intent(ContactUs.this, ContactUsWebView.class);
                    intent.putExtra("websiteUrl", "https://twitter.com/theholistree");
                    intent.putExtra("title", "Twitter");
                    startActivity(intent);
                }
            }
        });

        gpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 0;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(INTERNET) == PackageManager.PERMISSION_GRANTED){
                        Intent intent = new Intent(ContactUs.this, ContactUsWebView.class);
                        intent.putExtra("websiteUrl", "https://plus.google.com/105096416330610265727");
                        intent.putExtra("title", "Google Plus");
                        startActivity(intent);
                    }else{
                        requestPermissions(new String[]{INTERNET}, 0);
                    }
                }else{
                    Intent intent = new Intent(ContactUs.this, ContactUsWebView.class);
                    intent.putExtra("websiteUrl", "https://plus.google.com/105096416330610265727");
                    intent.putExtra("title", "Google Plus");
                    startActivity(intent);
                }
            }
        });

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 0;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(INTERNET) == PackageManager.PERMISSION_GRANTED){
                        Intent intent = new Intent(ContactUs.this, ContactUsWebView.class);
                        intent.putExtra("websiteUrl", "http://www.holistree.in");
                        intent.putExtra("title", "Holistree");
                        startActivity(intent);
                    }else{
                        requestPermissions(new String[]{INTERNET}, 0);
                    }
                }else{
                    Intent intent = new Intent(ContactUs.this, ContactUsWebView.class);
                    intent.putExtra("websiteUrl", "http://www.holistree.in");
                    intent.putExtra("title", "Holistree");
                    startActivity(intent);
                }
            }
        });

        blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 0;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(INTERNET) == PackageManager.PERMISSION_GRANTED){
                        Intent intent = new Intent(ContactUs.this, ContactUsWebView.class);
                        intent.putExtra("websiteUrl", "http://www.holistree.in/blog");
                        intent.putExtra("title", "Blogs");
                        startActivity(intent);
                    }else{
                        requestPermissions(new String[]{INTERNET}, 0);
                    }
                }else{
                    Intent intent = new Intent(ContactUs.this, ContactUsWebView.class);
                    intent.putExtra("websiteUrl", "http://www.holistree.in/blog");
                    intent.putExtra("title", "Blogs");
                    startActivity(intent);
                }
            }
        });
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
