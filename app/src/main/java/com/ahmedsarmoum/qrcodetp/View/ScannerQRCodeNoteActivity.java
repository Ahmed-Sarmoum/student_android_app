package com.ahmedsarmoum.qrcodetp.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ahmedsarmoum.qrcodetp.Controller.DatabaseHandler;
import com.ahmedsarmoum.qrcodetp.Model.Note;
import com.ahmedsarmoum.qrcodetp.Model.Presence;
import com.ahmedsarmoum.qrcodetp.Model.Student;
import com.ahmedsarmoum.qrcodetp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerQRCodeNoteActivity   extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    int isExist = 0;
    private static final int My_PERMISSION_REQUEST_CAMERA = 10;

    ZXingScannerView scannerView;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        databaseHandler = new DatabaseHandler(this);
    }

    @Override
    public void handleResult(Result rawResult) {

        if (isNumeric(rawResult.getText())) {
            Note note = new Note();
            ArrayList<Student> students = databaseHandler.getAllStudent();
            int isAdded = 0;
            if (students.size() > 0) {
                for (Student student : students) {
                    if (student.getId() == Integer.parseInt(rawResult.getText())) {
                        isAdded++;
                        View dialog = LayoutInflater.from(ScannerQRCodeNoteActivity.this).inflate(R.layout.model_add_note, null, false);
                        AlertDialog.Builder builder = new AlertDialog.Builder(ScannerQRCodeNoteActivity.this)
                                .setView(dialog);

                        TextView fullname = dialog.findViewById(R.id.fullname);
                        EditText examNote = dialog.findViewById(R.id.examNote);
                        EditText noteControl = dialog.findViewById(R.id.noteControl);
                        EditText observation = dialog.findViewById(R.id.observation);
                        Button btnsaveNote = dialog.findViewById(R.id.btnsaveNote);

                        List<Note> notes = databaseHandler.getAllNote();


                        for (Note note1: notes) {
                            if (note1.getStudent_id() == Integer.parseInt(rawResult.getText())) {
                                isExist++;
                                Student student1 = databaseHandler.getStudent(note1.getStudent_id());
                                fullname.setText(student1.getFirstName()+" "+student1.getLastName());
                                noteControl.setText(String.valueOf(note1.getNote_control()));
                                examNote.setText(String.valueOf(note1.getNote_examen()));
                                observation.setText(note1.getObservation());
                                note.setId(note1.getId());

                                btnsaveNote.setText("Update");
                                btnsaveNote.setBackgroundColor(Color.parseColor("#FFA500"));
                            }
                        }

                        fullname.setText(student.getFirstName() +" "+ student.getLastName());

                        AlertDialog alert = builder.create();
                        if (alert.getWindow() != null)
                            alert.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
                        alert.show();


                        btnsaveNote.setOnClickListener(view -> {
                            if (noteControl.getText().toString().matches("")) {
                                Snackbar.make(view,"Control Note is Require",Snackbar.LENGTH_LONG).show();
                                return;
                            }
                            if (examNote.getText().toString().matches("")) {
                                Snackbar.make(view,"Exam Note id Require",Snackbar.LENGTH_LONG).show();
                                return;
                            }

                            note.setStudent_id(student.getId());
                            note.setNote_control(Double.parseDouble(noteControl.getText().toString()));
                            note.setNote_examen(Double.parseDouble(examNote.getText().toString()));
                            note.setObservation(observation.getText().toString());

                            if (isExist > 0) {
                                databaseHandler.updateNote(note);
                                Toast.makeText(this, "Successfully Updated :)", Toast.LENGTH_LONG).show();
                            } else {
                                databaseHandler.addNote(note);
                                Toast.makeText(this, "Successfully Added :)", Toast.LENGTH_LONG).show();
                            }

                            alert.dismiss();
                            startActivity(new Intent(this, NoteActivity.class));
                            finish();
                        });
                        /*isAdded++;
                        databaseHandler.addNote(note);*/
                    }
                }
                if (isAdded == 0) {
                    Toast.makeText(this, "This Student Doesn't Exist, Please Verify His Name Into Student List And try Again!!!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Please,Your List Is Empty Add Student Then Try !!!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Content of This QRCode Not Available :( !!!", Toast.LENGTH_LONG).show();
        }



    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, My_PERMISSION_REQUEST_CAMERA);
        }

        scannerView.setResultHandler(this::handleResult);
        scannerView.startCamera();
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;

    }
}
