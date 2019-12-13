package com.example.pedarkharj_edit2.classes;

import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;

public class Participant  {
    //vital
    private int id;
    private String name;
    private Event event;
    private Contact contact;
    //money
    private float expense;
    private float debt;
    //
    private boolean chkImg;


        // constructors
    public Participant() {
            this.chkImg = false;
        }

        //main
    public Participant(String name) {
            this.name = name;
            this.chkImg = false;
        }

    public Participant(String name, float expense, float debt) {
            this.name = name;
            this.expense = expense;
            this.debt = debt;
            this.chkImg = false;
        }


    /**
     * getters
     */

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getBitmapStr() {
        return contact.getBitmapStr();
    }
    public boolean isChkImg() {
        return chkImg;
    }
    public float getExpense() {
        return expense;
    }
    public float getDebt() {
        return debt;
    }
    public Event getEvent() {
        return event;
    }
    public Contact getContact() {
        return contact;
    }

    /**
     * setters
     */
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setBitmapStr(String bitmapStr) {
        this.contact.setBitmapStr(bitmapStr);
    }
    public void setChkImg(boolean chkImg) {
        this.chkImg = chkImg;
    }
    public void setExpense(float expense) {
        this.expense = expense;
    }
    public void setDebt(float debt) {
        this.debt = debt;
    }
    public void setEvent(Event event) {
        this.event = event;
    }
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    /**********************************************************************************************/

    public String getResult() {
        if (expense-debt > 0)
            return "+ "+ (expense-debt);
        else  return String.valueOf(expense-debt);
    }

}
