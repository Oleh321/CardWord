package balychev.oleh.blch.cardword.database.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CardDbOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cardword_db.db";
    private static final int DATABASE_VERSION = 2;


    public CardDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_CARD_TABLE = "CREATE TABLE " + DBColumns.TABLE_CARD + " ( " +
                        DBColumns.COLUMN_CARD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DBColumns.COLUMN_FRONT_SIDE + " TEXT NOT NULL, " +
                        DBColumns.COLUMN_BACK_SIDE + " TEXT NOT NULL, " +
                        DBColumns.COLUMN_DATE_ADD + " INTEGER NOT NULL, " +
                        DBColumns.COLUMN_STATE_ID + " INTEGER, " +
                        DBColumns.COLUMN_REPEAT_DATE + " INTEGER);";

        db.execSQL(SQL_CREATE_CARD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBColumns.TABLE_CARD);
        onCreate(db);
    }


}
