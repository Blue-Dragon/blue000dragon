package com.example.pedarkharj_edit3.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.models.Contact;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.classes.models.Event;
import com.example.pedarkharj_edit3.classes.models.Participant;
import com.example.pedarkharj_edit3.classes.MyAdapter;
import com.example.pedarkharj_edit3.classes.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;


public class AddEventParticesActivity extends AppCompatActivity {
    List<Contact> contacts;
    List<Contact> selectedContactsNew, existedContacts;
    List<Participant> allContactsTo_participants, selectedPartices;
    List<Integer> selectedContactsIdsNew;
    Context mContext;
    Activity mActivity = this;
    MyAdapter adaptor, selectedAdaptor;
    DatabaseHelper db;
    Event existedEvent;

    int curEventId;
    boolean edit_mode;
    RecyclerView recyclerView_horizental, selected_recView;
    FloatingActionButton fab;
    ImageView backBtn;
    RelativeLayout meRl;
    TextView tvSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_partices);
        Toolbar toolbar =  findViewById(R.id.m_toolbar);
        setSupportActionBar(toolbar);

        inits();
        onClicks();
//        showSpotlightIntro();

        //-----------     RecView    -----------//
        setRecView_horizental(true); //show contacts (allContactsTo_participants) and init selectedContactsNew
        setSelectedRecView(); //setting selectedContactsNew if we are in edit mode

         // onClick
        Log.e("recOnClick", "onClick");
        recyclerView_horizental.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView_horizental, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
//                Participant participant = allContactsTo_participants.get(position);
                Contact contact = contacts.get(position);
                //reInit selectedContactsNew
                initSelectedContactIds();

                //don't want to show `me`
                Contact alreadyInMe = findContactById(selectedContactsNew , 1);
                Contact alreadyInMe2 = findContactById(existedContacts , 1);
                if (!edit_mode){
                    if (alreadyInMe!=null) selectedContactsNew.remove(alreadyInMe);
                    if (alreadyInMe2!=null) selectedContactsNew.remove(alreadyInMe2);
                }

                if (selectedContactsIdsNew.contains((int) contact.getId()) )
                    removePartice(view, contact);
                else
                    addPartice(view, contact);

