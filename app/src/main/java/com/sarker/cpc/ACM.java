package com.sarker.cpc;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ACM extends AppCompatActivity {

    private RecyclerView eRecyclerView;
    private EventAdapter eAdapter;
    private ArrayList<EventInfo> eList;
    private ArrayList<EventInfo> temp = new ArrayList<>();
    private FirebaseAuth mAuth;
    private TextView emptyEvent;
    private DatabaseReference eventRef;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_a_c_m);

        back = findViewById(R.id.ic_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        eventRef = FirebaseDatabase.getInstance().getReference().child("Events");

        emptyEvent = findViewById(R.id.empty_event);
        eRecyclerView = findViewById(R.id.acm_rv);

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


                    boolean found = false;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        String key = postSnapshot.getKey();
                        String type = dataSnapshot.child(key).child("wingType").getValue().toString();
                        EventInfo info = postSnapshot.getValue(EventInfo.class);

                        info.setKey(key);

                        if (type.equals("ACM Task Force")) {

                            temp.add(info);

                            found = true;
                        }
                    }

                    Collections.reverse(temp);
                    eList.addAll(temp);

                    eAdapter.notifyDataSetChanged();
                    if (!found){
                        emptyEvent.setVisibility(View.VISIBLE);
                    }else {
                        emptyEvent.setVisibility(View.GONE);
                    }


                } else {

                    emptyEvent.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ACM.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }

        });
    }
}