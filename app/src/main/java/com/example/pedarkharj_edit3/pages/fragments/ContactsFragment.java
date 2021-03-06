package com.example.pedarkharj_edit3.pages.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pedarkharj_edit3.classes.web_db_pref.SharedPrefManager;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.pedarkharj_edit3.MainActivity;
import com.example.pedarkharj_edit3.R;
import com.example.pedarkharj_edit3.classes.RecyclerTouchListener;
import com.example.pedarkharj_edit3.classes.models.Contact;
import com.example.pedarkharj_edit3.classes.MyAdapter;
import com.example.pedarkharj_edit3.classes.web_db_pref.DatabaseHelper;
import com.example.pedarkharj_edit3.pages.AddContactActivity;

import java.util.List;


public class ContactsFragment extends Fragment implements IContacts, IEditBar, View.OnClickListener {
    public static Contact pressedContact;
    List<Contact> contactList;
    Activity mActivity;
    Context mContext;
    DatabaseHelper db;
    MyAdapter adaptor;
    LinearLayoutManager linearLayoutManager;
    boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;

    RecyclerView recyclerView;
    FloatingActionButton fab;
    Toolbar toolbar;
    View mView;
    ImageView backBtn;
    Button getBtn;
    ProgressBar progressBar;
    View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_contacts, container, false);

        init();
        doOnClicks();
        // fab hides on scroll
        Routines.hideFabOnScroll(fab, recyclerView);

        //Floating Btn
        fab.setOnClickListener(view0 -> {
            startActivity(new Intent(getActivity(), AddContactActivity.class));
        });

        //Tutorial - TabTargetView
        if ( SharedPrefManager.getInstance(mActivity).getRunTurn(Routines.KEY_TURN_TIME_CONTACTS) == Routines.FIRST_RUN ){
            new Handler().postDelayed(this::showTabTargetsSequences1, 500);   // Delay 0.5 sec
        }

        // -------  recyclerView  -------//
        setRecView();
        Log.e("recOnClick", "onClick");
        /*
         *  onCLICK
         */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mContext, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                pressedContact = contactList.get(position);
//                Toast.makeText(mActivity, ""+ pressedContact.getName(), Toast.LENGTH_SHORT).show();
//                registerForContextMenu(view); // floating context menu
            }
        }));


        /*
         *  SCROLLING
         */
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState  == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
//                    isScrolling = true;
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                currentItems = linearLayoutManager.getChildCount();
//                totalItems = linearLayoutManager.getItemCount();
//                scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition();
//
//                if (isScrolling && (currentItems + scrollOutItems) == totalItems){
//                    //data fetch
//                    isScrolling = false;
//                    fetchData();
//                }
//
//            }
//        });

        return mView;
    }



    /********************************************       Methods     ****************************************************/
    private void init() {
        mContext = getContext();
        mActivity = getActivity();
        MainActivity.navPosition = Routines.CONTACTS;

        toolbar =  mView.findViewById(R.id.m_toolbar);
        ((AppCompatActivity)mActivity).setSupportActionBar(toolbar);

        backBtn = mView.findViewById(R.id.back_btn);
        recyclerView = mView.findViewById(R.id.recycler_view);
        getBtn = mView.findViewById(R.id.get);
        fab = mView.findViewById(R.id.fab);
        view  = mView.findViewById(R.id.view);

        db = new DatabaseHelper(mContext);
//        pressedContact = null;
    }

    private void doOnClicks() {
        getBtn.setOnClickListener(this);
//        backBtn.setOnClickListener(item -> Toast.makeText(mContext, "back", Toast.LENGTH_SHORT).show());
        setHasOptionsMenu(true); //for menu items in fragment (edit & delete)
    }

    private void setRecView() {
        contactList = db.getAllContacts();
//        List<Participant> participants = Routines.contactToPartic(mContacts0);

        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        contactList.remove(0);

        adaptor = new MyAdapter(mContext);
        adaptor.setActivity(mActivity);
        adaptor.setLayout(R.layout.sample_conntacts_horizental);
        adaptor.setContactList(contactList);
        adaptor.setIsFloatingActionBar(true);
        recyclerView.setAdapter(adaptor);

        backBtn.setOnClickListener(item -> mActivity.onBackPressed());
    }

    @Override
    public void onClick(View view) {
//        int permission = ContextCompat.checkSelfPermission(mContext, READ_CONTACTS);
//
//        if (Build.VERSION.SDK_INT >= 23 && permission != PackageManager.PERMISSION_GRANTED) {
////            // Should we show an explanation?
////            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, READ_CONTACTS))
////                Toast.makeText(mActivity, "Camera Permission is required for this app to run", Toast.LENGTH_SHORT).show();
//            Routines.requestPermissions(mActivity, new String[]{READ_CONTACTS}, Routines.PER_CODE_CAMERA_READexSTG);
//        }
//        else
//            readSystemContacts();
    }

    @Override
    public void readSystemContacts() {
//
//        Routines.getContact(mContext);
//        adaptor.notifyDataSetChanged();
//        mActivity.recreate();
    }

