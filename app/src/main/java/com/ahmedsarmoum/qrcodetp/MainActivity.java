package com.ahmedsarmoum.qrcodetp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.ahmedsarmoum.qrcodetp.View.ExportActivity;
import com.ahmedsarmoum.qrcodetp.View.NoteActivity;
import com.ahmedsarmoum.qrcodetp.View.PresonceActivity;
import com.ahmedsarmoum.qrcodetp.View.StudentActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout addStudentBtn = findViewById(R.id.addStudentBtn);
        LinearLayout presenceBtn = findViewById(R.id.presenceBtn);
        LinearLayout noteBtn = findViewById(R.id.noteBtn);
        LinearLayout exportBtn = findViewById(R.id.exportBtn);

        addStudentBtn.setTranslationY(100f);
        addStudentBtn.setAlpha(0f);
        addStudentBtn.animate().translationY(0f).alpha(1f).setDuration(400).setStartDelay(600);

        presenceBtn.setTranslationY(100f);
        presenceBtn.setAlpha(0f);
        presenceBtn.animate().translationY(0f).alpha(1f).setDuration(400).setStartDelay(800);

        noteBtn.setTranslationY(100f);
        noteBtn.setAlpha(0f);
        noteBtn.animate().translationY(0f).alpha(1f).setDuration(400).setStartDelay(900);

        exportBtn.setTranslationY(100f);
        exportBtn.setAlpha(0f);
        exportBtn.animate().translationY(0f).alpha(1f).setDuration(400).setStartDelay(1000);

        addStudentBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, StudentActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        presenceBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PresonceActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        noteBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NoteActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        exportBtn.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, ExportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
        });
    }



}