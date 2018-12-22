package com.example.josee.codingcafe;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.josee.codingcafe.Registration_Screen.GALLERY_PICK;

public class UpdateUserData extends AppCompatActivity {

    private CircleImageView imageView;
    ImageButton backButton, saveButton;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    private EditText firstName, lastName, contact, address, beerDay;
    private Button updateBtn, birthdayBtn;

    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference usersRef;
    private StorageReference UserProfileImageRef;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_data);

        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);

        imageView = (CircleImageView) findViewById(R.id.addUpdateImage);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        contact = findViewById(R.id.kontakNumber);
        address = findViewById(R.id.addressEditText);
//        updateBtn = findViewById(R.id.updateButton);
        beerDay = findViewById(R.id.bDayEditText);
        birthdayBtn = findViewById(R.id.beerDayBtn);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateUserData.this, Account_Settings_Page.class));
            }
        });

        birthdayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(UpdateUserData.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        beerDay.setText(mDay + "/" + (mMonth + 1) + "/" + mYear );
                    }
                }, day, month, year);
                datePickerDialog.show();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                /*Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_PICK);*/

                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_PICK);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAccountInfo();
            }
        });

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String image = dataSnapshot.child("profileImage").getValue().toString();

                if(!image.isEmpty())
                {
                    Picasso.get().load(image).into(imageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void validateAccountInfo() {
        String FirstName = firstName.getText().toString();
        String LastName = lastName.getText().toString();
        String ContactNumber = contact.getText().toString();
        String Lugar = address.getText().toString();
        String Birthday = beerDay.getText().toString();


        updateAccountInformation(FirstName, LastName, ContactNumber, Lugar, Birthday);
    }

    private void updateAccountInformation(String firstName, String lastName, String contactNumber, String lugar, String birthday) {
        HashMap userMap = new HashMap();
        userMap.put("firstName", firstName);
        userMap.put("lastName", lastName);
        userMap.put("contactNumber", contactNumber);
        userMap.put("address", lugar);
        userMap.put("birthDay", birthday);

        usersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText(UpdateUserData.this, "Account settings updated successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null)
        {
            Uri image = data.getData();
            imageView.setImageURI(image);

            if(resultCode == RESULT_OK)
            {

                //loadingBar.setTitle("Saving Information");
                //loadingBar.setMessage("Please wait while updating your account");
                //loadingBar.show();
                //loadingBar.setCanceledOnTouchOutside(true);

                Uri resultUri = image;

                StorageReference filePath = UserProfileImageRef.child(currentUserID + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(UpdateUserData.this, "Profile image stored successfully to firebase storage", Toast.LENGTH_SHORT).show();

                            final String downloadUrl = task.getResult().getDownloadUrl().toString();
                            usersRef.child("profileImage").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(UpdateUserData.this, "Profile image stored to firebase database successfully", Toast.LENGTH_SHORT).show();
                                                //loadingBar.dismiss();
                                            }
                                            else
                                            {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(UpdateUserData.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                //loadingBar.dismiss();
                                            }
                                        }
                                    });
                        }
                    }
                });
            }
            else
            {
                Toast.makeText(UpdateUserData.this, "Error occured: Image can not be cropped. Please try again", Toast.LENGTH_SHORT).show();
                //loadingBar.dismiss();
            }
//            Toast.makeText(getActivity(), "" + "true", Toast.LENGTH_SHORT).show();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(UpdateUserData.this);
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK)
            {

                loadingBar.setTitle("Saving Information");
                loadingBar.setMessage("Please wait while updating your account");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                Uri resultUri = result.getUri();

                StorageReference filePath = UserProfileImageRef.child(currentUserID + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(UpdateUserData.this, "Profile image stored successfully to firebase storage", Toast.LENGTH_SHORT).show();

                            final String downloadUrl = task.getResult().getDownloadUrl().toString();
                            usersRef.child("profileImage").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(UpdateUserData.this, "Profile image stored to firebase database successfully", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                            else
                                            {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(UpdateUserData.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                        }
                                    });
                        }
                    }
                });
            }
            else
            {
                Toast.makeText(UpdateUserData.this, "Error occured: Image can not be cropped. Please try again", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }
    }
}
