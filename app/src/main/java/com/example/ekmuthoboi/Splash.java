package com.example.ekmuthoboi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Splash extends AppCompatActivity {

    Animation topAnim, botAnim;
    ImageView image;
    Button getstarted;
    TextView slogan,itbl;
    Context context;

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        MeterReaderDBAdapter dbAdapter=new MeterReaderDBAdapter(context);
//        dbAdapter.open();
//        dbAdapter.close();



        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        botAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        image = findViewById(R.id.logo);
        slogan = findViewById(R.id.slogan);
        itbl = findViewById(R.id.itbl);
        getstarted = findViewById(R.id.get_started);

        slogan.setTypeface(ResourcesCompat.getFont(this, R.font.aldrich));
        itbl.setTypeface(ResourcesCompat.getFont(this, R.font.kaushan_script));

        image.setAnimation(topAnim);
        slogan.setAnimation(botAnim);
        itbl.setAnimation(botAnim);

        getstarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Splash.this,CategoryListView.class);

                    startActivity(i);

            }
        });





//        Thread t= new Thread() {
//            public void run() {
//
//                try {
//
//                    sleep(5000);
//                    Intent i=new Intent(Splash.this,CategoryListView.class);
//
//                    startActivity(i);
//
//
//
//                    finish();
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        t.start();
    }

    @Override
    protected void onResume() {
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
        super.onResume();
    }

    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(Splash.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(Splash.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(Splash.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Splash.this, "Camera Permission Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                Toast.makeText(Splash.this, "Camera Permission Denied", Toast.LENGTH_SHORT) .show();
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Splash.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Splash.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
