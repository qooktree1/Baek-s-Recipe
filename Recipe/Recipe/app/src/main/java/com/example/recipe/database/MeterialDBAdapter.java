package com.example.recipe.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

public class MeterialDBAdapter {


    private static final String DATABASE_NAME = "receipe.db";
    private static final String DATABASE_TABLE = "tb_meterial";

    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table "
            + DATABASE_TABLE + " (" + Entry.IDX
            + " INTEGER primary key autoincrement, " + Entry.title
            + " TEXT not null, " + Entry.receipe_idx
            + " TEXT not null);";
    private static final String TAG = "DBAdapter";

    public String[] COLUMNS = new String[]{Entry.IDX,
            Entry.title, Entry.receipe_idx
    };
    private String[] CountCOLUMNS = new String[]{"count(idx)"
    };
    private Context mContext;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    public MeterialDBAdapter(Context context) {
        mContext = context;
    }

    public MeterialDBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null)
            mDbHelper.close();
    }


    public long createEntry(String title,String receipe_idx) {
        ContentValues initialValues = new ContentValues();


        initialValues.put(Entry.title, title);
        initialValues.put(Entry.receipe_idx,receipe_idx);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public long updateEntry(String idx,String title,String receipe_idx) {
        ContentValues initialValues = new ContentValues();


        initialValues.put(Entry.title, title);
        initialValues.put(Entry.receipe_idx,receipe_idx);

        return mDb.update(DATABASE_TABLE, initialValues, Entry.IDX + " = " + idx, null);

    }


    public Cursor selectIDXEntry(String receipe_idx) {
        //
        Cursor qu = mDb.query(DATABASE_TABLE, COLUMNS,
                Entry.receipe_idx + " = " + receipe_idx,
                null, null, null, null);

        return qu;

    }








    public Cursor joinAllEntry(ArrayList<String> data) {
        if(data.size()==0) {
            return mDb.rawQuery("select A.title as m_title,B.title,B.img,B.idx from tb_meterial A"
                    + " left join tb_receipe B on A.receipe_idx = B.idx"
                    , null);
        }else{
            String where = "where ";
            for(int i=0;i<data.size();i++){
                if(i==0) {
                    where += "A.title like '%"+data.get(i)+"%'";
                }else{
                    where += " and A.title like '%"+data.get(i)+"%'";
                }
            }
            return mDb.rawQuery("select A.title as m_title,B.title,B.img,B.idx from tb_meterial A"
                            + " left join tb_receipe B on A.receipe_idx = B.idx"
                    +" "+where
                    , null);
        }
    }

    public Cursor fetchAllEntry() {

        return mDb.query(DATABASE_TABLE, COLUMNS, null, null, null, null, null);

    }




    public Cursor countAllEntry() {
        //return mDb.rawQuery("Select * from "+DATABASE_TABLE,null);
        return mDb.query(DATABASE_TABLE, COLUMNS, null, null, null, null, null);
    }




    public int fetchAllEntryLength() {
        return mDb.query(DATABASE_TABLE, COLUMNS, null, null, null, null, null).getCount();
    }

    public void delAllEntry() {
        mDb.delete(DATABASE_TABLE, null, null);
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destory all old data");
          //  db.execSQL("ALTER TABLE "+ DATABASE_TABLE +" ADD COLUMN "+ ChapterEntry.GAGAE_SPEED+"{TEXT}");
             db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
                onCreate(db);
        }

    }


    public class Entry implements BaseColumns {
        public static final String IDX = "idx";

        public static final String title = "title";
        public static final String receipe_idx = "receipe_idx";



    }

}
