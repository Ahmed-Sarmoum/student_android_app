package com.ahmedsarmoum.qrcodetp.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedsarmoum.qrcodetp.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;

import com.ahmedsarmoum.qrcodetp.Controller.DatabaseHandler;
import com.ahmedsarmoum.qrcodetp.Controller.StudentAdapter;
import com.ahmedsarmoum.qrcodetp.Model.Student;
import com.ahmedsarmoum.qrcodetp.R;

public class StudentActivity  extends AppCompatActivity  {

    DatabaseHandler databaseHandler;
    RecyclerView recyclerStudent;
    static StudentAdapter studentAdapter;
    AlertDialog alert;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        databaseHandler = new DatabaseHandler(this);
        recyclerStudent = findViewById(R.id.recyclerStudent);
        FloatingActionButton fab = findViewById(R.id.fab);

        studentAdapter = new StudentAdapter(this, databaseHandler.getAllStudent(), databaseHandler);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerStudent.setLayoutManager(layoutManager);
        recyclerStudent.setItemAnimator(new DefaultItemAnimator());
        recyclerStudent.setAdapter(studentAdapter);

        fab.setOnClickListener(view -> {
            View dialog = LayoutInflater.from(StudentActivity.this).inflate(R.layout.model_add_student, null, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(StudentActivity.this)
                    .setView(dialog);

            EditText firstname = dialog.findViewById(R.id.firstname);
            EditText lastname = dialog.findViewById(R.id.lastname);
            EditText adress = dialog.findViewById(R.id.adressText);
            EditText age = dialog.findViewById(R.id.age);
            RadioButton male = dialog.findViewById(R.id.male);
            RadioButton femele = dialog.findViewById(R.id.femele);


            alert = builder.create();
            if (alert.getWindow() != null)
                alert.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
            alert.show();

            dialog.findViewById(R.id.btnsave).setOnClickListener(view2 -> {
                Student student = new Student();
                if (firstname.getText().toString().matches("")) {
                    Snackbar.make(view2,"FirstName Require",Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (lastname.getText().toString().matches("")) {
                    Snackbar.make(view2,"LastName Require",Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (age.getText().toString().matches("")) {
                    Snackbar.make(view2,"Age Require",Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (adress.getText().toString().matches("")) {
                    Snackbar.make(view2,"Address Require",Snackbar.LENGTH_LONG).show();
                    return;
                }

                student.setFirstName(firstname.getText().toString());
                student.setLastName(lastname.getText().toString());
                if (male.isChecked()) {
                    student.setSexe(0);
                } else {
                    student.setSexe(1);
                }
                student.setAge(Integer.parseInt(age.getText().toString()));
                student.setAdress(adress.getText().toString());

                databaseHandler.addStudent(student);

                studentAdapter = new StudentAdapter(this, databaseHandler.getAllStudent(), databaseHandler);

                recyclerStudent.setLayoutManager(layoutManager);
                recyclerStudent.setItemAnimator(new DefaultItemAnimator());
                recyclerStudent.setAdapter(studentAdapter);

                alert.dismiss();
                Snackbar.make(view,"Add New Student",Snackbar.LENGTH_LONG).show();
            });
        });
    }




    @Override
    public void onBackPressed() {
        Intent intent = new Intent(StudentActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
