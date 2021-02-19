package com.sarker.cpc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private long backPressedTime;
    private Toast backtoast;
    private DrawerLayout mdrawerLayout;
    private DatabaseReference eventRef,update;
    private static int SIGN_OUT = 2500;
    private String url;
    private ProgressDialog progressDialog;

    private RecyclerView eRecyclerView;
    private EventAdapter eAdapter;
    private ArrayList<EventInfo> eList;
    private ArrayList<EventInfo> temp = new ArrayList<>();
    private FirebaseAuth mAuth;
    private TextView emptyEvent;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_main);



        NavigationView navigationView = findViewById(R.id.nav_view);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        //setSupportActionBar(toolbar);

        mdrawerLayout = findViewById(R.id.drawer);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mdrawerLayout, toolbar, R.string.open, R.string.close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimary));

        mdrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        update = FirebaseDatabase.getInstance().getReference().child("CheckUpdate");
        eventRef = FirebaseDatabase.getInstance().getReference().child("Events");

        emptyEvent = findViewById(R.id.empty_event);
        eRecyclerView = findViewById(R.id.event_rv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        eRecyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        eList = new ArrayList<>();
        eAdapter = new EventAdapter(this,eList);
        eRecyclerView.setAdapter(eAdapter);


        Query query = eventRef.orderByChild("eTime");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {


                    if (dataSnapshot.exists()) {

                        eList.clear();
                        eAdapter.notifyDataSetChanged();

                        Calendar calFordDate = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                        String saveCurrentDate = currentDate.format(calFordDate.getTime());

                        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                        String saveCurrentTime = currentTime.format(calFordDate.getTime());


                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            EventInfo info = postSnapshot.getValue(EventInfo.class);

                            info.setKey(postSnapshot.getKey());

                            if (getDateInMillis(saveCurrentDate + " " + saveCurrentTime) < Long.parseLong(info.geteTime())) {

                                temp.add(info);


                            }
                        }

                        Collections.reverse(temp);
                        eList.addAll(temp);

                        eAdapter.notifyDataSetChanged();
                        emptyEvent.setVisibility(View.GONE);

                    } else {

                        emptyEvent.setVisibility(View.VISIBLE);

                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }

        });





    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Intent n;
        switch (menuItem.getItemId()) {

            case R.id.nav_admin_panel :
                n = new Intent(this, AdminPanel.class); startActivity(n);
                break;

            case R.id.nav_wings:
                n = new Intent(this, WingsActivity.class); startActivity(n);
                break;

            case R.id.nav_members :
                //n = new Intent(this, ImportRoutine.class); startActivity(n);
                Toast.makeText(MainActivity.this, "will added soon...", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_check_update:

                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Checking...");
                progressDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        update.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()){


                                    String version = dataSnapshot.child("version").getValue(String.class);
                                    url = dataSnapshot.child("url").getValue(String.class);

                                    String VersionName = BuildConfig.VERSION_NAME;

                                    if (version.equals(VersionName)) {

                                        Toast.makeText(MainActivity.this, "DIU CPC Is Up To Date", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();

                                    } else{
                                        progressDialog.dismiss();

                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                        builder.setTitle("New Version Available");
                                        builder.setIcon(R.drawable.cpc);
                                        builder.setCancelable(true);
                                        builder.setMessage("Update DIU CPC App For Better Experience")
                                                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                                        intent.setData(Uri.parse(url));
                                                        startActivity(intent);

                                                        finish();

                                                    }
                                                }).setNegativeButton("Not Now",new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.cancel();
                                            }

                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }

                                }else {
                                    Toast.makeText(MainActivity.this, "DIU CPC Is Up To Date", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(MainActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                },SIGN_OUT);

                break;

            case R.id.nav_about :

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("   About CPC");
                builder.setIcon(R.drawable.cpc);
                builder.setCancelable(true);
                builder.setMessage("DIU CPC is the most primitive and extensive club as well as the biggest club in Daffodil International University. We work together to explore every field of Computer Science. Our honorable Department Head Professor Dr. Syed Akhter Hossain Sir is the founder and chief supervisor of the club. The working activities of CPC can be divided into 4 wings.\n" +
                        "\n" +
                        "These are:\n" +
                        "\n" +
                        "1. ACM Task Force\n" +
                        "2. Development\n" +
                        "3. Research and Journal\n" +
                        "4. Jobs and Career")
                        .setPositiveButton("Visit Us", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("http://cpc.daffodilvarsity.edu.bd/"));
                                startActivity(intent);

                                dialog.cancel();

                            }
                        }).setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }

                });
                AlertDialog alert = builder.create();
                alert.show();

                break;

            case R.id.nav_report_bug:

                String[] toemail = {"niloy64529@gmail.com"};

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, toemail);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Bug In \"DIU CPC\" App");
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent, "Choose an email client"));

                break;

            case R.id.nav_contact:

                //n = new Intent(this, Task.class); startActivity(n);
                Toast.makeText(MainActivity.this, "will added soon...", Toast.LENGTH_SHORT).show();
                break;

        }
        return true;
    }


    @Override
    public void onBackPressed() {

        if (mdrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mdrawerLayout.closeDrawer(GravityCompat.START);
        } else if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backtoast.cancel();
            super.onBackPressed();
            finish();
            return;
        } else {
            backtoast = Toast.makeText(MainActivity.this, "Press Again To Exit", Toast.LENGTH_SHORT);
            backtoast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    public static long getDateInMillis(String srcDate) {
        SimpleDateFormat desiredFormat = new SimpleDateFormat(
                "dd-MMMM-yyyy hh:mm aa");

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

}