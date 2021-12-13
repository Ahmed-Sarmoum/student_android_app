package com.ahmedsarmoum.qrcodetp.Controller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedsarmoum.qrcodetp.Model.Note;
import com.ahmedsarmoum.qrcodetp.Model.Student;
import com.ahmedsarmoum.qrcodetp.R;
import com.ahmedsarmoum.qrcodetp.View.NoteActivity;
import com.ahmedsarmoum.qrcodetp.View.StudentActivity;

import java.text.DecimalFormat;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewAdapter> {

    private Context context;
    private DatabaseHandler databaseHandler;
    private List<Note> notes;

    public  NoteAdapter(Context context, List<Note> notes, DatabaseHandler databaseHandler) {
        this.context = context;
        this.databaseHandler = databaseHandler;
        this.notes = notes;
    }
    @NonNull
    @Override
    public MyViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);

        return  new MyViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewAdapter holder, int position) {
        Note note = notes.get(position);
        DecimalFormat df = new DecimalFormat("#.00");
        Student student = databaseHandler.getStudent(note.getStudent_id());
        holder.nameS.setText(student.getFirstName()+" "+ student.getLastName());
        holder.control.setText(df.format(note.getNote_control()));
        holder.exam.setText(df.format(note.getNote_examen()));
        holder.date_update.setText(note.getCreated_at());

        holder.btnDeleteNote.setOnClickListener(view -> {
            View dialog = LayoutInflater.from(context).inflate(R.layout.logout_dialog,null, false);
            AlertDialog.Builder dBuilder = new AlertDialog.Builder(context)
                    .setView(dialog);

            final AlertDialog dAlert = dBuilder.create();
            if (dAlert.getWindow() != null)
                dAlert.getWindow().getAttributes().windowAnimations = R.style.FideingDialogAnimation;
            dAlert.show();

            dialog.findViewById(R.id.oui).setOnClickListener(view12 -> {
                databaseHandler.deleteNote(note);
                dAlert .dismiss();
                notes.remove(position);
                NoteActivity.notifyAdapter();
            });
            dialog.findViewById(R.id.non).setOnClickListener(view12 -> {
                dAlert .dismiss();
            });



        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class MyViewAdapter extends RecyclerView.ViewHolder {
        TextView nameS, control, exam, date_update;
        ImageView btnDeleteNote;
        public MyViewAdapter(@NonNull View itemView) {
            super(itemView);

            nameS = itemView.findViewById(R.id.nameS);
            control = itemView.findViewById(R.id.control);
            exam = itemView.findViewById(R.id.exam);
            date_update = itemView.findViewById(R.id.date_update);
            btnDeleteNote = itemView.findViewById(R.id.btnDeleteNote);
        }
    }
}
