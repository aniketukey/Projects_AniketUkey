package com.example.potholeReport;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class Complaint_page extends AppCompatActivity implements LocationListener {

        ImageView uploadImage;
        Button saveButton;
        EditText uploadAddress,uploadAddress2, uploadDesc, uploadContact;
        String imageURL;
        Uri uri;
        Button button_location;
        EditText Address_line , Locality;
        LocationManager locationManager;
        @SuppressLint("CutPasteId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_complaintpage);


            Locality = findViewById(R.id.uploadAddress);
            Address_line = findViewById(R.id.uploadAddress2);
            button_location = findViewById(R.id.button_location);
            //Runtime permissions
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 100);
            }


            button_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //create method
                    getLocation();
                }
            });


            uploadImage = findViewById(R.id.uploadImage);
            uploadDesc = findViewById(R.id.uploadDesc);
            uploadAddress = findViewById(R.id.uploadAddress);
            uploadAddress2 =findViewById(R.id.uploadAddress2);
            uploadContact = findViewById(R.id.uploadContact);
            saveButton = findViewById(R.id.saveButton);
            ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK){
                                Intent data = result.getData();
                                uri = data.getData();
                                uploadImage.setImageURI(uri);
                            } else {
                                Toast.makeText(Complaint_page.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );
            uploadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent photoPicker = new Intent(Intent.ACTION_PICK);
                    photoPicker.setType("image/*");
                    activityResultLauncher.launch(photoPicker);
                }
            });
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveData();
                }
            });
        }
        public void saveData(){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Complaints")
                    .child(uri.getLastPathSegment());
            AlertDialog.Builder builder = new AlertDialog.Builder(Complaint_page.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress);
            AlertDialog dialog = builder.create();
            dialog.show();
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete());
                    Uri urlImage = uriTask.getResult();
                    imageURL = urlImage.toString();
                    uploadData();
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });
        }
        public void uploadData(){
            String address = uploadAddress.getText().toString();
            String address2 = uploadAddress2.getText().toString();
            String desc = uploadDesc.getText().toString();
            String contact = uploadContact.getText().toString();
            DataClass dataClass = new DataClass(address,address2, desc, contact, imageURL);
            //We are changing the child from title to currentDate,
            // because we will be updating title as well and it may affect child value.
            String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            FirebaseDatabase.getInstance().getReference("Complaints").child(currentDate)
                    .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Complaint_page.this, "Saved", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Complaint_page.this,Homepage.class));
//                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Complaint_page.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, Complaint_page.this);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, ""+location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder( getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);

//            textView_location.setText(addresses.get(0).getLocality());

//            String city = addresses.get(0).getLocality();
//            String state = addresses.get(0).getAdminArea();
//            String country = addresses.get(0).getCountryName();
//            String postalCode = addresses.get(0).getPostalCode();

            String a = (addresses.get(0).getLocality());
            String b = (addresses.get(0).getSubLocality());
            String c = (addresses.get(0).getPostalCode());
//            String e = (addresses.get(0).getSubAdminArea());

            Locality.setText(a+","+b+","+c);
            Address_line.setText(address);








        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void onProviderEnabled(String provider) {

    }

    public void onProviderDisabled(String provider) {

    }
}