//    public void readContacts(){
//        ContentResolver cr = mContext.getContentResolver();
//        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
//                null, null, null, null);
//
//        if (cur.getCount() > 0) {
//            while (cur.moveToNext()) {
//                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
//                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
//                    System.out.println("name : " + name + ", ID : " + id);
//
//                    // get the phone number
//                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
//                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
//                            new String[]{id}, null);
//                    while (pCur.moveToNext()) {
//                        String phone = pCur.getString(
//                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        System.out.println("phone" + phone);
//                    }
//                    pCur.close();
//
//
//                    // get email and type
//
//                    Cursor emailCur = cr.query(
//                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
//                            null,
//                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
//                            new String[]{id}, null);
//                    while (emailCur.moveToNext()) {
//                        // This would allow you get several email addresses
//                        // if the email addresses were stored in an array
//                        String email = emailCur.getString(
//                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
//                        String emailType = emailCur.getString(
//                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
//
//                        System.out.println("Email " + email + " Email Type : " + emailType);
//                    }
//                    emailCur.close();
//
//                    // Get note.......
//                    String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
//                    String[] noteWhereParams = new String[]{id,
//                            ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
//                    Cursor noteCur = cr.query(ContactsContract.Data.CONTENT_URI, null, noteWhere, noteWhereParams, null);
//                    if (noteCur.moveToFirst()) {
//                        String note = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
//                        System.out.println("Note " + note);
//                    }
//                    noteCur.close();
//
//                    //Get Postal Address....
//
//                    String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
//                    String[] addrWhereParams = new String[]{id,
//                            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
//                    Cursor addrCur = cr.query(ContactsContract.Data.CONTENT_URI,
//                            null, null, null, null);
//                    while(addrCur.moveToNext()) {
//                        String poBox = addrCur.getString(
//                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
//                        String street = addrCur.getString(
//                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
//                        String city = addrCur.getString(
//                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
//                        String state = addrCur.getString(
//                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
//                        String postalCode = addrCur.getString(
//                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
//                        String country = addrCur.getString(
//                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
//                        String type = addrCur.getString(
//                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
//
//                        // Do something with these....
//
//                    }
//                    addrCur.close();
//
//                    // Get Instant Messenger.........
//                    String imWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
//                    String[] imWhereParams = new String[]{id,
//                            ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE};
//                    Cursor imCur = cr.query(ContactsContract.Data.CONTENT_URI,
//                            null, imWhere, imWhereParams, null);
//                    if (imCur.moveToFirst()) {
//                        String imName = imCur.getString(
//                                imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
//                        String imType;
//                        imType = imCur.getString(
//                                imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.TYPE));
//                    }
//                    imCur.close();
//
//                    // Get Organizations.........
//
//                    String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
//                    String[] orgWhereParams = new String[]{id,
//                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
//                    Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI,
//                            null, orgWhere, orgWhereParams, null);
//                    if (orgCur.moveToFirst()) {
//                        String orgName = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
//                        String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
//                    }
//                    orgCur.close();
//                }
//            }
//        }
//    }

    /**
     *  lazy loading
     */
    private void fetchData() {
        progressBar = new ProgressBar(mContext);
        progressBar.setVisibility( View.VISIBLE);

        for (int i=0 ; i<10; i++){

        }

    }

    /**
     * ContextMenu (Edit/Delete)
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
                //Edit
            case 100:
                Intent i = new Intent(mActivity, AddContactActivity.class);
                int id = (int) ContactsFragment.pressedContact.getId();
                i.putExtra(Routines.EDIT_MODE, Routines.EDIT_MODE_TRUE);
                i.putExtra(Routines.SEND_CONTACT_ID_INTENT, id);
//                mActivity.finish();
                startActivity(i);
               return true;

            //Delete
            case 101:
                adaptor.doDeleteStuff(mActivity, ContactsFragment.pressedContact);
                return true;


               default:
                   return super.onContextItemSelected(item);

        }

    }


    /**
     * first time tutorial (tapTarget)
     */
    public void showTabTargetsSequences1() {
// 1

        new TapTargetSequence(mActivity)
                // 2
                .targets(

//                        TapTarget.forView(fab, getString(R.string.addContactFab_title), getString(R.string.addContact_description))
//                                .outerCircleColor(R.color.colorPrimaryDark)
//                                .outerCircleAlpha(Routines.tapAlpha)
//                                .targetCircleColor(R.color.white)
//                                .titleTextSize(Routines.tapTitleSize)
//                                .titleTextColor(R.color.white)
//                                .descriptionTextSize(Routines.tapDescSize)
//                                .descriptionTextColor(R.color.bk1)
//                                .textTypeface(Typeface.SANS_SERIF)
//                                .dimColor(R.color.black).
//                                drawShadow(true)
//                                .cancelable(false)
//                                .tintTarget(false)
//                                .transparentTarget(true)
//                                .targetRadius(50),

                        TapTarget.forView(view, getString(R.string.contactHold_title), getString(R.string.contactHold_description))
                                .outerCircleColor(R.color.colorPrimaryDark)
                                .outerCircleAlpha(Routines.tapAlpha)
                                .targetCircleColor(R.color.black)
                                .titleTextSize(Routines.tapTitleSize)
                                .titleTextColor(R.color.white)
                                .descriptionTextSize(Routines.tapDescSize)
                                .descriptionTextColor(R.color.bk1)
                                .textTypeface(Typeface.SANS_SERIF)
                                .dimColor(R.color.black)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(false)
                                .transparentTarget(true)
                                .targetRadius(50)


                )

                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {
                        SharedPrefManager.getInstance(mActivity).setNextRunTurn(Routines.KEY_TURN_TIME_CONTACTS, Routines.SECOND_RUN);

                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {

                    }
                })

                // 6
                .start();

    }


}