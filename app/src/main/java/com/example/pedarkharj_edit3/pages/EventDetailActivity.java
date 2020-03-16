package com.example.pedarkharj_edit3.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedarkharj_edit3.MainActivity;
import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.MyAdapter;
import com.example.pedarkharj_edit3.classes.RecyclerTouchListener;
import com.example.pedarkharj_edit3.classes.Routines;
import com.example.pedarkharj_edit3.classes.models.Contact;
import com.example.pedarkharj_edit3.classes.models.Event;
import com.example.pedarkharj_edit3.classes.models.Expense;
import com.example.pedarkharj_edit3.classes.models.Participant;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.classes.web_db_pref.SharedPrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    List<Expense> mExpenses;
    List<Event> events;
    List<Integer> eventSpinerList;
    Map<Integer, Event> spinnerEventIdsMap;

    public static int lastSeenEventId;
    Context mContext ;
    Activity mActivity;
    MyAdapter adaptor;
    Event curEvent;
    DatabaseHelper db;
    //
    RecyclerView recyclerView;
    Spinner spinner;
    TextView tvR1, tvR2, tvC1, tvC2, tvL1, tvL2; //The rectangle above


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        mContext = this;
        mActivity = this;

        //-------------------------    inits    -------------------------- //
        Toolbar toolbar = findViewById(R.id.m_toolbar);
        ((AppCompatActivity)mActivity).setSupportActionBar(toolbar);

        db = new DatabaseHelper(mContext);

        mExpenses = new ArrayList<>();
        events = db.getAllEvents(); //for spinner && def partices
        eventSpinerList = new ArrayList<>();
        spinnerEventIdsMap = new HashMap<Integer, Event>(); //todo: use `new sparseArray<Event>` instead
        lastSeenEventId = SharedPrefManager.getInstance(mContext).getDefEventId();

        //the rectangle above
        tvL1 = findViewById(R.id.tv_title_my_expense);
        tvL2 = findViewById(R.id.tv_my_expense);
        tvC1 = findViewById(R.id.tv_title_my_dong);
        tvC2 = findViewById(R.id.tv_my_dong);
        tvR1 = findViewById(R.id.tv_title_my_result);
        tvR2 = findViewById(R.id.tv_my_result);


        /*
         * Setting default event and partices
         */
        recyclerView = findViewById(R.id.recycler_view);

        /* -------------------------     Spinner    -------------------------- */
        spinner = findViewById(R.id.spinner);
        List<String> list = new ArrayList<>();

        int j = 0;
        events = db.getAllEvents();
        for (Event event : events) {
            if (!event.getEventName().equals(Routines.EVENT_TEMP_NAME)) {
                list.add(event.getEventName());
                eventSpinerList.add(j);
                //save spinner events by order ids
                spinnerEventIdsMap.put(j++, event);
            }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);


        /*
         *  set the event to show as recView
         */
        // setting Cur Event
        if (lastSeenEventId > 0) {
            curEvent = db.getEventById(lastSeenEventId);
        } else curEvent = events.get(0);

        /*
         * setting spinner to show lastSeenEvent items
         */
        int selectedEventSpinnerId = 0;
        if (lastSeenEventId > 0) {
            for (int i = 0; i < events.size(); i++) {
                if (events.get(i).getId() == lastSeenEventId) {
                    selectedEventSpinnerId = i;
                    break;
                }
            }
        }

        SharedPrefManager.getInstance(mContext).saveLastSeenEventId(lastSeenEventId); //save  lastSeenEventId in SharedPref
        Log.i("fuck011", "lastSeenEventId: " + lastSeenEventId + "");

        spinner.setSelection(selectedEventSpinnerId); //def event

        spinner.setOnItemSelectedListener(this);
        Log.i("fuck011", "saveLastSeenEventId: " + curEvent.getId() + "");

        /* -------------------------     recView onClick    -------------------------- */
        List<Expense> expenseList = db.getAllExpensesOfEvent(curEvent);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                StringBuilder builder = new StringBuilder();
                Expense expense = expenseList.get(position);

                Intent i = new Intent(mContext, EventDetailEachExpenseActivity.class);
                i.putExtra(Routines.SEND_EXPENSE_ID_INTENT, expense.getId());
                startActivity(i);

            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        db.closeDB();
    }

    /********************************************       Methods     ****************************************************/
    //-------------------------     Spinner    --------------------------//
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Event event = spinnerEventIdsMap.get(i);
        if (event == null)
            event = events.get(0); //todo: change it. if we remove the last event, there shouldn't be any. dough!
        for (Event event0 : events) {
            Log.i("fuck025", event0.getEventName());
        }

        curEvent = event;
        setRecyclerView(curEvent); //show recyclerView
        SharedPrefManager.getInstance(mContext).saveLastSeenEventId(curEvent.getId()); //save curEvent (as defEvent for next time) to SharedPref
        initRectangleAbove(curEvent);  //init Rectangle


    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //-------------------------     RecyclerView    --------------------------//
    private void setRecyclerView(Event curEvent) {
        mExpenses = db.getAllExpensesOfEvent(curEvent);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //
        adaptor = new MyAdapter(mContext);
        adaptor.setLayout(R.layout.sample_each_expense);
        adaptor.setExpenseList(mExpenses);
        recyclerView.setAdapter(adaptor);
    }


    //todo: complete it
    private void initRectangleAbove(Event event) {
//        tvL2.setText(String.valueOf(0));
        List<Participant> participants = db.getAllParticeUnderEvent(event);
        if (participants.size() > 0) {
            int myExpenses = db.getParticTotalExpensePriceByParticeId(participants.get(0).getId()); //it is me. 1st partice of all
            int myDebt = db.getAllParticDebtsByParticeId(participants.get(0).getId()); //it is me. 1st partice of all
            int allEventExpenses = db.getEventTotalExpensesByEventId(event.getId());
            tvL2.setText(String.valueOf(allEventExpenses));
            tvC2.setText(String.valueOf(myExpenses));
            tvR2.setText(String.valueOf(myExpenses - myDebt));
        }

    }


}
