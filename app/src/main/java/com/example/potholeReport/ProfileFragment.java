package com.example.potholeReport;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private Button log_out;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        log_out= view.findViewById(R.id.log_out);


        log_out.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure you want to log out ?");
                builder.setTitle("");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent( getActivity(),MainActivity.class));
                    preferences.clearData(getActivity());
                    getActivity().finish();
                });
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                androidx.appcompat.app.AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


//        log_out.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent( getActivity(),MainActivity.class));
//                preferences.clearData(getActivity());
//                getActivity().finish();
//            }
//        });


        user= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
//        userID=user.getUid();

        if(user!= null) {
            userID = user.getUid(); //Do what you need to do with the id

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setView(R.layout.progress);
        AlertDialog dialog = builder.create();
        dialog.show();

        final TextView welcomeTextView= view.findViewById(R.id.greetings);
        final TextView usernameTextView= view.findViewById(R.id.username);
        final TextView emailTextView= view.findViewById(R.id.email);
        final TextView contactTextView= view.findViewById(R.id.contact);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    String username = userProfile.username;
                    String email = userProfile.email;
                    String contact = userProfile.contact;

                    welcomeTextView.setText("Welcome to Profile, " + username + " !");
                    usernameTextView.setText(username);
                    emailTextView.setText(email);
                    contactTextView.setText(contact);

                }

                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Kuch to badbad hai baba",Toast.LENGTH_LONG).show();

            }
        });
//        return view;
    }
        return view;
    }
}