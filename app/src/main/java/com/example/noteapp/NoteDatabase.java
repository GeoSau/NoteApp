package com.example.noteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NoteDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=2;
    private static final String DATABASE_NAME="note_db";
    private static final String DATABSE_TABLE ="notesTable";

    NoteDatabase(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    //columns
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";

    @Override
    public void onCreate(SQLiteDatabase db){
        //CREATE TABlE nametame(id INT PRIMARY KEY, title TEXT, content TEXT, date TEXT, time TEXT)
        String query = "Create TABLE " + DATABSE_TABLE + "("+KEY_ID+" INT PRIMARY KEY,"+
                KEY_TITLE+ " TEXT,"+
                KEY_CONTENT+" TEXT,"+
                KEY_DATE+" TEXT,"+
                KEY_TIME+" TEXT"+")";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if(oldVersion>=newVersion)
            return;
        db.execSQL("DROP TABLE IF EXISTS "+DATABSE_TABLE);
        onCreate(db);
    }

    public long addNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE, note.getTitle());
        contentValues.put(KEY_CONTENT,note.getContent());
        contentValues.put(KEY_DATE,note.getDate());
        contentValues.put(KEY_TIME,note.getTime());

        long ID = db.insert(DATABSE_TABLE,null,contentValues);
        Log.d("Inserted","ID --> " + ID);
        return ID;
    }

    public Note getNote(long id){
        //select * from databaseTable where id=1
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DATABSE_TABLE, new String[]{KEY_ID,KEY_TITLE,KEY_CONTENT,
        KEY_DATE,KEY_TIME},KEY_ID+"=?",new String[]{String.valueOf(id)},null,
                null,null);
        if(cursor!=null){
            cursor.moveToFirst();
        }

        return new Note(cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4));

    }
    public List<Note> getNotes(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Note> allNotes = new ArrayList<>();
        // select * from databaseName

        String query = "SELECT * FROM " + DATABSE_TABLE;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                Note note = new Note();
                note.setId(cursor.getLong(0));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setDate(cursor.getString(3));
                note.setTime(cursor.getString(4));

                allNotes.add(note);

            }while(cursor.moveToNext());
        }
        return allNotes;
    }
}