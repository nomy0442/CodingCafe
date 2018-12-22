package com.example.josee.codingcafe;

import android.accounts.Account;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Account_Settings_Page extends AppCompatActivity {
    Button passBtn;
    Button dataButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__settings__page);

        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        passBtn = findViewById(R.id.password);
        dataButton = findViewById(R.id.data);

        passBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent passwordUpdate = new Intent(Account_Settings_Page.this, Password.class);
                startActivity(passwordUpdate);
            }
        });

        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account_Settings_Page.this, UpdateUserData.class);
                startActivity(intent);
            }
        });
    }

    public void open_password(View v)
    {
        finish();
        Intent i = new Intent(Account_Settings_Page.this, Password.class);
        startActivity(i);
    }
}
