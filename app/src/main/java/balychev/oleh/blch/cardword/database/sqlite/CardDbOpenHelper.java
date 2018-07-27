package balychev.oleh.blch.cardword.database.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CardDbOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cardword.db";
    private static final int DATABASE_VERSION = 6;


    public CardDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_CARD_TABLE = "CREATE TABLE " + DBColumns.TABLE_CARD + " ( " +
                        DBColumns.COLUMN_CARD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DBColumns.COLUMN_FRONT_SIDE + " TEXT NOT NULL, " +
                        DBColumns.COLUMN_BACK_SIDE + " TEXT NOT NULL, " +
                        DBColumns.COLUMN_DATE_ADD + " TEXT NOT NULL, " +
                        DBColumns.COLUMN_STATE_ID + " INTEGER);";

        final String SQL_CREATE_REPEAT_TABLE = "CREATE TABLE " + DBColumns.TABLE_REPEAT+ " ( " +
                DBColumns.COLUMN_CARD_ID + " INTEGER PRIMARY KEY, " +
                DBColumns.COLUMN_REPEAT_DATE + " TEXT NOT NULL);";

        final String SQL_CREATE_CARD_VIEW = "CREATE VIEW " + DBColumns.VIEW_CARD +" AS " +
                "SELECT a." +  DBColumns.COLUMN_CARD_ID + ", " +
                "a." + DBColumns.COLUMN_FRONT_SIDE + ", " +
                "a." + DBColumns.COLUMN_BACK_SIDE + ", " +
                "a." + DBColumns.COLUMN_DATE_ADD + ", " +
                "a." + DBColumns.COLUMN_STATE_ID + ", " +
                "b." + DBColumns.COLUMN_REPEAT_DATE + " " +
                "FROM " + DBColumns.TABLE_CARD + " as a INNER JOIN "
                + DBColumns.TABLE_REPEAT + " as b ON a." + DBColumns.COLUMN_CARD_ID +
                " = b."+DBColumns.COLUMN_CARD_ID ;

        db.execSQL(SQL_CREATE_CARD_TABLE);
        db.execSQL(SQL_CREATE_REPEAT_TABLE);
        db.execSQL(SQL_CREATE_CARD_VIEW);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP VIEW IF EXISTS " +  DBColumns.VIEW_CARD);
        db.execSQL("DROP TABLE IF EXISTS " + DBColumns.TABLE_REPEAT );
        db.execSQL("DROP TABLE IF EXISTS " + DBColumns.TABLE_CARD);
        onCreate(db);
    }


}
