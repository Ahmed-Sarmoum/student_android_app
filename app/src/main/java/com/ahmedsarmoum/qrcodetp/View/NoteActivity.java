package com.ahmedsarmoum.qrcodetp.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedsarmoum.qrcodetp.Controller.DatabaseHandler;
import com.ahmedsarmoum.qrcodetp.Controller.NoteAdapter;
import com.ahmedsarmoum.qrcodetp.Controller.PresonceAdapter;
import com.ahmedsarmoum.qrcodetp.MainActivity;
import com.ahmedsarmoum.qrcodetp.Model.Note;
import com.ahmedsarmoum.qrcodetp.R;

public class NoteActivity extends AppCompatActivity {

    static NoteAdapter noteAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity);

        RecyclerView recyclerNote= findViewById(R.id.recyclerNote);
        ImageView imgQrcodeNote = findViewById(R.id.imgQrcodeNote);

        DatabaseHandler databaseHandler = new DatabaseHandler(this);

        databaseHandler = new DatabaseHandler(this);
        noteAdapter = new NoteAdapter(this, databaseHandler.getAllNote(), databaseHandler);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerNote.setLayoutManager(layoutManager);
        recyclerNote.setItemAnimator(new DefaultItemAnimator());
        recyclerNote.setAdapter(noteAdapter);

        imgQrcodeNote.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ScannerQRCodeNoteActivity.class));
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NoteActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    public static void notifyAdapter() {
        noteAdapter.notifyDataSetChanged();
    }
}
