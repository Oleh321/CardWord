package balychev.oleh.blch.cardword.database.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

import balychev.oleh.blch.cardword.model.Card;
import balychev.oleh.blch.cardword.model.Word;
import balychev.oleh.blch.cardword.utils.StateCardVariant;

public class CardDatabaseController {

    private SQLiteDatabase mDatabase;
    private int[] intervalDays = {0,1,5,15,30};

    private final static String SAVED_FORMAT = "dd.MM.yyyy";
    private final static int RESET_DAYS = 30;

    public CardDatabaseController(Context context) {
        mDatabase = new CardDbOpenHelper(context).getReadableDatabase();
    }

    private long addCard(Card card){
        ContentValues cardValues = new ContentValues();
        cardValues.put(DBColumns.COLUMN_FRONT_SIDE, card.getFrontSide());
        cardValues.put(DBColumns.COLUMN_BACK_SIDE, card.getBackSide());
        cardValues.put(DBColumns.COLUMN_DATE_ADD, card.getDateAdd());
        cardValues.put(DBColumns.COLUMN_STATE_ID, card.getState());
        cardValues.put(DBColumns.COLUMN_REPEAT_DATE, card.getDateRepeat());
        return mDatabase.insert(DBColumns.TABLE_CARD, null, cardValues);
    }

    public void deleteCard(long cardID){
        mDatabase.delete(DBColumns.TABLE_CARD, DBColumns.COLUMN_CARD_ID + " = ?",
                new String[]{String.valueOf(cardID)});
    }

    public Cursor getAllCards(StateCardVariant state){
        String[] viewColumns = {
                DBColumns.COLUMN_CARD_ID,
                DBColumns.COLUMN_FRONT_SIDE,
                DBColumns.COLUMN_BACK_SIDE,
                DBColumns.COLUMN_DATE_ADD,
                DBColumns.COLUMN_STATE_ID,
                DBColumns.COLUMN_REPEAT_DATE
        };

        String selection = null;
        switch (state){
            case ALL:
                selection = null;
                break;
            case FOR_REPEAT:
                selection = DBColumns.COLUMN_REPEAT_DATE + " NOT NULL AND " + DBColumns.COLUMN_REPEAT_DATE + " >= " + (new GregorianCalendar()).getTimeInMillis();
                break;
            case IN_PROGRESS:
                selection = DBColumns.COLUMN_REPEAT_DATE + " NOT NULL";
                break;
            case FINISHED:
                selection = DBColumns.COLUMN_REPEAT_DATE + " IS NULL";
                break;
            case FOR_RESET:
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.add(Calendar.DAY_OF_YEAR, RESET_DAYS);
                selection = DBColumns.COLUMN_REPEAT_DATE + " IS NOT NULL AND " + DBColumns.COLUMN_REPEAT_DATE + " >= " + (new GregorianCalendar()).getTimeInMillis();
        }

        Cursor repeatCursor = mDatabase.query(DBColumns.TABLE_CARD,
                viewColumns,
                selection,
                null,
                null,
                null,
                null
        );
        return repeatCursor;
    }

    public Cursor getCardByWord(String word){
        String[] viewColumns = {
                DBColumns.COLUMN_CARD_ID,
                DBColumns.COLUMN_FRONT_SIDE,
                DBColumns.COLUMN_BACK_SIDE,
                DBColumns.COLUMN_DATE_ADD,
                DBColumns.COLUMN_REPEAT_DATE,
                DBColumns.COLUMN_STATE_ID
        };

        return mDatabase.query(DBColumns.TABLE_CARD,
                 viewColumns,
                DBColumns.COLUMN_FRONT_SIDE + " = ?",
                 new String[]{word},
                null,
                null,
                null
        );
    }

