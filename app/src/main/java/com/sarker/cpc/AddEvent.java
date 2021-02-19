package com.sarker.cpc;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddEvent extends AppCompatActivity {

    private ImageView back,viewEventimge,addEventimage;
    private TextInputEditText Title, Description,Registration;
    private TextInputLayout titleEditTextLayout, descriptionTextLayout, registrationEditTextLayout;
    private Button btnAdd,time,date;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private String format,h1,m1,sTime,sDate, eventType = "", url, wings;
    private DatabaseReference eventRef;
    private RadioButton online, offline;

    StorageReference storageReference;

    StorageTask storageTask;
    Uri contentURI;

    private static final int PICK_FROM_GALLERY = 1;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && data.getData() != null) {
            contentURI = data.getData();

            Picasso.get().load(contentURI).fit().centerInside().into(viewEventimge);

        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_add_event);

        back = findViewById(R.id.back);
        addEventimage = findViewById(R.id.add_event_image);
        viewEventimge = findViewById(R.id.view_event_image);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnAdd = findViewById(R.id.btn_add_event);
        time = findViewById(R.id.e_time);
        date = findViewById(R.id.e_date);

        Title = findViewById(R.id.et_title);
        Description = findViewById(R.id.et_description);
        Registration = findViewById(R.id.et_registration);

        online = findViewById(R.id.online);
        offline = findViewById(R.id.offline);


        titleEditTextLayout = findViewById(R.id.editTextTitleLayout);
        descriptionTextLayout = findViewById(R.id.editTextDescriptionLayout);
        registrationEditTextLayout = findViewById(R.id.editTextRegistrationLayout);


        //mAuth = FirebaseAuth.getInstance();

        //user_id = mAuth.getCurrentUser().getUid();

        storageReference = FirebaseStorage.getInstance().getReference().child("Events");

        eventRef = FirebaseDatabase.getInstance().getReference().child("Events");

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(android.widget.TimePicker timePicker, int hour, int selectedMinute) {
                        if (hour == 0) {
                            hour += 12;
                            format = "AM";
                        } else if (hour == 12) {
                            format = "PM";
                        } else if (hour > 12) {
                            hour -= 12;
                            format = "PM";
                        } else {
                            format = "AM";
                        }

                        if(hour <10){
                            h1 = "0"+""+hour;
                        }else {
                            h1 = ""+hour;
                        }
                        if(selectedMinute <10){
                            m1 ="0"+""+selectedMinute;
                        }else {
                            m1 = ""+selectedMinute;
                        }

                        time.setText( h1 + ":" + m1+" "+format);
                        sTime = ""+h1 + ":" + ""+m1+" "+format ;
                    }

                }, hour, minute, DateFormat.is24HourFormat(AddEvent.this));
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEvent.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String m = "";
                                if(monthOfYear==0){
                                    m = "Jan";
                                }
                                else if(monthOfYear==1){
                                    m = "Feb";
                                }
                                else if(monthOfYear==2){
                                    m = "Mar";
                                }
                                else if(monthOfYear==3){
                                    m = "Apr";
                                }
                                else if(monthOfYear==4){
                                    m = "May";
                                }
                                else if(monthOfYear==5){
                                    m = "Jun";
                                }
                                else if(monthOfYear==6){
                                    m = "Jul";
                                }
                                else if(monthOfYear==7){
                                    m = "Aug";
                                }
                                else if(monthOfYear==8){
                                    m = "Sep";
                                }
                                else if(monthOfYear==9){
                                    m = "Oct";
                                }
                                else if(monthOfYear==10){
                                    m = "Nov";
                                }
                                else if(monthOfYear==11){
                                    m = "Dec";
                                }

                                date.setText(dayOfMonth + "-" + m + "-" + year);
                                sDate = dayOfMonth + "-" + m + "-" + year;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });


        String[] Wing = new String[]{"ACM Task Force", "Development", "Research and Journal", "Jobs and Career"};

        final AutoCompleteTextView dropdown = findViewById(R.id.wingDropdown);

        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        this,
                        R.layout.support_simple_spinner_dropdown_item,
                        Wing);
        dropdown.setAdapter(adapter);


        dropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                wings = adapter.getItem(position);
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String title = Title.getText().toString();
                String description = Description.getText().toString();
                String registration = Registration.getText().toString();



                if (title.isEmpty()) {
                    titleEditTextLayout.setError("*Title required");
                    Title.requestFocus();

                } else if (description.isEmpty()) {
                    descriptionTextLayout.setError("*Description required");
                    Description.requestFocus();
                } else if (registration.isEmpty()) {
                    registrationEditTextLayout.setError("*Registration Link required");
                    Registration.requestFocus();
                }
                else if (!(online.isChecked() || offline.isChecked() )) {

                    Toast.makeText(AddEvent.this, "Select Event Type", Toast.LENGTH_SHORT).show();

                }else if ( wings.equals(" ") ) {

                    Toast.makeText(AddEvent.this, "Select A Wing", Toast.LENGTH_SHORT).show();

                } else if (!(title.isEmpty() && description.isEmpty() && registration.isEmpty())) {

                    progressDialog = new ProgressDialog(AddEvent.this);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Adding Event...");
                    progressDialog.show();

                    if (contentURI != null) {
                        SaveChange();
                    } else {
                        Toast.makeText(AddEvent.this, "Select A Image", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(AddEvent.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }

        });

        addEventimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_FROM_GALLERY);
            }
        });

    }

    private void sendUserData() {

        progressDialog.dismiss();

        String title = Title.getText().toString();
        String description = Description.getText().toString();
        String registration = Registration.getText().toString();


        String saveCurrentDate, saveCurrentTime;

        if (online.isChecked()) {
            eventType = "Online";
        }
        if (offline.isChecked()) {
            eventType = "Offline";
        }


        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calFordDate.getTime());




        Map reg = new HashMap();

        reg.put("postTime",""+ getDateInMillis(saveCurrentDate+" "+saveCurrentTime));
        reg.put("eTitle", title);
        reg.put("eDes", description);
        reg.put("eTime", ""+ getDateInMillis(sDate+" "+sTime));
        reg.put("eType", eventType);
        reg.put("wingType", wings);
        reg.put("eRegister", registration);
        reg.put("eImage", url);

        eventRef.push().updateChildren(reg);


        Toast.makeText(AddEvent.this, "Event Successfully Added", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(AddEvent.this, MainActivity.class);
        startActivity(i);
        finishAffinity();


    }

    public static long getDateInMillis(String srcDate) {

        SimpleDateFormat desiredFormat = new SimpleDateFormat(
                "dd-MMMM-yyyy hh:mm a");

        long dateInMillis = 0;
        try {
            Date date = desiredFormat.parse(srcDate);
            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void SaveChange() {


        UUID random = UUID.randomUUID();

        StorageReference fileReference = storageReference.child(String.valueOf(random));
        storageTask = fileReference.putFile(contentURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful()) ;
                Uri uri = urlTask.getResult();
                url = uri.toString();

                sendUserData();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}