package com.example.potholeReport;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText emailEditText;
    private Button reset_passwordButton;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText=(EditText) findViewById(R.id.email);
        reset_passwordButton= (Button) findViewById(R.id.reset_password);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress);
        AlertDialog dialog = builder.create();
        dialog.show();

        auth = FirebaseAuth.getInstance();

        reset_passwordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_password();
            }
        });

        dialog.dismiss();
    }
    private void reset_password(){
        String email= emailEditText.getText().toString().trim();

        if(email.isEmpty()){
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Please provide valid email");
            emailEditText.requestFocus();
            return;
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this,"Check your email to reset password",Toast.LENGTH_LONG).show();


                }
                else{
                    Toast.makeText(ForgotPassword.this,"Try again! Something went Wrong",Toast.LENGTH_LONG).show();
                }


            }
        });
    }
}