package com.sarker.cpc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminPanel extends AppCompatActivity {

    private ImageView back;
    private CardView addEvent,teamManage,addBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_admin_panel);

        addEvent = findViewById(R.id.cv_add_event);
        teamManage = findViewById(R.id.cv_team);
        addBanner = findViewById(R.id.cv_add_banner);

        back = findViewById(R.id.ic_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent n = new Intent(AdminPanel.this, AddEvent.class);
                startActivity(n);

            }
        });


        teamManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent n = new Intent(AdminPanel.this, Development.class);
                //startActivity(n);
                Toast.makeText(AdminPanel.this, "will added soon...", Toast.LENGTH_SHORT).show();

            }
        });


        addBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent n = new Intent(AdminPanel.this, Development.class);
                //startActivity(n);
                Toast.makeText(AdminPanel.this, "will added soon...", Toast.LENGTH_SHORT).show();

            }
        });
    }
}