package com.example.josee.codingcafe;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Password extends AppCompatActivity {
    Button saveBtn;
    EditText newPword;
    FirebaseAuth auth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        newPword = findViewById(R.id.newPassword);
        saveBtn = findViewById(R.id.savePasswordButton);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null)
        {
            dialog.setMessage("Changing password, please wait...");
            dialog.show();

            user.updatePassword(newPword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                dialog.dismiss();
                                Toast.makeText(Password.this, "Password has been changed", Toast.LENGTH_SHORT).show();
                                /*auth.signOut();
                                finish();
                                Intent i = new Intent(passwordIntent.this, MainActivity.class);
                                startActivity(i);*/
                            }
                            else
                            {
                                dialog.dismiss();
                                Toast.makeText(Password.this, "Password cannot be changed...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