//                Log.d("recOnClick", contact.getName());
//                getSelectecPartice();

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

         // onClick selected
        Log.e("Selected_RecOnClick", "onClick");
        selected_recView.addOnItemTouchListener(new RecyclerTouchListener(mContext, selected_recView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
//                Participant participant = selectedPartices.get(position);
                Contact contact = selectedContactsNew.get(position);
                //remove partice from event
                removePartice(view, contact);
                setRecView_horizental(true);

//                setSelectedRecView(selectedPartices);
                getSelectecPartice();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        //------------     Floating Btn    ------------//
        fab.setOnClickListener(view -> {
//            if (edit_mode) db.createParticipantUnderEvent(participant, existedEvent);

            //show `me` on selectedContactsNew
            Contact me = findContactById(db.getAllContacts() , 1);
            Contact alreadyInMe = findContactById(existedContacts , 1);
            if (me!=null && alreadyInMe==null) selectedContactsNew.add(0, me);

            removeExistedParticeFromBefore();

            for (Contact contact: selectedContactsNew){
                Log.d("selectedContactsNew", ""+ contact.getName());
            }
            Log.d("selectedContactsNew", ".");

            //get saved partices in tempEvent
            if (!edit_mode){
                selectedPartices = Routines.addParticesToTempEvent(selectedContactsNew, db);

            }else if (existedEvent != null){
                selectedPartices = db.createEventNewParticipants(existedEvent, selectedContactsNew); //save new added ones
                selectedPartices = db.getAllParticeUnderEvent(existedEvent); // save all partices to pass to next slide
            }

            //all partices
            int[] myIds = new int[selectedPartices.size()];
            int i = 0;
            for (Participant participant: selectedPartices){
                Log.d("allParticesIds", "i: "+ i );
                myIds[i++] = participant.getId();
            }

            //new partices' contacts
            int[] existedIds = new int[existedContacts.size()];
            i = 0;
            for (Contact  c : existedContacts){
                Log.d("existedContactsTest", c.getName());
                existedIds[i++] = (int) c.getId();
            }

            if (selectedPartices.size() > 0){

                int eventId = selectedPartices.get(0).getEvent().getId();
//                Log.d("Fuck09", "eventId_ sent"+ eventId);
                Intent intent = new Intent(mContext, AddEventFinalActivity.class);
                if (edit_mode){
                    eventId = curEventId;
                    intent.putExtra(Routines.EDIT_MODE, Routines.EDIT_MODE_TRUE);
                }
                intent.putExtra(Routines.EXISTED_PARTIC_CONTACT_IDS_INTENT, existedIds);
                intent.putExtra(Routines.NEW_EVENT_PARTIC_IDS_INTENT, myIds);
                intent.putExtra(Routines.NEW_EVENT_PARTIC_EVENT_ID_INTENT, eventId);
                startActivity(intent);
                finish();

            }else {
                Toast.makeText(mContext, "لطفا اعضا را انتخاب کنید", Toast.LENGTH_SHORT).show();
            }
//
        });
        //
        db.closeDB();
    }

    private void removeExistedParticeFromBefore() {
        for (Contact contact : existedContacts){
            if (selectedContactsNew.contains(contact)){
                selectedContactsNew.remove(contact);
            }
        }
        initSelectedContactIds();
    }


    /***********************************       METHODS     ***********************************/
    private void inits() {
        mContext = this;
//        edit_mode = false;

        backBtn = findViewById(R.id.back_btn);
        recyclerView_horizental = findViewById(R.id.rv);
        selected_recView = findViewById(R.id.rv_01);
        fab = this.findViewById(R.id.fab);
        meRl = findViewById(R.id.rl3);
        tvSub = findViewById(R.id.tv_sub);

        ImageView img = meRl.findViewById(R.id.prof_pic);
        setGrayScale(img);

        db = new DatabaseHelper(mContext);
        contacts = db.getAllContacts();
        //don't show `me` on contacts
        Contact me = findContactById(contacts, 1);
        if (!edit_mode){
            if (me!=null) contacts.remove(me);
        }

        selectedPartices = new ArrayList<>();
        selectedContactsNew = new ArrayList<>();
        allContactsTo_participants = Routines.contactToPartic(contacts);
        selectedContactsIdsNew = new ArrayList<>();
        existedContacts = new ArrayList<>();
        Routines.contactsSelectedIds = initSelectedContactIds(); //init both `selectedContactsIdsNew` and `Routines.contactsSelectedIds`

        /*
         * init selectedPartice if we are in edit mode
         */
        curEventId = getIntent().getIntExtra(Routines.SEND_EVENT_ID_INTENT, 0);
        if (curEventId > 0){
            edit_mode = true;
            existedEvent = db.getEventById(curEventId);
            selectedPartices = db.getAllParticeUnderEvent(curEventId);
            meRl.setVisibility(View.GONE);

            existedContacts = getContacts(selectedPartices);
            //don't show `me` on contacts
            me = findContactById(selectedContactsNew, 1);
            if (me!=null) selectedContactsNew.remove(me);

            selectedContactsNew = getContacts(selectedPartices); //then we delete common contacts in both lists
        }

        Typeface tf = Routines.getTypeFaceYakan(mContext);
        tvSub.setTypeface(tf);
    }

    private List<Contact> getContacts(List<Participant> selectedPartices) {
        List<Contact> contactList = new ArrayList<>();
        for (Participant participant: selectedPartices){
            contactList.add(participant.getContact());
        }
        return contactList;
    }

    private void onClicks() {
        backBtn.setOnClickListener(item -> onBackPressed());

    }

    //-------------------------     RecyclerView    --------------------------//
    private void setRecView_horizental(boolean isAddEventParticeMode) {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1, GridLayoutManager.VERTICAL, false);
        recyclerView_horizental.setLayoutManager(gridLayoutManager);
        recyclerView_horizental.setItemAnimator(new DefaultItemAnimator());
//        //don't show `me` on contacts
//        Contact me = findContactById(contacts, 1);
//        contacts.remove(me);

        adaptor = new MyAdapter(mContext);
        adaptor.setLayout(R.layout.sample_conntacts_horizental);
        adaptor.setContactList(contacts);
        adaptor.setExistedContacts(existedContacts);
        //
        initSelectedContactIds();
        Log.d("Fuck0", selectedContactsIdsNew.size() + "  selectedContactsIdsNew");

        if (edit_mode || isAddEventParticeMode){
            adaptor.setIsAddEventParticeMode(true);
            Log.d("Fuck0", Routines.contactsSelectedIds.size() + "  screw u");
        }
        //
        recyclerView_horizental.setAdapter(adaptor);
    }


    /**
     * setting selectedPartice if we are in edit mode
     */
    private void setSelectedRecView(){

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1, GridLayoutManager.HORIZONTAL, false);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        selected_recView.setLayoutManager(gridLayoutManager);


        selectedAdaptor = new MyAdapter(mContext);
        selectedAdaptor.setLayout(R.layout.sample_contact);
        selectedAdaptor.setContactList(selectedContactsNew);
        selectedAdaptor.setExistedContacts(existedContacts);
        selectedAdaptor.setIsAddEventParticeMode(true);
        selected_recView.setAdapter(selectedAdaptor);

    }


    private void removePartice(View view, Contact contact) {
        Contact contact1 = findContactById(existedContacts, contact.getId()); //check if it's already existed
        if (contact1 == null){
            //        db.deletePartic(participant);
            contact = findContactById(selectedContactsNew, contact.getId());
            selectedContactsNew.remove(contact);
            removeColorSelected(view); //change color
            initSelectedContactIds();
            selectedAdaptor.notifyDataSetChanged();
//        adaptor.notifyDataSetChanged();
        }
    }


    public static Contact findContactById(List<Contact> selectedContacts, long contactId) {
        Contact contact = null;
        for (Contact contact1 : selectedContacts){
            if (contact1.getId() == contactId){
                contact = contact1;
                break;
            }
        }
        return contact;
    }

    private void removeColorSelected(View view) {
        view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent_white));

    }

    private void addPartice(View view, Contact contact) {
        selectedContactsNew.add(contact);
//        if (edit_mode) db.createParticipantUnderEvent(participant, existedEvent);
        //change color
        setColorSelected(view);
        initSelectedContactIds();
        selectedAdaptor.notifyDataSetChanged();
    }

    private void setColorSelected(View view) {
        view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.selected_green));
    }



    private List<Integer> initSelectedContactIds() {
        //selectedContactsIdsNew of all selected partices
        selectedContactsIdsNew.clear();
        Routines.contactsSelectedIds.clear();


        for (int i = 0; i< selectedContactsNew.size(); i++){
            selectedContactsIdsNew.add(i, (int) selectedContactsNew.get(i).getId());
        }

        return Routines.contactsSelectedIds = selectedContactsIdsNew;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    void getSelectecPartice(){
        StringBuilder builder = new StringBuilder();
        builder.append("selected partices:\n");
        for (Participant participant: selectedPartices){
            builder.append(participant.getId()+" : "+ participant.getContact().getId()+" -> "+ participant.getName()+"\n");
        }
        builder.append("\n");
        Log.d("selectedParticeIds", builder.toString());
    }


    private void setGrayScale(ImageView imageView){
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0.01f);
        imageView.setColorFilter(new ColorMatrixColorFilter(matrix));
    }

}
