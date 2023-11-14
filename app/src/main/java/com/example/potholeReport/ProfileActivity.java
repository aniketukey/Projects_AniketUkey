package com.example.potholeReport;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private Button log_out, go_to_homepage;
//    private TextView textView9,textView10, textView11 ,username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        go_to_homepage= findViewById(R.id.go_to_homepage);
        log_out=(Button)  findViewById(R.id.log_out);

        log_out.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure you want to log out ?");
                builder.setTitle("");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent( ProfileActivity.this,MainActivity.class));
                    preferences.clearData(ProfileActivity.this);
                    ProfileActivity.this.finish();
                });
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                androidx.appcompat.app.AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        go_to_homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,Authority.class));
            }
        });



        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userID=user.getUid();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress);
        AlertDialog dialog = builder.create();
        dialog.show();

        final TextView welcomeTextView=(TextView) findViewById(R.id.greetings);
        final TextView usernameTextView=(TextView) findViewById(R.id.username);
        final TextView emailTextView=(TextView) findViewById(R.id.email);
        final TextView contactTextView=(TextView) findViewById(R.id.contact);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile=snapshot.getValue(User.class);

                if(userProfile !=null){
                    String username =userProfile.username;
                    String email =userProfile.email;
                    String contact =userProfile.contact;



                    welcomeTextView.setText("Welcome to Profile, "+username+" !");
                    usernameTextView.setText(username);
                    emailTextView.setText(email);
                    contactTextView.setText(contact);

                }
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();

            }
        });
    }
}