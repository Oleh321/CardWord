package balychev.oleh.blch.cardword.database.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.Log;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import balychev.oleh.blch.cardword.database.sqlite.DBColumns;
import balychev.oleh.blch.cardword.model.Card;
import balychev.oleh.blch.cardword.model.Repeat;
import balychev.oleh.blch.cardword.model.Word;
import balychev.oleh.blch.cardword.utils.StateCardVariant;

public class CardDatabaseController {

    private SQLiteDatabase mDatabase;
    private int[] intervalDays = {0,1,5,15,30};

    private final static String SAVED_FORMAT = "dd.MM.yyyy";
    private final static int RESET = 30;

    public CardDatabaseController(Context context) {
        mDatabase = new CardDbOpenHelper(context).getReadableDatabase();
    }

    // Добавляет карту и сразу устанавивает обучение
    public void addCard(Word word){
        int state = 0;
        ContentValues cardValues = new ContentValues();
        cardValues.put(DBColumns.COLUMN_FRONT_SIDE, word.getWord());
        cardValues.put(DBColumns.COLUMN_BACK_SIDE, word.getDefinition());
        cardValues.put(DBColumns.COLUMN_DATE_ADD,
                DateFormat.format(SAVED_FORMAT, new GregorianCalendar()).toString());
        cardValues.put(DBColumns.COLUMN_STATE_ID, state);

        long cardID = mDatabase.insert(DBColumns.TABLE_CARD, null, cardValues);
        updateRepeat(cardID, state);
    }

    //Поменять прогресс карты и время повторения
    public void setCardState(long cardID, int stateIndex){
        ContentValues cardValues = new ContentValues();
        if (stateIndex < intervalDays.length) {
            cardValues.put(DBColumns.COLUMN_STATE_ID, stateIndex);
        } else {
            cardValues.putNull(DBColumns.COLUMN_STATE_ID);
        }

        mDatabase.update(DBColumns.TABLE_CARD,
                cardValues,
                DBColumns.COLUMN_CARD_ID + " = ?",
                new String[] { String.valueOf(cardID) });
        updateRepeat(cardID, stateIndex);

    }

    // Управляет таблицей повторений
    public void updateRepeat(long cardID, int stateIndex){

        if (existRepeat(cardID)){
            mDatabase.delete(DBColumns.TABLE_REPEAT,
                    DBColumns.COLUMN_CARD_ID + " = ?",
                    new String[]{String.valueOf(cardID)});
        }

        if(stateIndex > intervalDays.length-1){
            return;
        }

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_YEAR, intervalDays[stateIndex]);

        ContentValues repeatValues = new ContentValues();
        repeatValues.put(DBColumns.COLUMN_CARD_ID, cardID);
        repeatValues.put(DBColumns.COLUMN_REPEAT_DATE,DateFormat.format(SAVED_FORMAT, calendar).toString());

