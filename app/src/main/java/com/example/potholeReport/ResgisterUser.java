package com.example.potholeReport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ResgisterUser extends AppCompatActivity{
    private TextView  Banner ,register_user,log_in_page;
    private EditText etusername,etemail,etcontact,etpassword;
    private CheckBox isCitizenBox, isAuthorityBox;

    private FirebaseAuth mAuth;

    EditText fullName, email, password, phone;
    Button registerBtn;
    TextView goToLogin;
    boolean valid = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgister_user);

        mAuth =FirebaseAuth.getInstance();



        etusername = (EditText) findViewById(R.id.etusername);
        etcontact = (EditText) findViewById(R.id.etlatitude);
        etemail = (EditText) findViewById(R.id.etlongitude);
        etpassword = (EditText) findViewById(R.id.etpassword);

        Banner =(TextView) findViewById(R.id.banner);

        register_user=(Button) findViewById(R.id.register_complaint);
        goToLogin = (TextView) findViewById(R.id.log_in_page);

        isCitizenBox = (CheckBox) findViewById(R.id.iscitizenbox);
        isAuthorityBox = (CheckBox) findViewById(R.id.isauthoritybox);


        isCitizenBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    isAuthorityBox.setChecked(false);
                }

            }
        });

        isAuthorityBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    isCitizenBox.setChecked(false);
                }

            }
        });



        register_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField(etusername);
                checkField(etcontact);
                checkField(etemail);
                checkField(etpassword);

                if (valid) {
                    mAuth.createUserWithEmailAndPassword(etemail.getText().toString(), etpassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(ResgisterUser.this, "Account Created", Toast.LENGTH_SHORT).show();

                            String uid= mAuth.getUid();
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("username", etusername.getText().toString());
                            userInfo.put("contact", etcontact.getText().toString());
                            userInfo.put("email", etemail.getText().toString());
                            userInfo.put( "uid", uid);
//                            userInfo.put("password", etpassword.getText().toString());

                            if(isCitizenBox.isChecked()){
                                userInfo.put( "isUser",  0);
                            }

                            if(isAuthorityBox.isChecked()){
                                userInfo.put( "isUser",  1);
                            }

                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userInfo);


                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ResgisterUser.this, "Failed To Create Account ", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }

      });
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        Banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

    public void checkField(EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            textField.setError("Error");
            valid = false;
        } else {
            valid = true;
        }
    }

}