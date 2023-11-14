package com.example.potholeReport;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private TextView register,forgot_password;
    private EditText  etemail, etpassword;

    private Button log_in;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this::onClick);

        forgot_password = (TextView) findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(this::onClick);

        log_in = (Button) findViewById(R.id.log_in);
        log_in.setOnClickListener(this::onClick);

        etemail=(EditText)  findViewById(R.id.etlongitude);
        etpassword=(EditText) findViewById(R.id.etpassword);

        mAuth= FirebaseAuth.getInstance();


    }

    public void onClick(View v) {
        switch (v.getId()){

            case R.id.register:
                startActivity(new Intent(this,ResgisterUser.class));
                break;

            case R.id.log_in:
                userLogin();
                break;

            case R.id.forgot_password:
                startActivity(new Intent(this,ForgotPassword.class));
                break;


        }
    }

    private void userLogin() {

        String email= etemail.getText().toString().trim();
        String password= etpassword.getText().toString().trim();

        if(email.isEmpty()){
            etemail.setError("Enter Email");
            etemail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etemail.setError("Incorrect Email");
            etemail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            etpassword.setError("Enter Password");
            etpassword.requestFocus();
            return;
        }
        if (password.length()<6){
            etpassword.setError("Incorrect Password");
            etpassword.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {



                    String uid =task.getResult().getUser().getUid();
                    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("Users").child(uid).child("isUser").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int isUser=snapshot.getValue(Integer.class);

                            if(isUser==0){
//                                startActivity(new Intent(MainActivity.this, Homepage.class));
                                preferences.setDataLogin(MainActivity.this, true);
                                preferences.setDataAs(MainActivity.this, "Citizen");
                                startActivity(new Intent(MainActivity.this, Homepage.class));
                            }
                            else {
//                                startActivity(new Intent(MainActivity.this, Authority.class));
                                preferences.setDataLogin(MainActivity.this, true);
                                preferences.setDataAs(MainActivity.this, "Authority");
                                startActivity(new Intent(MainActivity.this, Authority.class));
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


//                    startActivity(new Intent(MainActivity.this, Homepage.class));


                }
                else {
                    Toast.makeText(MainActivity.this,"Error while logging in...",Toast.LENGTH_LONG).show();

                }
            }
        });


    }
    @Override
    protected void onStart() {
        super.onStart();
        if (preferences.getDataLogin(this)) {
            if (preferences.getDataAs(this).equals("Citizen")) {
                startActivity(new Intent(this, Homepage.class));
                finish();
            } else {
                startActivity(new Intent(this, Authority.class));
                finish();
            }
        }
    }
}





