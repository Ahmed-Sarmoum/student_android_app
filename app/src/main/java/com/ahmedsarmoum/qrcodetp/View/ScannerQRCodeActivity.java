package com.ahmedsarmoum.qrcodetp.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ahmedsarmoum.qrcodetp.Controller.DatabaseHandler;
import com.ahmedsarmoum.qrcodetp.Model.Presence;
import com.ahmedsarmoum.qrcodetp.Model.Student;
import com.google.zxing.Result;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerQRCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int My_PERMISSION_REQUEST_CAMERA = 0;

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

        String fullname = "";
        if (isNumeric(rawResult.getText())) {
            Presence presence = new Presence();
            ArrayList<Student> students = databaseHandler.getAllStudent();
            ArrayList<Presence> presences = databaseHandler.getAllPresence();
            int isAdded = 0;
            int isExist = 0;
            presence.setStudent_id(Integer.parseInt(rawResult.getText()));
            if (students.size() > 0) {
                for (Student student : students) {
                    if (student.getId() == Integer.parseInt(rawResult.getText())) {
                        fullname = student.getFirstName() + " " + student.getLastName();
                        for (Presence presence1: presences) {
                            if (presence1.getStudent_id() == Integer.parseInt(rawResult.getText())) {
                                String[] v = presence1.getCreated_at().split(" ");
                                LocalDate parsed =LocalDate.parse(v[0]);
                                LocalDate current = LocalDate.now();

                                Period p = Period.between(parsed, current);
                                Log.i("date Now: ",p.getYears() + " Years, " + p.getMonths() + " Months, " + p.getDays() + " Days");
                                if (p.getYears() == 0 && p.getMonths() == 0 && p.getDays() == 0) {
                                    Toast.makeText(this, fullname+" already exist !!", Toast.LENGTH_LONG).show();
                                    isExist++;
                                }
                            }
                        }
                        if (isExist == 0) {
                            isAdded++;

                            databaseHandler.addPresence(presence);
                        }

                    }
                }
                if (isAdded == 0) {
                    Toast.makeText(this, "This Student Doesn't Exist, Please Verify His Name Into Student List And try Again!!!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, fullname +" Is Present", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Please,Your List Is Empty Add Student Then Try !!!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Content of This QRCode Not Available :( !!!", Toast.LENGTH_LONG).show();
        }


        startActivity(new Intent(this, PresonceActivity.class));
        finish();
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
