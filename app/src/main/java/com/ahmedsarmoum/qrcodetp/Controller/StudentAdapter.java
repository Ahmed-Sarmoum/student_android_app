package com.ahmedsarmoum.qrcodetp.Controller;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.ahmedsarmoum.qrcodetp.Model.Presence;
import com.ahmedsarmoum.qrcodetp.Model.Student;
import com.ahmedsarmoum.qrcodetp.R;
import com.ahmedsarmoum.qrcodetp.View.StudentActivity;
import com.google.android.material.snackbar.Snackbar;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {

    private Context context;
    private List<Student> students;
    private DatabaseHandler databaseHandler;

    public StudentAdapter(Context context, List<Student> students, DatabaseHandler databaseHandler) {
        this.context = context;
        this.databaseHandler = databaseHandler;
        this.students = students;
    }

    @NonNull
    @Override
    public StudentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAdapter.MyViewHolder holder, int position) {
        Student student = students.get(position);

        holder.name.setText(student.getFirstName() +" "+ student.getLastName());
        holder.adress.setText(student.getAdress());
        Drawable myDrawable;
        if (student.getSexe() == 0) {
            myDrawable = ContextCompat.getDrawable(context,R.drawable.student);
        }else {
            myDrawable = ContextCompat.getDrawable(context,R.drawable.femele);
        }
        holder.imageTier.setImageDrawable(myDrawable);
        holder.edit.setOnClickListener(view -> {
            View dialog = LayoutInflater.from(context).inflate(R.layout.model_add_student, null, false);
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context)
                    .setView(dialog);

            EditText firstname = dialog.findViewById(R.id.firstname);
            EditText lastname = dialog.findViewById(R.id.lastname);
            EditText adress = dialog.findViewById(R.id.adressText);
            EditText age = dialog.findViewById(R.id.age);
            RadioButton male = dialog.findViewById(R.id.male);
            RadioButton femele = dialog.findViewById(R.id.femele);
            Button update = dialog.findViewById(R.id.btnsave);

            firstname.setText(student.getFirstName());
            lastname.setText(student.getLastName());
            adress.setText(student.getAdress());
            age.setText(String.valueOf(student.getAge()));

            if(student.getSexe() == 0) {
                male.setChecked(true);
            } else {
                femele.setChecked(true);
            }


            update.setText("Update");
            update.setBackgroundColor(Color.parseColor("#ff0000"));


            AlertDialog alert = builder.create();
            if (alert.getWindow() != null)
                alert.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
            alert.show();

            dialog.findViewById(R.id.btnsave).setOnClickListener(view2 -> {
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

                databaseHandler.updateStudent(student);

                context.startActivity(new Intent(context, StudentActivity.class));
                Toast.makeText(context, "Successfully Updated! :)", Toast.LENGTH_LONG).show();
            });

        });


        holder.delete.setOnClickListener(view -> {
            View dialog = LayoutInflater.from(context).inflate(R.layout.logout_dialog,null, false);
            AlertDialog.Builder dBuilder = new AlertDialog.Builder(context)
                    .setView(dialog);


            final AlertDialog dAlert = dBuilder.create();
            if (dAlert.getWindow() != null)
                dAlert.getWindow().getAttributes().windowAnimations = R.style.FideingDialogAnimation;
            dAlert.show();
            dialog.findViewById(R.id.oui).setOnClickListener(view12 -> {
            ArrayList<Presence> presences = databaseHandler.getAllPresence();
            for (Presence presence: presences) {
                if (presence.getStudent_id() == student.getId()) {
                    databaseHandler.deletePresence(presence);
                }
            }
            databaseHandler.deleteStudent(student);
            context.startActivity(new Intent(context, StudentActivity.class));

           Toast.makeText(context, "Successfully Deleted!!", Toast.LENGTH_LONG).show();
                dAlert .dismiss();
            });
            dialog.findViewById(R.id.non).setOnClickListener(view1 -> dAlert .dismiss());
        });

    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,adress;
        ImageView edit, delete, imageTier;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            adress = itemView.findViewById(R.id.adress);

            edit = itemView.findViewById(R.id.btnEdit);
            delete = itemView.findViewById(R.id.btnDeleter);
            imageTier = itemView.findViewById(R.id.imageTier);
        }
    }
}
