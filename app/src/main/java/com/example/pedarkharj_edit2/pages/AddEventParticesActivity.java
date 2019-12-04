package com.example.pedarkharj_edit2.pages;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.pedarkharj_edit2.R;
import com.example.pedarkharj_edit2.classes.Contact;
import com.example.pedarkharj_edit2.classes.DatabaseHelper;
import com.example.pedarkharj_edit2.classes.Participant;
import com.example.pedarkharj_edit2.classes.ParticipantAdapter;
import com.example.pedarkharj_edit2.classes.RecyclerTouchListener;
import com.example.pedarkharj_edit2.classes.Routines;

import java.util.ArrayList;
import java.util.List;

public class AddEventParticesActivity extends AppCompatActivity {
    Context mContext;
    RecyclerView recyclerView, rec_01;
    List<Contact> contacts;
    List<Participant> participants, participants_01;
    ParticipantAdapter adaptor;
    DatabaseHelper db;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_partices);
        Toolbar toolbar =  findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);

        mContext = this;

        //back imageView btn
        ImageView backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(item -> finish());

        /**
         * RecView & DB
         */
        db = new DatabaseHelper(mContext);
        rec_01 = findViewById(R.id.rv_01);
        participants = new ArrayList<>();
        participants_01 = new ArrayList<>();

        recyclerView = findViewById(R.id.rv);
        setRecView(); //show contacts

        //-------------------------     Floating Btn    --------------------------//
        fab = this.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {

            //get saved partices in tempEvent
            Routines.addParticesToTempEvent(participants_01, db);

            int[] ids = new int[participants_01.size()];
            int eventId = 0;
            int i = 0;
            for (Participant participant: participants_01){
                Log.d("Fuck06", "i: "+ i );
                ids[i++] = participant.getId();
            }

            Log.d("Fuck06", "eventId before send: "+ eventId );
            eventId = participants_01.get(0).getEvent().getId();


            Log.d("Fuck06", "eventId at send: "+ eventId );

            Intent intent = new Intent(mContext, AddEventFinalActivity.class);
            intent.putExtra(Routines.NEW_EVENT_PARTIC_IDS_INTENT, ids);
            intent.putExtra(Routines.NEW_EVENT_PARTIC_EVENT_ID_INTENT, eventId);
            Log.d("fuckEvent",  "id: " + ids[0] + " "+ ids[1]);
            startActivity(intent);
//
        });



        //
        db.closeDB();
    }

    /***********************************       METHODS     ***********************************/

    //-------------------------     RecyclerView    --------------------------//
    private void setRecView() {
        /**
         *  All contacts
         */

        /**
         * todo: set Contact instead of Partice... :
         * I'm forming Contacts as Participants, so I won't need to create another
         * adaptor or even edit that. change this shit later in order not to get fucked up!
         */
        contacts = db.getAllContacts();
        participants = Routines.contactToPartic(contacts);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        //
        adaptor = new ParticipantAdapter(mContext, R.layout.sample_conntacts_horizental, participants);
        recyclerView.setAdapter(adaptor);

        //onClick
        Log.e("recOnClick", "onClick");
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Participant participant = participants.get(position);
                participants_01.add(participant);
                setRecView01(participants_01);

                Log.d("recOnClick", participant.getName());
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    private void setRecView01(List<Participant> participants){

        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rec_01.setLayoutManager(gridLayoutManager);
        //
        adaptor = new ParticipantAdapter(mContext, R.layout.sample_contact, participants);
        rec_01.setAdapter(adaptor);

    }








    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

}