package com.example.ekmuthoboi;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BookDescription extends AppCompatActivity {
    TextView title, des;
    String product_des="";
    Bundle bundle = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_description);


        title=(TextView) findViewById(R.id.title_des);
        des = (TextView) findViewById(R.id.des);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                product_des= null;
            } else {
                product_des= extras.getString("product_DESC");
            }
        } else {
            product_des= (String) savedInstanceState.getSerializable("product_DESC");
        }


        des.setText(product_des);
    }
}
