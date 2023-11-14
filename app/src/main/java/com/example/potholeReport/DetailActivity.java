package com.example.potholeReport;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
public class DetailActivity extends AppCompatActivity {
    TextView detailDesc, detailAddress,detailAddress2, detailContact;
    ImageView detailImage;
    String key = "";
    String imageUrl = "";
    Button call ,map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        detailDesc = findViewById(R.id.detailDesc);
        detailImage = findViewById(R.id.detailImage);
        detailAddress = findViewById(R.id.detailAddress);
        detailAddress2 = findViewById(R.id.detailAddress2);

        detailContact = findViewById(R.id.detailContact);

        call=findViewById(R.id.button3);
        map=findViewById(R.id.button6);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            detailDesc.setText(bundle.getString("Description"));
            detailAddress.setText(bundle.getString("Address"));
            detailAddress2.setText(bundle.getString("Address2"));

            detailContact.setText(bundle.getString("Contact"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }


        call.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String number=detailContact.getText().toString();
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" +number));

                startActivity(intent);
            }
        });
        map.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String map=detailAddress2.getText().toString();
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://maps.google.com/maps?q=loc:" +map));

                startActivity(intent);


            }
        });


    }

}