package com.example.pedarkharj_edit2.classes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.pedarkharj_edit2.R;
import com.example.pedarkharj_edit2.pages.AddExpenseActivity;

import java.util.ArrayList;

public class BuyerDialog extends Dialog implements View.OnClickListener {
    private Activity mActivity;
    public Dialog d;
    private Button yes, no;
    RecyclerView recyclerView;
    ArrayList<Participant> participants;
    ParticipantAdapter adapter;


    public BuyerDialog(Activity mActivity) {
        super(mActivity);
        // TODO Auto-generated constructor stub
        this.mActivity = mActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_buyer_dialog);
        yes = (Button) findViewById(R.id.btn_yes);     yes.setOnClickListener(this);
        no = (Button) findViewById(R.id.btn_no);        no.setOnClickListener(this);
        recyclerView = findViewById(R.id.chooseBuyer_RecView);
        doRecyclerView();


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_yes:
                mActivity.startActivity(new Intent(mActivity, AddExpenseActivity.class));
                mActivity.finish();
                break;

            case R.id.btn_no:
                dismiss();
                break;

            default:
                break;
        }
        dismiss();
    }

    private Bitmap drawableToBitmap(int drawable){
        return BitmapFactory.decodeResource(mActivity.getResources(), drawable);
    }

    private void doRecyclerView() {
        participants = new ArrayList<Participant>();
        participants.add(new Participant(drawableToBitmap(R.drawable.w), "hamed"));
        participants.add(new Participant("reza dasdf dadas dasd"));
        participants.add(new Participant(drawableToBitmap(R.drawable.r),"غلوم"));
        participants.add(new Participant("حسین عباس پور"));
        participants.add(new Participant("محمد صیدالی"));
        participants.add(new Participant("پیمان"));
        participants.add(new Participant("reza"));
        participants.add(new Participant(drawableToBitmap(R.drawable.r),"مری"));
        participants.add(new Participant("غلوم"));
        participants.add(new Participant(drawableToBitmap(R.drawable.w),"حامد گنجعلی"));
        participants.add(new Participant("حسین حسی حشسیح حظسز شسزبح پور"));
        participants.add(new Participant("حامد گنجعلی"));
        participants.add(new Participant("محمد صیدالی"));
        participants.add(new Participant("مری"));
        participants.add(new Participant("پیمان"));
        participants.add(new Participant("reza"));
        participants.add(new Participant("غلوم"));
        participants.add(new Participant("حسین عباس پور"));
        participants.add(new Participant("حامد گنجعلی"));
        participants.add(new Participant("محمد صیدالی"));
        participants.add(new Participant("مری"));
        participants.add(new Participant("پیمان"));
        participants.add(new Participant("reza"));
        participants.add(new Participant("غلوم"));
        participants.add(new Participant("حسین عباس پور"));
        participants.add(new Participant("حامد گنجعلی"));
        participants.add(new Participant("محمد صیدالی"));
        participants.add(new Participant("مری"));
        participants.add(new Participant("پیمان"));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 4, GridLayoutManager.VERTICAL, false);
//        gridLayoutManager.setOrientation(gridLayoutManager.scrollHorizontallyBy(3));
        recyclerView.setLayoutManager(gridLayoutManager);
        //
        adapter = new ParticipantAdapter(mActivity, R.layout.sample_contact, participants, 3);
        recyclerView.setAdapter(adapter);
    }

}