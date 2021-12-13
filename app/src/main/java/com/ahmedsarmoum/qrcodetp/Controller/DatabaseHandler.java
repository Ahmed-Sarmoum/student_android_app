package com.ahmedsarmoum.qrcodetp.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import com.ahmedsarmoum.qrcodetp.Model.Note;
import com.ahmedsarmoum.qrcodetp.Model.Presence;
import com.ahmedsarmoum.qrcodetp.Model.Student;
import com.ahmedsarmoum.qrcodetp.Utils.Utils;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(@Nullable Context context) {
        super(context, Utils.DATABASE_NAME, null,Utils.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STUDENT_TABLE = "CREATE TABLE "+ Utils.STUDENT_TABLE
                +"(" + Utils.KEY_ID+" INTEGER PRIMARY KEY,"
                +Utils.KEY_FIRSTNAME+" TEXT,"
                +Utils.KEY_LASTNAME+" TEXT,"
                +Utils.KEY_SEXE+" INTEGER,"
                +Utils.KEY_ADRESS+" TEXT,"
                +Utils.KEY_AGE +" INTEGER,"
                +Utils.KEY_CREATED_AT+" DATETIME DEFAULT CURRENT_TIMESTAMP)";

        String CREATE_PRESENCE_TABLE = "CREATE TABLE "+ Utils.PRESENCE_TABLE
                +"("+ Utils.KEY_ID+" INTEGER PRIMARY KEY, "
                +Utils.KEY_STUDENT_ID+" INTEGER,"
                +Utils.KEY_CREATED_AT+" DATATIME DEFAULT CURRENT_TIMESTAMP)";

        String CREATE_NOTE_TABLE = "CREATE TABLE "+ Utils.NOTE_TABLE
                +"("+Utils.KEY_ID +" INTEGER PRIMARY KEY,"
                +Utils.KEY_STUDENT_ID+" INTEGER,"
                +Utils.KEY_NOTE_CONTROL+" FLOAT,"
                +Utils.KEY_NOTE_EXAMEN+" FLOAT,"
                +Utils.KEY_OBSERVATION+ " TEXT,"
                +Utils.KEY_CREATED_AT+" DATETIME DEFAULT CURRENT_TIMESTAMP)";

        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_PRESENCE_TABLE);
        db.execSQL(CREATE_NOTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ Utils.STUDENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ Utils.PRESENCE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ Utils.NOTE_TABLE);

        onCreate(db);
    }

    public void addStudent(Student student) {
        Log.e("age=>", student.getAge()+"");
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Utils.KEY_FIRSTNAME, student.getFirstName());
        contentValues.put(Utils.KEY_LASTNAME, student.getLastName());
        contentValues.put(Utils.KEY_SEXE, student.getSexe());
        contentValues.put(Utils.KEY_ADRESS, student.getAdress());
        contentValues.put(Utils.KEY_AGE, student.getAge());

        database.insert(Utils.STUDENT_TABLE, null, contentValues);
        database.close();
    }

    public ArrayList<Student> getAllStudent() {
        SQLiteDatabase database = getReadableDatabase();
        ArrayList<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM " + Utils.STUDENT_TABLE;

        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst())
            do {
                Student student = new Student();
                student.setId(cursor.getInt(0));
                student.setFirstName(cursor.getString(1));
                student.setLastName(cursor.getString(2));
                student.setSexe(cursor.getInt(3));
                student.setAdress(cursor.getString(4));
                student.setAge(cursor.getInt(5));
                student.setCreated_at(cursor.getString(6));

                students.add(student);
            } while (cursor.moveToNext());

            return students;
    }
    public Student getStudent(int id) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(Utils.STUDENT_TABLE,
                new String[] {Utils.KEY_ID, Utils.KEY_FIRSTNAME, Utils.KEY_LASTNAME, Utils.KEY_SEXE,
                        Utils.KEY_ADRESS, Utils.KEY_AGE, Utils.KEY_CREATED_AT}, Utils.KEY_ID+ "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        Student student = new Student(cursor.getInt(0), cursor.getString(1),
                cursor.getString(2), cursor.getInt(3), cursor.getString(4),
                cursor.getInt(5), cursor.getString(6));
        return student;
    }

    public int updateStudent(Student student) {
        SQLiteDatabase  database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Utils.KEY_FIRSTNAME, student.getFirstName());
        contentValues.put(Utils.KEY_LASTNAME, student.getLastName());
        contentValues.put(Utils.KEY_SEXE, student.getSexe());
        contentValues.put(Utils.KEY_ADRESS, student.getAdress());
        contentValues.put(Utils.KEY_AGE, student.getAge());

        int res = database.update(Utils.STUDENT_TABLE, contentValues, Utils.KEY_ID + "=?",
                new String[]{String.valueOf(student.getId())});
        database.close();

        return res;


    }

    public void deleteStudent(Student student) {
        SQLiteDatabase database = getWritableDatabase();

        database.delete(Utils.STUDENT_TABLE, Utils.KEY_ID + "=?",
                new String[]{String.valueOf(student.getId())});

        database.close();
    }

    public void addPresence(Presence presence) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Utils.KEY_STUDENT_ID, presence.getStudent_id());

        database.insert(Utils.PRESENCE_TABLE, null, contentValues);
        database.close();
    }

    public ArrayList<Presence> getAllPresence() {
        SQLiteDatabase database = getReadableDatabase();
        ArrayList<Presence> presences = new ArrayList<>();

        String sql = "SELECT * FROM " + Utils.PRESENCE_TABLE;

        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst())
            do {
                Presence presence = new Presence();
                presence.setId(cursor.getInt(0));
                presence.setStudent_id(cursor.getInt(1));
                presence.setCreated_at(cursor.getString(2));

                presences.add(presence);
            }while (cursor.moveToNext());

        return presences;
    }

    public void deletePresence(Presence presence) {
        SQLiteDatabase database = getWritableDatabase();

        database.delete(Utils.PRESENCE_TABLE, Utils.KEY_ID + "=?",
                new String[]{String.valueOf(presence.getId())});

        database.close();
    }


    public void addNote(Note note) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Utils.KEY_STUDENT_ID, note.getStudent_id());
        contentValues.put(Utils.KEY_NOTE_CONTROL, note.getNote_control());
        contentValues.put(Utils.KEY_NOTE_EXAMEN, note.getNote_examen());
        contentValues.put(Utils.KEY_OBSERVATION, note.getObservation());

        database.insert(Utils.NOTE_TABLE, null, contentValues);
        database.close();
    }

    public ArrayList<Note> getAllNote() {
        SQLiteDatabase database = this.getReadableDatabase();
        ArrayList<Note> notes = new ArrayList<>();

        String sql = "SELECT * FROM "+Utils.NOTE_TABLE;
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor.moveToFirst())
            do {
                Note note = new Note();
                note.setId(cursor.getInt(0));
                note.setStudent_id(cursor.getInt(1));
                note.setNote_control(cursor.getFloat(2));
                note.setNote_examen(cursor.getFloat(3));
                note.setObservation(cursor.getString(4));
                note.setCreated_at(cursor.getString(5));

                notes.add(note);
            }while (cursor.moveToNext());

            return notes;
    }

    public void deleteNote(Note note) {
        SQLiteDatabase database = getWritableDatabase();

        database.delete(Utils.NOTE_TABLE, Utils.KEY_ID + "=?",
                new String[]{String.valueOf(note.getId())});

        database.close();
    }

    public int updateNote(Note note) {
        Log.e("observation",note.getObservation());
        SQLiteDatabase  database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Utils.KEY_STUDENT_ID, note.getStudent_id());
        contentValues.put(Utils.KEY_NOTE_CONTROL, note.getNote_control());
        contentValues.put(Utils.KEY_NOTE_EXAMEN, note.getNote_examen());
        contentValues.put(Utils.KEY_OBSERVATION, note.getObservation());

        int res = database.update(Utils.NOTE_TABLE, contentValues, Utils.KEY_ID + "=?",
                new String[]{String.valueOf(note.getId())});
        database.close();

        return res;
    }
}
