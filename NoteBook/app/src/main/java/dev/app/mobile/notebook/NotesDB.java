package dev.app.mobile.notebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.xml.datatype.DatatypeFactory;

public class NotesDB extends SQLiteOpenHelper {

    private static final String dbName = "dbNotes";
    private static final String tblName  = "notes";
    private static final String tblDelName  = "deleted_notes";
    private static final String col1 = "id";
    private static final String col2 = "content";
    private static final String col3 = "create_date";
    private static final String col4 = "edit_date";
    private static final String col5 = "hash_id";

    private static final String strCrtTbl =
            "CREATE TABLE " + tblName + " (" + col1 + " INTEGER PRIMARY KEY, "
                    + col2 + " TEXT, " + col3 + " TEXT, " + col4 + " TEXT, "
                    + col5 + " TEXT)";
    private static final String strDropTbl =
            "DROP TABLE IF EXISTS " + tblName;
    private static final String strCrtDelTbl = "CREATE TABLE " + tblDelName + " (" + col1 + " INTEGER PRIMARY KEY, "
                    + col5 + " TEXT)";
    private static final String strDropDelTbl =
            "DROP TABLE IF EXISTS " + tblDelName;

    public NotesDB(Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(strCrtTbl);
        db.execSQL(strCrtDelTbl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        db.execSQL(strDropTbl);
        db.execSQL(strDropDelTbl);
        onCreate(db);
    }

    public float InsertItem(NotesDBModel model) {
        float result = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        values.put(col2, model.getContent());
        values.put(col3, dateFormat.format(model.getCreateDate()));
        values.put(col4, dateFormat.format(model.getEditDate()));
        values.put(col5, model.getHashId());
        result = db.insert(tblName, null, values);
        return result;
    }

    public void DeleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        NotesDBModel model = GetItem(id);
        String query = "DELETE FROM " + tblName + " WHERE " + col1 + " = " + id;
        db.execSQL(query);
        ContentValues values = new ContentValues();
        values.put(col5, model.getHashId());
        db.insert(tblDelName, null, values);
    }

    public NotesDBModel GetItem(int id) {
        NotesDBModel model = new NotesDBModel();
        String query = "SELECT * FROM " + tblName + " WHERE " + col1 + " = " + id;
        Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        model.setId(cursor.getInt(cursor.getColumnIndex(col1)));
        model.setContent(cursor.getString(cursor.getColumnIndex(col2)));
        try {
            model.setCreateDate(dateFormat.parse(cursor.getString(cursor.getColumnIndex(col3))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            model.setEditDate(dateFormat.parse(cursor.getString(cursor.getColumnIndex(col4))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        model.setHashId(cursor.getString(cursor.getColumnIndex(col5)));
        return model;
    }

    public boolean CheckItemDel(String cond) {
        NotesDBModel model = new NotesDBModel();
        String query = "SELECT * FROM " + tblDelName + " WHERE " + cond;
        Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
        if (cursor.getCount() > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public NotesDBModel FindItem(String cond) {
        NotesDBModel model = new NotesDBModel();
        String query = "SELECT * FROM " + tblName + " WHERE " + cond;
        Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
        if (cursor.getCount() <= 0){
            return null;
        }
        if (cursor != null) {
            cursor.moveToFirst();
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        model.setId(cursor.getInt(cursor.getColumnIndex(col1)));
        model.setContent(cursor.getString(cursor.getColumnIndex(col2)));
        try {
            model.setCreateDate(dateFormat.parse(cursor.getString(cursor.getColumnIndex(col3))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            model.setEditDate(dateFormat.parse(cursor.getString(cursor.getColumnIndex(col4))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        model.setHashId(cursor.getString(cursor.getColumnIndex(col5)));
        return model;
    }

    public List<NotesDBModel> GetAllItems() {
        List<NotesDBModel> list = new ArrayList<NotesDBModel>();
        String query = "SELECT * FROM " + tblName;
        Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                NotesDBModel model = new NotesDBModel();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                model.setId(cursor.getInt(cursor.getColumnIndex(col1)));
                model.setContent(cursor.getString(cursor.getColumnIndex(col2)));
                try {
                    model.setCreateDate(dateFormat.parse(cursor.getString(cursor.getColumnIndex(col3))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    model.setEditDate(dateFormat.parse(cursor.getString(cursor.getColumnIndex(col4))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                model.setHashId(cursor.getString(cursor.getColumnIndex(col5)));
                list.add(model);
            }while(cursor.moveToNext());
        }
        return list;
    }

    public List<String> GetDelList() {
        List<String> list = new ArrayList<String>();
        String query = "SELECT * FROM " + tblDelName;
        Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndex(col5)));
            } while(cursor.moveToNext());
        }
        return list;
    }

    public int LastInsertRowID() {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT LAST_INSERT_ROWID()", null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("LAST_INSERT_ROWID()"));
    }

    public int NewNote(String str) {
        NotesDBModel item = new NotesDBModel();
        item.setContent(str);
        item.setCreateDate(new Date());
        item.setEditDate(new Date());
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String hash = md.toString();
            md.update(str.getBytes());
            md.update(String.valueOf(item.getCreateDate().getTime()).getBytes());
            md.update(String.valueOf(new Random().nextLong()).getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            item.setHashId(sb.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        InsertItem(item);
        return LastInsertRowID();
    }

    public void UpdateNote(String str, int id) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String query = "UPDATE " + tblName + " SET content = '" + str + "', edit_date = '" +
                dateFormat.format(date) + "' WHERE id = " + String.valueOf(id);
        Log.e("x", query);
        Cursor c = this.getWritableDatabase().rawQuery(query, null);
        c.moveToFirst();
        c.close();
    }

    public void UpdateItem(NotesDBModel model) {
        Date date = model.getEditDate();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String query = "UPDATE " + tblName + " SET content = '" + model.getContent() + "', edit_date = '" +
                dateFormat.format(date) + "' WHERE id = " + String.valueOf(model.getId());
        Log.e("x", query);
        Cursor c = this.getWritableDatabase().rawQuery(query, null);
        c.moveToFirst();
        c.close();
    }

    public void DeleteItemFromHash(String hash) {
        String query = "DELETE FROM " + tblName + " WHERE " + col5 + " = '" + hash + "'";
        //Log.e("query>", query);
        Cursor c = this.getWritableDatabase().rawQuery(query, null);
        c.moveToFirst();
        c.close();
    }

}