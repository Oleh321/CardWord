package balychev.oleh.blch.cardword.model;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import balychev.oleh.blch.cardword.database.sqlite.DBColumns;

public class Card{
    private Long id;
    private String frontSide;
    private String backSide;
    private Long dateAdd;
    private Long dateRepeat;
    private Integer state;

    public Card(Long id, String frontSide, String backSide, Long dateAdd, Long dateRepeat, Integer state) {
        this.id = id;
        this.frontSide = frontSide;
        this.backSide = backSide;
        this.dateAdd = dateAdd;
        this.dateRepeat = dateRepeat;
        this.state = state;
    }

    public Long getDateRepeat() {
        return dateRepeat;
    }

    public void setDateRepeat(Long dateRepeat) {
        this.dateRepeat = dateRepeat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getFrontSide() {
        return frontSide;
    }

    public void setFrontSide(String frontSide) {
        this.frontSide = frontSide;
    }

    public String getBackSide() {
        return backSide;
    }

    public void setBackSide(String backSide) {
        this.backSide = backSide;
    }

    public Long getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Long dateAdd) {
        this.dateAdd = dateAdd;
    }

    @SuppressLint("SimpleDateFormat")
    final static SimpleDateFormat FORMAT = new SimpleDateFormat(
            "dd.MM.yyyy");

    public static String getDateInFormat(Long date){
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(date);
        return FORMAT.format(calendar.getTime());
    }

    public static Card parseCursor(Cursor c){
        if(c==null)
            return null;
        return new Card(
                Long.parseLong(c.getString(c.getColumnIndex(DBColumns.COLUMN_CARD_ID))),
                c.getString(c.getColumnIndex(DBColumns.COLUMN_FRONT_SIDE)),
                c.getString(c.getColumnIndex(DBColumns.COLUMN_BACK_SIDE)),
                Long.parseLong(c.getString(c.getColumnIndex(DBColumns.COLUMN_DATE_ADD))),
                Long.parseLong(c.getString(c.getColumnIndex(DBColumns.COLUMN_REPEAT_DATE))),
                Integer.parseInt(c.getString(c.getColumnIndex(DBColumns.COLUMN_STATE_ID)))
        );
    }


}

