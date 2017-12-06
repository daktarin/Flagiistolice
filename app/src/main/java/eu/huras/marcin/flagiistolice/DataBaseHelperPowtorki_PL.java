package eu.huras.marcin.flagiistolice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class DataBaseHelperPowtorki_PL extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "powtorki_pl.db";
    public static final String TABLE_NAME = "powtorki_table_pl";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "FLAGA";
    public static final String COL_3 = "PANSTWO";
    public static final String COL_4 = "STOLICA";


    public DataBaseHelperPowtorki_PL(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, FLAGA INTEGER, PANSTWO TEXT, STOLICA TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String flaga, String panstwo, String stolica) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, flaga);
        contentValues.put(COL_3, panstwo);
        contentValues.put(COL_4, stolica);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }



    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;
    }

    public void deleteRow(String value)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME+ " WHERE "+COL_3+"='"+value+"'");
        db.close();
    }



}