        mDatabase.insert(DBColumns.TABLE_REPEAT, null, repeatValues);

    }

    // Если есть повторение
    public boolean existRepeat(long cardID){
        String[] repeatColumns = {
            DBColumns.COLUMN_CARD_ID,
            DBColumns.COLUMN_REPEAT_DATE
        };

        Cursor repeatCursor = mDatabase.query(DBColumns.TABLE_REPEAT,
                repeatColumns,
                DBColumns.COLUMN_CARD_ID + " = ?",
                new String[]{String.valueOf(cardID)},
                null,
                null,
                null
                );
        boolean flag = false;
        if(repeatCursor.moveToFirst()){
            flag = true;
        }
       repeatCursor.close();
        return flag;
    }

    public Card getCardByWord(String word){
        String[] viewColumns = {
                DBColumns.COLUMN_CARD_ID,
                DBColumns.COLUMN_FRONT_SIDE,
                DBColumns.COLUMN_BACK_SIDE,
                DBColumns.COLUMN_DATE_ADD,
                DBColumns.COLUMN_STATE_ID
        };

        Cursor repeatCursor = mDatabase.query(DBColumns.TABLE_CARD,
                viewColumns,
                DBColumns.COLUMN_FRONT_SIDE + " = ?",
                new String[]{word},
                null,
                null,
                null
        );

        boolean flag = false;
        if(repeatCursor.moveToFirst()){
            return new Card(
                    Long.parseLong(repeatCursor.getString(repeatCursor.getColumnIndex(DBColumns.COLUMN_CARD_ID))),
                    repeatCursor.getString(repeatCursor.getColumnIndex(DBColumns.COLUMN_FRONT_SIDE)),
                    repeatCursor.getString(repeatCursor.getColumnIndex(DBColumns.COLUMN_BACK_SIDE)),
                    repeatCursor.getString(repeatCursor.getColumnIndex(DBColumns.COLUMN_DATE_ADD)),
                    Integer.parseInt(repeatCursor.getString(repeatCursor.getColumnIndex(DBColumns.COLUMN_STATE_ID))));
        }
        repeatCursor.close();
        return null;
    }

    // Удаляет карту и все, что с ней связано
    public void deleteCard(long cardID){
        mDatabase.delete(DBColumns.TABLE_REPEAT, DBColumns.COLUMN_CARD_ID+ " = " + cardID, null);
        mDatabase.delete(DBColumns.TABLE_CARD, DBColumns.COLUMN_CARD_ID+ " = " + cardID, null);
    }


    public ArrayList<Card> getCards(StateCardVariant state)  {
        String[] viewColumns = {
                DBColumns.COLUMN_CARD_ID,
                DBColumns.COLUMN_FRONT_SIDE,
                DBColumns.COLUMN_BACK_SIDE,
                DBColumns.COLUMN_DATE_ADD,
                DBColumns.COLUMN_STATE_ID,
                DBColumns.COLUMN_REPEAT_DATE
        };

        ArrayList<Card> cards = new ArrayList<>();

        Cursor repeatCursor = mDatabase.query(DBColumns.VIEW_CARD,
                viewColumns,
                null,
                null,
                null,
                null,
                null
        );

        if(repeatCursor.moveToFirst()){
            do{
                SimpleDateFormat format = new SimpleDateFormat(SAVED_FORMAT);
                GregorianCalendar calendar = new GregorianCalendar();
                try {
                    calendar.setTime(format.parse(repeatCursor.getString(repeatCursor.getColumnIndex(DBColumns.COLUMN_REPEAT_DATE))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Card card = new Card(
                                Long.parseLong(repeatCursor.getString(repeatCursor.getColumnIndex(DBColumns.COLUMN_CARD_ID))),
                                repeatCursor.getString(repeatCursor.getColumnIndex(DBColumns.COLUMN_FRONT_SIDE)),
                                repeatCursor.getString(repeatCursor.getColumnIndex(DBColumns.COLUMN_BACK_SIDE)),
                                repeatCursor.getString(repeatCursor.getColumnIndex(DBColumns.COLUMN_DATE_ADD)),
                                Integer.parseInt(repeatCursor.getString(repeatCursor.getColumnIndex(DBColumns.COLUMN_STATE_ID)))
                        );

                switch (state){
                    case ALL:
                        cards.add(card);
                        break;
                    case FOR_REPEAT:
                        if (!new GregorianCalendar().before(calendar))
                            cards.add(card);
                        break;
                    case IN_PROGRESS:
                        if(card.getState() != null){
                            cards.add(card);
                        }
                        break;
                    case FINISHED:
                        if(card.getState() == null){
                            cards.add(card);
                        }
                        break;
                    case FOR_RESET:
                        calendar.add(Calendar.DAY_OF_YEAR, RESET);
                        if(!new GregorianCalendar().before(calendar)){
                            cards.add(card);
                        }
                }

            }while(repeatCursor.moveToNext());
        }
        repeatCursor.close();
        return cards;
    }

    public ArrayList<Card> getCardsByWord(StateCardVariant state, String word, int option)  {
        String[] viewColumns = {
                DBColumns.COLUMN_CARD_ID,
                DBColumns.COLUMN_FRONT_SIDE,
                DBColumns.COLUMN_BACK_SIDE,
                DBColumns.COLUMN_DATE_ADD,
                DBColumns.COLUMN_STATE_ID,
                DBColumns.COLUMN_REPEAT_DATE
        };

        ArrayList<Card> cards = new ArrayList<>();

        String selection = null;
        if (option == 0){
            selection = DBColumns.COLUMN_FRONT_SIDE + " Like '%" + word + "%'";
        } else if(option == 1){
            selection = DBColumns.COLUMN_BACK_SIDE + " Like '%" + word + "%'";
        }

        Cursor repeatCursor = mDatabase.query(DBColumns.VIEW_CARD,
                viewColumns,
                selection,
                null,
                null,
                null,
                null
        );

        if(repeatCursor.moveToFirst()){
            do{
                SimpleDateFormat format = new SimpleDateFormat(SAVED_FORMAT);
                GregorianCalendar calendar = new GregorianCalendar();
                try {
                    calendar.setTime(format.parse(repeatCursor.getString(repeatCursor.getColumnIndex(DBColumns.COLUMN_REPEAT_DATE))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Card card = new Card(
                        Long.parseLong(repeatCursor.getString(repeatCursor.getColumnIndex(DBColumns.COLUMN_CARD_ID))),
                        repeatCursor.getString(repeatCursor.getColumnIndex(DBColumns.COLUMN_FRONT_SIDE)),
                        repeatCursor.getString(repeatCursor.getColumnIndex(DBColumns.COLUMN_BACK_SIDE)),
                        repeatCursor.getString(repeatCursor.getColumnIndex(DBColumns.COLUMN_DATE_ADD)),
                        Integer.parseInt(repeatCursor.getString(repeatCursor.getColumnIndex(DBColumns.COLUMN_STATE_ID)))
                );

                switch (state){
                    case ALL:
                        cards.add(card);
                        break;
                    case FOR_REPEAT:
                        if (!new GregorianCalendar().before(calendar))
                            cards.add(card);
                        break;
                    case IN_PROGRESS:
                        if(card.getState() != null){
                            cards.add(card);
                        }
                        break;
                    case FINISHED:
                        if(card.getState() == null){
                            cards.add(card);
                        }
                        break;
                    case FOR_RESET:
                        calendar.add(Calendar.DAY_OF_YEAR, RESET);
                        if(!new GregorianCalendar().before(calendar)){
                            cards.add(card);
                        }
                }

            }while(repeatCursor.moveToNext());
        }
        repeatCursor.close();
        return cards;
    }


    public void close(){
        mDatabase.close();
    }

}