    public Cursor searchCards(StateCardVariant state, String search, int param){
        String[] viewColumns = {
                DBColumns.COLUMN_CARD_ID,
                DBColumns.COLUMN_FRONT_SIDE,
                DBColumns.COLUMN_BACK_SIDE,
                DBColumns.COLUMN_DATE_ADD,
                DBColumns.COLUMN_STATE_ID,
                DBColumns.COLUMN_REPEAT_DATE
        };

        String selection = null;

        if (param == 0){
            selection = DBColumns.COLUMN_FRONT_SIDE + " Like '%" + search + "%'";
        } else if(param == 1){
            selection = DBColumns.COLUMN_BACK_SIDE + " Like '%" + search + "%'";
        }

        switch (state){
            case ALL:
                break;
            case FOR_REPEAT:
                selection =  " AND " + DBColumns.COLUMN_REPEAT_DATE + " NOT NULL AND " + DBColumns.COLUMN_REPEAT_DATE + " >= " + (new GregorianCalendar()).getTimeInMillis();
                break;
            case IN_PROGRESS:
                selection = " AND " + DBColumns.COLUMN_REPEAT_DATE + " NOT NULL";
                break;
            case FINISHED:
                selection = " AND " + DBColumns.COLUMN_REPEAT_DATE + " IS NULL";
                break;
            case FOR_RESET:
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.add(Calendar.DAY_OF_YEAR, RESET_DAYS);
                selection = " AND " + DBColumns.COLUMN_REPEAT_DATE +
                        " NOT NULL AND " + DBColumns.COLUMN_REPEAT_DATE +
                        " >= " + (new GregorianCalendar()).getTimeInMillis();
        }

        Cursor repeatCursor = mDatabase.query(DBColumns.TABLE_CARD,
                viewColumns,
                selection,
                null,
                null,
                null,
                null
        );

        return repeatCursor;
    }


    public void addNewCard(Word word){
        GregorianCalendar date = new GregorianCalendar();
        Card card = new Card(0L,
                word.getWord(),
                word.getDefinition(),
                date.getTimeInMillis(),
                date.getTimeInMillis(),
                0);
        addCard(card);
    }

    public void increaseCardState(Long cardID, int state){
        ContentValues cardValues = new ContentValues();
        if(state == intervalDays.length){
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.add(Calendar.DAY_OF_MONTH, intervalDays[state]);

            cardValues.put(DBColumns.COLUMN_STATE_ID, state);
            cardValues.put(DBColumns.COLUMN_REPEAT_DATE,
                    calendar.getTimeInMillis());
        } else {
            cardValues.putNull(DBColumns.COLUMN_STATE_ID);
            cardValues.putNull(DBColumns.COLUMN_REPEAT_DATE);
        }

        mDatabase.update(DBColumns.TABLE_CARD,
                cardValues,
                DBColumns.COLUMN_CARD_ID + " = ?",
                new String[] { String.valueOf(cardID) });
    }

    public void resetProgress(Long cardID){
        ContentValues cardValues = new ContentValues();
            cardValues.put(DBColumns.COLUMN_STATE_ID, 0);
            cardValues.put(DBColumns.COLUMN_REPEAT_DATE,
                    (new GregorianCalendar()).getTimeInMillis());

        mDatabase.update(DBColumns.TABLE_CARD,
                cardValues,
                DBColumns.COLUMN_CARD_ID + " = ?",
                new String[] { String.valueOf(cardID) });
    }

    public void markAsLearned(Long cardID){
        ContentValues cardValues = new ContentValues();
        cardValues.putNull(DBColumns.COLUMN_STATE_ID);
        cardValues.putNull(DBColumns.COLUMN_REPEAT_DATE);

        mDatabase.update(DBColumns.TABLE_CARD,
                cardValues,
                DBColumns.COLUMN_CARD_ID + " = ?",
                new String[] { String.valueOf(cardID) });
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~TEST~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public void addTestWord(Word word){
        int state = (new Random()).nextInt(6);
        ContentValues cardValues = new ContentValues();
        cardValues.put(DBColumns.COLUMN_FRONT_SIDE, word.getWord());
        cardValues.put(DBColumns.COLUMN_BACK_SIDE, word.getDefinition());
        cardValues.put(DBColumns.COLUMN_DATE_ADD,
                DateFormat.format(SAVED_FORMAT, new GregorianCalendar()).toString());

        if(state!=5){
            cardValues.put(DBColumns.COLUMN_STATE_ID, state);
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.add(Calendar.DAY_OF_MONTH, intervalDays[state]);
            cardValues.put(DBColumns.COLUMN_REPEAT_DATE,
                    DateFormat.format(SAVED_FORMAT, calendar).toString());
        } else {
            cardValues.putNull(DBColumns.COLUMN_STATE_ID);
            cardValues.putNull(DBColumns.COLUMN_REPEAT_DATE);
        }

        long cardID = mDatabase.insert(DBColumns.TABLE_CARD, null, cardValues);

    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public void close(){
        mDatabase.close();
    }

}
